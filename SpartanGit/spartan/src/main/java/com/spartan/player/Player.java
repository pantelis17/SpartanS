/**
 * This class is about the Player.
 * Implementation of important methods such as how many active pawns has the player , available Moves , when make move
 * Is an abstact class ,because we have two red and blue player . Each player has different active pawns and legal moves
 *  There are setters that set the board .Also there is a method called BotMove . This method is used only in SinglePlayer .
 * The bot acts as a opponent .
 *
 */
package com.spartan.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.spartan.Alliance;
import com.spartan.board.Board;
import com.spartan.board.Move;
import com.spartan.board.Tile;
import com.spartan.pieces.Pawn;
import com.spartan.player.MoveTransition.MoveStatus;

/**
 *
 * This class is used in SinglePlayer and in Server and Client . That's why we
 * implements Serializable in order to support socket network programming The
 * abstract methods are implemented in the subclasses RedPlayer and BluePlayer.
 */
public abstract class Player implements Serializable {

    //fields.
    protected Board board;
    protected ArrayList<Move> legalMoves;
    protected ArrayList<Pawn> legalPawns;

    /**
     * Contructor
     *
     * @param board , the Board of the game.
     */
    Player(Board board) {
        this.board = board;
        this.legalMoves = new ArrayList<>();

        this.legalPawns = new ArrayList<>();
    }

    public void SetBoard(Board board) {
        this.board = board;
    }

    //Abstract methods . Implemented in Subclasses RedPlayer and Blue . See more info there...
    //--------------------------------------------------------------------------------------------//  
    public abstract ArrayList<Pawn> getActivePawns();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    public abstract ArrayList<Integer> calculateStartMove();

    public abstract ArrayList<Integer> getStart();

    public abstract void DeletePawn(Pawn p);

    public abstract ArrayList<Pawn> Random();

    public abstract void setStack(ArrayList<Pawn> p);
    
    public abstract void setCordinateofPawn(int pos, int cordinate, int panel, boolean Start);
    //--------------------------------------------------------------------------------------------//  
    public boolean isLegalMove(final Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isLegalPawn(final Pawn pawn) {
        return this.legalPawns.contains(pawn);
    }

    /**
     *
     * @param move , we check the move if is illegal or done .
     * @return MoveTransition , a move with the new Board and the new status of
     * the move.
     */
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
    /**
     * Bot action. The bot makes a random move . When the opponent's pawn is
     * close and the pawn has value 10 then attacks the bot with 1. 1 is winnig
     * 10 . Also, there is a chance to attack the opponent. If the value of pawn
     * is greater than the opponent's there is a chance to attack . So we there
     * is a loop for that and if the chance(integer) is 2 or 4 or 8 breaks the
     * loop.
     *
     * @param tiles
     * @return a move
     */
    public Move BotMove(ArrayList<Tile> tiles) {
        //check if 1 us near the 10 of opponent.
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
        //a simple way to make an attack .
        //chance can break the loop and return the value of a (the random move) . The move should be legal.
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

    /**
     * Caclulate all legal Moves of the player ,updating the legalPawns and the
     * legalMoves of the Player. We use an arraylist so each time we calculate
     * the pawns and the moves we need to clear the arrylist and start again .
     * Otherwise the old legal Moves and pawns will remain .
     *
     * @param board
     */
    public void calculateLegalMoves(Board board) {
        legalPawns.clear();
        legalMoves.clear();
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
