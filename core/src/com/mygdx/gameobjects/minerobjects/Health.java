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
package com.mygdx.gameobjects.minerobjects;

/**
 * Class which represent the health of the Miner
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Health {
    private float health;

    /**
     * Constructor called to instanciate this class with an health given
     * @param health the health to initialize
     */
    public Health(float health) {
        this.health = health;
    }
    
    /**
     * Constructor called to instanciate this class with an
     * hardcoded health.
     */
    public Health() {
        this.health = 1;
    }
    
    /**
     * Remove an amount into the health
     * @param amount the amount to remove
     */
    public void remove(float amount) {
        health -= amount;
    }
    
    /**
     * Add an amount into the health
     * @param amount the amount to add
     */
    public void add(float amount) {
        health += amount;
    }
    
    /**
     * Get the health value
     * @return the health value
     */
    public float getHealth() {
        return health;
    }

    /**
     * Set health with a value
     * @param health the value to set to health
     */
    public void setHealth(float health) {
        this.health = health;
    }
    
}
