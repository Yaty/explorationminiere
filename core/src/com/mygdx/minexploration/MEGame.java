package com.mygdx.minexploration;

import com.mygdx.minexploration.handlers.SauvegardeHandler;
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
    private int idPartie; // id de la partie chargé
    private String nomGame; // nom de la game
    private int level;
    public static boolean VICTOIRE = false;
    
    /**
     * Chargement des textures, assignation du screen au menu principal
     */
    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new MainMenuScreen(this));
    }
    
    /**
     * Sauvegarde simple
     */
    public void save() {
        if(getScreen().getClass().getSimpleName().equals("GameScreen")) { // Pour être sûr qu'on lui envoi une référence vers l'instance de GameScreen
            new SauvegardeHandler(this).save();
        }
    }
    
    /**
     * Sauvegarde puis quitte le jeu
     */
    public void quitAndSave() {
        if(getScreen().getClass().getSimpleName().equals("GameScreen")) { // Pour être sûr qu'on lui envoi une référence vers l'instance de GameScreen
            new SauvegardeHandler(this).save();
            shutdown();
        }
    }
    
    /**
     * Quitte le jeu proprement
     */
    public void shutdown() {
        Gdx.app.exit();
    }
    
    /**
     * Création d'une nouvelle partie au niveau 1
     * @param idPartie
     * @param nomGame
     */
    public void nouvellePartie(int idPartie, String nomGame) {
        this.idPartie = idPartie;
        this.nomGame = nomGame;
        this.level = 1;
        Gdx.files.local("map/" + idPartie); // Création du dossier de la partie
        FileHandle fileName = Gdx.files.local("map/" + idPartie + '/' + nomGame + ".name"); // Création fichier pour nommé la partie
        fileName.writeString("Ficher pour associer un nom a une partie. Pas très propre ...", false);
        creationPremierNiveau();
    }
    
            
    private void creationPremierNiveau() {
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
                
        new GenerationAleatoire(surface, objet, "map/" + idPartie + "/map.tmx", level);
            
        // Lancement de la partie sur le niveau 1
        setScreen(new GameScreen(this, "map/" + idPartie + "/map.tmx", false));
    }
    
    public void chargerUnePartie(int idPartie, String nomGame) {
        this.idPartie = idPartie;
        this.nomGame = nomGame;
        charcherUnNiveau();
    }
    
    private void charcherUnNiveau() {
        setScreen(new GameScreen(this, "map/" + idPartie + "/map.tmx", true));
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
