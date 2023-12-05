package modele.plateau;

/**
 * Entités qui ne bougent pas (murs...)
 */
public abstract class EntiteStatique {
    protected Jeu jeu;

    /**
     * Constructeur d' EntiteStatique
     * @param _jeu
     */
    public EntiteStatique(Jeu _jeu) {
        jeu = _jeu;
    }

    public abstract boolean traversable();

}