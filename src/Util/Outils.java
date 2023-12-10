package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static modele.plateau.Salle.SIZE_X;
import static modele.plateau.Salle.SIZE_Y;

/**
 * Donne un accès à un ensemble outils tel que random
 * @classe Outils
 */
public class Outils {

    /**
     * Renvoie un nombre entier aléatoire compris entre min et max.
     *
     * @param max la valeur maximum que peut prendre l'entier aléatoire.
     * @param min la valeur minimum que peut prendre l'entier aléatoire.
     * @return l'entier aléatoire généré.
     * @throws IllegalArgumentException si min est >= à max.
     */

    public static int nombreAleatoire(int min, int max) throws IllegalArgumentException {
        if (min >= max) {
            throw new IllegalArgumentException("La valeur minimale doit être strictement "
                    + "inférieure à la valeur maximale. ");
        }

        int etendue = max - min + 1;

        return ((int) (Math.random() * etendue)) + min;
    }

    public static int[][] TabCharToTabInt( char[][] TabChar){
        int[][] TabInt = new int[SIZE_X][SIZE_Y];

        for (int i = 0; i < SIZE_X; i++){
            for (int j = 0; j < SIZE_Y; j++){
                TabInt[i][j] = Character.getNumericValue(TabChar[i][j]);
            }
        }

        return TabInt;
    }


    /**
     * Permet de lire un ficher txt caractère par caractère pour placer ces caractères dans un tableau 2D de caractères
     * @param tab tableau 2D qui va acceuillir les caractères
     * @param size_x la valeur entière qui correspond au nombre de lignes du tableau 2D
     * @param size_y la valeur entière qui correspond au nombre de colonnes du tableau 2D
     * @param nomSalle chaîne de caractères qui correspond au nom de la salle choisie (fichier txt)
     * @param type type du fichier à lire (0 -> caractère des objets, 1 -> indice des objets, 2 -> couleur des objets)
     */
    public static void convertionTxtToTableau2D(char[][] tab, int size_x, int size_y, String nomSalle, int type){

        String nomFichier = "src/modele/plateau/" + nomSalle + ".txt"; //Chemin du fichier par défaut

        switch (type) { //Chemin du fichier à lire selon le type
            case 0 -> nomFichier = "src/modele/plateau/" + nomSalle + ".txt";
            case 1 -> nomFichier = "src/modele/plateau/" + nomSalle + "Indices.txt";
            case 2 -> nomFichier = "src/modele/plateau/" + nomSalle + "Couleurs.txt";
            default -> System.out.println("Mauvais type, le type doit être un int compris entre 0 et 2 inclus");
        }

        File file = new File(nomFichier);

        if(!file.exists()) {                                    //Si le fichier n'existe pas, on en créer un

            try{
                file.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        else {

            //Essayer de se connecter au fichier
            try{

                FileReader lecture = new FileReader(file);

                char [] a = new char[size_x*size_y];

                lecture.read(a);                                //lire le contenu du fichier dans un tableau 1D

                int i = 0;

                for (int y = 0; y < size_y; y++){

                    for (int x = 0; x < size_x; x++){
                        tab[x][y] = a[i];                      //On transfère les données du tableau 1D dans le 2D
                        i++;
                    }
                }

                lecture.close();                               //Fin de lecture

            } catch (IOException e){
                e.printStackTrace();
                System.out.println("Erreur lors de la connection au fichier");
            }

        }

    }

}
