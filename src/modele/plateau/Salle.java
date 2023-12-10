package modele.plateau;

import Util.Outils;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Classe qui permet la génération aléatoire d'une salle
 * @classe Salle
 */
public class Salle{

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    private Jeu jeu;

    private String nomSalle;



    /**
     * Constructeur de la classe Salle
     * @param _jeu
     */
    public Salle(Jeu _jeu){

        jeu = _jeu;
        nomSalle = "salleRectangle";
    }

    /**
     * Getter de nomSalle
     */
    public String getNomSalle(){ return nomSalle; }

    /**
     * Setter de nomSalle
     * @param _nomSalle
     */
    public void setNomSalle( String _nomSalle){ nomSalle = _nomSalle; }

    /**
     * Méthode qui génère une salle aléatoirement
     * @param
     */
    public void salleAleatoire(Heros heros1, Heros heros2, Heros heros3, Heros heros4){

        jeu.reinitialisationDeSalle();//Réinisialise le tableau contenant la salle précédente

        heros1.setState(0);
        heros2.setState(0);
        heros3.setState(0);
        heros4.setState(0);

        nomSalle = AleaNomSalle();                                      //Nom de la salle choisie aléatoirement

        char[][] tab = new char[SIZE_X][SIZE_Y];                        //Création du tableau qui stockera les caractères du fichier txt de la salle
        Outils.convertionTxtToTableau2D(tab, SIZE_X, SIZE_Y, nomSalle,0);    //Permet de lire et d'écrire dans tab le fichier txt qui correspond à la salle choisie aléatoirement

        char[][] tabCharIndice = new char[SIZE_X][SIZE_Y];
        Outils.convertionTxtToTableau2D(tabCharIndice, SIZE_X, SIZE_Y, nomSalle, 1);
        int[][] tabIndice = Outils.TabCharToTabInt(tabCharIndice);

        char[][] tabCouleur = new char[SIZE_X][SIZE_Y];
        Outils.convertionTxtToTableau2D(tabCouleur, SIZE_X, SIZE_Y, nomSalle, 2);
        System.out.println(tab);

        char c;                                                         //caractère qui nous permet de savoir le type de l'élément à placer
        int indice_courant;
        char couleur_courante;

        int nb_cle = 1;
        int nb_coffre = Outils.nombreAleatoire(0,4);
        int nb_capsule = Outils.nombreAleatoire(2,5);
        int nb_caseEnflammee = Outils.nombreAleatoire(5,10);
        int nb_murSuppl = Outils.nombreAleatoire(5,10);
        int nb_trou = Outils.nombreAleatoire(5,10);
        int nb_pique = Outils.nombreAleatoire(5,10);
        int nb_piquePoison = Outils.nombreAleatoire(1,3);
        int nb_coeur = Outils.nombreAleatoire(1,4);
        int nb_PiqueMobile = Outils.nombreAleatoire(1,3);

        //Parcourt le pattern de la salle et la créer
        for(int i = 0; i < SIZE_X; i++){

            for(int j = 0; j < SIZE_Y; j++){

                c = tab[i][j];
                indice_courant = tabIndice[i][j];
                couleur_courante = tabCouleur[i][j];

                switch (c){

                    //Placer un mur
                    case 'M':
                        jeu.addEntiteStatique(new Mur(jeu), i, j);
                        break;

                    //Placer une porte
                    case 'P':
                        if(couleur_courante == 'B'){
                            jeu.addEntiteStatique(new Porte(jeu, indice_courant, couleur_courante, true), i, j);
                        }else {
                            jeu.addEntiteStatique(new Porte(jeu, indice_courant, couleur_courante, false), i, j);
                        }
                        break;

                    //Placer les agents
                    case 'J':
                        jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                        if(indice_courant == 1) {
                            heros1.setX(i);
                            heros1.setY(j);
                        }
                        if(indice_courant == 2) {
                            heros2.setX(i);
                            heros2.setY(j);
                        }
                        if(indice_courant == 3) {
                            heros3.setX(i);
                            heros3.setY(j);
                        }
                        if(indice_courant == 4) {
                            heros4.setX(i);
                            heros4.setY(j);
                        }
                        break;

                    //Placer un levier
                    case 'L':
                        jeu.addEntiteStatique(new Levier(jeu,indice_courant, couleur_courante), i, j);
                        break;

                    //Placer différents types de cases
                    case 'V':
                        /*
                        int k = Outils.nombreAleatoire(1,25);

                        //Placer une clé
                        if ((k == 1) & (nb_cle != 0) & (nomSalle != "salleLabyrinthe.txt")){

                            jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                            ((CaseNormale)jeu.grilleEntitesStatiques[i][j]).setObjet(new Cle());
                            nb_cle--;
                        }

                        //Placer une capsule
                        else if((k == 2) & (nb_capsule != 0)){

                            jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                            ((CaseNormale)jeu.grilleEntitesStatiques[i][j]).setObjet(new CapsuleEau());
                            nb_capsule--;
                        }

                        //Placer un coffre
                        else if((k == 3) & (nb_coffre != 0) & (nomSalle != "salleTresors.txt")){

                            jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                            ((CaseNormale)jeu.grilleEntitesStatiques[i][j]).setObjet(new Coffre());
                            nb_coffre--;
                        }

                        //Placer un piège de feu (case unique)
                        else if((k == 4) & (nb_caseEnflammee != 0)){

                            jeu.addEntiteStatique(new CaseUnique(jeu), i, j);
                            nb_caseEnflammee--;
                        }

                        //Placer un mur
                        else if((k == 5) & (nb_murSuppl != 0) & (nomSalle != "salleLabyrinthe.txt")){

                            jeu.addEntiteStatique(new Mur(jeu), i, j);
                            nb_murSuppl--;
                        }

                        //Placer un trou (case vide)
                        else if((k == 6) & (nb_trou != 0)){

                            jeu.addEntiteStatique(new CaseVide(jeu), i, j);
                            nb_trou--;
                        }

                        //Placer un piège de piques non empoisonnés
                        else if((k == 7) & (nb_pique != 0)){

                            jeu.addEntiteStatique(new CasePique(jeu,'N'), i, j);
                            nb_pique--;
                        }

                        //Placer un piège de piques empoisonnés
                        else if((k == 8) & (nb_piquePoison != 0)){

                            jeu.addEntiteStatique(new CasePique(jeu,'P'), i, j);
                            nb_piquePoison--;
                        }

                        //Placer une vie
                        else if((k == 9) & (nb_coeur != 0)){
                            jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                            ((CaseNormale)jeu.grilleEntitesStatiques[i][j]).setObjet(new Coeur());
                            nb_coeur--;
                        }

                        //Placer un piège mobile de piques non empoisonnés
                        else if((k == 10) & (nb_PiqueMobile != 0)){
                            jeu.addEntiteStatique(jeu.ajoutPiegeMobile(), i, j);
                            nb_PiqueMobile--;
                        }



                        //Placer une case normale
                        else {
                            jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                        }
                        */
                        jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                        break;

                    //Placer une case normale
                    case 'O':
                        jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                        break;

                    //Placer un piège de feu (case unique) qui est activé
                    case 'S':
                        CaseUnique caseUnique = new CaseUnique(jeu);
                        caseUnique.activer();
                        jeu.addEntiteStatique(caseUnique, i, j);
                        break;

                    //Placer une case normale qui détient une clé
                    case 'C':
                        if(nb_cle != 0){
                            jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                            ((CaseNormale)jeu.grilleEntitesStatiques[i][j]).setObjet(new Cle());
                            nb_cle--;
                        }
                        break;

                    //Placer une case normale qui détient un coffre (trésors)
                    case 'T':
                        jeu.grilleEntitesStatiques[i][j] = new CaseNormale(jeu);
                        ((CaseNormale)jeu.grilleEntitesStatiques[i][j]).setObjet(new Coffre());
                        break;

                    default:
                        System.out.println("Le caractère n'est pas valide");
                }

            }
        }

    }

    /**
     * Fonction qui donne le nom d'un pattern de salle
     * @return nomSalle
     */
    private String AleaNomSalle() {
        nomSalle = "salleRectangle";     //Valeur par défaut
        int i = Outils.nombreAleatoire(1,3);    //Pour choisir le nom de la salle aléatoirement

        switch (i) {
            case 1 -> nomSalle = "salleRectangle";
            //case 2 -> nomSalle = "salleRonde";
            case 2 -> nomSalle = "salleSeparation";
            //case 4 -> nomSalle = "salleLabyrinthe";
            case 3 -> nomSalle = "salleTresors";
            default -> System.out.println("Chiffre incorrect");
        }
        return nomSalle;
    }
}


/* Ce a quoi ressemble une salle rectangle

Salle rectangle vide :
MMMMMMMMMMMMMMMMMMMM
MVVVVVVVVVVVVVVVVVVM
MVVVVVVVVVVVVVVVVVVM
MVVVVVVVVVVVVVVVVVVM
PJVVVVVVVVVVVVVVVVOP
MVVVVVVVVVVVVVVVVVVM
MVVVVVVVVVVVVVVVVVVM
MVVVVVVVVVVVVVVVVVVM
MVVVVVVVVVVVVVVVVVVM
MMMMMMMMMMMMMMMMMMMM

Salle rectangle avec Objets :
MMMMMMMMMMMMMMMMMMMM
MLVPVVVVVVVVVVVVVVVM
MMMMVLVVVVVVVVVVVVVM
MJVVVVVVVVVVVVVVVVVM
PJVJVVVVVVVVVVVVVVOP
MJVVVVVVVVVVVVVVVVVM
MMMMMMMMMPMMMMMMMMMM
MVVVVLVVVVVVVVVVVVVM
MVVVVVVVVVVVVVVVVVVM
MMMMMMMMMMMMMMMMMMMM

Salle rectangle indices :
00000000000000000000
01020000000000000000
00000300000000000000
01000000000000000000
02030000000000000001
01000000000000000000
00000000030000000000
00000200000000000000
00000000000000000000
00000000000000000000

Salle rectangle couleurs :
AAAAAAAAAAAAAAAAAAAA
ABAVAAAAAAAAAAAAAAAA
AAAAARAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAB
AAAAAAAAAAAAAAAAAAAA
AAAAAAAAARAAAAAAAAAA
AAAAAVAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAA

*/


/* Ce a quoi ressemble une salle ronde

salle ronde vide :
MMMMMMMMMMMMMMMMMMMM
MMMVVVVVVVVVVVVVVMMM
MMVVVVVVVVVVVVVVVVMM
MVVVVVVVVVVVVVVVVVVM
PJVVVVVVVVVVVVVVVVOP
MVVVVVVVVVVVVVVVVVVM
MVVVVVVVVVVVVVVVVVVM
MMVVVVVVVVVVVVVVVVMM
MMMVVVVVVVVVVVVVVMMM
MMMMMMMMMMMMMMMMMMMM
 */


/* Ce a quoi ressemble une salle Separation
MMMMMMMMMMMMMMMMMMMM
MJVLMVVVVVVVMVVVVVVM
MJVVPVVVVVVVPVVVVVVM
MVVVMVVVVVVLMVVVVVVM
MMMMMMMMMMMMMVVVVVOP
MVVVMVVVVLVVMVVVVVVM
MJVVMVVVVVVVMLVVVVVM
MJVVPVVVVVVVMVVVVVVM
MVVLMVVVVVVVPVVVVVVM
MMMMMMMMMMMMMMMMMMMM

Indices :
00000000000000000000
01010000000000000000
02005000000030000000
00000000000200000000
00000000000000000004
00000000030000000000
03000000000004000000
04001000000000000000
00050000000020000000
00000000000000000000

Couleurs :
AAAAAAAAAAAAAAAAAAAA
AAARAAAAAAAAAAAAAAAA
AAAAVAAAAAAAVAAAAAAA
AAAAAAAAAAARAAAAAAAA
AAAAAAAAAAAAAAAAAAAB
AAAAAAAAAVAAAAAAAAAA
AAAAAAAAAAAAABAAAAAA
AAAARAAAAAAAAAAAAAAA
AAAVAAAAAAAARAAAAAAA
AAAAAAAAAAAAAAAAAAAA
 */

/* Ce a quoi ressemble une salle Labyrinthe
MMMMMMMMMMMMMMMMMMMM
PJVVVVVOMOVVVVOVVVVM
MMMMMMMOMOMMMMOMMMMM
MOOVVVOOOOMVVMOVVVOM
MOMMMMMMMMMVVMMMMVOM
MOMCMVVVVVVVVMOVVVVM
MOOSMVVVVVVVVVOMVVVM
MMMMMVVVVMVVVMMVMMMM
MVVVVVVVVMVOVVVVVVOP
MMMMMMMMMMMMMMMMMMMM
 */


/* Ce a quoi ressemble une salle aux trésors (sortie)
MVVVVVVVVVVVVVVVVVVV
MVVVVVVVVVVVVVVVVVVV
MVVVVVVVVVVVVVVVVVVV
MJVVVVVVVVVVVVVVVVVV
PJVJVVVVVVVVVVVVVVVV
MJVVVVVVVVVVVVVVVVVV
MVVVVVVVVVVVVVVVVVVV
MVVVVVVVVVVVVVVVVVVV
MVVVVVVVVVVVVVVVVVVV
MVVVVVVVVVVVVVVVVVVV

00000000000000000000
00000000000000000000
00000000000000000000
01000000000000000000
02030000000000000000
04000000000000000000
00000000000000000000
00000000000000000000
00000000000000000000
00000000000000000000
 */
