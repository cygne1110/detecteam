package modele.plateau;

/**
 * Permet d'ouvrir une porte
 * @classe Levier
 * @superClasse EntiteStatique
 */
public class Levier extends EntiteStatique {
    private boolean active;



    /**
     * Constructeur de Levier
     * @param _jeu
     */
    public Levier(Jeu _jeu) {
        super(_jeu);
        active = false;
    }

    public boolean getActive() {
        return active;
    }

    public void activerLevier(){
        active = true;
    }

    @Override
    public boolean traversable() {
        return true;
    }
}
