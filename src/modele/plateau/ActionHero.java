package modele.plateau;

/**
 * Action d'un héro
 * @classe ActionHero
 */
public class ActionHero {
    private Heros hero;

    /**
     * Constructeur avec paramètre
     */
    public ActionHero(Heros h) {
        hero = h;
    }


    /**
     * Récupère l'entité qui se situe en face du héros
     * @return EntiteStatique
    */
    private EntiteStatique entiteEnFace(){
        if (hero.getOrientation() == 'N') {
            return hero.getJeu().getEntite(hero.getX(), hero.getY() - 1);
        } else if (hero.getOrientation() == 'O') {
            return hero.getJeu().getEntite(hero.getX() - 1, hero.getY());
        } else if (hero.getOrientation() == 'S') {
            return hero.getJeu().getEntite(hero.getX(), hero.getY() + 1);
        } else if (hero.getOrientation() == 'E') {
            return hero.getJeu().getEntite(hero.getX() + 1, hero.getY());
        }
        return null;
    }



    ///////////////////////////////////////////////////////////////////////////
    //Fonctions Action
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Lance une capsule pour désactiver un case unique
     */
    public void lancerCapsule() {
        if (hero.getSac_a_dos().getNbCapsuleEau() > 0) {
            EntiteStatique e = entiteEnFace();

            if (e instanceof CaseUnique) {
                if (((CaseUnique) e).getActiver()) {
                    hero.getSac_a_dos().supprimerObjet(hero.getSac_a_dos().recherche('E'));
                    ((CaseUnique) e).desactiver();
                }
            }
        }
    }

    /**
     * Action ouverture de porte
     */
    public void ouvrirPorte(){
        if (hero.getSac_a_dos().getNbCle() > 0){
            EntiteStatique e = entiteEnFace();
            if(e instanceof Porte){
                if(!(((Porte) e).getOuverte())) {
                    ((Porte) e).porteOuverte();
                    hero.getSac_a_dos().supprimerObjet(hero.getSac_a_dos().recherche('C'));
                }
            }
        }

    }


    /**
     * Action ouverture de coffre
     */
    public void ouvertureCoffre(Coffre c) {
        int i = 1;
        int j = 0;
        EntiteStatique e;
        Ramassable o = null;
        while(i < 9 && j < 4 ) {
            e = hero.getJeu().getVosinInd(i,hero.getX(),hero.getY());
            o = c.getCoffreObjet().getObjet(j);
            if (e instanceof CaseNormale) {
                if (((CaseNormale) e).getObjet() == null) {
                    ((CaseNormale) e).setObjet(o);
                    j++;
                }
            }
            i++;
        }
        e = null;
        ((Coffre)c).getCoffreObjet().resetInventaire();
        o = null;
    }

    /**
     * Sauter par dessus un trou
     */
    public void sauter(){

        EntiteStatique e = null;
        EntiteStatique ee = null;


        switch (hero.getOrientation()) {
            case 'E' -> {
                e = hero.getJeu().getEntite(hero.getX() + 1, hero.getY());
                ee = hero.getJeu().getEntite(hero.getX() + 2, hero.getY());
                if (e instanceof CaseVide) {
                    if (!(ee instanceof CaseVide) & !(ee instanceof Mur) & !(ee instanceof CaseUnique)) {
                        hero.setX(hero.getX() + 2);
                    }
                }
            }
            case 'O' -> {
                e = hero.getJeu().getEntite(hero.getX() - 1, hero.getY());
                ee = hero.getJeu().getEntite(hero.getX() - 2, hero.getY());
                if (e instanceof CaseVide) {
                    if (!(ee instanceof CaseVide) & !(ee instanceof Mur) & !(ee instanceof CaseUnique)) {
                        hero.setX(hero.getX() - 2);
                    }
                }
            }
            case 'N' -> {
                e = hero.getJeu().getEntite(hero.getX(), hero.getY() - 1);
                ee = hero.getJeu().getEntite(hero.getX(), hero.getY() - 2);
                if (e instanceof CaseVide) {
                    if (!(ee instanceof CaseVide) & !(ee instanceof Mur) & !(ee instanceof CaseUnique)) {
                        hero.setY(hero.getY() - 2);
                    }
                }
            }
            case 'S' -> {
                e = hero.getJeu().getEntite(hero.getX(), hero.getY() + 1);
                ee = hero.getJeu().getEntite(hero.getX(), hero.getY() + 2);
                if (e instanceof CaseVide) {
                    if (!(ee instanceof CaseVide) & !(ee instanceof Mur) & !(ee instanceof CaseUnique)) {
                        hero.setY(hero.getY() + 2);
                    }
                }
            }
            default -> System.out.println("Le caractère n'est pas valide");
        }
    }
}
