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

/**
 * This Class is used to initialize every pawn which can be moved only 1 square per turn.
 */
public class NormalPawn extends Pawn implements Serializable {

    private static final int[] CANDIDATE_VALID_MOVES = { -10, -1, 1, 10 };//this is the changes of the coordinate of the pawn

    /**
     * 
     * @param position is the coordinate of the pawn in the board
     * @param alliance is the color of the pawn
     * @param value is the value of the pawn
     * @param pos is the coordinate of the pawn in the hand of the player
     */
    public NormalPawn(final int position, final Alliance alliance, final int value, final int pos) {
        super(position, alliance, value, pos);
    }

    /**
     * 
     * @param board is the current board of the game
     * @return the legal moves of this pawn.
     */
    @Override
    public List<Move> calculateValidMoves(final Board board) {
        List<Move> validMoves = new ArrayList<>();
        for (final int currentCandidate : CANDIDATE_VALID_MOVES) { // for each change of the coordinates
            if (isFirstColumn(this.positionOfPawn, currentCandidate) ||
                    isTenthColumn(this.positionOfPawn, currentCandidate)) { // if we are in the end or in the start of a row and we are going to change row with 
                continue;// a left or a right move then we cancel this move.
            }
            final int candidateDestinationCoordinate = this.positionOfPawn + currentCandidate;
            if (BoardUtilities.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile destinationTile = board.getTile(candidateDestinationCoordinate);
                if (!destinationTile.isTileOccupied()) {// if the tile we want to move on isnt occupied then we add the move to the arraylist
                    validMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Pawn destinationPawn = destinationTile.getPawn();
                    final Alliance destinationAlliance = destinationPawn.getPawnAlliance();
                    if (this.pawnAlliance != destinationAlliance) { // if the tile is occupied and the alliance of the pawn which is there is different from the alliance of this pawn
                        validMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, destinationPawn));//then we add the move to the arraylist
                    }

                }

            }

        }
        return Collections.unmodifiableList(validMoves);

    }

    private static boolean isFirstColumn(final int candidateDestinationCordinate, final int currentCandidate) {
        return BoardUtilities.FIRST_COLUMN[candidateDestinationCordinate] && currentCandidate == -1;
        //check if a left move will lead to a different row.
    }

    private static boolean isTenthColumn(final int candidateDestinationCordinate, final int currentCandidate) {
        return BoardUtilities.TENTH_COLUMN[candidateDestinationCordinate] && currentCandidate == 1;
        //check if a right move will lead to a different row.
    }
}
