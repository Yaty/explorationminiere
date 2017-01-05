package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
    private final float GRAVITE = -0.4f, LARGEUR, HAUTEUR, MAX_VELOCITE = 4f, SAUT_VELOCITE = 8f, ECHELLE_VELOCITE = 6f;
    private int argent;
    private final Inventaire inventaire;
    private final Equipement equipement;

    public void setEtatMineur(Etat etat) {
        this.etat = etat;
    }

    public void gestionArgent(int id) {
        if (id == cellsHandler.getIdDiamant()) argent += 500;
        else if (id == cellsHandler.getIdCharbon()) argent += 10;
        else if (id == cellsHandler.getIdTerre()) argent++;
        else if (id == cellsHandler.getIdEmeraude()) argent += 100;
        else if (id == cellsHandler.getIdGlowstone()) argent += 10;
        else if (id == cellsHandler.getIdOr()) argent += 50;
        else if (id == cellsHandler.getIdHerbe()) argent++;
        else if (id == cellsHandler.getIdFer()) argent += 30;
        else if (id == cellsHandler.getIdLapis()) argent += 80;
    }

    public int getArgent() {
        return argent;
    }

    public void reload(TiledMap map) {
        this.map = map;
        etat = Etat.Arret;
        dirMineur = Direction.Arret;
        position.set(getXDepart(), getYDepart());
        mineurAuSol = teteVersLaDroite = true;
        isOnEchelle = false;
        wasMoving = false;   
        cellsHandler.reload();
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
    private boolean teteVersLaDroite, mineurAuSol, moving, wasMoving, isOnEchelle;
    private final float UNITE = 1/64f;
    private TiledMap map;
    private final CellsHandler cellsHandler;
    private Deplacement deplacement;
    private boolean poserEchelle = true;
    private float health = 1f;
    
    /**
     * Constructeur du Mineur
     * Initialise toutes les variables et instancie CellsHandler
     * @param map la carte
     */
    public Mineur(TiledMap map) {
        LARGEUR = UNITE * AssetLoader.regions[0].getRegionWidth();
        HAUTEUR = UNITE * AssetLoader.regions[0].getRegionHeight();
        etat = Etat.Arret;
        dirMineur = Direction.Arret;
        this.map = map;
        position = new Vector2(getXDepart(), getYDepart());
        runTime = 0f;
        mineurAuSol = teteVersLaDroite = true;
        isOnEchelle = false;
        wasMoving = false;
        cellsHandler = new CellsHandler(this);
        argent = 0;
        inventaire = new Inventaire();
        equipement = new Equipement();
    }
    
    public Mineur(TiledMap map, int argent, Vector2 position, Inventaire inventaire, Equipement equipement) {
        LARGEUR = UNITE * AssetLoader.regions[0].getRegionWidth();
        HAUTEUR = UNITE * AssetLoader.regions[0].getRegionHeight();
        etat = Etat.Arret;
        dirMineur = Direction.Arret;
        this.map = map;   
        runTime = 0f;
        mineurAuSol = teteVersLaDroite = true;
        isOnEchelle = false;
        wasMoving = false;
        cellsHandler = new CellsHandler(this);
        this.argent = argent;
        this.position = position;
        this.inventaire = inventaire;
        this.equipement = equipement;
    }
    
    /**
     * @return La coordonnée en X du mineur au départ du jeu
     */
    private float getXDepart() {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("surface");
        return layer.getWidth()/2 + LARGEUR/2;
    }
    
    public Inventaire getInventaire() {
        return inventaire;
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
    
    public float getHealth(){
        return this.health;
    }
    
    public void setHealth(float health){
        this.health=health;
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
    
    public boolean getPoserEchelle(){
        return poserEchelle;
    }

    /**
     * @param moving valeur qui va être affecté
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void gestionVie() {
        // Note : Toutes la gestion ne se passe pas ici (pour la saut c'est dans Deplacement et pour le descente c'est dans CellsHandler)
        //Si la vie du mineur tombe en dessous de 0
        if(cellsHandler.isMineurInBase() || cellsHandler.isMineurInSurface())
            health += 0.0005f;
        else if(dirMineur == Direction.Gauche || dirMineur == Direction.Droite)
            health -= 0.0005f;
        
        if(health <= 0f){
            health = 1f;
            respawn(getXDepart(),getYDepart());
        } else if(health > 1f)
            health = 1f;
    }
    
    private void respawn(float xspawn,float yspawn){
        position.x = xspawn;
        float y = yspawn;
        float x = xspawn;
        //position.y=getYDepart();
        while(cellsHandler.getBloc((int)getXDepart(),(int)y-1) == 0){
            y--;
            // Je sais pas encore vraiment ce qu'on va faire ici ... En fait c'est simple mais c'est chiant
            if(y<0) break;
        }
        position.y = y;
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
            dirMineur = Direction.Gauche;
            setTeteVersLaDroite(false);
        }
        if (InputHandler.keys[32] || InputHandler.keys[22]) {
            moving = true;
            dirMineur = Direction.Droite;
            setTeteVersLaDroite(true);
        }
        if ((InputHandler.keys[19] || InputHandler.keys[54]) && mineurAuSol) {
            moving = true;
            dirMineur = Direction.Haut;
        }
        if(InputHandler.keys[20] || InputHandler.keys[47]) {
            moving = true;
            dirMineur = Direction.Bas;
        }
        
        // faut un timer dans le cas ou on monte une echelle 
        if(Gdx.input.isKeyJustPressed(33) && inventaire.firstSlotWithItem(Item.ECHELLE).getAmount() > 0 && this.poserEchelle == true && (this.deplacement == null || this.dirMineur == Direction.Haut) && this.isOnEchelle()==false ){ // Echelle (E)
            System.out.println("Preminer if");
            if(this.getCellsHandler().isCellObjectHere((int)position.x, (int)position.y)){
                System.out.println("Deuxieme if");
                cellsHandler.setLadder((int) position.x,(int) position.y);
                try {
                    inventaire.remove(Item.ECHELLE, 1);
                } catch (Exception ex) {
                    Gdx.app.debug("Exception suppression échelles", ex.getMessage());
                }
                poserEchelle = false;
            }
        }
        if(!this.getCellsHandler().isCellObjectHere((int)position.x, (int)position.y) && Gdx.input.isKeyJustPressed(33) && this.poserEchelle == true){
                cellsHandler.setLadder((int) position.x,(int) position.y);
            }
        
        if(Gdx.input.isKeyJustPressed(46)){
            System.out.println("\naa");
            int direction = 0;
            if(this.isTeteVersLaDroite())
                direction = 1;
            else
                direction = -1;
            
            if(cellsHandler.getObject((int) position.x+direction, (int) position.y) == 0){
                cellsHandler.setPilier((int) position.x, (int) position.y);
            }else{
                cellsHandler.ramassePilier((int) position.x+direction, (int) position.y);
        
            }
        }
        
        if(Gdx.input.isKeyJustPressed(48)){
            cellsHandler.setTNT((int) position.x, (int) position.y);
        }
        
        if(Gdx.input.isKeyJustPressed(53)){
            cellsHandler.makeTNTexplode((int) position.x, (int) position.y);
        }
        
        if(Gdx.input.isKeyJustPressed(Keys.B)) {
            cellsHandler.genererBase(isTeteVersLaDroite() ? ((int) position.x)+1 : ((int) position.x)-2, (int) position.y);
        }
        
        // Instanceof pour éviter de créer pleins de fois des objets alors que deplacement est déjà définit
        if(moving && !(deplacement instanceof Fluide)) {// Si on est dans déplacement dit de type fluide et que le mineur n'est pas en train de sauter
            wasMoving = true;
            deplacement = new Fluide(this);
        } else if(!moving && wasMoving && !(deplacement instanceof Amortissement) && etat.equals(Etat.Deplacement)){ // Sinon c'est un amortissement
            deplacement = new Amortissement(this);
        }
        
        // Ca bug car il faut empecher tout deplacement tant que le joueur est en vol
        
        if(deplacement != null) {
            poserEchelle = true;
            //System.out.println("1Dpl : " + deplacement.getClass() + " Etat : " + etat + " Direction : " + dirMineur + " wasMoving : " + wasMoving);
            deplacement.move();
            if(isOnEchelle && !cellsHandler.isLadderHere((int) position.x, (int) position.y))
                isOnEchelle = false;
            //System.out.println("2Dpl : " + deplacement.getClass() + " Etat : " + etat + " Direction : " + dirMineur + " wasMoving : " + wasMoving);
            if(deplacement.getVelocite().isZero() && !etat.equals(Etat.Miner)) {
                deplacement = null;
                wasMoving = false;
                dirMineur = Direction.Arret;
                etat = Etat.Arret;
                if(cellsHandler.isLadderHere((int) position.x, (int) position.y)) 
                    isOnEchelle = true;
            }          
        }
        gestionVie();
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
    
    public void removeArgent(int argent) {
        this.argent -= argent;
    }
    
    public Equipement getEquipement() {
        return equipement;
    }
}
