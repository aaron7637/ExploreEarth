package com.example.exploreearth.app;

public class ChosenCountry {
    public static String chosenCountry = "Afghanistan";

    ChosenCountry() {
    }

    static void setCountry(String country) {
        chosenCountry = country;
    }

    static String getCountry() {
        return chosenCountry;
    }

    static Boolean isSingleWord() {
        Boolean isSingle = true;

        for (int i = 0; i < chosenCountry.length(); i++) {
            if (chosenCountry.charAt(i) == ' ') {
                isSingle = false;
            }
        }

        return isSingle;
    }
}  
 