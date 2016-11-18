package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.gameobjects.Inventaire;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.mehelpers.InputHandler;
import com.mygdx.mehelpers.KeyBoard;
import com.mygdx.mehelpers.inventaire.InventoryActor;
import com.mygdx.minexploration.MEGame;

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
    
    private InventoryActor inventoryActor;
    private MenuPause menuPause;
    public static Stage stage;
    
    /**
     * Lance le jeu au niveau 1
     * @param game le jeu
     * @param cheminMap
     */ 
    public GameScreen(MEGame game, String cheminMap) {
        Gdx.app.log("GameScreen", "GameScreen créé");
        this.game = game;
        gameWorld = new GameWorld(cheminMap);
        gameRenderer = new GameRenderer(gameWorld);
        pause = false;
    }
    
    
    
    /**
     * Méthode appelée à la création du jeu
     */    
    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show appelé");
        stage = new Stage();

        // On utilise un "hub" qui va switcher entre nos classes qui gères les entrées -> pouvoir recuperer les clics du stage et les entrées du jeu
        InputProcessor clavier = new KeyBoard();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(clavier);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        Skin skin = new Skin(Gdx.files.internal("skin/inventaire/uiskin.json"));
        Skin skin2 = new Skin(Gdx.files.internal("skin/uiskin.json"));
    
        DragAndDrop dragAndDrop = new DragAndDrop();
        inventoryActor = new InventoryActor(new Inventaire(), dragAndDrop, skin);
        stage.addActor(inventoryActor);
        
        menuPause = new MenuPause(skin2, game, this);

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
                game.createMenuFin();
            }

            // show the inventory when any key is pressed
            if (Gdx.input.isKeyJustPressed(37)) {
                if(inventoryActor.isVisible())
                    inventoryActor.setVisible(false);
                else
                    inventoryActor.setVisible(true);
            }
        }
        if (Gdx.input.isKeyJustPressed(44) || Gdx.input.isKeyJustPressed(131)) {
            if(menuPause.isVisible()) {
                menuPause.setVisible(false);
                this.unPause();
            } else {
                menuPause.setVisible(true);
                this.pause();
            }
        }
        
        // handle all inputs and draw the whole UI
        stage.act(delta);
        stage.draw();
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
    
    public void unPause() {
        pause = false;
    }
    
    /**
     * Méthode appelée quand le jeu passe sort de la pause
     */   
    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume appelé");
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
}
