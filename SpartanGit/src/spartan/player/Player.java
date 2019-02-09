package spartan.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import spartan.Alliance;
import spartan.board.Board;
import spartan.board.Move;
import spartan.board.Tile;
import spartan.pieces.Pawn;
import spartan.player.MoveTransition.MoveStatus;

/**
 *
 * @author user
 */
public abstract class Player implements Serializable {

    protected Board board;
    protected ArrayList<Move> legalMoves;
    protected ArrayList<Pawn> legalPawns;
    //protected final Flag playersFlag;

    Player(Board board) {
        this.board = board;
        this.legalMoves = new ArrayList<>();
        //      this.playersFlag = establish();
        this.legalPawns = new ArrayList<>();
    }

    public void SetBoard(Board board) {
        this.board = board;
    }

    /* private Flag establish() {
        for(final Pawn pawn : getActivePawns()){
            if(pawn.getValue() == -1){
                return (Flag) pawn;
            }
        }
        return null;
    }
     */
    public abstract ArrayList<Pawn> getActivePawns();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    public abstract ArrayList<Integer> calculateStartMove();

    public abstract ArrayList<Integer> getStart();

    public boolean isLegalMove(final Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isLegalPawn(final Pawn pawn) {
        return this.legalPawns.contains(pawn);
    }

    /*
    public boolean isDraw(){
        return this.legalPawns.isEmpty() && this.oppenentPawns.isEmpty();
    }*/
    public MoveTransition makeMove(final Move move) {

        if (!this.legalMoves.contains(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionedBoard = move.execute();
        return new MoveTransition(this.board, transitionedBoard, move, MoveStatus.DONE);
    }

    public ArrayList<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public ArrayList<Pawn> getAvailablePawns() {
        return this.legalPawns;
    }

    public void setLegalMoves(ArrayList<Move> moves) {
        this.legalMoves = moves;
    }

    public void setLegalPawns(ArrayList<Pawn> pawn) {
        this.legalPawns = pawn;
    }

    public abstract ArrayList<Pawn> Random();

    public abstract void setStack(ArrayList<Pawn> p);

    public abstract void setCordinateofPawn(int pos, int cordinate, int panel, boolean Start);

    public Move BotMove(ArrayList<Tile> tiles) {
        for (Move m : this.legalMoves) {
            if (tiles.get(m.getDestinationCoordinate()).isTileOccupied()) {
                if (m.getPawn().getValue() == 1 && tiles.get(m.getDestinationCoordinate()).getPawn().getValue() == 10) {
                    if (tiles.get(m.getDestinationCoordinate()).getPawn().getSide()) {
                        return m;
                    } else {
                        break;
                    }
                }
            }
        }
        Random move = new Random();
        //  boolean flag = true;
        int a, chance = 0;
        while (true) {
            a = move.nextInt(this.legalMoves.size());
            if (tiles.get(legalMoves.get(a).getDestinationCoordinate()).isTileOccupied()) {
                if (tiles.get(legalMoves.get(a).getDestinationCoordinate()).getPawn().getValue() > legalMoves.get(a).getPawn().getValue()) {
                    chance = move.nextInt(10);

                    if (chance == 2 || chance == 4 || chance == 8) {
                        break;// flag = false;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return legalMoves.get(a);
    }

    public abstract void DeletePawn(Pawn p);

    public void calculateLegalMoves(Board board) {
        legalPawns.clear();
        legalMoves.clear();
        board.toStringBoard();
        final ArrayList<Move> legals = new ArrayList<>();
        for (Pawn pawn : getActivePawns()) {

            List<Move> m = pawn.calculateValidMoves(board);
            if (!m.isEmpty()) {
                legalPawns.add(pawn);
                legals.addAll(m);

            }

        }
        this.legalMoves = legals;
    }
}
