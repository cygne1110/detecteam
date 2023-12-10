/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import Util.Outils;

import java.util.Observable;
import java.util.Queue;

/**
 * Nerf central des modèles
 * @classe  Jeu
 * @superClasse Observable
 * @interface Runnable
 */
public class Jeu extends Observable implements Runnable {

    public static final int SIZE_X = 20; // Taille maximale d'un terrain en largeur
    public static final int SIZE_Y = 10; // Taille maximale d'un terrain en longueur

    public final int NB_TOUR  = 10; // Pour les piéges mobiles

    private final int pause = 200; // période de rafraichissement

    private Heros heros; // Le héros du jeu
    private Salle salle; // La salle dans laquel est le héro

    protected EntiteStatique[][] grilleEntitesStatiques = new EntiteStatique[SIZE_X][SIZE_Y];

    private CompteurGlobale tour; // Les tours boucle de 0 à 10

    private final int MAX_PIEGE_MOBILE = 5; // Nombre maximal de piège mobile pouvant être placé
    private CasePiegeMobile[] tabCasePiegeMobile; // Tableau qui contient les piéges mobiles placé sur le terrain
    int compteurPiegeMobile; // Indique le nombre de piège présent dans la salle ( <MAX_PIEGE_MOBILE )



    ///////////////////////////////////////////////////////////////////////////
    //Constructeur
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Constructeur de Jeu
     */
    public Jeu() {
        heros = new Heros(this, 0, 0, 'B');
        tour = new CompteurGlobale(NB_TOUR);
        tabCasePiegeMobile = new CasePiegeMobile[MAX_PIEGE_MOBILE];
        compteurPiegeMobile = 0;
        salle = new Salle(this);
        initialisationDesEntites();
    }



    ///////////////////////////////////////////////////////////////////////////
    //Getters
    ///////////////////////////////////////////////////////////////////////////

    public Salle getSalle(){return salle;}


    public Heros getHeros() {
        return heros;
    }


    public CompteurGlobale getTour() { return tour; }


    public EntiteStatique getEntite(int x, int y) {
        if (x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y) {
            // L'entité demandée est en-dehors de la grille
            return null;
        }
        return grilleEntitesStatiques[x][y];
    }


    public int getMAX_PIEGE_MOBILE() {
        return MAX_PIEGE_MOBILE;
    }

    public EntiteStatique[][] getGrilleEntitesStatiques(){
        return grilleEntitesStatiques;
    }



    ///////////////////////////////////////////////////////////////////////////
    //Setters
    ///////////////////////////////////////////////////////////////////////////

    public void setEntite(int x, int y, EntiteStatique e) {
        this.grilleEntitesStatiques[x][y] = e;
    }

    //public void setPorte() { }



    ///////////////////////////////////////////////////////////////////////////
    //Fonction piége mobile
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Active les piéges en fonction du tour auquel on est (tour varie entre 0 et 10)
     */
    public void actPiegeMobile(){
        for (int i = 0 ; i < compteurPiegeMobile ; i++){
            tabCasePiegeMobile[i].visible(tour.getCompteur());
        }
    }


    /**
     * Ajoute un piége mobile
     * @return casePiqueMobile ou null en fonction de s'il reste de la place pour rajouter des pièges mobiles
     */
    public CasePiqueMobile ajoutPiegeMobile(){
        if(this.compteurPiegeMobile < MAX_PIEGE_MOBILE) {
            CasePiqueMobile c = new CasePiqueMobile(this, Outils.nombreAleatoire(2,6));
            this.tabCasePiegeMobile[compteurPiegeMobile] = c;
            this.compteurPiegeMobile++;
            return c;
        }
        return null;
    }



    ///////////////////////////////////////////////////////////////////////////
    //Fonction ajout et initialisation à la salle
    ///////////////////////////////////////////////////////////////////////////

    protected void addEntiteStatique(EntiteStatique e, int x, int y) {
        grilleEntitesStatiques[x][y] = e;
    }


    /**
     * Ajoute des objets au terrain (coeur,cle,CapsuleEau,antipoison,....)
     */
    private void initialisationDesPickUp() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                if (grilleEntitesStatiques[x][y] == null) {
                    grilleEntitesStatiques[x][y] = new CaseNormale(this);
                    /*
                    if (Outils.nombreAleatoire(1, 75) == 12) {// Clé 1chance/75
                        ((CaseNormale) grilleEntitesStatiques[x][y]).setObjet(new Cle());
                    }
                    else if (Outils.nombreAleatoire(1, 50) == 13) { // Capsule d'eau 1chance/50
                        ((CaseNormale) grilleEntitesStatiques[x][y]).setObjet(new CapsuleEau());
                    }
                    else if (Outils.nombreAleatoire(1, 75) == 14) { // Coffre 1chance/75
                        ((CaseNormale) grilleEntitesStatiques[x][y]).setObjet(new Coffre());
                    }
                    else if (Outils.nombreAleatoire(1, 25) == 15) { // Coffre 1chance/25
                        ((CaseNormale) grilleEntitesStatiques[x][y]).setObjet(new Coeur());
                    }
                    else if (Outils.nombreAleatoire(1, 25) == 16) { // Coffre 1chance/25
                        ((CaseNormale) grilleEntitesStatiques[x][y]).setObjet(new AntiPoison());
                    }
                    */
                }
            }
        }
    }


    /**
     * Ajoute des Entités statiques au terrain (piéges,mur,caseUnique,caseNormale,...)
     */
    private void initialisationDesEntites() {
        heros.setX(4);heros.setY(4);


        // murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addEntiteStatique(new Mur(this), x, 0);
            addEntiteStatique(new Mur(this), x, 9);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addEntiteStatique(new Mur(this), 0, y);
            addEntiteStatique(new Mur(this), 19, y);
        }

        addEntiteStatique(new Mur(this), 5, 1);
        addEntiteStatique(new Mur(this), 5, 2);

        addEntiteStatique(new CaseNormale(this), 6, 7);
        addEntiteStatique(new CaseNormale(this), 5, 8);

        addEntiteStatique(new Levier(this, 1, 'B'),8,5);
        addEntiteStatique(new CaseNormale(this),4,7);
        addEntiteStatique(new CaseNormale(this),2,3);
        addEntiteStatique(new CaseNormale(this),2,2);
        addEntiteStatique(new CaseNormale(this),6,6);

        addEntiteStatique(new CaseNormale(this),7,6);
        addEntiteStatique(new CaseNormale(this),4,8);

        addEntiteStatique(new Porte(this, 1, 'B', true), 19, 5);

        initialisationDesPickUp();
    }



    ///////////////////////////////////////////////////////////////////////////
    //Fonction de réinitialisation
    ///////////////////////////////////////////////////////////////////////////

    protected void reinitialisationDeSalle(){
        //Réinitialisation de la salle à nulle
        for(int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleEntitesStatiques[x][y] = null;

            }
        }
        for (int x = 0; x < MAX_PIEGE_MOBILE ; x++){ // On réinitialise le nombre de piége aussi
            tabCasePiegeMobile[x] = null;
        }
        compteurPiegeMobile = 0;
    }


    /**
     * Redémarre une partie
     */
    public void restart(){
        heros.resetHero();
        reinitialisationDeSalle();
        initialisationDesEntites();
    }


    ///////////////////////////////////////////////////////////////////////////
    //Fonction start and run
    ///////////////////////////////////////////////////////////////////////////

    public void start(){
        new Thread(this).start();
    }


    public void run() {

        while (true) {
            Coord2D dest = new Coord2D(10, 7);
            setChanged();
            notifyObservers();
            if(getHeros().getNbVie() < 1) {
                restart();
            }

            Queue<Coord2D> path = getHeros().pathFind(dest);
            Coord2D next = path.remove();
            System.out.println(next.x + " " + next.y);

            try {
                Thread.sleep(pause + 2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }



    ///////////////////////////////////////////////////////////////////////////
    //Fonction sur les voisins d'une case
    ///////////////////////////////////////////////////////////////////////////

    public EntiteStatique getVoisinE(int x,int y){
        return grilleEntitesStatiques[x+1][y];
    }


    public EntiteStatique getVoisinS(int x,int y){
        return grilleEntitesStatiques[x][y+1];
    }


    public EntiteStatique getVoisinO(int x,int y){
        return grilleEntitesStatiques[x-1][y];
    }


    public EntiteStatique getVoisinN(int x,int y){
        return grilleEntitesStatiques[x][y-1];
    }


    public EntiteStatique getVoisinNE(int x,int y){
        return grilleEntitesStatiques[x+1][y-1];
    }


    public EntiteStatique getVoisinNO(int x,int y){
        return grilleEntitesStatiques[x-1][y-1];
    }


    public EntiteStatique getVoisinSE(int x,int y){
        return grilleEntitesStatiques[x+1][y+1];
    }


    public EntiteStatique getVoisinSO(int x,int y){
        return grilleEntitesStatiques[x-1][y+1];
    }


    /**
     * Permet de récupérer l'entité voisine d'une case(x,y) avec possibilité de choisir quel voisin on veut
     * 1 : Nord, 2 : Nord-Est, 3 : Est, 4 : Sud-Est, 5 : Sud, 6 : Sud-Ouest, 7 : Ouest, 8 : Nord-Ouest
     */
    public EntiteStatique getVosinInd(int indiceChoix,int x,int y) throws IllegalArgumentException{
        return switch (indiceChoix) {
            case 1 -> getVoisinN(x, y);
            case 2 -> getVoisinNE(x, y);
            case 3 -> getVoisinE(x, y);
            case 4 -> getVoisinSE(x, y);
            case 5 -> getVoisinS(x, y);
            case 6 -> getVoisinSO(x, y);
            case 7 -> getVoisinO(x, y);
            case 8 -> getVoisinNO(x, y);
            default -> throw new IllegalArgumentException("La valeur minimale est 1 "
                    + "la valeur maximale est 8. ");
        };
    }
}
