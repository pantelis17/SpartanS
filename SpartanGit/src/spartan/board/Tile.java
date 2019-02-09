
package spartan.board;

import java.io.Serializable;


import spartan.pieces.Pawn;

/**
 *
 * @author user
 */
public class Tile implements Serializable {
    

    private final int tileCordinates;
    //private static final Map<Integer,EmptyTile> EMPTY_TILES  = createAllPossibleEmptyTiles();
    private final Pawn PawnOnTile;
    public Tile(int tileCordinates,Pawn pawn){
        this.tileCordinates = tileCordinates;
        this.PawnOnTile = pawn;
    }
   public int getTileCordinates(){
      return this.tileCordinates;
   }
/*    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer,EmptyTile> emptyTilesMap = new HashMap<>();
        for ( int i = 0; i < BoardUtilities.NUM_TILES; i++){
         emptyTilesMap.put(i, new EmptyTile(i));
        }
        return Collections.unmodifiableMap(EMPTY_TILES);
    }*/
    
        public boolean isTileOccupied() {
            if(PawnOnTile == null)
                return false;
            else
                return true;
        }

        public Pawn getPawn() {
            return this.PawnOnTile;
        }
        
        public boolean isLake(){
            if(tileCordinates == 42 || tileCordinates == 43 || tileCordinates == 52 || tileCordinates == 53 || tileCordinates ==  46 || tileCordinates == 47 || tileCordinates == 56 || tileCordinates == 57)
                return true;
            else
                return false;
        }
        
    }

