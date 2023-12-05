package modele.plateau;

/**
 * C'est un piège qui ne se déplace pas
 * @classe CasePiegeStatique (ABSTRAITE)
 * @superClasse CasePiege
 */
public abstract class CasePiegeStatique extends CasePiege{

    /**
     * Constructeur avec paramètre
     */
    public CasePiegeStatique(Jeu _jeu, char type) { super(_jeu, type); }
}
