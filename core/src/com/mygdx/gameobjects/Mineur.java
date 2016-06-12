package com.mygdx.gameobjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.mehelpers.AssetLoader;
import com.mygdx.mehelpers.CellsHandler;
import com.mygdx.mehelpers.Deplacement.Amortissement;
import com.mygdx.mehelpers.Deplacement.Deplacement;
import com.mygdx.mehelpers.Deplacement.Fluide;
import com.mygdx.mehelpers.InputHandler;

/**
 *
 * @author Hugo
 */
public class Mineur {
    private final float GRAVITE, LARGEUR, HAUTEUR, MAX_VELOCITE, SAUT_VELOCITE, AMORTISSEMENT;

    /**
     *
     */
    public enum Direction { Haut, Bas, Gauche, Droite, Arret };

    /**
     *
     */
    public enum Etat { Miner, Deplacement, Arret, Sauter };
    private Etat etat;
    private Direction dirMineur;
    private final Vector2 position;
    private float runTime;
    private boolean teteVersLaDroite, mineurAuSol, moving, isInAmortissement, wasInAmortissement, wasMoving;
    private final float UNITE = 1/64f;
    private final TiledMap map;
    private final CellsHandler cellsHandler;
    private Deplacement deplacement;
    
    /**
     *
     * @param map
     */
    public Mineur(TiledMap map) {
        GRAVITE = -0.1f;
        LARGEUR = UNITE * AssetLoader.regions[0].getRegionWidth();
        HAUTEUR = UNITE * AssetLoader.regions[0].getRegionHeight();
        MAX_VELOCITE = 2f;
        SAUT_VELOCITE = 4f;
        AMORTISSEMENT = 0.87f;
        etat = Etat.Arret;
        dirMineur = Direction.Arret;
        position = new Vector2(5.5f - LARGEUR/2, 13);
        runTime = 0f;
        mineurAuSol = teteVersLaDroite = true;
        isInAmortissement = false;
        wasInAmortissement = false;
        wasMoving = false;
        this.map = map;
        cellsHandler = new CellsHandler(this);
    }
    
    /**
     *
     * @return
     */
    public float getGRAVITE() {
        return GRAVITE;
    }
    
    /**
     *
     * @return
     */
    public boolean isMoving() {
        return moving;
    }

    /**
     *
     * @param moving
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     *
     * @param deltaTime
     */
    public void update(float deltaTime) { //deltaTime = temps passé entre deux frames
        if (deltaTime == 0) return; // Si rien ne s'est passé on sort
        if(deltaTime > 0.1f) deltaTime = 0.1f; // Pour garder le jeu fluide
        runTime += deltaTime;
       
        moving = false;
        
        if(InputHandler.keys[45] || InputHandler.keys[21]) {
            moving = true;
            dirMineur = Direction.Gauche;
            etat = Etat.Deplacement;
        }
        if (InputHandler.keys[32] || InputHandler.keys[22]) {
            moving = true;
            dirMineur = Direction.Droite;
            etat = Etat.Deplacement;
        }
        if ((InputHandler.keys[19] || InputHandler.keys[54]) && mineurAuSol) {
            moving = true;
            dirMineur = Direction.Haut;
        }
        if(InputHandler.keys[20] || InputHandler.keys[47]) {
            moving = true;
            dirMineur = Direction.Bas;
        }
        
        
        if(InputHandler.keys[33]) // Echelle (F)
            cellsHandler.setLadder((int) position.x,(int) position.y);
        
        
        // Instanceof pour éviter de créer pleins de fois des objets alors que deplacement est déjà définit
        if(moving && !(deplacement instanceof Fluide)) { // Si on est dans déplacement dit de type fluide et que le mineur n'est pas en train de sauter
            wasMoving = true;
            deplacement = new Fluide(this);
        } else if(!moving && wasMoving && !(deplacement instanceof Amortissement) && !etat.equals(Etat.Sauter)){ // Sinon c'est un amortissement
            deplacement = new Amortissement(this);
        }
        
        // Sa bug car il faut empecher tout deplacement tant que le joueur est en vol
        
        
        if(deplacement != null) {
            deplacement.move();
            System.out.println(deplacement.getClass());
            if(deplacement.getVelocite().isZero() && !(deplacement instanceof Fluide)) {
                deplacement = null;
                wasMoving = false;
                dirMineur = Direction.Arret;
                etat = Etat.Arret;
            }          
        }
    }    

    /**
     *
     * @return
     */
    public boolean isIsInAmortissement() {
        return isInAmortissement;
    }
    
    /**
     *
     * @param bool
     */
    public void setWasMoving(boolean bool) {
        wasMoving = bool;
    }

    /**
     *
     * @param isInAmortissement
     */
    public void setIsInAmortissement(boolean isInAmortissement) {
        this.isInAmortissement = isInAmortissement;
    }

    /**
     *
     * @return
     */
    public boolean isWasInAmortissement() {
        return wasInAmortissement;
    }

    /**
     *
     * @param wasInAmortissement
     */
    public void setWasInAmortissement(boolean wasInAmortissement) {
        this.wasInAmortissement = wasInAmortissement;
    }
    
    /**
     *
     * @return
     */
    public Etat getEtatMineur() {
        return etat;
    }
    
    /**
     *
     * @return
     */
    public TiledMap getMap(){
        return this.map;
    }
    
    /**
     *
     * @param typeDepla
     */
    public void setTypeDeplacement(Deplacement typeDepla) {
        deplacement = typeDepla;
    }
    
     /**
     *
     */
    public Deplacement getTypeDeplacement() {
        return deplacement;
    }   
    
    /**
     *
     * @return
     */
    public Direction getDirectionMineur() {
        return dirMineur;
    }
    
    /**
     *
     * @param etat
     */
    public void setEtatMineur(Etat etat) {
        this.etat = etat;
    }
    
    /**
     *
     * @param dir
     */
    public void setDirectionMineur(Direction dir) {
        dirMineur = dir;
    }
     
    /**
     *
     * @return
     */
    public CellsHandler getCellsHandler(){
        return this.cellsHandler;
    }
    
    /**
     *
     * @return
     */
    public float getLARGEUR() {
        return LARGEUR;
    }

    /**
     *
     * @return
     */
    public float getHAUTEUR() {
        return HAUTEUR;
    }

    /**
     *
     * @return
     */
    public float getMAX_VELOCITE() {
        return MAX_VELOCITE;
    }

    /**
     *
     * @return
     */
    public float getSAUT_VELOCITE() {
        return SAUT_VELOCITE;
    }

    /**
     *
     * @return
     */
    public float getAMORTISSEMENT() {
        return AMORTISSEMENT;
    }

    /**
     *
     * @return
     */
    public Vector2 getPosition() {
        return position;
    }
 
    /**
     *
     * @return
     */
    public float getRunTime() {
        return runTime;
    }

    /**
     *
     * @param teteVersLaDroite
     */
    public void setTeteVersLaDroite(boolean teteVersLaDroite) {
        this.teteVersLaDroite = teteVersLaDroite;
    }
    
    /**
     *
     * @return
     */
    public boolean isTeteVersLaDroite() {
        return teteVersLaDroite;
    }

    /**
     *
     * @param mineurAuSol
     */
    public void setMineurAuSol(boolean mineurAuSol) {
        this.mineurAuSol = mineurAuSol;
    }
    
    /**
     *
     * @return
     */
    public boolean isMineurAuSol() {
        return mineurAuSol;
    }   
}
