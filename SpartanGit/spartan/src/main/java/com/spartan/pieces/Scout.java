/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartan.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.spartan.board.Board;
import com.spartan.board.BoardUtilities;
import com.spartan.board.Move;
import com.spartan.board.Tile;
import com.spartan.board.Move.*;
import com.spartan.enumerations.Alliance;

public class Scout extends Pawn implements Serializable {

    private static final int[] CANDIDATE_VALID_MOVES = {-10, -1, 1, 10};//this is the changes of the cordinate of the pawn

    /**
     *
     * @param position is the position of the pawn in the board
     * @param alliance is the color of the pawn
     * @param pos is the position of the pawn in the hand of the player
     */
    public Scout(final int position, final Alliance alliance, final int pos) {
        super(position, alliance, 2, pos);
    }

    /**
     *
     * @param board is the current board of the game
     * @return the legal moves of this pawn.
     */
    @Override
    public List<Move> calculateValidMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_VALID_MOVES) {// for each change in the cordinates
            int candidateDestinationCoordinate = this.positionOfPawn;
            while (BoardUtilities.isValidTileCordinate(candidateDestinationCoordinate)) {// while scout can move continue to check  the squares
                if (isFirstColumn(currentCandidateOffset, candidateDestinationCoordinate)
                        || isTenthColumn(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;// if he is going to change row after this move we stop it
                }
                candidateDestinationCoordinate += currentCandidateOffset;//add again the same change to check if we can do this move
                if (BoardUtilities.isValidTileCordinate(candidateDestinationCoordinate)) {// if we can make this move then
                    final Tile destinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!destinationTile.isTileOccupied()) {// if the tile isnt occupied
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));//we add the move and continue
                    } else {//if it is occupied
                        final Pawn destinationPawn = destinationTile.getPawn();
                        final Alliance destinationAlliance = destinationPawn.getAlliance();
                        if (this.pawnAlliance != destinationAlliance) {// if the pawn there is different alliance with this pawn
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, destinationPawn));//add this to the legal moves
                        }
                        break;// break the loop bacause the pawn cant jump over other pawns
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    private static boolean isFirstColumn(final int candidateDestinationCordinate, final int currentCandidate) {
        return BoardUtilities.FIRST_COLUMN[currentCandidate] && candidateDestinationCordinate == -1;
    }

    private static boolean isTenthColumn(final int candidateDestinationCordinate, final int currentCandidate) {

        return BoardUtilities.TENTH_COLUMN[currentCandidate] && candidateDestinationCordinate == 1;

    }
}
