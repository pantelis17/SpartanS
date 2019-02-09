/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartan.board;


public class BoardUtilities {

    public static boolean[] FIRST_COLUMN = initialize(0);
    public static boolean[] TENTH_COLUMN = initialize(9);
    public static final int NUM_TILES = 100;
     public static final int NUM_TILES_PER_ROW = 10;
    
    private static boolean[] initialize(int colNumber){ 
     final boolean[] column = new boolean[NUM_TILES];
     do{
         column[colNumber] = true;
         colNumber += NUM_TILES_PER_ROW;
     }while(colNumber < NUM_TILES); 
     return column;
    }
    
    
    private BoardUtilities(){
        
    }
    public static boolean isValidTileCordinate(final int coordinate) {
         if(tileIsOnLake(coordinate))
            return false;
         else
        return coordinate >= 0 && coordinate < NUM_TILES;   //lakes
    }
     private static boolean tileIsOnLake(final int tileCordinates){
        if(tileCordinates==42 || tileCordinates==43 || tileCordinates==46 || tileCordinates==47 ||
               tileCordinates==52 || tileCordinates==53 || tileCordinates==56 || tileCordinates==57)
                return true;
         else
                return false;
    }
    
}
