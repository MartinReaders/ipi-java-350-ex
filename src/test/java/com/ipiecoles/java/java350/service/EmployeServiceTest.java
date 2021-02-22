package com.ipiecoles.java.java350.service;
import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;
import java.util.List;
@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;
    @Test
    public void testEmbaucheEmployeExisteDeja() throws EmployeException {
        //Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Employe employeExistant = new Employe("Doe", "Jane", "T00001", LocalDate.now(), 1500d, 1, 1.0);
        //Simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        //Simuler que la recherche par matricule renvoie un employé (un employé a été embauché entre temps)
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);
        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait dû lancer une exception");
        }catch (Exception e){
            //Then
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
        }
    }



    @Test
    public void testEmbauchePremierEmploye() throws EmployeException {
        //Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //Simuler qu'ail y ya 99999 employe en bas (ou du moins que lea matruclue le plus haut)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");



        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait du lancer exeption");
        } catch (EmployeException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        }
        //When

        //Then
//        Employe employe = employeRepository.findByMatricule("T00001");
        //   Assertions.assertThat(employe).isNotNull();
        // Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        //Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        //Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        //Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        //Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        //Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }





    @Test
    public void testEmbauchePremierEmployes() throws EmployeException {
        //Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //Simuler qu'aucun employé n'est présent (ou du moins aucun matricule)

        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);

        //Simuler que la recherche par matricule ne renvoie pas de résultats
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(null);
        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
//        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());


        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }


    /**
     * testcaluclPerf + emp
     *
     */

    @Test
    void testCalculPerformanceCommercialPerforMinus() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1500d, 5, 1.0);
        Long caA = 500L;
        Long caO = 1500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(0D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(2);
    }



    /**
     * testcaluclPerf - emp
     */

    @Test
    void testCalculPerformanceCommercialPerforPlus() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1500d, 5, 1.0);
        Long caA = 500L;
        Long caO = 1500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(2D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(1);
    }




    /**
     * testcaluclPerf -  emp 2
     */

    @Test
    void testCalculPerformanceCommercialPerforPlus2() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1000d, 5, 1.0);
        Long caA = 1500L;
        Long caO = 2500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(0D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(2);
    }



    /**
     * testcaluclPerf -  emp 2
     */

    @Test
    void testCalculPerformanceCommercialPerforMinus2() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1000d, 5, 1.0);
        Long caA = 1500L;
        Long caO = 2500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(6D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(1);
    }



    /**
     * testcaluclPerf -  emp 3
     */

    @Test
    void testCalculPerformanceCommercialPerforMinus3() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1000d, 15, 1.0);
        Long caA = 1500L;
        Long caO = 2500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(6D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(1);
    }


    /**
     * testcaluclPerf -  emp 3
     */

    @Test
    void testCalculPerformanceCommercialPerforPlus3() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1000d, 5, 1.0);
        Long caA = 1500L;
        Long caO = 2500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(10D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(1);
    }




    /**
     * testcaluclPerf +  emp 4
     */

    @Test
    void testCalculPerformanceCommercialPerforPlus4() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1000d, 10, 1.0);
        Long caA = 1800L;
        Long caO = 1500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(6D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(12);
    }




    /**
     * testcaluclPerf -  emp 4
     */

    @Test
    void testCalculPerformanceCommercialPerforMinus4() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1000d, 8, 1.0);
        Long caA = 1800L;
        Long caO = 1500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(12D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(9);
    }




    /**
     * testcaluclPerf -  emp 5
     */

    @Test
    void testCalculPerformanceCommercialPerforMinus5() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1000d, 8, 1.0);
        Long caA = 2100L;
        Long caO = 1500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(12D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(12);
    }





    /**
     * testcaluclPerf +  emp 5
     */

    @Test
    void testCalculPerformanceCommercialPerforPlus5() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1000d, 6, 1.0);
        Long caA = 2100L;
        Long caO = 1500L;

        //When
        Mockito.when(employeRepository.findByMatricule(employe.getMatricule())).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(12D);
        employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO);

        //Then
        Assertions.assertThat(employe.getPerformance()).isEqualTo(10);
    }




    @Test
    void testcalculPerfeCaTraiteNegative() {
        //GIVEN
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        //When
        Long caTraite = -10L;
        Long caObjectif = 1500L;
        //THAN
        Assertions.assertThatThrownBy(()->employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif)).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }


    @Test
    void testcalculPerfeCaTraiteNull() {
        //GIVEN
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        //When
        Long caTraite = null;
        Long caObjectif = 1500L;
        //THAN
        Assertions.assertThatThrownBy(()->employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif)).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }



    @Test
    void testcalculPerfecaObjectifNull() {
        //GIVEN
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        //When
        Long caTraite = 10L;
        Long caObjectif = null;
        //THAN
        Assertions.assertThatThrownBy(()->employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif)).isInstanceOf(EmployeException.class)
                .hasMessageContaining("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }



    @Test
    void testcalculPerfecaObjectifNegatif() {
        //GIVEN
        Employe employe = new Employe();
        employe.setMatricule("C00001");
        //When
        Long caTraite = 10L;
        Long caObjectif = -1100L;
        //THAN
        Assertions.assertThatThrownBy(()->employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif)).isInstanceOf(EmployeException.class)
                .hasMessageContaining("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }


    @Test
    void testcalculPerfecaMatriculNotC() {
        //GIVEN
        Employe employe = new Employe();
        employe.setMatricule("T1234");
        //When
        Long caTraite = 800L;
        Long caObjectif = 1100L;
        //Than
        Assertions.assertThatThrownBy(()->employeService.calculPerformanceCommercial(employe.getMatricule(), caTraite, caObjectif)).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le matricule ne peut être null et doit commencer par un C !");
    }


    @Test
    void testcalculPerfecaMatriculNull() {
        //GIVEN
        Employe employe = new Employe();
        employe.setMatricule(null);
        //Than
        Long caA = 800L;
        Long caO = 1100L;
        //Whan
        Assertions.assertThatThrownBy(()->employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO)).isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le matricule ne peut être null et doit commencer par un C !");
    }


    /**
     *
     * @throws EmployeException
     * Si matricule null
     */
    @Test
    void testCalculPerformanceCommerciMatrExistPas() throws EmployeException {
        //Given
        Employe employe = new Employe("Martin", "KH", "C00001", LocalDate.now(), 1000d, 6, 1.0);
        Long caA = 2100L;
        Long caO = 1500L;


        //When
        Mockito.when(employeRepository.findByMatricule(Mockito.anyString())).thenReturn(null);

        //Then
        Assertions.assertThatThrownBy(() ->employeService.calculPerformanceCommercial(employe.getMatricule(), caA, caO))
                .isInstanceOf(EmployeException.class)
                .hasMessageContaining("Le matricule " + employe.getMatricule() + " n'existe pas !");
    }









}