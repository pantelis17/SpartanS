package com.spartan.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.spartan.Alliance;
import com.spartan.pieces.Pawn;
import com.spartan.player.BluePlayer;
import com.spartan.player.Player;
import com.spartan.player.RedPlayer;

/**
 *This class contains the functions for the changes in the board.
 */
public class Board implements Serializable {

    private ArrayList<Tile> gameBoard;// This arrayList contains every tile in the board. A tile is the square in the board. This board contains 100 tiles.
    private final BluePlayer bluePlayer;// This is the blue player
    private final RedPlayer redPlayer;//this is the red Player
    private Player currentPlayer; // This is the player who's turn is on.

    public Board() {
        this.gameBoard = createGameBuilder();//initialize the board's tiles
        this.bluePlayer = new BluePlayer(this);//create a new blueplayer
        this.redPlayer = new RedPlayer(this);//create a new redPLayer
        this.currentPlayer = this.redPlayer;// the currentPlayer is the red. This comes from the rules of the game
        this.toStringBoard();// print the current board
    }

    /**
     * We call this function every time the players are changing turns.
     */
    public void changeCurrentPlayer() {
        this.currentPlayer = this.currentPlayer.getOpponent();// if the currentPlayer is the red then the currentPlayer will be now the blue.
    }

    
    public ArrayList<Tile> getGameBoard() {
        return this.gameBoard;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * 
     * @param tileCordinate is the number of the tile in the board
     * @return the specific tile with every information for it ( pawn on it, cordinates etc)
     */
    public Tile getTile(int tileCordinate) {
        return this.gameBoard.get(tileCordinate);
    }

    /**
     * Check if the player has all of his pawn in board and if he is able to make at least one move with this set.
     * 
     * @param a is the alliance of the player
     * @return true if the player has every pawn on board else return false
     */
    public boolean boardFull(Alliance a) {
        if (!a.isBlue()) {// if the player has red color
            int unableToMove = 0;// this value check how many pawns of the front line cant move
            for (int i = 60; i < 100; i++) {//for each tile from 60-100 (because is the red player) check if the tile has pawn on it
                if (!this.gameBoard.get(i).isTileOccupied()) {// if at least one tile isnt occupied then we stop the loop and return false.
                    return false;
                }
                if (i < 70) {//each tile in the front line
                    if (this.gameBoard.get(i).getPawn().getValue() == -1 || this.gameBoard.get(i).getPawn().getValue() == 0 || i == 62 || i == 63 || i == 66 || i == 67) {
                        unableToMove++;//if it is bomb , flag or it is behind the columns 
                    }
                }
                if (unableToMove == 10) {
                    return false; // if we have 10 unable to move pawns the the player cant move anything so we break the loop and return false
                }
            }

            return true;
        } else {
            int unableToMove = 0; // this value check how many pawns of the front line cant move
            for (int i = 0; i < 40; i++) {//for each tile from 0-40 (because is the blue player) check if the tile has pawn on it
                if (!this.gameBoard.get(i).isTileOccupied()) {
                    return false;// if at least one tile isnt occupied then we stop the loop and return false.
                }
                if (i >= 30) {//each tile in the front line
                    if (this.gameBoard.get(i).getPawn().getValue() == -1 || this.gameBoard.get(i).getPawn().getValue() == 0 || i == 32 || i == 33 || i == 36 || i == 37) {
                        unableToMove++;//if it is bomb , flag or it is behind the columns
                    }
                }
                if (unableToMove == 10) {
                    return false;// if we have 10 unable to move pawns the the player cant move anything so we break the loop and return false
                }
            }
            return true;
        }
    }

    private ArrayList<Tile> createGameBuilder() {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < BoardUtilities.NUM_TILES; i++) {//depense on the how many time our board has 
            tiles.add(new Tile(i, null));// create a new tile object for each square
        }

        return tiles;
    }

    /**
     * 
     * @param position the cordinate where the pawn will be
     * @param p is the pawn we move
     */
    public void setPawnOnBoard(int position, Pawn p) {
        this.gameBoard.remove(position);// remove this tile from our arraylist
        this.gameBoard.add(position, new Tile(position, p));// create a new tile in its position
    }

    /**
     * Is used to print our board in the cmd.
     */
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

    /**
     * 
     * @return every move which exist for both players 
     */
    public List<Move> getAllLegalMoves() {
        List<Move> m = new ArrayList<>();//create a new arraylist for the moves
        for (Move move : this.bluePlayer.getLegalMoves()) {
            m.add(move);//add every move from the blue player
        }
        for (Move move : this.redPlayer.getLegalMoves()) {
            m.add(move);// add every move from the red player
        }
        return Collections.unmodifiableList(m);
    }

    public BluePlayer getBluePlayer() {
        return this.bluePlayer;
    }

    public RedPlayer getRedPlayer() {
        return this.redPlayer;
    }

    /**
     * This function is used to define who will be the winner after a conflict between the pawns.
     * The winner is determined from the rules of the game.
     * @param attacker is the pawn which make the attack
     * @param defender is the pawn which getting the attack.
     * @return the pawn which win or null if we had a draw.
     */
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
}
