
package com.mygdx.mehelpers.handlers.deplacements;

import com.mygdx.mehelpers.handlers.handlers.CollisionHandler;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.mehelpers.handlers.handlers.MapHandler;

/**
 * Classe abstraite pour gérer les déplacements
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public abstract class Deplacement {
    public static float GRAVITE = -0.4f, MAX_VELOCITE = 4f, SAUT_VELOCITE = 8f, ECHELLE_VELOCITE = 6f;
    protected CollisionHandler collision;
    public MapHandler mapHandler;
    protected Vector2 velocite, positionMineur;
    
    /**
     * @param positionMineur
     * @param mapHandler
     */
    public Deplacement(MapHandler mapHandler, Vector2 positionMineur) {
        velocite = new Vector2(0,0);
        this.positionMineur = positionMineur;
        this.mapHandler = mapHandler;
        collision = new CollisionHandler(this);
    }
    
    public Vector2 getPositionMineur() {
        return positionMineur;
    }

    /**
     * @return l'objet collision
     */
    public CollisionHandler getCollision() {
        return collision;
    }

    /**
     * @param collision un objet collision
     */
    public void setCollision(CollisionHandler collision) {
        this.collision = collision;
    }

    /**
     * @return le vecteur velocite
     */
    public Vector2 getVelocite() {
        return velocite;
    }

    /**
     *
     * @param velocite le vecteur vélocité
     */
    public void setVelocite(Vector2 velocite) {
        this.velocite = velocite;
    }
    
    /**
     * Méthode abstraite que les classes filles doivent re-définir
     */
    public abstract void move();

    public abstract Vector2 getTargetPosition();

    public void dispose() {
        collision.dispose();
    }

}
