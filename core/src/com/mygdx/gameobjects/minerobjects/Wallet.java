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

import com.mygdx.mehelpers.handlers.handlers.MapHandler;

/**
 * Class which represent a wallet
 * @author Alexis Clément, Hugo Da Roit, Benjamin Lévèque, Alexis Montagne
 */
public class Wallet {
    private int money;

    /**
     * Constructor to create a wallet with an amount of money
     * @param money
     */
    public Wallet(int money) {
        this.money = money;
    }
    
    /**
     * Get money in the wallet
     * @return the money
     */
    public int getMoney() {
        return money;
    }
    
    /**
     * Add to the wallet some money according to a block which was beforehand
     * desotroyed by the miner.
     * @param idBlock the id of the destroyed block
     */
    public void collectMoney(int idBlock) {
        if (idBlock == MapHandler.idDiamond) money += 500;
        else if (idBlock == MapHandler.idCoal) money += 10;
        else if (idBlock == MapHandler.idDirt) money++;
        else if (idBlock == MapHandler.idEmerald) money += 100;
        else if (idBlock == MapHandler.idGlowstone) money += 10;
        else if (idBlock == MapHandler.idGold) money += 50;
        else if (idBlock == MapHandler.idGrass) money++;
        else if (idBlock == MapHandler.idIron) money += 30;
        else if (idBlock == MapHandler.idLapis) money += 80;
    }

    /**
     * Remove an amount of money in the wallet
     * @param amount the amount to remove
     */
    public void withdraw(int amount) {
        money -= amount;
    }
}
