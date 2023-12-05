package modele.plateau;

/**
 * C'est un piège qui se déplace
 * @classe CasePiegeMobile (ABSTRAITE)
 * @superClasse CasePiege
 */
public abstract class CasePiegeMobile extends CasePiege{
    int modulo; // Les objets se déplace tous les deux tours (tour mod 2), tous les tours 3 (tour mod 3) en fonction du modulo choisi


    /**
     * Constructeur avec paramètre
     */
    public CasePiegeMobile(Jeu _jeu,int modulo) {
        super(_jeu,'N');
        this.modulo = modulo;
    }


    /**
     * Méthode abstraite à définir dans les sous-classes
     * Indique si la case est visible ou non. Tout dépend à quel tour on est dans le jeu
     */
    public abstract void visible(int tour);
}
