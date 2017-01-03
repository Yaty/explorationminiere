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
public enum Item {
    TNT("tnt"), ECHELLE("echelle"), PILIER("pilier"), MAGASIN("magasin"), PIOCHE("pioche_base");

    private String textureRegion;

    private Item(String textureRegion) {
        this.textureRegion = textureRegion;
    }

    public String getTextureRegion() {
        return textureRegion;
    }    
}
