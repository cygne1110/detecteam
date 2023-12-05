package modele.plateau;

/**
 * Case normale
 * @classe CaseNormale
 * @superClasse EntiteStatique
 */
public class CaseNormale extends EntiteStatique {
    Ramassable objet;

    /**
     * Constructeur avec paramètre
     */
    public CaseNormale(Jeu _jeu) {
        super(_jeu);
        objet = null;
    }


    /**
     * Permet de récupérer l'objet de la case
     * @return Ramassable objet
     */
    public Ramassable getObjet() {
        return objet;
    }


    /**
     * Permet d'affecter un objet à la case
     */
    public void setObjet(Ramassable objet) {
        this.objet = objet;
    }


    /**
     * Détermine si l'entité est traversable ou non
    */
    @Override
    public boolean traversable() {
        return true;
    }

}
