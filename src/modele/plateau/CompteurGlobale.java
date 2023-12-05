package modele.plateau;

/**
 * C'est un compteur qui compte jusqu'au max puis repart de 0
 * @classe CompteurGlobale
 */
public class CompteurGlobale {
    private int compteur;
    private int max;

    /**
     * Constructeur de CompteurGlobale
     * @param max
     */
    public CompteurGlobale(int max) {
        this.max = max;
        this.compteur = 0;
    }


    public int getCompteur() {
        return compteur;
    }


    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }


    public void incr(){
        compteur++;
        if(this.compteur > max){
            compteur = 0;
        }
    }
}
