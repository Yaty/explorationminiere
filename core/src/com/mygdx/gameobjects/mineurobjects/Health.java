/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.gameobjects.mineurobjects;

/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class Health {
    private float health;

    public Health(float health) {
        this.health = health;
    }
    
    public Health() {
        this.health = 1;
    }
    
    public void remove(float amount) {
        health -= amount;
    }
    
    public void add(float amount) {
        health += amount;
    }
    
    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }
    
}
