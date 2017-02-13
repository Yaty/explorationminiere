package com.mygdx.gameobjects;

import com.mygdx.gameobjects.mineurobjects.Inventaire;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.mineurobjects.Money;
import com.mygdx.mehelpers.AssetLoader;
import com.mygdx.gameobjects.mineurobjects.Health;
import com.mygdx.mehelpers.handlers.handlers.InputHandler;

/**
 * Classe représentant le personnage du Mineur
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Mineur {
    public static boolean DPL_FLUIDE = false, DPL_AMORTISSEMENT = false;
    public static float LARGEUR, HAUTEUR;
    private final Money argent;
    private final Inventaire inventaire, equipement;
    public static Etat etat;
    public static Direction dirMineur;
    private final Vector2 position;
    private float runTime;
    public static boolean teteVersLaDroite, mineurAuSol, wasMoving, isOnEchelle, MINEUR_BOUGE;
    public static int HAUTEUR_CHUTE; // a faire
    private final float UNITE = 1/64f;
    private final Health health;
        
    /**
     * Représente la direction du mineur
     */
    public enum Direction { 
        Haut, 
        Bas, 
        Gauche, 
        Droite, 
        Arret
    };

    /**
     * Représente l'état du mineur
     */
    public enum Etat { 
        Miner, 
        Deplacement, 
        Arret, 
        Sauter,
        Echelle
    };
    
    /**
     * Constructeur du Mineur
     * Initialise toutes les variables et instancie CellsHandler
     * @param positionSpawn
     */
    public Mineur(Vector2 positionSpawn) {
        resetMineur();
        this.position = positionSpawn;
        runTime = 0f;
        this.argent = new Money(0);
        this.inventaire = new Inventaire();
        this.equipement = new Inventaire("pioche_bois");
        this.health = new Health();
    }
    
    public Mineur(int argent, Vector2 position, Inventaire inventaire, Inventaire equipement, float health) {
        resetMineur();
        runTime = 0f;
        this.argent = new Money(argent);
        this.position = position;
        this.inventaire = inventaire;
        this.equipement = equipement;
        this.health = new Health(health);
    }
    
    public void dispose() {
        equipement.dispose();
        inventaire.dispose();
    }

    public int getArgent() {
        return argent.getArgent();
    }
    
    public void recolterArgent(int idBlock) {
        argent.recolterArgent(idBlock);
    }
    
    public void retirerArgent(int somme) {
        argent.retirer(somme);
    }

    public void reload() {
        resetMineur();
    }
    
    private void resetMineur() {
        LARGEUR = UNITE * AssetLoader.regions[0].getRegionWidth();
        HAUTEUR = UNITE * AssetLoader.regions[0].getRegionHeight();
        etat = Etat.Arret;
        dirMineur = Direction.Arret;
        mineurAuSol = teteVersLaDroite = true;
        DPL_AMORTISSEMENT = DPL_FLUIDE = wasMoving = isOnEchelle = false;
    }

    public void teleportation(Vector2 posTp) {
        int distance = (int) Math.sqrt(Math.pow(posTp.x - position.x, 2) + Math.pow(posTp.y - position.y, 2));
        int coef = 50;
        if (argent.getArgent() >= coef*distance) {
            position.set(posTp);
            position.x += LARGEUR/2;
            resetMineur();
            argent.retirer(coef * distance);
        }
    }
    
    public Inventaire getInventaire() {
        return inventaire;
    }
    
    public Health getHealth(){
        return health;
    }
    
    public void respawn(Vector2 spawnPos){
        position.x = spawnPos.x;
        position.y = spawnPos.y;
    }
    
    private void preparerDeplacement() {
        if(InputHandler.ALLER_GAUCHE) {
            MINEUR_BOUGE = true;
            dirMineur = Direction.Gauche;
            teteVersLaDroite = false;
        }
        if(InputHandler.ALLER_DROITE) {
            MINEUR_BOUGE = true;
            dirMineur = Direction.Droite;
            teteVersLaDroite = true;
        }
        if(InputHandler.ALLER_BAS) {
            MINEUR_BOUGE = true;
            dirMineur = Direction.Bas;
        }
        if(InputHandler.ALLER_HAUT) {
            if(mineurAuSol) {
                MINEUR_BOUGE = true;
                dirMineur = Direction.Haut;
            }
        }
    }
    
    /**
     * @param deltaTime temps passé durant la dernière frame
     */
    public void update(float deltaTime) { //deltaTime = temps passé entre deux frames
        if (deltaTime == 0) return; // Si rien ne s'est passé on sort
        if(deltaTime > 0.1f) deltaTime = 0.1f; // Pour garder le jeu fluide
        runTime += deltaTime;
        
        preparerDeplacement();
        
        // Instanceof pour éviter de créer pleins de fois des objets alors que deplacement est déjà définit
        if(MINEUR_BOUGE && !DPL_FLUIDE) {// Si on est dans déplacement dit de type fluide et que le mineur n'est pas en train de sauter
            wasMoving = true;
            DPL_FLUIDE = true;
            DPL_AMORTISSEMENT = false;
        } else if(!MINEUR_BOUGE && wasMoving && !DPL_AMORTISSEMENT && etat.equals(Etat.Deplacement)){ // Sinon c'est un amortissement
            DPL_AMORTISSEMENT = true;
            DPL_FLUIDE = false;
        }
        
        resetForNextLoop();
    }
    
    private void resetForNextLoop() {
        MINEUR_BOUGE = false;
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

    public Inventaire getEquipement() {
        return equipement;
    }
}
