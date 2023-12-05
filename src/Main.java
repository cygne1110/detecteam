/**
 * ROGUE-LIKE
 * #1 COGONI Guillaume p1810070 Game Master
 * #2 LAPORTE Laetitia p1804311 Développeuse Senior & game designer
 */


import VueControleur.VueControleur;
import modele.plateau.Jeu;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Main {
    public static void main(String[] args) {

        Jeu jeu = new Jeu();

        VueControleur vc = new VueControleur(jeu);

        jeu.addObserver(vc);

        vc.setVisible(true);
        jeu.start();


    }
}
