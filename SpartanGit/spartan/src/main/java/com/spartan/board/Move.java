package com.spartan.board;

import java.io.Serializable;

import com.spartan.pieces.Pawn;

/**
 * This class with it's subclasses contains the funtions we need to make a move.
 */
public abstract class Move implements Serializable {

    private final Board board;
    private final Pawn movedPawn;
    private final int destinationCordinate;

    private Move(final Board board, final Pawn movedPawn, final int destinationCordinate) {
        this.board = board;
        this.destinationCordinate = destinationCordinate;
        this.movedPawn = movedPawn;
    }

    public int getCurrentCoordinate() {
        return this.movedPawn.getPositionOfPawn();
    }

    public int getDestinationCoordinate() {
        return this.destinationCordinate;
    }

    public Pawn getPawn() {
        return movedPawn;
    }

    /**
     *
     * @return the current board after the move
     */
    public Board execute() {
        return board;
    }

    /**
     * This class is used when we make a move to an empty tile.
     */
    public static final class MajorMove extends Move implements Serializable {

        public MajorMove(final Board board, final Pawn movedPawn, final int destinationCordinate) {
            super(board, movedPawn, destinationCordinate);
        }

    }

    /**
     * This class is used when we make a move to another pawn of the opponent.
     */
    public static final class AttackMove extends Move implements Serializable {

        private final Pawn attackedPawn;

        public AttackMove(final Board board, final Pawn movedPawn, final int destinationCordinate, final Pawn attackedPawn) {
            super(board, movedPawn, destinationCordinate);
            this.attackedPawn = attackedPawn;
        }
    }
/**
 * This class is used when we cant make the move we choose.
 */
    private static class NullMove
            extends Move implements Serializable {

        private NullMove() {
            super(null, null, -1);
        }

        @Override
        public int getCurrentCoordinate() {
            return 43;
        }

        @Override
        public int getDestinationCoordinate() {
            return 43;
        }

        @Override
        public String toString() {
            return "Null Move";
        }
    }

    /**
     * This class create the move and check if it is valid.
     */
    public static class MoveFactory {

        private static final Move NULL_MOVE = new NullMove();

        private MoveFactory() {
            throw new RuntimeException("Not instantiatable!");
        }

        public static Move getNullMove() {
            return NULL_MOVE;
        }
        
        /**
         * 
         * @param board the current board before the move
         * @param currentCoordinate is the current codrinate of the pawn
         * @param destinationCoordinate is the cordinate we want to move
         * @return the move we choose if it is valid else return a null move
         */
        public static Move createMove(final Board board,
                final int currentCoordinate,
                final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {

                if (move.getCurrentCoordinate() == currentCoordinate
                        && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;// if the move exists in the legal moves of the player then we return the move
                }
            }
            return NULL_MOVE;// if it doesnt exist we return a null move.
        }
    }
}
