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
package com.mygdx.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.gameobjects.Base;
import com.mygdx.gameobjects.Miner;
import com.mygdx.mehelpers.AssetLoader;
import java.util.LinkedList;

/**
 * Class to render the world
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class GameRenderer {
    private final GameWorld gameWorld;
    private final OrthographicCamera orthoCamera; // Caméra Orthographique
    private final OrthogonalTiledMapRenderer tiledMapRenderer; // Va dessiner la map
    private final float UNITY = 1/64f;
    private float runTime = 0;
    private final SpriteBatch spriteBatch;
    private static SelectBox<String> tpList;
    private final BitmapFont state, direction, shifting, velocity, position, target, money, tp, fps;
    private final NinePatch health, healthContainer, death;
    private final Skin skin;
    private final Stage stage;
    private final TextButton okTpButton;
    
    /**
     * Create a game renderer
     * @param gameWorld the world to render
     * @param screenStage the stage to render some objects
     */
    public GameRenderer(final GameWorld gameWorld, Stage screenStage) {
        this.gameWorld = gameWorld;
        stage = screenStage;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(gameWorld.getMap(), UNITY);
        
        orthoCamera = new OrthographicCamera();
        orthoCamera.setToOrtho(false, 15, 15); // False pour y pointé vers le haut, les dimensions que la camera prend
        orthoCamera.update();
        tiledMapRenderer.setView(orthoCamera);
        
        //debugRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        
        state = new BitmapFont();
        direction = new BitmapFont(); 
        shifting = new BitmapFont(); 
        velocity = new BitmapFont();
        position = new BitmapFont();
        target = new BitmapFont();
        money = new BitmapFont();
        fps = new BitmapFont();

        health = new NinePatch(AssetLoader.healthBarTexture);
        healthContainer = new NinePatch(AssetLoader.healthbarContainerTexture);
        death = new NinePatch(AssetLoader.death);
        
        skin = new Skin(Gdx.files.internal("./skin/uiskin.json"));
        tpList = new SelectBox(skin);
        tpList.setWidth(75);
        tpList.setPosition(Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 70);
        tp = new BitmapFont();
        okTpButton = new TextButton("Ok", skin);
        okTpButton.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 65);
        okTpButton.setWidth(50);
        okTpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int idSelect = tpList.getSelectedIndex();
                if(idSelect != -1) {
                    Vector2 posTp = gameWorld.getHandlers().getMapHandler().getBaseById(idSelect).getPos();
                    posTp.x += 3;
                    if(!gameWorld.getHandlers().getMapHandler().isCellSurfaceHere((int) posTp.x, (int) posTp.y) && gameWorld.getHandlers().getMapHandler().coordIsInMap((int) posTp.x, (int) posTp.y))
                        gameWorld.getMiner().teleportation(posTp);
                }
            }
        });
                
        stage.addActor(okTpButton);
        stage.addActor(tpList);
    }
    
    
    private static void setTpList(int nbBases) {
        String[] bases = new String[nbBases];
        for(int i = 0 ; i< bases.length ; i++) bases[i] = "Base " + String.valueOf(i);
        tpList.clearItems();
        tpList.setItems(bases);
    }
    
    /**
     * Set the list of base with a new list
     * @param bases the new list of base
     */
    public static void setTpList(LinkedList<Base> bases) {
       setTpList(bases.size());
    }
    
    /**
     * Reloading the map
     * @param map the map to reload
     */
    public void reload(TiledMap map) {
        tiledMapRenderer.setMap(map);
    }
    
    /**
     * Called to dispose properly objects
     */
    public void dispose() {
        money.dispose();
        //debugRenderer.dispose();
        shifting.dispose();
        direction.dispose();
        state.dispose();
        position.dispose();
        fps.dispose();
        skin.dispose();
        spriteBatch.dispose();
        target.dispose();
        tiledMapRenderer.dispose();
        tp.dispose();
        velocity.dispose();
    }
    
    /**
     * Render the game, called every frame
     * @param runTime time since the game's launch
     */
    public void render(float runTime) {
        this.runTime = runTime;
        Gdx.gl.glClearColor(0, 0, 0, 1); // On vide l'écran, couleur noir
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        orthoCamera.position.x = gameWorld.getMiner().getPosition().x + Miner.WIDTH/2;
        orthoCamera.position.y = gameWorld.getMiner().getPosition().y + Miner.HEIGHT/2;
        orthoCamera.update();
        
        renderBackground();
        tiledMapRenderer.setView(orthoCamera);
        tiledMapRenderer.render();
        renderMiner();
        renderGUI();
        
        stage.act();
    }
    
    private void renderBackground() {
        spriteBatch.begin();
        spriteBatch.draw(AssetLoader.backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();        
    }
    
    /**
     * Render the miner
     */       
    private void renderMiner() {
        TextureRegion frame;
        if(Miner.state == Miner.State.MOVING)
            frame = (TextureRegion) AssetLoader.walking.getKeyFrame(runTime);
        else if (Miner.state == Miner.State.JUMPING)
            frame = (TextureRegion) AssetLoader.jumping.getKeyFrame(runTime);
        else
            frame = (TextureRegion) AssetLoader.standing.getKeyFrame(runTime);
        
        Batch batcher = tiledMapRenderer.getBatch();
        batcher.begin();
        if(Miner.headTowardsRight)
            batcher.draw(frame, gameWorld.getMiner().getPosition().x, gameWorld.getMiner().getPosition().y, Miner.WIDTH, Miner.HEIGHT);
        else
            batcher.draw(frame, gameWorld.getMiner().getPosition().x + Miner.WIDTH, gameWorld.getMiner().getPosition().y, -Miner.WIDTH, Miner.HEIGHT);
        batcher.end();
    }
    
    private void renderGUI(){
        spriteBatch.begin();
        healthContainer.draw(spriteBatch, 5, 5, AssetLoader.healthbarContainerTexture.getWidth(), AssetLoader.healthbarContainerTexture.getHeight());
        health.draw(spriteBatch, 10, 10, (Integer)AssetLoader.healthBarTexture.getWidth()*gameWorld.getMiner().getHealth(), AssetLoader.healthBarTexture.getHeight());
        money.draw(spriteBatch, "Argent : " + gameWorld.getMiner().getMoney(), 800, 25);
        tp.draw(spriteBatch, "Téléportation : ", Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 20);
        fps.draw(spriteBatch, "FPS : " + Math.floor(1/Gdx.graphics.getDeltaTime()), 5, Gdx.graphics.getHeight()-10);
        if(Miner.hasDied){
            death.draw(spriteBatch, 200, 200, AssetLoader.death.getWidth(), AssetLoader.death.getHeight());
            death.draw(spriteBatch, 320, 320, AssetLoader.death.getWidth(), AssetLoader.death.getHeight());
            new Timer().scheduleTask(new Timer.Task(){
                @Override
                public void run(){
                    Miner.hasDied = false;
                }
            }, 3f);
        }
        spriteBatch.end();
    }

    /**
     * Get the camera
     * @return the orthographic camera
     */
    public OrthographicCamera getCamera() {
        return orthoCamera;
    }
}
