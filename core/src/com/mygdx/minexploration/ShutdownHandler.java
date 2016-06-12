/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.minexploration;

import com.badlogic.gdx.Gdx;

/**
 * La classe va gêrer la fermeture du jeu proprement
 * Elle servira plus tard à la sauvegarde auto avec le ShutdownHook
 * @author Hugo
 */
public class ShutdownHandler {

    /**
     *
     */
    public static void shutdown() {
        Gdx.app.exit();
    }
    
    /*
    Pour le ShutdownHook voir : http://hellotojavaworld.blogspot.fr/2010/11/runtimeaddshutdownhook.html
    Exemple dans le Shingshang Java d'Hugo.
    public void attachShutDownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Dernière action avant Shutdown
            }
        });
    }    
    */
}
