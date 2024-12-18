
package main.java.spartan.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.spartan.Alliance;
import main.java.spartan.board.Board;
import main.java.spartan.board.Move;


/**
 *This class is used to initialize the Bomb pawn 
 * 
 * @author Pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos 2980
 */
public class Bomb extends Pawn implements Serializable{
    /**
     * 
     * @param position is the position which it has on the board
     * @param alliance is the alliance of the pawn ( the color )
     * @param pos is the position which the pawn has in the hand of the player. Also its the position 
     * which this pawn will return if it die.
     */
    public Bomb(final int position , final Alliance alliance,final int pos){
        super(position,alliance,0,pos);
    }
    
    
    @Override
    public List<Move> calculateValidMoves(final Board board) {
        return new ArrayList<>(); // A bomb has no valid moves.
    }
}
