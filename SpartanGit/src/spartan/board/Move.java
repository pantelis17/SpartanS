
package spartan.board;

import java.io.Serializable;
import spartan.pieces.Pawn;

/**
 *
 * @author user
 */
public abstract class Move implements Serializable{
    private final Board board;
    private final Pawn movedPawn;
    private final int destinationCordinate;
    private Move(final Board board,final Pawn movedPawn,final int destinationCordinate){
        this.board = board;
        this.destinationCordinate = destinationCordinate;
        this.movedPawn = movedPawn;
    }
    public int getCurrentCoordinate(){
        return this.movedPawn.getPositionOfPawn();
    }
    public int getDestinationCoordinate(){
        return this.destinationCordinate;
    }
    public Pawn getPawn(){
        return movedPawn;
    }
    public Board execute(){
        return board;
    }
    public static final class MajorMove extends Move implements Serializable{
        
        public MajorMove(final Board board,final Pawn movedPawn,final int destinationCordinate){
            super(board,movedPawn,destinationCordinate);
        }

    }
    
      public static final class AttackMove extends Move implements Serializable{
        private final Pawn attackedPawn;
        public AttackMove(final Board board,final Pawn movedPawn,final int destinationCordinate,final Pawn attackedPawn){
            super(board,movedPawn,destinationCordinate);
            this.attackedPawn = attackedPawn;
        }
    }
      
    private static class NullMove 
            extends Move implements Serializable {

        private NullMove() {
            super(null,null, -1);
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
      public static class MoveFactory {

        private static final Move NULL_MOVE = new NullMove();

        private MoveFactory() {
            throw new RuntimeException("Not instantiatable!");
        }

        public static Move getNullMove() {
            return NULL_MOVE;
        }

        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                
                if (move.getCurrentCoordinate() == currentCoordinate &&
                    move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
