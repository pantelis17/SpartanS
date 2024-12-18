package main.java.spartan;

import main.java.spartan.enumeration.DeskDirectionE;
import main.java.spartan.player.BluePlayer;
import main.java.spartan.player.Player;
import main.java.spartan.player.RedPlayer;


/**
 * This enum contain the function we need to determine important information 
 * for the alliance (color) of the player.
 */
public enum Alliance {

    BLUE() { // if the player is blue the function return this values.

        public boolean isBlue() {// return true because the color of the player is blue.
            return true;
        }

        public DeskDirectionE getDirection() {// return an int which show us that this player is in the upper side of the board
            return DeskDirectionE.UP_DIRECTION;
        }

        public DeskDirectionE getOppositeDirection() {//return an int which show us that the opponent is in the lower side of the board
            return DeskDirectionE.DOWN_DIRECTION;
        }

        public Player choosePlayerByAlliance(final BluePlayer bluePlayer, final RedPlayer redPlayer) { //return the player who is the blue one
                return bluePlayer;
        }
    },
    RED() {//if the player is red we return this values.

        public boolean isBlue() {//the color of the player is red so not blue so false
            return false;
        }

        public DeskDirectionE getDirection() {//the player is in the lower side of the board
            return DeskDirectionE.DOWN_DIRECTION;
        }

        public DeskDirectionE getOppositeDirection() {// his opponent is in the upper side of the board
            return DeskDirectionE.UP_DIRECTION;
        }

        public Player choosePlayerByAlliance(BluePlayer bluePlayer, RedPlayer redPlayer) {// return the red player out of 2
            return redPlayer;
        }
    };

    public abstract DeskDirectionE getDirection(); // return the side of the player in the board

    public abstract DeskDirectionE getOppositeDirection();//return the opponent side in the board

    public abstract boolean isBlue();// determine if the player has blue color

    public abstract Player choosePlayerByAlliance(final BluePlayer bluePlayer, final RedPlayer redPlayer); // return the player we need
}
