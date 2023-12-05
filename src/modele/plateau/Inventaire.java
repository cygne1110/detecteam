package modele.plateau;

/**
 * C'est une collection de ramassables
 */
public class Inventaire {

    ///////////////////////////////////////////////////////////////////////////////
    // Attributs
    ///////////////////////////////////////////////////////////////////////////////

    private Ramassable[] tabInventaire;
    private int nbObjetActuel;
    private int nbCapsuleEau;
    private int nbCle;
    private final int taille;



    ///////////////////////////////////////////////////////////////////////////////
    // Constructeur
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Constructeur de l'Inventaire
     * @param taille
     */
    public Inventaire(int taille) {
        this.taille = taille;
        this.tabInventaire = new Ramassable[taille]; // Un inventaire ne peut contenir que MAX_TAILLE ramassables
        this.nbObjetActuel =  0;
        this.nbCapsuleEau = 0;
        this.nbCle = 0;
    }



    ///////////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////////

    public int getTaille() {
        return this.taille;
    }


    public int getNbObjetActuel() {
        return nbObjetActuel;
    }


    public int getNbCapsuleEau() {
        return nbCapsuleEau;
    }


    public int getNbCle() {
        return nbCle;
    }


    public Ramassable getObjet(int indice){
        return tabInventaire[indice];
    }



    ///////////////////////////////////////////////////////////////////////////////
    // Setters
    ///////////////////////////////////////////////////////////////////////////////

    public void setNbObjetActuel(int nbObjetActuel) {
        this.nbObjetActuel = nbObjetActuel;
    }


    public void setNbCapsuleEau(int nbCapsuleEau) {
        this.nbCapsuleEau = nbCapsuleEau;
    }


    public void setNbCle(int nbCle) {
        this.nbCle = nbCle;
    }




    ///////////////////////////////////////////////////////////////////////////////
    // Fonction d'ajout et suppression
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Prend un ramassable en paramètre et l'ajoute à l'inventaire
     * @param objet
     */
    public boolean ajoutObjet(Ramassable objet){
        if(this.nbObjetActuel < 30){ // Si reste de la place dans l'inventaire
            this.tabInventaire[nbObjetActuel] = objet;
            nbObjetActuel++;
            this.miseAJourInventaire(objet,'A'); // Met à jour les variables de comptage en signalant un ajout 'A'
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * Supprime un objet de l'inventaire puis effectue un décalage (Reorganise le tableau)
     * @param indiceTab
     */
    public void supprimerObjet(int indiceTab){
        try {
            this.miseAJourInventaire(this.tabInventaire[indiceTab], 'S'); // Met à jour les variables de comptage en signalant une suppression 'S'
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            System.exit(-1);
        }

        if(indiceTab == nbObjetActuel-1){ // Si on efface le dernière élément
            this.tabInventaire[indiceTab] = null;
        }
        else{ // L'objet supprimer ce trouve en plein milieu du tableau donc obligation de reorganiser après suppression
            this.tabInventaire[indiceTab] = null;
            this.reOrganisationTab(indiceTab);
        }
        this.nbObjetActuel--;
    }



    ///////////////////////////////////////////////////////////////////////////////
    // Fonctions autres
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Recherche un objet dans le tableau et renvoie l'indice de sa position
     * Renvoi -1 si la recherche ne mène nulle part
     * @param objet
     */
    public int recherche(char objet){
        int i = 0;
        int indice = -1;
        while (i < this.nbObjetActuel && indice == -1){
            if(objet == 'C'){
                if(this.tabInventaire[i] instanceof Cle){
                    indice = i;
                }
            }
            else if(objet == 'E'){
                if(this.tabInventaire[i] instanceof CapsuleEau){
                    indice = i;
                }
            }
            i++;
        }
        return indice;
    }


    /**
     * Reorganise le tableau à partir de l'indice qui met le tableau à défaut (Case vide)
     * @param indDecalage
     */
    private void reOrganisationTab(int indDecalage){
        for (int i = indDecalage ; i <= this.nbObjetActuel-2 ; i++){
            this.tabInventaire[i] = this.tabInventaire[i+1];
            this.tabInventaire[i+1] = null;
        }
    }


    /**
     *  Met à jour de l'inventaire, quel type d'objet a été ajouté "A"/ supprimé "S"
     * @param objet
     * @param natureAction
     */
    private void miseAJourInventaire(Ramassable objet,char natureAction) {
        if(natureAction == 'A'){ // Mise à jour après un ajout
            if(objet instanceof Cle){nbCle++;}
            else if(objet instanceof CapsuleEau){this.nbCapsuleEau++;}
        }
        else if(natureAction == 'S'){ // Mise à jour après une suppression
            if(objet instanceof Cle){nbCle--;}
            else if(objet instanceof CapsuleEau){this.nbCapsuleEau--;}
        }
    }



    ///////////////////////////////////////////////////////////////////////////
    //Fonction de reset
    ///////////////////////////////////////////////////////////////////////////

    public void resetInventaire(){
        for (int i = 0 ; i < nbObjetActuel ; i++){
            this.tabInventaire[i] = null;
        }
        this.nbObjetActuel =  0;
        this.nbCapsuleEau = 0;
        this.nbCle = 0;
    }
}

