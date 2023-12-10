package modele.plateau;

/**
 * Permet d'ouvrir une porte
 * @classe Levier
 * @superClasse EntiteStatique
 */
public class Levier extends EntiteStatique {
    private boolean active;
    private int indice;
    private char couleur;


    /**
     * Constructeur de Levier
     * @param _jeu
     */
    public Levier(Jeu _jeu, int _indice, char _couleur) {
        super(_jeu);
        indice = _indice;
        couleur = _couleur;
        active = false;
    }

    public boolean getActive() {
        return active;
    }
    public  int getIndice(){ return indice; }
    public char getCouleur(){ return couleur; }

    public void activerLevier()
    {
        active = true;
    }

    @Override
    public boolean traversable() {
        return true;
    }
}
