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
public class Scout extends Pawn implements Serializable{
    
    
    private static final int[] CANDIDATE_VALID_MOVES = {-10, -1, 1, 10};
    public Scout(final int position , final Alliance alliance,final int pos){
        super(position,alliance,2,pos);
    }
    @Override
    public List<Move> calculateValidMoves(final Board board) {
        
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_VALID_MOVES) {
            int candidateDestinationCoordinate = this.positionOfPawn;
            while (BoardUtilities.isValidTileCordinate(candidateDestinationCoordinate)) {
                if(isFirstColumn(currentCandidateOffset, candidateDestinationCoordinate) ||
                   isTenthColumn(currentCandidateOffset, candidateDestinationCoordinate))
                    break;
                candidateDestinationCoordinate += currentCandidateOffset;
               if(BoardUtilities.isValidTileCordinate(candidateDestinationCoordinate)){
                final Tile destinationTile = board.getTile(candidateDestinationCoordinate);
                if(!destinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                }else{
                    final Pawn destinationPawn = destinationTile.getPawn();
                    final Alliance destinationAlliance = destinationPawn.getAlliance();
                    if(this.pawnAlliance != destinationAlliance){
                        legalMoves.add(new AttackMove(board,this,candidateDestinationCoordinate,destinationPawn));
                     }
                    break;
            }
                
            }
                }
            }
        return Collections.unmodifiableList(legalMoves);
    
    }
    
    /*@Override
     public String toString(){
         return Pawntype.SCOUT.toSting();
     }*/
   private static boolean isFirstColumn(final int candidateDestinationCordinate, final int  currentCandidate){
        return BoardUtilities.FIRST_COLUMN[ currentCandidate] &&  candidateDestinationCordinate == -1;   
    }
    private static boolean isTenthColumn(final int candidateDestinationCordinate, final int  currentCandidate){
             
        return BoardUtilities.TENTH_COLUMN[ currentCandidate] && candidateDestinationCordinate == 1;
             
    }
}
