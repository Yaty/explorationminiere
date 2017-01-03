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
    TNT("tnt", "TNT", "Permet de faire exploser des blocs aux alentours !"),
    ECHELLE("echelle", "Echelle", "Afin de pouvoir remonter à la surface."),
    PILIER("pilier", "Pilier", "Pour éviter que les blocs de pierre vous tombent sur la tete ;)"),
    MAGASIN("magasin", "Magasin", "Afin d'acheter de nouveaux objets."),
    PIOCHE("pioche_base", "Pioche base", "Cette pioche va vous permettre de casser des blocs.");

    private String textureRegion, nom, description;

    private Item(String textureRegion, String nom, String description) {
        this.textureRegion = textureRegion;
        this.nom = nom;
        this.description = description;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getNom() {
        return nom;
    }

    public String getTextureRegion() {
        return textureRegion;
    }    
    
    public String getDescription() {
        return description;
    }
}
