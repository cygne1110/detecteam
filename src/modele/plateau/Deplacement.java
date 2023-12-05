package modele.plateau;

/**
 * C'est les déplacements d'un héro
 * Chaque déplacement à des conséquences
 * @classe Déplacement
 */
public class Deplacement {
    Heros hero;



    ///////////////////////////////////////////////////////////////////////////
    //Constructeurs
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Constructeur de Deplacement
     * @param h
     */
    public Deplacement(Heros h) {
        hero = h;
    }



    ///////////////////////////////////////////////////////////////////////////
    //Fonctions de déplacement
    ///////////////////////////////////////////////////////////////////////////

    public void droite() {
        hero.setOrientation('E');
        if (traversable(hero.getX() + 1, hero.getY())) {
            hero.setX(hero.getX()+1);
            consequenceDeplacement(hero.getX(),hero.getY());
        }
    }


    public void gauche() {
        hero.setOrientation('O');
        if (traversable(hero.getX() - 1, hero.getY())) {
            hero.setX(hero.getX()-1);
            consequenceDeplacement(hero.getX(),hero.getY());
        }
    }


    public void bas() {
        hero.setOrientation('S');
        if (traversable(hero.getX(), hero.getY() + 1)) {
            hero.setY(hero.getY()+1);
            consequenceDeplacement(hero.getX(),hero.getY());
        }
    }


    public void haut() {
        hero.setOrientation('N');
        if (traversable(hero.getX(), hero.getY() - 1)) {
            hero.setY(hero.getY()-1);;
            consequenceDeplacement(hero.getX(),hero.getY());
        }
    }



    ///////////////////////////////////////////////////////////////////////////
    //Autres fonctions
    ///////////////////////////////////////////////////////////////////////////

    private boolean traversable(int x, int y) {
        if (x > 0 && x < Jeu.SIZE_X && y > 0 && y < Jeu.SIZE_Y) {
            return hero.getJeu().getEntite(x, y).traversable();
        } else {
            return false;
        }
    }


    public void consequenceDeplacement(int x, int y){
        hero.EstSurCasePiege(x, y);
        hero.LaCaseAUnObjet(x, y);
        hero.EstSurPorte(x,y);
        hero.EstSurLevier(x,y);
        if(hero.getStatut() != null){
            hero.getStatut().decrCompteur(hero);
        }
    }
}
