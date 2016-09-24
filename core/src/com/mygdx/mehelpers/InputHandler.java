/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

/**
 * Classe gérant les entrées utilisateurs
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class InputHandler {
    public static final int NUM_KEYBOARD_KEYS = 155; // 155 touches géré pas libgdx

    /**
     * Tableau à une dimension
     * Chaque case représente une touche du clavier
     * Voir https://libgdx.badlogicgames.com/nightlies/docs/api/constant-values.html#com.badlogic.gdx.Input.Keys.NUM_8
     */
    public static boolean[] keys;

    /**
     * Tableau à une dimension
     * Chaque case représente une touche du clavier
     * Voir https://libgdx.badlogicgames.com/nightlies/docs/api/constant-values.html#com.badlogic.gdx.Input.Keys.NUM_8
     */
    public static boolean[] pkeys;
    
    static {
        keys = new boolean[NUM_KEYBOARD_KEYS];
        pkeys = new boolean[NUM_KEYBOARD_KEYS];
    }

    /**
     * Passe l'élément i à la valeur de b
     * @param i l'entier qui correspond à une touche
     * @param b la valeur à mettre
     */
    public static void setKey(int i, boolean b) { keys[i] = b; }

    /**
     * @param i l'entier représentant une touche du clavier
     * @return vrai si la touche identifié par l'entier i est enfoncé, faux sinon
     */
    public static boolean isDown(int i) { return keys[i]; }
    
    

}
