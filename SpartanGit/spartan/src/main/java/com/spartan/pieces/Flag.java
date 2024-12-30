
package com.spartan.pieces;

import java.util.ArrayList;
import java.util.List;

import com.spartan.board.Board;
import com.spartan.board.Move;
import com.spartan.enumerations.Alliance;


/**
 * This class is used to initialize the flag.
 */
public class Flag extends Pawn {
    
    /**
     * 
     * @param position is the position of the flag in the board
     * @param alliance is the alliance of the flag. ( The color of the flag )
     */
    public Flag(final int position , final Alliance alliance){
        super(position, alliance, -1, 0);
    }
    
    @Override
    public List<Move> calculateValidMoves(final Board board) {
        return new ArrayList<>();// A flag has no legal moves
    }
}
