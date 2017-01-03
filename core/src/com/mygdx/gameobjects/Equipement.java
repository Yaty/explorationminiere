package com.mygdx.gameobjects;

import java.util.ArrayList;
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
        slotPioche = new Slot(Item.PIOCHE, 1, 1);
    }
    
    public Slot getPioche() {
        return slotPioche;
    }
}
