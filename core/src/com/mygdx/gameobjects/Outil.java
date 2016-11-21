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
public abstract class Outil {
    private String nom;
    
    public Outil(String nom) {
        this.nom = nom;
    }
    
    public String getNom() {
        return nom;
    }
}
