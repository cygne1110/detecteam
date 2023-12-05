package modele.plateau;

/**
 * Un objetUtilisable est un ramassable
 * @classe ObjetUtilisable
 * @superClasse
 */
public abstract class ObjetUtilisable extends Ramassable{
    public ObjetUtilisable() {
    }
    public abstract void effet(Heros h);

}
