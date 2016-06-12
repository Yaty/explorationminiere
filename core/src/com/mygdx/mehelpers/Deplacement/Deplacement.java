
package com.mygdx.mehelpers.Deplacement;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.Mineur;

/**
 *
 * @author Hugo
 */
public abstract class Deplacement {
    protected Mineur mineur;
    protected Collision collision;
    protected Vector2 velocite;
    
    /**
     *
     * @param mineur
     */
    public Deplacement(Mineur mineur) {
        this.mineur = mineur;
        velocite = new Vector2(0,0);
        collision = new Collision(this);
    }

    /**
     *
     * @return
     */
    public Mineur getMineur() {
        return mineur;
    }

    /**
     *
     * @param mineur
     */
    public void setMineur(Mineur mineur) {
        this.mineur = mineur;
    }

    /**
     *
     * @return
     */
    public Collision getCollision() {
        return collision;
    }

    /**
     *
     * @param collision
     */
    public void setCollision(Collision collision) {
        this.collision = collision;
    }

    /**
     *
     * @return
     */
    public Vector2 getVelocite() {
        return velocite;
    }

    /**
     *
     * @param velocite
     */
    public void setVelocite(Vector2 velocite) {
        this.velocite = velocite;
    }
    
    /**
     *
     */
    public abstract void move();
    
    
}
