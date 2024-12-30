/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartan.pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.spartan.board.Board;
import com.spartan.board.BoardUtilities;
import com.spartan.board.Move;
import com.spartan.board.Tile;
import com.spartan.board.Move.*;
import com.spartan.enumerations.Alliance;

public class Scout extends Pawn {

    private static final int[] CANDIDATE_VALID_MOVES = {-10, -1, 1, 10};//this is the changes of the coordinate of the pawn

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
    
        for (final var offset : CANDIDATE_VALID_MOVES) {
            var currentCoordinate = this.positionOfPawn;
    
            while (BoardUtilities.isValidTileCoordinate(currentCoordinate)) {
                if (isEdgeCase(currentCoordinate, offset)) {
                    break;
                }
                currentCoordinate += offset;
                if (BoardUtilities.isValidTileCoordinate(currentCoordinate)) {
                    final var destinationTile = board.getTile(currentCoordinate);
    
                    if (!destinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, currentCoordinate));
                    } else {
                        final var occupyingPawn = destinationTile.getPawn();
                        if (this.pawnAlliance != occupyingPawn.getPawnAlliance()) {
                            legalMoves.add(new AttackMove(board, this, currentCoordinate, occupyingPawn));
                        }
                        break;
                    }
                }
            }
        }
    
        return Collections.unmodifiableList(legalMoves);
    }
    
    private boolean isEdgeCase(final int currentCoordinate, final int offset) {
        return isFirstColumn(offset, currentCoordinate) || isTenthColumn(offset, currentCoordinate);
    }

    private boolean isFirstColumn(final int candidateDestinationCoordinate, final int currentCandidate) {
        return BoardUtilities.FIRST_COLUMN[currentCandidate] && candidateDestinationCoordinate == -1;
    }

    private boolean isTenthColumn(final int candidateDestinationCoordinate, final int currentCandidate) {
        return BoardUtilities.TENTH_COLUMN[currentCandidate] && candidateDestinationCoordinate == 1;
    }
}
