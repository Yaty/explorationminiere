package com.mygdx.minexploration;

import com.badlogic.gdx.Game; // On importe Game qui implémente ApplicationListener
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.mehelpers.AssetLoader;
import com.mygdx.mehelpers.GenerationAleatoire;
import com.mygdx.screens.GameScreen;
import com.mygdx.screens.MainMenuScreen;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 *
 * @author Hugo
 */
public class MEGame extends Game {
    private int idGame; // id de la partie chargé
    private String nomGame; // nom de la game
    private int level;
    private SauvegardeHandler save;
    
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
            save = new SauvegardeHandler(this);
            save.save();
        }
    }
    
    public void quitAndSave() {
        Gdx.app.log("MEGame", "Sauvegarde puis extinction.");
        if(getScreen().getClass().getSimpleName().equals("GameScreen")) { // Pour être sûr qu'on lui envoi une référence vers l'instance de GameScreen
            save = new SauvegardeHandler(this);
            save.save();
            ShutdownHandler.shutdown();
        }
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
        System.out.println("CREATION DUNE NOUVELLE PARTIE");
        System.out.println("Identifiant (dossier) : " + idGame);
        System.out.println("Nom de la partie (truc.game) : " + nomGame);
        // Création du dossier
        File dir = new File("./map/" + idGame);
        dir.mkdir();
        
        // Remplissage du dossier
        File fichierName = new File("./map/" + idGame + "/" + nomGame + ".name");
        try {
            fichierName.createNewFile();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        newLevel();
    }
    
            
    private void newLevel() {
        // Remplissage du dossier
            // Création du dossier du niveau level
        File dir = new File("./map/");
            // Remplissage du dossier par un TMX généré
        File[] surfaceFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".png");
            }
        });
        
        File[] objetFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".gif");
            }
        });
        String surface[] = new String[surfaceFiles.length];
        for(int i = 0 ; i < surfaceFiles.length ; i++)
            surface[i] = surfaceFiles[i].getName();
        
        String objet[] = new String[objetFiles.length];
        for(int i = 0 ; i < objetFiles.length ; i++)
            objet[i] = objetFiles[i].getName();
                
        GenerationAleatoire generateur = new GenerationAleatoire(surface, objet, "./map/" + idGame + "/map.tmx", level);
            
        // Lancement de la partie sur le niveau 1
        setScreen(new GameScreen(this, "./map/" + idGame + "/map.tmx"));
    }
    
    public void loadingGame(int idGame, String nomGame) {
        this.idGame = idGame;
        this.nomGame = nomGame;
        loadingLevel();
    }
    
    private void loadingLevel() {
        setScreen(new GameScreen(this, "./map/" + idGame + "/map.tmx"));
    }
    
    public void backToMainMenuScreen(){
        setScreen(new MainMenuScreen(this));
    }

    /**
     *
     */    
    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
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
