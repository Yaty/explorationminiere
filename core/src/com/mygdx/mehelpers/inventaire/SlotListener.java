/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers.inventaire;

/**
 *
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public interface SlotListener {
    /**
    * Will be called whenever the slot has changed.
     * @param slot Le slot changé.
    */
    void hasChanged(Slot slot);    
}
