package com.mygdx.mehelpers.Deplacement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameobjects.Mineur.Etat;

/**
 * Classe gérant le déplacement du mineur en mode fluide
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Fluide extends Deplacement {
    
    /**
     * @param mineur le mineur
     */
    public Fluide(Mineur mineur) {
        super(mineur);
    }
    
    /**
     * Méthode appelée à chaque frame lorsque le mineur est en mode fluide.
     * Celle-ci va faire bouger le mineur dynamiquement en fonction de la
     * direction choisit par le joueur.
     */
    @Override
    public void move() {
        boolean lancerDestruction = false;
        int x = (int) (mineur.getPosition().x + mineur.getLARGEUR()/2);
        int y = (int) mineur.getPosition().y;
        
        switch(mineur.getDirectionMineur()) {
            case Haut:
                if(!collision.isTiledHere(x, y+1)) { // Si pas de bloc en x et y + 1
                    if(!mineur.getEtatMineur().equals(Etat.Sauter)) {
                        velocite.y = mineur.getSAUT_VELOCITE();
                        mineur.setMineurAuSol(false);
                        mineur.setEtatMineur(Etat.Sauter);
                    }
                } else {
                    lancerDestruction = true;
                    y++;
                }
                break;
            case Droite:
                if(!collision.isTiledHere(x+1, y)) {
                    velocite.x = mineur.getMAX_VELOCITE();
                    mineur.setTeteVersLaDroite(true);
                } else {
                    lancerDestruction = true;
                    x++;
                }
                break;
            case Bas:
                if(!collision.isTiledHere(x, y-1)) {
                    lancerDestruction = true;
                    y--;
                }
                break;
            case Gauche:
                if(!collision.isTiledHere(x-1, y)) {
                    velocite.x = -mineur.getMAX_VELOCITE();
                    mineur.setTeteVersLaDroite(false);
                } else {
                    lancerDestruction = true;
                    x--;
                }
                break;
            default:
                break;
        }
        if (lancerDestruction)
            mineur.getCellsHandler().destructionBloc(x, y);
        velocite.x = MathUtils.clamp(velocite.x, -mineur.getMAX_VELOCITE(), mineur.getMAX_VELOCITE()); // On borne
        velocite.add(0, mineur.getGRAVITE()); // Ajout gravité
        if(Math.abs(velocite.x) < 1) { // Si le velocite est trop faible on stop le mineur
            velocite.x = 0; // Et on detecte dans la boucle pour changer de mode de depla
            if(mineur.isMineurAuSol()) mineur.setDirectionMineur(Mineur.Direction.Arret);
        }
        velocite.scl(Gdx.graphics.getDeltaTime()); // On "scale" par le temps passé pendant la frame
        collision.handleCollision(); // Gestion des colisions
        mineur.getPosition().add(velocite);
        velocite.scl(1/Gdx.graphics.getDeltaTime());
    }
}