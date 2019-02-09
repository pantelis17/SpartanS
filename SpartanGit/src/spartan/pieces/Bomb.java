
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
public class Bomb extends Pawn implements Serializable{
    
    public Bomb(final int position , final Alliance alliance,final int pos){
        super(position,alliance,0,pos);
    }
    @Override
    public List<Move> calculateValidMoves(final Board board) {
        return new ArrayList<>();
    }
    /*  @Override
     public String toString(){
         return Pawntype.BOMB.toSting();
     }
       */
}
