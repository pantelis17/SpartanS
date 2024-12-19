package com.spartan.player;

import com.spartan.board.Board;
import com.spartan.enumerations.Alliance;
import com.spartan.pieces.StackPawns;

/**
 *  This class contain every function we need for the bluePlayer.
 */
public class BluePlayer extends Player {

    public BluePlayer(final Board board) {
        super(board, new StackPawns(Alliance.BLUE).getStack(), 0, 40);
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getRedPlayer();
    }
}
