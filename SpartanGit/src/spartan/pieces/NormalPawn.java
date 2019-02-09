/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartan.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import spartan.Alliance;
import spartan.board.Board;
import spartan.board.BoardUtilities;
import spartan.board.Move.*;
import spartan.board.Move;
import spartan.board.Tile;

/**
 *
 * @author user
 */
public class NormalPawn extends Pawn implements Serializable {
    
    
    private static final int[] CANDIDATE_VALID_MOVES = {-10, -1, 1, 10};
    public NormalPawn(final int position , final Alliance alliance,final int value,final int pos){
        super(position,alliance,value,pos);
    }

    @Override
    public List<Move> calculateValidMoves(final Board board) {
        List<Move> ValidMoves = new ArrayList<>();
        for(final int currentCandidate :CANDIDATE_VALID_MOVES){
              
               if(isFirstColumn(this.positionOfPawn,currentCandidate) ||
                   isTenthColumn(this.positionOfPawn, currentCandidate)){
                    continue;
            }
               final int candidateDestinationCordinate = this.positionOfPawn +currentCandidate;
            if(BoardUtilities.isValidTileCordinate(candidateDestinationCordinate)){
                final Tile destinationTile = board.getTile(candidateDestinationCordinate);
                if(!destinationTile.isTileOccupied()){
                    ValidMoves.add(new MajorMove(board,this,candidateDestinationCordinate));
                }else{
                    final Pawn destinationPawn = destinationTile.getPawn();
                    final Alliance destinationAlliance = destinationPawn.getAlliance();
                    if(this.pawnAlliance != destinationAlliance){
                        ValidMoves.add(new AttackMove(board,this,candidateDestinationCordinate,destinationPawn));
                }
                   
            }
                
        }
        
    }
        return Collections.unmodifiableList(ValidMoves);
    
    }
     
    /* @Override
     public String toString(){
         return Pawntype.NORMALPAWN.toSting();
     }*/
    private static boolean isFirstColumn(final int candidateDestinationCordinate, final int  currentCandidate){
        return BoardUtilities.FIRST_COLUMN[candidateDestinationCordinate] &&  currentCandidate == -1;
        
    }
    private static boolean isTenthColumn(final int candidateDestinationCordinate, final int  currentCandidate){          
        return BoardUtilities.TENTH_COLUMN[candidateDestinationCordinate] && currentCandidate == 1;
           
    }
}
