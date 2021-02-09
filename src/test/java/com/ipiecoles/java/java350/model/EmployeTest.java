package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest(name = "Perf {0}, matriclule {1}, txActiite {2}, anciennte {3}=>prime{4}")
    @CsvSource({
            "1, 'T12345', 1.0, 0, 1000.0",
            "1, 'T12345', 0.5,0, 500.0",
            "2, 'T12345', 1.0, 0, 2300.0",
            "1, 'T12345', 1.0, 2, 1200.0",
    })
    public void testGetPrimeAnnuele(Integer performance, String matricul, Double tauxActivite, Long nbAnnesAnciente,
                                    Double primeAttendu){
        //Given


        Employe employe = new Employe("Doe", "jhon", matricul,
                LocalDate.now().minusYears(nbAnnesAnciente), 1500d,
                performance,tauxActivite);

        //When
        Double prime = employe.getPrimeAnnuelle();

        //Then

        Assertions.assertThat(prime).isEqualTo(primeAttendu);


    }








}
