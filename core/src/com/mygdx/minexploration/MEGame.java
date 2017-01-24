package com.mygdx.minexploration;

import com.badlogic.gdx.Game; // On importe Game qui implémente ApplicationListener
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.mehelpers.AssetLoader;
import com.mygdx.mehelpers.GenerationAleatoire;
import com.mygdx.screens.GameScreen;
import com.mygdx.screens.MainMenuScreen;
import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Hugo
 */
public class MEGame extends Game {
    private int idGame; // id de la partie chargé
    private String nomGame; // nom de la game
    private int level;
    
    /**
     *
     */
    @Override
    public void create() {
        // Chargement des textures, assignation du screen au menu principal
        AssetLoader.load();
        setScreen(new MainMenuScreen(this));
    }
    
    public void save() {
        Gdx.app.log("MEGame", "Sauvegarde.");
        if(getScreen().getClass().getSimpleName().equals("GameScreen")) { // Pour être sûr qu'on lui envoi une référence vers l'instance de GameScreen
            new SauvegardeHandler(this).save();
        }
    }
    
    public void quitAndSave() {
        if(getScreen().getClass().getSimpleName().equals("GameScreen")) { // Pour être sûr qu'on lui envoi une référence vers l'instance de GameScreen
            new SauvegardeHandler(this).save();
            shutdown();
        }
    }
    
    public void shutdown() {
        Gdx.app.exit();
    }
    
    /**
     * Création d'une nouvelle partie au niveau 1
     * @param idGame
     * @param nomGame
     */
    public void newGame(int idGame, String nomGame) {
        this.idGame = idGame;
        this.nomGame = nomGame;
        this.level = 1;
        Gdx.files.local("map/" + idGame); // Création dossier
        FileHandle fileName = Gdx.files.local("map/" + idGame + '/' + nomGame + ".name"); // Création fichier
        fileName.writeString("Ficher pour associer un nom a une partie. Pas très propre ...", false);
        newLevel();
    }
    
            
    private void newLevel() {
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
                
        new GenerationAleatoire(surface, objet, "map/" + idGame + "/map.tmx", level);
            
        // Lancement de la partie sur le niveau 1
        setScreen(new GameScreen(this, "map/" + idGame + "/map.tmx", false));
    }
    
    public void loadingGame(int idGame, String nomGame) {
        this.idGame = idGame;
        this.nomGame = nomGame;
        loadingLevel();
    }
    
    private void loadingLevel() {
        setScreen(new GameScreen(this, "map/" + idGame + "/map.tmx", true));
    }
    
    public void backToMainMenuScreen(){
        setScreen(new MainMenuScreen(this));
    }

    /**
     *
     */    
    @Override
    public void dispose() {
        AssetLoader.dispose();
        screen.dispose();
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getNomGame() {
        return nomGame;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
}
