/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.spartan.board;

/**
 * This class contain the specifications of the board.
 *
 * @author Pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos
 * 2980
 */
public class BoardUtilities {

    public static boolean[] FIRST_COLUMN = initialize(0);
    public static boolean[] TENTH_COLUMN = initialize(9);
    public static final int NUM_TILES = 100;
    public static final int NUM_TILES_PER_ROW = 10;

    /**
     *
     * @param colNumber the column we want
     * @return an array with boolean values which show us which is the row we
     * want.
     */
    private static boolean[] initialize(int colNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[colNumber] = true;
            colNumber += NUM_TILES_PER_ROW;
        } while (colNumber < NUM_TILES);
        return column;
    }

    private BoardUtilities() {

    }

    /**
     * Check if a tile is valid to move there.
     * 
     * @param coordinate is the number of the tile we want to check
     * @return 
     */
    public static boolean isValidTileCordinate(final int coordinate) {
        if (tileIsOnColumn(coordinate)) {
            return false; // if the tile is on a column then you cant move there
        } else {
            return coordinate >= 0 && coordinate < NUM_TILES;   //check if the cordinate is valid
        }
    }

    /**
     * Determine if the tile we want is on a column which is invalide cordinate.
     * 
     * @param tileCordinates is the cordinate of the tile we want.
     * @return 
     */
    private static boolean tileIsOnColumn(final int tileCordinates) {
        if (tileCordinates == 42 || tileCordinates == 43 || tileCordinates == 46 || tileCordinates == 47
                || tileCordinates == 52 || tileCordinates == 53 || tileCordinates == 56 || tileCordinates == 57) {
            return true;
        } else {
            return false;
        }
    }

}
