package modele.plateau;

/**
 * C'est une EntiteStatique non traversable
 * @classe Mur
 * @superClasse EntiteStatique
 */
public class Mur extends EntiteStatique {

    /**
     * Constructeur de la classe Mur
     * @param _jeu
     */
    public Mur(Jeu _jeu) { super(_jeu); }

    /**
     *  Fonction qui retourne un booléen qui dit qu'un mur n'est pas traversable
     * @return boolean qui est à Faux
     */
    @Override
    public boolean traversable() {
        return false;
    }
}
