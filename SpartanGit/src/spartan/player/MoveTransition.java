/*
 * This class is about of the move . It contains some usefull componets of Board in order to adjust the move in Board.
 * 
 */
package spartan.player;

import spartan.board.Board;
import spartan.board.Move;

/**
 *
 * The fields are the Board (from and to) . Each move changes the structure of
 * Board so we need from and to. Move Status indicates what kind of Move was.
 * For example ,if it is Done and if is Illegal .
 */
public class MoveTransition {

    private final Board fromBoard;
    private final Board toBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;

    /**
     * Constructor and Initialization.
     *
     * @param fromBoard
     * @param toBoard
     * @param transitionMove
     * @param moveStatus
     */
    public MoveTransition(final Board fromBoard,
            final Board toBoard,
            final Move transitionMove,
            final MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    /**
     * Getters of fields.      *
     */
    public Board getFromBoard() {
        return this.fromBoard;
    }

    public Board getToBoard() {
        return this.toBoard;
    }

    public Move getTransitionMove() {
        return this.transitionMove;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    /**
     * Enumeration of the moveStatus,
     * implements the abstact method of isDone.
     */

    public enum MoveStatus {

        DONE {//legal move
            @Override
            public boolean isDone() {
                return true;
            }
        },
        ILLEGAL_MOVE {//illegal move
            @Override
            public boolean isDone() {
                return false;
            }
        };

        public abstract boolean isDone();

    }
}
