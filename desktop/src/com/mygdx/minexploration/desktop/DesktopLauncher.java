package com.mygdx.minexploration.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.minexploration.MEGame;

/**
 *
 * @author Hugo
 */
public class DesktopLauncher {

    /**
     *
     * @param arg
     */
    public static void main (String[] arg) {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            // Configuration - A prendre d'un fichier plus tard
            config.title = "Exploration Miniere";
            config.width = 960; // Voir si c'est trop gros
            config.height = 960;
            config.resizable = false;
            LwjglApplication lwjglApplication = new LwjglApplication(new MEGame(), config);
	}
}
