/* 
 * Copyright 2017 
 * - Hugo Da Roit - Benjamin Lévêque
 * - Alexis Montagne - Alexis Clément
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mygdx.minexploration;

import com.mygdx.minexploration.handlers.SaveHandler;
import com.badlogic.gdx.Game; // On importe Game qui implémente ApplicationListener
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mehelpers.AssetLoader;
import com.mygdx.mehelpers.RandomMapGenerator;
import com.mygdx.screens.GameScreen;
import com.mygdx.screens.MainMenuScreen;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Main game class
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class MEGame extends Game {
    private int idGame; // id de la partie chargé
    private String gameName; // nom de la game
    private int level;
    public static boolean VICTORY = false;
    
    /**
     * Texture loading
     * Set the screen
     */
    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new MainMenuScreen(this));
    }
    
    /**
     * Just save the game
     */
    public void save() {
        if(getScreen().getClass().getSimpleName().equals("GameScreen")) { // Pour être sûr qu'on lui envoi une référence vers l'instance de GameScreen
            new SaveHandler(this).save();
        }
    }
    
    /**
     * Save then quit
     */
    public void quitAndSave() {
        if(getScreen().getClass().getSimpleName().equals("GameScreen")) { // Pour être sûr qu'on lui envoi une référence vers l'instance de GameScreen
            new SaveHandler(this).save();
            shutdown();
        }
    }
    
    /**
     * Shutdown the game properly
     */
    public void shutdown() {
        Gdx.app.exit();
    }
    
    /**
     * Create a new game at level 1
     * @param idGame
     * @param gameName
     */
    public void newFirstLevel(int idGame, String gameName) {
        this.idGame = idGame;
        this.gameName = gameName;
        this.level = 1;
        Gdx.files.local("map/" + idGame); // Création du dossier de la partie
        FileHandle fileName = Gdx.files.local("map/" + idGame + '/' + gameName + ".name"); // Création fichier pour nommé la partie
        fileName.writeString("Ficher pour associer un nom a une partie. Pas très propre ...", false);
        createFirstLevel();
    }
    
            
    private void createFirstLevel() {
        FileHandle dir = Gdx.files.local("map");
        // Remplissage du dossier par un TMX généré
        FileHandle[] surfaceFiles = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".png");
            }
        });
        
        FileHandle[] objetFiles = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".gif");
            }
        });
        
        String surface[] = new String[surfaceFiles.length];
        for(int i = 0 ; i < surfaceFiles.length ; i++)
            surface[i] = surfaceFiles[i].name();
        
        String objet[] = new String[objetFiles.length];
        for(int i = 0 ; i < objetFiles.length ; i++)
            objet[i] = objetFiles[i].name();
                
        new RandomMapGenerator(surface, objet, "map/" + idGame + "/map.tmx", level);
            
        // Lancement de la partie sur le niveau 1
        setScreen(new GameScreen(this, "map/" + idGame + "/map.tmx", false));
    }
    
    /**
     * Load a game
     * @param idPartie
     * @param nomGame
     */
    public void loadGame(int idPartie, String nomGame) {
        this.idGame = idPartie;
        this.gameName = nomGame;
        loadLevel();
    }
    
    private void loadLevel() {
        setScreen(new GameScreen(this, "map/" + idGame + "/map.tmx", true));
    }
    
    /**
     * Set the screen to the main menu
     */
    public void backToMainMenuScreen(){
        setScreen(new MainMenuScreen(this));
    }

    /**
     * Dipose textures and the screen
     */    
    @Override
    public void dispose() {
        AssetLoader.dispose();
        screen.dispose();
    }
    
    /**
     * Get the level
     * @return the level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Get game name
     * @return the game name
     */
    public String getGameName() {
        return gameName;
    }
    
    /**
     * Set the level
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }
}
