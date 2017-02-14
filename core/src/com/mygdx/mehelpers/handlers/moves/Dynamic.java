package com.mygdx.mehelpers.handlers.deplacements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.gameobjects.Mineur.Etat;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;

/**
 * Classe gérant le déplacement du mineur en mode fluide
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Fluide extends Deplacement {
    
    /**
     * @param positionMineur
     * @param mapHandler
     */
    public Fluide(MapHandler mapHandler, Vector2 positionMineur) {
        super(mapHandler, positionMineur);
    }
    
    /**
     * Méthode appelée à chaque frame lorsque le mineur est en mode fluide.
     * Celle-ci va faire bouger le mineur dynamiquement en fonction de la
     * direction choisit par le joueur.
     */
    @Override
    public void move() {
        if(mapHandler.isLadderHere((int) positionMineur.x, (int) positionMineur.y)) 
            Mineur.isOnEchelle = true;
        else if(Mineur.isOnEchelle)
            Mineur.isOnEchelle = false;
        
        
        boolean lancerDestruction = false;
        int x = (int) (positionMineur.x + Mineur.LARGEUR/2);
        int y = (int) positionMineur.y;
        switch(Mineur.dirMineur) {
            case Haut:
                if(!mapHandler.isCellSurfaceHere(x, y+1)) { // Si pas de bloc en x et y + 1
                    if(Mineur.isOnEchelle) {
                        velocite.y = ECHELLE_VELOCITE;
                        Mineur.mineurAuSol = false;
                        Mineur.etat = Etat.Echelle;
                        if(Gdx.input.isKeyPressed(19) && mapHandler.getBloc(x, y-1) ==0){
                                velocite.y = GRAVITE; //faut rester appuyé
                        }
                        velocite.y = ECHELLE_VELOCITE;
                        if(Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.Z)){
                            velocite.y = 0.2f - GRAVITE; //faut rester appuyé
                        } 
                    } else if(!Mineur.etat.equals(Etat.Sauter)) {
                        velocite.y = SAUT_VELOCITE;
                        Mineur.mineurAuSol = false;
                        Mineur.etat = Etat.Sauter;
                    }
                } else {
                    // Lancer animation ici ?
                    lancerDestruction = true;
                    y++;
                }
                if(Mineur.isOnEchelle && velocite.y==0){
                    lancerDestruction = true;
                }
                break;
            case Droite:
                if(!mapHandler.isCellSurfaceHere(x+1, y)) {
                    velocite.x = MAX_VELOCITE;
                    Mineur.teteVersLaDroite = true;
                    Mineur.etat = Etat.Deplacement;
                } else {
                    lancerDestruction = true;
                    x++;
                }
                break;
            case Bas:
                if(Mineur.etat.equals(Etat.Echelle)) {
                    Mineur.etat = Etat.Echelle;
                    velocite.y = -ECHELLE_VELOCITE;
                    if(Gdx.input.isKeyPressed(20) || Gdx.input.isKeyJustPressed(47)){
                        velocite.y = GRAVITE; //faut rester appuyé
                    }

                } else if(mapHandler.isCellSurfaceHere(x, y-1)) {
                    //mineur.setEtaMineur(Etat.Arret);
                    lancerDestruction = true;
                    y--;
                }
                break;
            case Gauche:
                if(!mapHandler.isCellSurfaceHere(x-1, y)) {
                    velocite.x = -MAX_VELOCITE;
                    Mineur.teteVersLaDroite = false;
                    Mineur.etat = Etat.Deplacement;
                } else {
                    lancerDestruction = true;
                    x--;
                }
                break;
            default:
                break;
        }
        if (lancerDestruction)
            mapHandler.destructionBloc(x, y);
        velocite.x = MathUtils.clamp(velocite.x, -MAX_VELOCITE, MAX_VELOCITE); // On borne
        velocite.add(0, GRAVITE); // Ajout gravité si sur echelle
        if(Math.abs(velocite.x) < 1) { // Si le velocite est trop faible on stop le mineur
            velocite.x = 0; // Et on detecte dans la boucle pour changer de mode de depla
            if(Mineur.mineurAuSol) Mineur.dirMineur = Mineur.Direction.Arret;
        }
        velocite.scl(Gdx.graphics.getDeltaTime()); // On "scale" par le temps passé pendant la frame
        collision.handle(); // Gestion des colisions
        positionMineur.add(velocite);
        velocite.scl(1/Gdx.graphics.getDeltaTime());
        if(Mineur.isOnEchelle && (Gdx.input.isKeyJustPressed(19)) && mapHandler.getBloc(x, y-1) ==0 && Mineur.etat.equals(Etat.Echelle) )
            velocite.y=0f;

    }

    @Override
    public Vector2 getTargetPosition() {
        return null;
    }
    
    public float getVelociteY(){
        return velocite.y;
    }
}