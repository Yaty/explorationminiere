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
import com.mygdx.gameobjects.BaseIntermediaire;
import com.mygdx.gameobjects.Mineur;
import com.mygdx.mehelpers.AssetLoader;
import java.util.LinkedList;

/**
 *
 * @author Hugo
 */
public class GameRenderer {
    private final GameWorld gameWorld;
    private final OrthographicCamera orthoCamera; // Caméra Orthographique
    private final OrthogonalTiledMapRenderer tiledMapRenderer; // Va dessiner la map
    private final float UNITE = 1/64f;
    private float runTime = 0;
    private final SpriteBatch spriteBatch;
    private static SelectBox<String> tpList;
    private final BitmapFont etat, direction, deplacement, velocite, position, target, argent, tp, fps;
    private final NinePatch health, healthContainer;
    private final Skin skin;
    private final Stage stage;
    private final TextButton okTp;
    
    /**
     * @param gameWorld un objet gameWorld
     * @param screenStage
     */
    public GameRenderer(final GameWorld gameWorld, Stage screenStage) {
        this.gameWorld = gameWorld;
        stage = screenStage;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(gameWorld.getMap(), UNITE);
        
        orthoCamera = new OrthographicCamera();
        orthoCamera.setToOrtho(false, 15, 15); // False pour y pointé vers le haut, les dimensions que la camera prend
        orthoCamera.update();
        tiledMapRenderer.setView(orthoCamera);
        
        //debugRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        
        etat = new BitmapFont();
        direction = new BitmapFont(); 
        deplacement = new BitmapFont(); 
        velocite = new BitmapFont();
        position = new BitmapFont();
        target = new BitmapFont();
        argent = new BitmapFont();
        fps = new BitmapFont();

        health = new NinePatch(AssetLoader.healthBarTexture);
        healthContainer = new NinePatch(AssetLoader.healthbarContainerTexture);
        
        skin = new Skin(Gdx.files.internal("./skin/uiskin.json"));
        tpList = new SelectBox(skin);
        tpList.setWidth(75);
        tpList.setPosition(Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 70);
        tp = new BitmapFont();
        okTp = new TextButton("Ok", skin);
        okTp.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 65);
        okTp.setWidth(50);
        okTp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int idSelect = tpList.getSelectedIndex();
                if(idSelect != -1) {
                    Vector2 posTp = gameWorld.getHandlers().getMapHandler().getBaseById(idSelect).getPos();
                    posTp.x += 3;
                    if(!gameWorld.getHandlers().getMapHandler().isCellSurfaceHere((int) posTp.x, (int) posTp.y) && gameWorld.getHandlers().getMapHandler().coordIsInMap((int) posTp.x, (int) posTp.y))
                        gameWorld.getMineur().teleportation(posTp);
                }
            }
        });
                
        stage.addActor(okTp);
        stage.addActor(tpList);
    }
    
    private static void setTpList(int nbBases) {
        String[] bases = new String[nbBases];
        for(int i = 0 ; i< bases.length ; i++) bases[i] = "Base " + String.valueOf(i);
        tpList.clearItems();
        tpList.setItems(bases);
    }
    
    public static void setTist(LinkedList<BaseIntermediaire> bases) {
       setTpList(bases.size());
    }
    
    public void reload(TiledMap map) {
        tiledMapRenderer.setMap(map);
    }
    
    public void dispose() {
        argent.dispose();
        //debugRenderer.dispose();
        deplacement.dispose();
        direction.dispose();
        etat.dispose();
        position.dispose();
        fps.dispose();
        skin.dispose();
        spriteBatch.dispose();
        target.dispose();
        tiledMapRenderer.dispose();
        tp.dispose();
        velocite.dispose();
    }
    
    /**
     * Va rendre le jeu via la caméra
     * @param runTime le temps passé depuis le début
     */
    public void render(float runTime) { // Une frame = un lancement de la méthode render()
        this.runTime = runTime;
        Gdx.gl.glClearColor(0, 0, 0, 1); // On vide l'écran, couleur noir
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        orthoCamera.position.x = gameWorld.getMineur().getPosition().x + Mineur.LARGEUR/2;
        orthoCamera.position.y = gameWorld.getMineur().getPosition().y + Mineur.HAUTEUR/2;
        orthoCamera.update();
        
        renderBackground();
        tiledMapRenderer.setView(orthoCamera);
        tiledMapRenderer.render();
        renderMineur();
        renderGUI();
        
        stage.act();
        stage.draw();
    }
    
    private void renderBackground() {
        spriteBatch.begin();
        spriteBatch.draw(AssetLoader.backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();        
    }
    
    /**
     * Va rendre le mineur avec les animations
     */       
    private void renderMineur() {
        TextureRegion frame;
        if("Deplacement".equals(Mineur.etat.name()))
            frame = (TextureRegion) AssetLoader.marcher.getKeyFrame(runTime);
        else if ("Haut".equals(Mineur.dirMineur.name()))
            frame = (TextureRegion) AssetLoader.sauter.getKeyFrame(runTime);
        else
            frame = (TextureRegion) AssetLoader.debout.getKeyFrame(runTime);
        
        Batch batcher = tiledMapRenderer.getBatch();
        batcher.begin();
        if(Mineur.teteVersLaDroite)
            batcher.draw(frame, gameWorld.getMineur().getPosition().x, gameWorld.getMineur().getPosition().y, Mineur.LARGEUR, Mineur.HAUTEUR);
        else
            batcher.draw(frame, gameWorld.getMineur().getPosition().x + Mineur.LARGEUR, gameWorld.getMineur().getPosition().y, -Mineur.LARGEUR, Mineur.HAUTEUR);
        batcher.end();
    }
    
    private void renderGUI(){
        spriteBatch.begin();
        healthContainer.draw(spriteBatch, 5, 5, AssetLoader.healthbarContainerTexture.getWidth(), AssetLoader.healthbarContainerTexture.getHeight());
        health.draw(spriteBatch, 10, 10, (Integer)AssetLoader.healthBarTexture.getWidth()*gameWorld.getMineur().getHealth().getHealth(), AssetLoader.healthBarTexture.getHeight());
        argent.draw(spriteBatch, "Argent : " + gameWorld.getMineur().getArgent(), 800, 25);
        tp.draw(spriteBatch, "Téléportation : ", Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 20);
        fps.draw(spriteBatch, "FPS : " + Math.floor(1/Gdx.graphics.getDeltaTime()), 5, Gdx.graphics.getHeight()-10);
        spriteBatch.end();
    }
    
    public GameWorld getGameWorld() {
        return gameWorld;
    }
    
    public float getUnite() {
        return UNITE;
    }
    
    public OrthographicCamera getCamera() {
        return orthoCamera;
    }
}
