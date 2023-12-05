package modele.plateau;

/**
 * C'est une case sur laquelle on ne peux passer qu'une seule fois après il faudra utiliser une capsule d'eau
 * Pour le rendre réutilisable
 * @classe CaseUnique
 * @superClasse CasePiegeStatique
 */
public class CaseUnique extends CasePiegeStatique {
    public CaseUnique(Jeu _jeu) {
        super(_jeu,'N');
    }

    @Override
    public void effet(Heros h) {
    }

    /**
     * La case est traversable si elle n'a pas encore été activée
     * Elle s'active si on marche dessus
     */
    @Override
    public boolean traversable() {
        return !super.getActiver();
    }
}
