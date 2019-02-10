package spartan;

import spartan.player.BluePlayer;
import spartan.player.Player;
import spartan.player.RedPlayer;


/**
 * This enum contain the function we need to determine important information 
 * for the alliance (color) of the player.
 * 
 * @author pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos 2980
 */
public enum Alliance {

    BLUE() { // if the player is blue the function return this values.

        @Override
        public boolean isBlue() {// return true because the color of the player is blue.
            return true;
        }


        @Override
        public int getDirection() {// return an int which show us that this player is in the upper side of the board
            return UP_DIRECTION;
        }

        @Override
        public int getOppositeDirection() {//return an int which show us that the opponent is in the lower side of the board
            return DOWN_DIRECTION;
        }


        @Override
        public Player choosePlayerByAlliance(final BluePlayer bluePlayer, final RedPlayer redPlayer) { //return the player who is the blue one
                return bluePlayer;
        }


    },
    RED() {//if the player is red we return this values.

        @Override
        public boolean isBlue() {//the color of the player is red so not blue so false
            return false;
        }

        @Override
        public int getDirection() {//the player is in the lower side of the board
            return DOWN_DIRECTION;
        }

        @Override
        public int getOppositeDirection() {// his opponent is in the upper side of the board
            return UP_DIRECTION;
        }

        @Override
        public Player choosePlayerByAlliance(BluePlayer bluePlayer, RedPlayer redPlayer) {// return the red player out of 2
            return redPlayer;
        }

    };

    public abstract int getDirection(); // return the side of the player in the board

    public abstract int getOppositeDirection();//return the opponent side in the board

    public abstract boolean isBlue();// determine if the player has blue color

    public abstract Player choosePlayerByAlliance(final BluePlayer bluePlayer, final RedPlayer redPlayer); // return the player we need
    private static final int UP_DIRECTION = -1; 

    private static final int DOWN_DIRECTION = 1;

}
