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
package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.mehelpers.RandomMapGenerator;
import com.mygdx.mehelpers.handlers.handlers.InputHandler;
import com.mygdx.mehelpers.inventory.InventoryActor;
import com.mygdx.minexploration.MEGame;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Game screen
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class GameScreen implements Screen {
    private final GameWorld gameWorld;
    private final GameRenderer gameRenderer;
    private final MEGame game;
    private boolean pause;
    
    private MenuPause menuPause;

    public static Stage stage;
    private final int gameId;
    
    /**
     * Constructor
     * @param game
     * @param cheminMap
     * @param chargement
     */ 
    public GameScreen(MEGame game, String cheminMap, boolean chargement) {
        Gdx.app.log("GameScreen", "GameScreen créé");
        this.game = game;
        gameWorld = new GameWorld(cheminMap, chargement);
        
        String id = new String();
        int i = 4;
        while(Character.isDigit(cheminMap.charAt(i))) {
            id += cheminMap.charAt(i);
            i++;
        }
        
        gameId = Integer.parseInt(id);
        stage = new Stage();
        gameRenderer = new GameRenderer(gameWorld, stage);
        pause = false;
    }
    
    /**
     * Get game id
     * @return game id
     */
    public int getGameId() {
        return gameId;
    }
    
    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show appelé");

        // On utilise un "hub" qui va switcher entre nos classes qui gères les entrées -> pouvoir recuperer les clics du stage et les entrées du jeu
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputHandler());
        
        Gdx.input.setInputProcessor(multiplexer);
        
        stage.addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if(gameRenderer.getCamera().zoom + amount * 0.05f > 1 && gameRenderer.getCamera().zoom + amount * 0.05f < 10) {
                    gameRenderer.getCamera().zoom += amount * 0.05f;
                }
                return true;
            }
        });

        Skin skin = new Skin(Gdx.files.internal("skin/inventaire/uiskin.json"));
        Skin skin2 = new Skin(Gdx.files.internal("skin/uiskin.json"));
    
        DragAndDrop dragAndDrop = new DragAndDrop();
        InventoryActor inventoryActor = new InventoryActor("Inventaire", gameWorld.getMiner().getInventory(), Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight(), dragAndDrop, skin, this);
        inventoryActor.setMovable(false);
        stage.addActor(inventoryActor);
        
        InventoryActor equipementActor = new InventoryActor("Equipement", gameWorld.getMiner().getEquipment(), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight(), dragAndDrop, skin, this);
        equipementActor.setMovable(false);
        stage.addActor(equipementActor);
        
        menuPause = new MenuPause(skin2, game);

        stage.addActor(menuPause);
    }
    
    @Override
    public void render(float delta) {
        if(!pause) {
            gameWorld.update(delta);
            gameRenderer.render(gameWorld.getMiner().getRunTime());
            if(MEGame.VICTORY) {
                MEGame.VICTORY = false;
                goToTheNextLevel();
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
    
    /**
     * Go to the next level.
     */
    public void goToTheNextLevel() {
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
        new RandomMapGenerator(surface, objet, "./map/" + gameId + "/map.tmx", game.getLevel());
        gameWorld.reload();
        gameRenderer.reload(gameWorld.getMap());
    }
     
    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resize appelé");
    }
    
    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause appelé");
        pause = true;
    }
     
    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume appelé");
        pause = false;
    }
      
    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide appelé");
    }
    
    @Override
    public void dispose() {
        gameRenderer.dispose();
        gameWorld.dispose();
        stage.dispose();
    }
    
    /**
     * Get the world
     * @return the world
     */
    public GameWorld getWorld() {
        return gameWorld;
    }
}
