package com.spartan.player;

import com.spartan.board.Board;
import com.spartan.enumerations.Alliance;
import com.spartan.pieces.StackPawns;

/**
 *  This class contain every function we need for the redPlayer.
 */
public class RedPlayer extends Player {

    public RedPlayer(final Board board) {
        super(board, new StackPawns(Alliance.RED).getStack(), 60, 100);
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBluePlayer();
    }
}
