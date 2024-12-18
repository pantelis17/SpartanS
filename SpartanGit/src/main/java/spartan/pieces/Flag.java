
package main.java.spartan.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.spartan.Alliance;
import main.java.spartan.board.Board;
import main.java.spartan.board.Move;


/**
 * This class is used to initialize the flag.
 * 
 * @author Pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos 2980
 */
public class Flag extends Pawn implements Serializable{
    
    /**
     * 
     * @param position is the position of the flag in the board
     * @param alliance is the alliance of the flag. ( The color of the flag )
     */
    public Flag(final int position , final Alliance alliance){
        super(position,alliance,-1,0);
    }
    
    @Override
    public List<Move> calculateValidMoves(final Board board) {
        return new ArrayList<>();// A flag has no legal moves
    }
}
