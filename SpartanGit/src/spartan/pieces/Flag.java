
package spartan.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import spartan.Alliance;
import spartan.board.Board;
import spartan.board.Move;


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
