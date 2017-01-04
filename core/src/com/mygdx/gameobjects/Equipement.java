package com.mygdx.gameobjects;

import com.mygdx.mehelpers.inventaire.Slot;

/**
 * Classe qui représente l'inventaire du mineur
 * http://pixelscientists.com/wordpress/?p=15
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Equipement {
    //private int echelles, piliers, tnt;
    private Slot slotPioche;
  
    public Equipement() {      
        slotPioche = new Slot(Item.PIOCHE_BOIS, 1);
    }
    
    public Equipement(String nom) {
        Item pioche = Item.getItemFromTextureName(nom);
        slotPioche = new Slot(pioche, 1);
    }
    
    public Slot getPioche() {
        return slotPioche;
    }
}
