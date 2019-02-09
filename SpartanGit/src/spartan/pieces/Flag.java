
package spartan.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import spartan.Alliance;
import spartan.board.Board;

import spartan.board.Move;


/**
 *
 * @author user
 */
public class Flag extends Pawn implements Serializable{
    
    public Flag(final int position , final Alliance alliance){
        super(position,alliance,-1,0);
    }
    @Override
    public List<Move> calculateValidMoves(final Board board) {
    
        return new ArrayList<>();
    }
     
   /*  @Override
     public String toString(){
         return Pawntype.FLAG.toSting();
     }*/
}
