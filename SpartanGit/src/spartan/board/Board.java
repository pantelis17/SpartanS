package spartan.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import spartan.Alliance;
import spartan.pieces.Pawn;
import spartan.player.BluePlayer;
import spartan.player.Player;
import spartan.player.RedPlayer;

/**
 *
 * @author user
 */
public class Board implements Serializable {

    private ArrayList<Tile> gameBoard;
    private final BluePlayer bluePlayer;
    private final RedPlayer redPlayer;
    private Player currentPlayer;

    public Board() {
        this.gameBoard = createGameBuilder();
        this.bluePlayer = new BluePlayer(this);
        this.redPlayer = new RedPlayer(this);
        // this.currentPlayer = builder.nextMoveMaker.choosePlayerByAlliance(this.bluePlayer, this.redPlayer);
        this.currentPlayer = this.redPlayer;
        this.toStringBoard();
    }

    public void changeCurrentPlayer() {
        this.currentPlayer = this.currentPlayer.getOpponent();
    }

    public ArrayList<Tile> getGameBoard() {
        return this.gameBoard;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Tile getTile(int tileCordinate) {
        return this.gameBoard.get(tileCordinate);
    }

    public boolean boardFull(Alliance a) {
        if (!a.isBlue()) {
            int unableToMove = 0;
            for (int i = 60; i < 100; i++) {
                if (!this.gameBoard.get(i).isTileOccupied()) {
                    return false;
                }
                if (i < 70) {
                    if (this.gameBoard.get(i).getPawn().getValue() == -1 || this.gameBoard.get(i).getPawn().getValue() == 0 || i == 62 || i == 63 || i == 66 || i == 67) {
                        unableToMove++;
                    }
                }
                if (unableToMove == 10) {
                    return false;
                }
            }

            return true;
        } else {
            int unableToMove = 0;
            for (int i = 0; i < 40; i++) {
                if (!this.gameBoard.get(i).isTileOccupied()) {
                    return false;
                }
                if (i >= 30) {
                    if (this.gameBoard.get(i).getPawn().getValue() == -1 || this.gameBoard.get(i).getPawn().getValue() == 0 || i == 32 || i == 33 || i == 36 || i == 37) {
                        unableToMove++;
                    }
                }
                if (unableToMove == 10) {
                    return false;
                }
            }
            return true;
        }
    }

    private ArrayList<Tile> createGameBuilder() {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < BoardUtilities.NUM_TILES; i++) {
            tiles.add(new Tile(i, null));
        }

        return tiles;
    }

    public void setPawnOnBoard(int position, Pawn p) {
        this.gameBoard.remove(position);
        this.gameBoard.add(position, new Tile(position, p));
        /* this.blue = calculateActivePawns(this.gameBoard , Alliance.BLUE);
        this.red = calculateActivePawns(this.gameBoard , Alliance.RED);
        this.blueLegalMoves = calculateLegalMoves(this.blue);
        this.redLegalMoves = calculateLegalMoves(this.red);
        this.availbleBluePawns = calculatePawnMove(this.blue );
        this.availbleRedPawns = calculatePawnMove(this.red);*/
    }

    public void toStringBoard() {
        int count = 0;
        for (Tile tile : this.gameBoard) {
            if (tile.isTileOccupied()) {
                System.out.print(tile.getPawn().getValue());
            } else {
                System.out.print("-");
            }
            if (count % 10 == 9) {
                System.out.println("");
            }
            count++;
        }
    }

    /* 
     */ public List<Move> getAllLegalMoves() {
        List<Move> ainte = new ArrayList<>();
        for (Move move : this.bluePlayer.getLegalMoves()) {
            ainte.add(move);
        }
        for (Move move : this.redPlayer.getLegalMoves()) {
            ainte.add(move);
        }
        return Collections.unmodifiableList(ainte);
    }

    public BluePlayer getBluePlayer() {
        return this.bluePlayer;
    }

    public RedPlayer getRedPlayer() {
        return this.redPlayer;
    }

    public Pawn Conflict(Pawn attacker, Pawn defender) {
        if (attacker.getValue() == 1 && defender.getValue() == 10) {
            return attacker;
        }
        if (attacker.getValue() == 3 && defender.getValue() == 0) {
            return attacker;
        }
        if (defender.getValue() == 0) {
            return null;
        }
        if (attacker.getValue() == defender.getValue()) {
            return null;
        }
        if (attacker.getValue() > defender.getValue()) {
            return attacker;
        } else {
            return defender;
        }
    }

    /* public static class Builder{
        Map<Integer,Pawn> boardConfg;
        Alliance nextMoveMaker;
        
        public Builder() {
            this.boardConfg = new HashMap<>();
        }
        public Builder setPawn(final Pawn pawn){
            this.boardConfg.put(pawn.getPositionOfPawn(), pawn);
            return this;
        }
        public Builder setMoveMaker( final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        public Board build(){
            return new Board(this);
        
        }
    }*/
}
