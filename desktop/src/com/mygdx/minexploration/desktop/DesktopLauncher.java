package com.mygdx.minexploration.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.minexploration.MEGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            // Configuration - A prendre d'un fichier plus tard
            config.title = "Exploration Miniere";
            config.width = 960;
            config.height = 960;
            config.resizable = false;
            new LwjglApplication(new MEGame(), config);
	}
}
