package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

class  EmployeTest {

@Test
void testGetNBAnneAncienteDateEmbaucheNull(){

    //GIVEN
    Employe employe = new Employe();
    employe.setDateEmbauche(null);


    //WHEN
    Integer nbAnneeAnciennte = employe.getNombreAnneeAnciennete();

    //THEN, négative, 0 , null
    Assertions.assertThat(nbAnneeAnciennte);

}

@Test
void testGetNbAnneAncienteDateEbaucheInfoNow(){
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
    void testGetPrimeAnnuele(Integer performance, String matricul, Double tauxActivite, Long nbAnnesAnciente,
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




    @Test
    void getPrimeAnnuelleMatriculNull(){
        //Given
        Employe employe = new Employe("Doe", "John", null, LocalDate.now(), 1500d, 1, 1.0);

        //When
        Double prime = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(prime).isEqualTo(1000.0);

    }



    @Test
    /**
     * > employe.augmenterSalaire(null)) ->si procontage null
     *
     *  hasMessageContaining("Null proc")->si le retourr de message contien Null proc
     */

    void augmenterSalaireProcentNull(){

        //GIVEN
        Employe employe = new Employe("Doe", "John", null, LocalDate.now(), 1500d, 1, 1.0);

        //Then
        assertThatThrownBy(() -> employe.augmenterSalaire(null)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Null proc");




    }


    @Test
    /**
     * > salore nulle
     *
     *  hasMessageContaining("slaire est null")->si le retourr de message contien slaire est null
     */

    void augmenterSalaireSaliretNull(){

        //GIVEN
        Employe employe = new Employe("Doe", "John", null, LocalDate.now(), null, 1, 1.0);

        //Then
        assertThatThrownBy(() -> employe.augmenterSalaire(null)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("slaire est null");




    }



    @Test
    /**
     * > salore negative
     *
     *  hasMessageContaining("slaire negative")->si le retourr de message contien slaire negative
     */

    void augmenterSalaireSaliretNegative(){

        //GIVEN
        Employe employe = new Employe("Doe", "John", null, LocalDate.now(), -1500d, 1, 1.0);

        //Then
        assertThatThrownBy(() -> employe.augmenterSalaire(null)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("slaire negative");




    }


    @Test
    /**
     * Procontage negative
     *
     *  hasMessageContaining("negattive proc")->si le retourr de message contien negattive proc
     */

    void augmenterSalaireProcNegative(){

        //GIVEN
        Employe employe = new Employe("Doe", "John", null, LocalDate.now(), 1500d, 1, 1.0);

        //Then
        assertThatThrownBy(() -> employe.augmenterSalaire(-10d)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("negattive proc");




    }


    /**
     *
     * @param year
     * @param month
     * @param day
     * @param res
     */

    @ParameterizedTest
    @CsvSource({
            "2019, 12, 12, 8",
            "2021, 12, 1, 10",
            "2022, 5, 12, 10",
            "2032, 4, 4, 11"
    })
    testNbRtt(Integer year, Integer month, Integer day, Integer res) {
        //GIVEN
        Employe employe = new Employe();

        //WHEN
        Integer rttnbJ = employe.getNbRtt(LocalDate.of(year, month, day));

        //THE
        assertThat(rttnbJ).isEqualTo(res);
    }






}
