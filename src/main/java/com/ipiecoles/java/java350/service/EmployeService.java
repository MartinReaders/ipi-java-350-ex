package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

@Service
public class EmployeService {

    private static Logger logger = LoggerFactory.getLogger(EmployeService.class);
    @Autowired
    private EmployeRepository employeRepository;

    /**
     * Méthode enregistrant un nouvel employé dans l'entreprise
     *
     * @param nom Le nom de l'employé
     * @param prenom Le prénom de l'employé
     * @param poste Le poste de l'employé
     * @param niveauEtude Le niveau d'étude de l'employé
     * @param tempsPartiel Le pourcentage d'activité en cas de temps partiel
     *
     * @throws EmployeException Si on arrive au bout des matricules possibles
     * @throws EntityExistsException Si le matricule correspond à un employé existant
     */
    public void embaucheEmploye(String nom, String prenom, Poste poste, NiveauEtude niveauEtude, Double tempsPartiel) throws EmployeException, EntityExistsException {
        logger.info("Embauche d'un employé avec les infos suivantes : nom : {}, prénom : {}, poste {}, niveau d'étude : {}, taux activité : {}",
                nom, prenom, poste, niveauEtude, tempsPartiel);
        //Récupération du type d'employé à partir du poste
        String typeEmploye = poste.name().substring(0,1);
        //Récupération du dernier matricule...
        String lastMatricule = employeRepository.findLastMatricule();
        if(lastMatricule == null){
            logger.warn("Aucun matricule trouvé, matricule initial affecté");
            lastMatricule = Entreprise.MATRICULE_INITIAL;
        }
        //... et incrémentation
        Integer numeroMatricule = Integer.parseInt(lastMatricule) + 1;
        if(numeroMatricule >= 100000){
            logger.error("Limite des 100000 matricules atteinte !");
            throw new EmployeException("Limite des 100000 matricules atteinte !");
        }
        //On complète le numéro avec des 0 à gauche
        String matricule = "00000" + numeroMatricule;
        matricule = typeEmploye + matricule.substring(matricule.length() - 5);
        //On vérifie l'existence d'un employé avec ce matricule
        if(employeRepository.findByMatricule(matricule) != null){
            logger.error("L'employé de matricule " + matricule + " existe déjà en BDD");
            throw new EntityExistsException("L'employé de matricule " + matricule + " existe déjà en BDD");
        }
        //Calcul du salaire
        Double salaire = Entreprise.COEFF_SALAIRE_ETUDES.get(niveauEtude) * Entreprise.SALAIRE_BASE;
        if(tempsPartiel != null){
            salaire = Math.round(salaire * tempsPartiel * 100) / 100d;
        }
        //Création et sauvegarde en BDD de l'employé.
        Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), salaire, Entreprise.PERFORMANCE_BASE, tempsPartiel);
        employe = employeRepository.save(employe);
        logger.info("Employé créé : {}", employe.toString());
    }
}
