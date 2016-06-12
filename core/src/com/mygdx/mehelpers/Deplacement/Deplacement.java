
package com.mygdx.mehelpers.Deplacement;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;

/**
 * Classe abstraite pour gérer les déplacements
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public abstract class Deplacement {
    protected Mineur mineur;
    protected Collision collision;
    protected Vector2 velocite;
    
    /**
     * @param mineur objet mineurr
     */
    public Deplacement(Mineur mineur) {
        this.mineur = mineur;
        velocite = new Vector2(0,0);
        collision = new Collision(this);
    }

    /**
     * @return le mineur
     */
    public Mineur getMineur() {
        return mineur;
    }

    /**
     * @param mineur le mineur
     */
    public void setMineur(Mineur mineur) {
        this.mineur = mineur;
    }

    /**
     * @return l'objet collision
     */
    public Collision getCollision() {
        return collision;
    }

    /**
     * @param collision un objet collision
     */
    public void setCollision(Collision collision) {
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
 
}
