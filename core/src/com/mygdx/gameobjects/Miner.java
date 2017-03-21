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
package com.mygdx.gameobjects;

import com.mygdx.gameobjects.minerobjects.Inventory;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.gameobjects.minerobjects.Wallet;
import com.mygdx.gameobjects.minerobjects.Health;
import com.mygdx.gameobjects.minerobjects.Item;
import static com.mygdx.mehelpers.AssetLoader.dig_sound;
import static com.mygdx.mehelpers.AssetLoader.run_sound;
import com.mygdx.mehelpers.handlers.handlers.InputHandler;

/**
 * Class which represent the Miner
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Miner {
    public static boolean MV_DYNAMIC = false, MV_BRAKING = false;
    public static float WIDTH, HEIGHT;
    private final Wallet wallet;
    private final Inventory inventory, equipment;
    public static State state;
    public static Direction direction;
    private final Vector2 position;
    private float runTime;
    public static boolean headTowardsRight, minerOnTheGround, wasMoving, isOnLadder, MINER_MOVING,hasDied;
    public static int FALL_HEIGHT; // a faire
    private final Health health;
        
    /**
     * Directon of the miner
     */
    public enum Direction { 
        TOP, 
        BOTTOM, 
        LEFT, 
        RIGHT, 
        STOPPED
    };

    /**
     * The state of the miner
     */
    public enum State { 
        MINING, 
        MOVING, 
        STOPPED, 
        JUMPING,
        LADDER_CLIMBING
    };
    
    /**
     * Miner constructor
     * @param positionSpawn the position where the miner needs to be
     */
    public Miner(Vector2 positionSpawn) {
        resetMiner();
        this.position = positionSpawn;
        runTime = 0f;
        this.wallet = new Wallet(0);
        this.inventory = new Inventory(15, 10, 5, 1);
        this.equipment = new Inventory("pioche_bois");
        this.health = new Health();
    }
    
    /**
     * Constructor to use when loading a game
     * @param money amount of money
     * @param position start position
     * @param inventory the inventory
     * @param equipement the equipment
     * @param health curent health
     */
    public Miner(int money, Vector2 position, Inventory inventory, Inventory equipement, float health) {
        resetMiner();
        runTime = 0f;
        this.wallet = new Wallet(money);
        this.position = position;
        this.inventory = inventory;
        this.equipment = equipement;
        this.health = new Health(health);
    }
    
    /**
     * Called when the game is being disposed
     */
    public void dispose() {
        equipment.dispose();
        inventory.dispose();
    }

    /**
     * Get the amount of money the miner have
     * @return the amount of money
     */
    public int getMoney() {
        return wallet.getMoney();
    }
    
    /**
     * Call the wallet to add money according to the broken block 
     * @param idBlock the destroyed block
     */
    public void collectMoney(int idBlock) {
        wallet.collectMoney(idBlock);
    }
    
    /**
     * Remove money in the wallet
     * @param amount the amount to remove
     */
    public void withdrawMoney(int amount) {
        wallet.withdraw(amount);
    }
    
    public static void printInfos() {
        System.out.println("###### MINER ######");
        System.out.println("State     : " + Miner.state);
        System.out.println("Direction : " + Miner.direction);
        System.out.println("HeadRight : " + Miner.headTowardsRight);
        System.out.println("On ladder : " + Miner.isOnLadder);
        System.out.println("Grounded  : " + Miner.minerOnTheGround);
        System.out.println("Moved     : " + Miner.wasMoving);
        System.out.println("MV        : " + Miner.MV_BRAKING + " - " + Miner.MV_DYNAMIC);
        System.out.println("###### MINER ######");
    }

    /**
     * Reset the miner
     */
    public void reload() {
        resetMiner();
        if(inventory.checkInventory(Item.LADDER) == 0)
            inventory.store(Item.LADDER, 20);
        if(inventory.checkInventory(Item.PILLAR) == 0)
            inventory.store(Item.PILLAR, 10);
        if(inventory.checkInventory(Item.TNT) == 0)
            inventory.store(Item.TNT, 2);
        if(inventory.checkInventory(Item.BASE) == 0)
            inventory.store(Item.BASE, 1);
    }
    
    private void resetMiner() {
        System.out.println("RESET MINER");
        state = State.STOPPED;
        direction = Direction.STOPPED;
        minerOnTheGround = headTowardsRight = true;
        MV_BRAKING = MV_DYNAMIC = wasMoving = isOnLadder = false;
    }
    
    public boolean isDead(){
        if(hasDied)
            return true;
        return false;
    }
    
    public void setHasDied(boolean b){
        hasDied = b;
    }

    /**
     * Teleport the miner at a given position, also remove money according
     * to the distance
     * @param posTp the position where the miner should be teleported
     */
    public void teleportation(Vector2 posTp) {
        int distance = (int) Math.sqrt(Math.pow(posTp.x - position.x, 2) + Math.pow(posTp.y - position.y, 2));
        int coef = 50;
        if (wallet.getMoney() >= coef*distance) {
            position.set(posTp);
            position.x += WIDTH/2;
            resetMiner();
            wallet.withdraw(coef * distance);
        }
    }
    
    /**
     * Getter for the inventory
     * @return the inventory of the miner
     */
    public Inventory getInventory() {
        return inventory;
    }
    
    /**
     * Get the health of the miner
     * @return the health
     */
    public float getHealth(){
        return health.getHealth();
    }
    
    /**
     * Teleport the miner at a given position freely
     * @param spawnPos the position where the miner will be teleported
     */
    public void respawn(Vector2 spawnPos){
        position.x = spawnPos.x;
        position.y = spawnPos.y;
    }
    
    private void prepareNextMove() {
        if(InputHandler.GO_LEFT) {
            MINER_MOVING = true;
            direction = Direction.LEFT;
            headTowardsRight = false;
        }
        if(InputHandler.GO_RIGHT) {
            MINER_MOVING = true;
            direction = Direction.RIGHT;
            headTowardsRight = true;
        }
        if(InputHandler.GO_DOWN) {
            MINER_MOVING = true;
            direction = Direction.BOTTOM;
        }
        if(InputHandler.GO_UPWARDS) {
            MINER_MOVING = true;
            direction = Direction.TOP;
        }
    }
    
    /**
     * Update the miner
     * Called every frame
     * @param deltaTime time passed during the last frame
     */
    public void update(float deltaTime) { //deltaTime = temps passé entre deux frames
        if (deltaTime == 0) return; // Si rien ne s'est passé on sort
        if(deltaTime > 0.1f) deltaTime = 0.1f; // Pour garder le jeu fluide
        runTime += deltaTime;
        
        resetForNextLoop();
        prepareNextMove();
        
        // Instanceof pour éviter de créer pleins de fois des objets alors que deplacement est déjà définit
        if(MINER_MOVING && !MV_DYNAMIC) {// Si on est dans déplacement dit de type fluide et que le mineur n'est pas en train de jumping
            wasMoving = true;
            MV_DYNAMIC = true;
            MV_BRAKING = false;
        } else if(!MINER_MOVING && wasMoving && !MV_BRAKING && state.equals(State.MOVING)){ // Sinon c'est un amortissement
            MV_BRAKING = true;
            MV_DYNAMIC = false;
        } 
        
        playSound();
        
    }
    
    public static void playSound(){
        if(state != State.STOPPED && dig_sound.isPlaying())
            dig_sound.stop();

        if(state == State.MOVING && minerOnTheGround & !run_sound.isPlaying())
            run_sound.play();
        else if(state == State.STOPPED && run_sound.isPlaying())
            run_sound.stop();
    }
    
    private void resetForNextLoop() {
        MINER_MOVING = false;
    }

    
    /**
     * @return the position of the miner
     */
    public Vector2 getPosition() {
        return position;
    }
    
    /**
     * Get the health object
     * @return the healh object
     */
    public Health getHealthObj() {
        return health;
    }
 
    /**
     * @return runtime
     */
    public float getRunTime() {
        return runTime;
    }
    
    public static void stopMiner() {
        MV_BRAKING = false;
        MV_DYNAMIC = false;
        direction = Direction.STOPPED;
        state = State.STOPPED;
        
    }

    /**
     * Get the equipment object
     * @return the equipment object
     */
    public Inventory getEquipment() {
        return equipment;
    }
}
