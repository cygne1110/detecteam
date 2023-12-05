package modele.plateau;

/**
 * C'est le statut d'un joueur (Empoisonné,...)
 * @classe Statut
 */
public abstract class Statut {
    private int compteur;
    private int compteurMax;

    /**
     * Constructeur de Statut
     * @param compteurMax
     */
    public Statut(int compteurMax) {
        this.compteurMax = compteurMax;
        this.compteur = compteurMax;
    }

    public void decrCompteur(Heros h){
        if(compteur <= 0){
            compteur = compteurMax;
            effet(h);
        }
        else{
            compteur--;
        }
    }

    /**
     * Applique des effets sur le héros en fonction du piège activé
     * @param h le héros
     */
    public abstract void effet(Heros h);

}
