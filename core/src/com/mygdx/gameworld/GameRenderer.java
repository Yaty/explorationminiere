package com.mygdx.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.mehelpers.AssetLoader;

/**
 *
 * @author Hugo
 */
public class GameRenderer {
    
    private final GameWorld gameWorld;
    private final OrthographicCamera orthoCamera; // Caméra Orthographique
    private final OrthogonalTiledMapRenderer tiledMapRenderer; // Va dessiner la map
    private final float UNITE = 1/64f;
    private final ShapeRenderer debugRenderer;
    private float runTime = 0;
    private final SpriteBatch spriteBatch;
    private BitmapFont etat, direction, deplacement, velocite, position, target;
    
    /**
     * @param gameWorld un objet gameWorld
     */
    public GameRenderer(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        
        tiledMapRenderer = new OrthogonalTiledMapRenderer(gameWorld.getMap(), UNITE);
        
        orthoCamera = new OrthographicCamera();
        orthoCamera.setToOrtho(false, 15, 15); // False pour y pointé vers le haut, les dimensions que la camera prend
        orthoCamera.update();
        
        debugRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        
        etat = new BitmapFont();
        direction = new BitmapFont(); 
        deplacement = new BitmapFont(); 
        velocite = new BitmapFont();
        position = new BitmapFont();
        target = new BitmapFont();  
    }
    
    /**
     * Va rendre le jeu via la caméra
     * @param runTime le temps passé depuis le début
     */
    public void render(float runTime) { // Une frame = un lancement de la méthode render()
        this.runTime = runTime;
        // Gdx.app.log("GameRender", "FPS : " + 1/deltaTime);
        Gdx.gl.glClearColor(0, 0, 0, 1); // On vide l'écran, couleur noir
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        float deltaTime = Gdx.graphics.getDeltaTime();
        //Gdx.app.log("GameRenderer", "Runtime : " + runTime);
        //Gdx.app.log("GameRendrer", "Deltatime : " + deltaTime);
        gameWorld.getMineur().update(deltaTime);
        // Voir avec l'unité, c'est le bordel
        orthoCamera.position.x = gameWorld.getMineur().getPosition().x;
        orthoCamera.position.y = gameWorld.getMineur().getPosition().y;
        orthoCamera.update();
        
        renderBackground();
        
        //Gdx.app.log("Position orthoCaméra", orthoCamera.position.toString());
        //Gdx.app.log("Position mineur", gameWorld.getMineur().getPosition().toString());
        //Gdx.app.log("GameRenderer", (String) gameWorld.getMineur().getEtatMineur().name());
        tiledMapRenderer.setView(orthoCamera);
        tiledMapRenderer.render();

        renderMineur();
        //if(InputHandler.keys[30]) 
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
        if("Deplacement".equals(gameWorld.getMineur().getEtatMineur().name()))
            frame = AssetLoader.marcher.getKeyFrame(runTime);
        else if ("Haut".equals(gameWorld.getMineur().getDirectionMineur().name()))
            frame = AssetLoader.sauter.getKeyFrame(runTime);
        else
            frame = AssetLoader.debout.getKeyFrame(runTime);
        
        //Gdx.app.log("renderMineur", "x : " + gameWorld.getMineur().getPosition().x + " y : " + gameWorld.getMineur().getPosition().y);
        
        Batch batcher = tiledMapRenderer.getBatch();
        batcher.begin();
        if(gameWorld.getMineur().isTeteVersLaDroite())
            batcher.draw(frame, gameWorld.getMineur().getPosition().x, gameWorld.getMineur().getPosition().y, gameWorld.getMineur().getLARGEUR(), gameWorld.getMineur().getHAUTEUR());
        else
            batcher.draw(frame, gameWorld.getMineur().getPosition().x + gameWorld.getMineur().getLARGEUR(), gameWorld.getMineur().getPosition().y, -gameWorld.getMineur().getLARGEUR(), gameWorld.getMineur().getHAUTEUR());
        batcher.end();
    }
    
    /**
     * Va rendre le mode debug
     * Ajout des lignes jaunes pour définir les blocs
     * Ajout du rectangle rouge pour définir le mineur
     */       
    private void renderDebug () {      
        spriteBatch.begin();
        etat.draw(spriteBatch, "Etat : " + gameWorld.getMineur().getEtatMineur().name(), 5, 950);
        direction.draw(spriteBatch, "Direction : " + gameWorld.getMineur().getDirectionMineur().name(), 5, 935);
        if(gameWorld.getMineur().getTypeDeplacement() != null) {
            deplacement.draw(spriteBatch, "Déplacement : " + gameWorld.getMineur().getTypeDeplacement().getClass().getSimpleName(), 5, 920);
            if("Ammortissement".equals(gameWorld.getMineur().getTypeDeplacement().toString()))
                target.draw(spriteBatch, "Target : " + gameWorld.getMineur().getDeplacement().getTargetPosition().toString(), 5, 905);
            velocite.draw(spriteBatch, "Vélocité : " + gameWorld.getMineur().getDeplacement().getVelocite().toString(), 150, 950);
        }
        position.draw(spriteBatch, "Position : " + gameWorld.getMineur().getPosition().toString(), 150, 935);
        spriteBatch.end();
        
        debugRenderer.setProjectionMatrix(orthoCamera.combined);
        debugRenderer.begin(ShapeType.Line);
        debugRenderer.setColor(Color.RED);
        debugRenderer.rect(gameWorld.getMineur().getPosition().x, gameWorld.getMineur().getPosition().y, gameWorld.getMineur().getLARGEUR(), gameWorld.getMineur().getHAUTEUR());
        debugRenderer.rect(0, 5, gameWorld.getMineur().getLARGEUR(), gameWorld.getMineur().getHAUTEUR());
        debugRenderer.setColor(Color.YELLOW);
        TiledMapTileLayer layer = (TiledMapTileLayer) gameWorld.getMap().getLayers().get("surface");
        for (int y = 0; y <= layer.getHeight(); y++) {
                for (int x = 0; x <= layer.getWidth(); x++) {
                        Cell cell = layer.getCell(x, y);
                        if (cell != null) {
                                if (orthoCamera.frustum.boundsInFrustum(x + 0.5f, y + 0.5f, 0, 1, 1, 0))
                                        debugRenderer.rect(x, y, 1, 1);
                        }
                }
        }
        debugRenderer.end();
    }
}
