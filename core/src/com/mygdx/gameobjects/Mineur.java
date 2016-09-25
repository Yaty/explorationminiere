package com.mygdx.gameobjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.mehelpers.AssetLoader;
import com.mygdx.mehelpers.CellsHandler;
import com.mygdx.mehelpers.Deplacement.Amortissement;
import com.mygdx.mehelpers.Deplacement.Deplacement;
import com.mygdx.mehelpers.Deplacement.Fluide;
import com.mygdx.mehelpers.InputHandler;

/**
 * Classe représentant le personnage du Mineur
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Mineur {
    private final float GRAVITE, LARGEUR, HAUTEUR, MAX_VELOCITE, SAUT_VELOCITE, ECHELLE_VELOCITE;

    int nbEchelle = 10;

    public void setEtatMineur(Etat etat) {
        this.etat = etat;
    }

    /**
     * Représente la direction du mineur
     */
    public enum Direction { 

        /**
         * Va vers le haut (un saut)
         */
        Haut, 

        /**
         * Va vers le bas (une chute)
         */
        Bas, 

        /**
         * Va vers la gauche
         */
        Gauche, 

        /** 
         * Va vers la droite
         */
        Droite, 

        /**
         * Ne bouge pas
         */
        Arret };

    /**
     * Représente l'état du mineur
     */
    public enum Etat { 

        /**
         * Il mine
         */
        Miner, 

        /**
         * Il se déplace
         */
        Deplacement, 

        /**
         * Il est arrêté
         */
        Arret, 

        /**
         * Il saute
         */
        Sauter,
    
        /**
         * Il est sur une échelle
         */
        Echelle
    };
    private Etat etat;
    private Direction dirMineur;
    private final Vector2 position;
    private float runTime;
    private boolean teteVersLaDroite, mineurAuSol, moving, isInAmortissement, wasInAmortissement, wasMoving, isOnEchelle;
    private final float UNITE = 1/64f;
    private final TiledMap map;
    private final CellsHandler cellsHandler;
    private Deplacement deplacement;
    private boolean hasMovedWhileBreaking;
    
    /**
     * Constructeur du Mineur
     * Initialise toutes les variables et instancie CellsHandler
     * @param map la carte
     */
    public Mineur(TiledMap map) {
        GRAVITE = -0.1f;
        LARGEUR = UNITE * AssetLoader.regions[0].getRegionWidth();
        HAUTEUR = UNITE * AssetLoader.regions[0].getRegionHeight();
        MAX_VELOCITE = 2f;
        SAUT_VELOCITE = 4f;
        ECHELLE_VELOCITE = 2f;
        etat = Etat.Arret;
        dirMineur = Direction.Arret;
        this.map = map;
        position = new Vector2(getXDepart(), getYDepart());
        runTime = 0f;
        mineurAuSol = teteVersLaDroite = true;
        isInAmortissement = false;
        isOnEchelle = false;
        wasInAmortissement = false;
        wasMoving = false;
        cellsHandler = new CellsHandler(this);
        hasMovedWhileBreaking = false;
        
    }
    
    public void testHasMovedWhileBreaking(Direction dir){
        if (etat == Etat.Miner){
            System.out.println("DirMineur ="+ dirMineur + " dir=" + dir +".");
            if(dirMineur != dir){
                this.hasMovedWhileBreaking = true;
            }else{
                this.hasMovedWhileBreaking = false;
            }
        }
            
    }
    
    public void setHasMovedWhileBreaking(boolean bool){
        this.hasMovedWhileBreaking = bool;
    }
    
    public boolean getHasMovedWhileBreaking(){
        return this.hasMovedWhileBreaking;
    }
    /**
     * @return La coordonnée en X du mineur au départ du jeu
     */
    private float getXDepart() {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("surface");
        return layer.getWidth()/2 + LARGEUR/2;
        
    }
     /**
     * @return La coordonnée en Y du mineur au départ du jeu
     */   
    private float getYDepart() {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("surface");
        return layer.getHeight()-3; 
    }
    
    /**
     * @return la valeur gravité.
     */
    public float getGRAVITE() {
        return GRAVITE;
    }
    
    /**
     * @return la variable moving
     */
    public boolean isMoving() {
        return moving;
    }
    
    public float getVelociteMaxEchelle() {
        return ECHELLE_VELOCITE;
    }

    /**
     * @param moving valeur qui va être affecté
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * @param deltaTime temps passé durant la dernière frame
     */
    public void update(float deltaTime) { //deltaTime = temps passé entre deux frames
        if (deltaTime == 0) return; // Si rien ne s'est passé on sort
        if(deltaTime > 0.1f) deltaTime = 0.1f; // Pour garder le jeu fluide
        runTime += deltaTime;
       
        moving = false;
        
        if(InputHandler.keys[45] || InputHandler.keys[21]) {
            moving = true;
            testHasMovedWhileBreaking(Direction.Gauche);
            dirMineur = Direction.Gauche;
            
        }
        if (InputHandler.keys[32] || InputHandler.keys[22]) {
            moving = true;
            testHasMovedWhileBreaking(Direction.Droite);
            dirMineur = Direction.Droite;
        }
        if ((InputHandler.keys[19] || InputHandler.keys[54]) && mineurAuSol) {
            moving = true;
            testHasMovedWhileBreaking(Direction.Haut);
            dirMineur = Direction.Haut;
        }
        if(InputHandler.keys[20] || InputHandler.keys[47]) {
            moving = true;
            testHasMovedWhileBreaking(Direction.Bas);
            dirMineur = Direction.Bas;
        }
        
        
        if(InputHandler.keys[33]) // Echelle (F)
        if(InputHandler.keys[33] && this.nbEchelle >= 0 ){ // Echelle (E)
            cellsHandler.setLadder((int) position.x,(int) position.y);
               //soon limiter par nb echelle
        }
        
        
        // Instanceof pour éviter de créer pleins de fois des objets alors que deplacement est déjà définit
        if(moving && !(deplacement instanceof Fluide)) { // Si on est dans déplacement dit de type fluide et que le mineur n'est pas en train de sauter
            wasMoving = true;
            deplacement = new Fluide(this);
        } else if(!moving && wasMoving && !(deplacement instanceof Amortissement) && !etat.equals(Etat.Sauter) && !etat.equals(Etat.Echelle)){ // Sinon c'est un amortissement
            deplacement = new Amortissement(this);
        }
        
        // Ca bug car il faut empecher tout deplacement tant que le joueur est en vol
        

        
        if(deplacement != null) {
            //System.out.println("1Dpl : " + deplacement.getClass() + " Etat : " + etat + " Direction : " + dirMineur + " wasMoving : " + wasMoving);
            if(!etat.equals(Etat.Miner)) {
                deplacement.move();
                if(isOnEchelle && !cellsHandler.isLadderHere((int) position.x, (int) position.y))
                    isOnEchelle = false;
            }
            //System.out.println("2Dpl : " + deplacement.getClass() + " Etat : " + etat + " Direction : " + dirMineur + " wasMoving : " + wasMoving);
            if(deplacement.getVelocite().isZero() && !(deplacement instanceof Fluide)) {
                deplacement = null;
                wasMoving = false;
                dirMineur = Direction.Arret;
                etat = Etat.Arret;
                if(cellsHandler.isLadderHere((int) position.x, (int) position.y)) 
                    isOnEchelle = true;
            }          
        }
    }  

    public Deplacement getDeplacement() {
        return this.deplacement;
    }
    
    /**
     * @return vrai si le mineur est mode amortissement
     */
    public boolean isIsInAmortissement() {
        return deplacement instanceof Amortissement;
    }
    
    /**
     * @return vrai si le mineur est arreté sur une echelle
     */
    public boolean isOnEchelle() {
        return isOnEchelle;
    }
    
    /**
     * @param bool valeur a affecter
     */
    public void setWasMoving(boolean bool) {
        wasMoving = bool;
    }
    
    public boolean getWasMoving(){
        return wasMoving;
    }

    /**
     * @return l'état du mineur
     */
    public Etat getEtatMineur() {
        return etat;
    }
    
    /**
     * @return la map (la carte)
     */
    public TiledMap getMap(){
        return this.map;
    }
    
    /**
     * Change le type de déplacement du mineur
     * @param typeDepla objet a affecter
     */
    public void setTypeDeplacement(Deplacement typeDepla) {
        deplacement = typeDepla;
    }
    
     /**
     * @return l'objet deplacement
     */
    public Deplacement getTypeDeplacement() {
        return deplacement;
    }   
    
    /**
     * @return la direction du mineur
     */
    public Direction getDirectionMineur() {
        return dirMineur;
    }
    
    /**
     * @param dir variable à affecter
     */
    public void setDirectionMineur(Direction dir) {
        dirMineur = dir;
    }
     
    /**
     * @return l'objet qui gère les cellules, blocs
     */
    public CellsHandler getCellsHandler(){
        return this.cellsHandler;
    }
    
    /**
     * @return la largeur du mineur
     */
    public float getLARGEUR() {
        return LARGEUR;
    }

    /**
     * @return la hauteur du mineur
     */
    public float getHAUTEUR() {
        return HAUTEUR;
    }

    /**
     * @return la velocite maximale en abscisse du mineur
     */
    public float getMAX_VELOCITE() {
        return MAX_VELOCITE;
    }

    /**
     * @return la velocite maximale en ordonnée du mineur
     */
    public float getSAUT_VELOCITE() {
        return SAUT_VELOCITE;
    }

    /**
     * @return le vecteur position
     */
    public Vector2 getPosition() {
        return position;
    }
 
    /**
     * @return runtime
     */
    public float getRunTime() {
        return runTime;
    }

    /**
     * @param teteVersLaDroite booléen à affecter
     */
    public void setTeteVersLaDroite(boolean teteVersLaDroite) {
        this.teteVersLaDroite = teteVersLaDroite;
    }
    
    /**
     * @return vrai si le mineur va vers la droite, faux sinon
     */
    public boolean isTeteVersLaDroite() {
        return teteVersLaDroite;
    }

    /**
     * @param mineurAuSol booléen à affecter
     */
    public void setMineurAuSol(boolean mineurAuSol) {
        this.mineurAuSol = mineurAuSol;
    }
    
    /**
     * @return vrai si le mineur  touche le sol, faux sinon
     */
    public boolean isMineurAuSol() {
        return mineurAuSol;
    }
}
