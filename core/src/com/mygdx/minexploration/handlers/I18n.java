/* 
 * Copyright (C) 2017
 * Mail : Hugo Da Roit - contact@hdaroit.fr
 * GitHub : https://github.com/Yaty
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mygdx.minexploration.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @see ResourceBundle
 * @author Cem Ikta
 */
public enum I18n {
    MENU("Menu"),
    GAME("Game");

    private final I18NBundle resourceBundle;
    private final static Logger LOGGER = Logger.getLogger(I18n.class.getName());
    private I18n(String bundleFile) {
        resourceBundle = I18NBundle.createBundle(Gdx.files.internal("i18n/" + bundleFile));
    }

    /**
     * Gets a string for the given key from resource bundle.
     *
     * @param key the key for the desired string
     * @return the string for the given key
     */
    public String getString(String key) {
        try {
            return resourceBundle.get(key);
        } catch (MissingResourceException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return "err#";
        }
    }

}
