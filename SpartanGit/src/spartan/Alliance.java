package spartan;

import spartan.player.BluePlayer;
import spartan.player.Player;
import spartan.player.RedPlayer;

public enum Alliance {

    BLUE() {

        @Override
        public boolean isBlue() {
            return true;
        }


        @Override
        public int getDirection() {
            return UP_DIRECTION;
        }

        @Override
        public int getOppositeDirection() {
            return DOWN_DIRECTION;
        }


        @Override
        public Player choosePlayerByAlliance(final BluePlayer bluePlayer, final RedPlayer redPlayer) {
                return bluePlayer;
        }


    },
    RED() {

        @Override
        public boolean isBlue() {
            return false;
        }

        @Override
        public int getDirection() {
            return DOWN_DIRECTION;
        }

        @Override
        public int getOppositeDirection() {
            return UP_DIRECTION;
        }

        @Override
        public Player choosePlayerByAlliance(BluePlayer bluePlayer, RedPlayer redPlayer) {
            return redPlayer;
        }

    };

    public abstract int getDirection();

    public abstract int getOppositeDirection();

    public abstract boolean isBlue();

    public abstract Player choosePlayerByAlliance(final BluePlayer bluePlayer, final RedPlayer redPlayer);
    private static final int UP_DIRECTION = -1;

    private static final int DOWN_DIRECTION = 1;

}
