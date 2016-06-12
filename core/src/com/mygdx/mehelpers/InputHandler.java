/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.mehelpers;

/**
 *
 * @author Hugo
 */
public class InputHandler {
    // https://libgdx.badlogicgames.com/nightlies/docs/api/constant-values.html#com.badlogic.gdx.Input.Keys.NUM_8
    public static final int NUM_KEYBOARD_KEYS = 155; // 155 touches géré pas libgdx

    /**
     *
     */
    public static boolean[] keys;

    /**
     *
     */
    public static boolean[] pkeys;
    static {
        keys = new boolean[NUM_KEYBOARD_KEYS];
        pkeys = new boolean[NUM_KEYBOARD_KEYS];
    }

    /**
     *
     * @param i
     * @param b
     */
    public static void setKey(int i, boolean b) { keys[i] = b; }

    /**
     *
     * @param i
     * @return
     */
    public static boolean isDown(int i) { return keys[i]; }
}
