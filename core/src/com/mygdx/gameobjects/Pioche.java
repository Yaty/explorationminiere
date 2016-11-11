/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.gameobjects;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Pioche extends Outil {
    private float vitesse;    
    
    public Pioche(String nom, float vitesse) {
        super(nom);
        this.vitesse = vitesse;
    }

    public float getVitesse() {
        return vitesse;
    }

    public void setVitesse(float vitesse) throws Exception {
        if(vitesse >= 1.0f)
            this.vitesse = vitesse;
        else
            throw new Exception("Vous ne pouvez pas définir une vitesse inférieur à 1.");
    }
}
