package main.java.spartan.board;

import java.io.Serializable;

import main.java.spartan.pieces.Pawn;

/**
 *This class contains the functions we use to handle the tiles.
 * 
 * @author Pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos 2980
 */
public class Tile implements Serializable {

    private final int tileCordinates;
    private final Pawn PawnOnTile;

    public Tile(int tileCordinates, Pawn pawn) {
        this.tileCordinates = tileCordinates;
        this.PawnOnTile = pawn;
    }

    public int getTileCordinates() {
        return this.tileCordinates;
    }

    /**
     * Check if the tile has pawn on it.
     * @return true if it has pawn else return false.
     */
    public boolean isTileOccupied() {
        if (PawnOnTile == null) {//the pawn is null means that we dont have any pawn on this tile
            return false;
        } else {
            return true;
        }
    }

    /**
     * 
     * @return the pawn which is on this tile 
     */
    public Pawn getPawn() {
        return this.PawnOnTile;
    }

    /**
     * 
     * @return check is the tile is on  a column.
     */
    public boolean isLake() {
        if (tileCordinates == 42 || tileCordinates == 43 || tileCordinates == 52 || tileCordinates == 53 || tileCordinates == 46 || tileCordinates == 47 || tileCordinates == 56 || tileCordinates == 57) {
            return true;
        } else {
            return false;
        }
    }

}
