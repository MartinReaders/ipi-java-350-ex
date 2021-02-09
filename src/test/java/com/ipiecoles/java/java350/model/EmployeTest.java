package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class  EmployeTest {

@Test
public void testGetNBAnneAncienteDateEmbaucheNull(){

    //GIVEN
    Employe employe = new Employe();
    employe.setDateEmbauche(null);


    //WHEN
    Integer nbAnneeAnciennte = employe.getNombreAnneeAnciennete();

    //THEN, négative, 0 , null
    Assertions.assertThat(nbAnneeAnciennte);

}

@Test
public  void testGetNbAnneAncienteDateEbaucheInfoNow(){
    //GIVEN
    Employe employe = new Employe("Doe", "Jhn", "T12345",
            LocalDate.of(2015,8,21), 1500d,1,1.0);


    //WHEN
    Integer anneAnciennete = employe.getNombreAnneeAnciennete();

    //THEN
    Assertions.assertThat(anneAnciennete).isGreaterThanOrEqualTo(0);

}

    @Test
    public void testGetPrimeAnnuele(){
        //Given
        Integer perfomance = 1;
        String matricul ="T12345";
        Double tauxActivite = 1.0;
        Long nbAnnesAnciennete = 0L;


        Employe employe = new Employe("Doe", "jhon", matricul,
                LocalDate.now().minusYears(nbAnnesAnciennete), 1500d,
                perfomance,tauxActivite);

        //When
        Double prime = employe.getPrimeAnnuelle();

        //Then
        Double primeAttendue=1000.0;
        Assertions.assertThat(prime).isEqualTo(primeAttendue);


    }






}
