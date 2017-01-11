package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.mehelpers.GenerationAleatoire;
import com.mygdx.mehelpers.inventaire.InventoryActor;
import com.mygdx.minexploration.MEGame;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Classe qui est l'écran du jeu, c'est elle qui contient les éléments du jeu
 * et se fait afficher par un TiledMapRenderer.
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class GameScreen implements Screen {
    private final GameWorld gameWorld;
    private final GameRenderer gameRenderer;
    private final MEGame game;
    private boolean pause;
    
    private MenuPause menuPause;

    public static Stage stage;
    private int idPartie;
    
    /**
     * Lance le jeu au niveau 1
     * @param game le jeu
     * @param cheminMap
     * @param chargement
     */ 
    public GameScreen(MEGame game, String cheminMap, boolean chargement) {
        Gdx.app.log("GameScreen", "GameScreen créé");
        this.game = game;
        gameWorld = new GameWorld(cheminMap, chargement);
        
        String id = new String();
        int i = 6;
        
        while(true) {
            if(Character.isDigit(cheminMap.charAt(i))) {
                id += cheminMap.charAt(i);
                i++;
            } else
                break;            
        }

        idPartie = Integer.parseInt(id);
        stage = new Stage();
        gameRenderer = new GameRenderer(gameWorld, stage);
        pause = false;
    }
    
    public int getIdPartie() {
        return idPartie;
    }
    
    /**
     * Méthode appelée à la création du jeu
     */    
    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show appelé");

        // On utilise un "hub" qui va switcher entre nos classes qui gères les entrées -> pouvoir recuperer les clics du stage et les entrées du jeu
        Gdx.input.setInputProcessor(stage);
        
        stage.addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if(gameRenderer.getCamera().zoom + amount * 0.05f > 1 && gameRenderer.getCamera().zoom + amount * 0.05f < 10) {
                    gameRenderer.getCamera().zoom += amount * 0.05f;
                    return true;
                } return false;
            }
        });

        Skin skin = new Skin(Gdx.files.internal("skin/inventaire/uiskin.json"));
        Skin skin2 = new Skin(Gdx.files.internal("skin/uiskin.json"));
    
        DragAndDrop dragAndDrop = new DragAndDrop();
        InventoryActor inventoryActor = new InventoryActor("Inventaire", gameWorld.getMineur().getInventaire(), Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight(), dragAndDrop, skin, this);
        inventoryActor.setMovable(false);
        stage.addActor(inventoryActor);
        
        InventoryActor equipementActor = new InventoryActor("Equipement", gameWorld.getMineur().getEquipement(), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight(), dragAndDrop, skin, this);
        equipementActor.setMovable(false);
        stage.addActor(equipementActor);
        
        menuPause = new MenuPause(skin2, game);

        stage.addActor(menuPause);
    }
    
    /**
     * Méthode appelée à chaque frame
     */   
    @Override
    public void render(float delta) {
        if(!pause) {
            gameWorld.update(delta);
            gameRenderer.render(gameWorld.getMineur().getRunTime());
            if(gameWorld.getMineur().getCellsHandler().isVictory()) {
                newLevel();
                gameWorld.getMineur().getCellsHandler().setVictory(false);
            }
        }
        // P ou ESC
        if (Gdx.input.isKeyJustPressed(Keys.P) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            if(menuPause.isVisible()) {
                menuPause.setVisible(false);
                resume();
            } else {
                menuPause.setVisible(true);
                this.pause();
            }
        }
        
        // handle all inputs and draw the whole UI
        stage.act(delta);
        stage.draw();
    }
    
    public void newLevel() {
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
                
        game.setLevel(game.getLevel()+1);
        new GenerationAleatoire(surface, objet, "./map/" + idPartie + "/map.tmx", game.getLevel());
        gameWorld.reload();
        gameRenderer.reload(gameWorld.getMap());
    }
    
    /**
     * Redimensionne la fenetre
     * @param width la largeur
     * @param height la hauteur
     */   
    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resize appelé");
    }
    
    /**
     * Méthode appelée quand le jeu passe en pause
     */   
    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause appelé");
        pause = true;
    }
    
    /**
     * Méthode appelée quand le jeu passe sort de la pause
     */   
    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume appelé");
        pause = false;
    }
    
    /**
     * Méthode appelée quand la fenetre est minimiser
     */   
    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide appelé");
    }
    
    /**
     * Méthode appelée quand on demande de supprimer le contenu de l'écran
     */   
    @Override
    public void dispose() {
        stage.dispose();
    }
    
    public GameWorld getWorld() {
        return gameWorld;
    }
}
