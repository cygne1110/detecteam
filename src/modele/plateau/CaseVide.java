package modele.plateau;

/**
 * C'est une case qui lorsqu'on marche dessus, tue le héro.
 * On peut passer par-dessus en sautant
 * @classe CaseVide
 * @superClasse CasePiegeStatique
 */

public class CaseVide extends CasePiegeStatique {
    /**
     * Constructeur de CaseVide
     * @param _jeu
     */
    public CaseVide(Jeu _jeu) {
        super(_jeu,'V');
    }

    @Override
    public boolean traversable() {
        return true;
    }

    @Override
    public void effet(Heros h) {
        h.setNbVie(0);
        super.activer();
        if(h.getTypeStatut() != super.getType())
            h.setStatut(super.quelStatut(super.getType()));
    }

}