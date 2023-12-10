package modele.plateau;

/**
 * Permet d'accéder à la salle suivante
 * @classe Porte
 * @superClasse EntiteStatique
 */
public class Porte extends EntiteStatique {

    private boolean ouverte;
    private boolean finale;
    private int indice;
    private char couleur;


    /**
     * Constructeur de Porte
     * @param _jeu
     */
    public Porte(Jeu _jeu, int _indice, char _couleur, boolean _finale) {
        super(_jeu);
        ouverte = false;
        finale = _finale;
        indice = _indice;
        couleur = _couleur;
    }


    public boolean getOuverte(){ return ouverte; }
    public  int getIndice(){ return indice; }
    public char getCouleur(){ return couleur; }
    public boolean getFinale(){ return finale; }


    @Override
    public boolean traversable() {
        return ouverte;
    }


    public void porteOuverte(){
        ouverte = true;
    }
}
