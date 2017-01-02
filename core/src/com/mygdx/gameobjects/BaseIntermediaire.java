/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Rectangle;

public class BaseIntermediaire extends Rectangle {
    public static final int LARGEUR = 6, HAUTEUR = 3;
    
    public BaseIntermediaire(int x0, int y0) {
        super();
        this.set(x0, y0, LARGEUR, HAUTEUR);
        
    }    
}
