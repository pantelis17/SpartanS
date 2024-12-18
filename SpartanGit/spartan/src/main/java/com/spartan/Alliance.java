package com.spartan;

/**
 * This enum contain the function we need to determine important information 
 * for the alliance (color) of the player.
 */
public enum Alliance {

    BLUE() { // if the player is blue the function return this values.

        public boolean isBlue() {// return true because the color of the player is blue.
            return true;
        }
    },
    RED() {//if the player is red we return this values.

        public boolean isBlue() {//the color of the player is red so not blue so false
            return false;
        }
    };

    public abstract boolean isBlue();// determine if the player has blue color
}
