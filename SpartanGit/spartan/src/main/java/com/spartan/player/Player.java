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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.spartan.board.Board;
import com.spartan.board.Move;
import com.spartan.board.Tile;
import com.spartan.enumerations.Alliance;
import com.spartan.enumerations.MoveStatus;
import com.spartan.pieces.Pawn;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * This class is used in SinglePlayer and in Server and Client . That's why we
 * implements Serializable in order to support socket network programming The
 * abstract methods are implemented in the subclasses RedPlayer and BluePlayer.
 */
public abstract class Player implements Serializable {

    //fields.
    @Setter
    protected Board board;
    protected Random randomGenerator;
    private int startDeck;
    private int endDeck;
    @Getter private List<Integer> startMove;
    @Getter @Setter private List<Pawn> activePawns;
    @Getter @Setter private List<Move> legalMoves;
    @Getter @Setter private List<Pawn> legalPawns;

    protected Player(Board board, List<Pawn> activePawns, int startDeck, int endDeck) {
        this.board = board;
        this.activePawns = activePawns;
        this.randomGenerator = new Random();
        this.legalMoves = new ArrayList<>();
        this.legalPawns = new ArrayList<>();
        this.startDeck = startDeck;
        this.endDeck = endDeck;
        this.startMove = IntStream.range(startDeck, endDeck)
                .boxed()
                .toList();
    }

    //Abstract methods . Implemented in Subclasses RedPlayer and Blue . See more info there...
    //--------------------------------------------------------------------------------------------// 
    public abstract Alliance getAlliance();

    public abstract Player getOpponent();
    //--------------------------------------------------------------------------------------------//  

    /**
     * This function is used to create the set of the board for the bluePlayer
     * 
     * @return a new arraylist with the new coordinates of the pawns
     */
    public List<Pawn> initRandomPlacement() {
        final var random = new ArrayList<Pawn>();
        final var coordinate = new ArrayList<Integer>(); // Arraylist of the coordinates 
        final var isBlueAlliance = getAlliance().isBlue();
        int[] proposalsForBomb = { -1, 1, -10, 10, 9, 11, -9, -11 }; // Candidate places for the bomb
        int flag = -1;
        int bombs = 3;
        int spies = 4;
    
        // Fill the coordinates array
        for (var i = startDeck; i < endDeck; i++) {
            coordinate.add(i);
        }
    
        for (final var pawn : activePawns) {
            if (pawn.getValue() == -1) { 
                flag = placeFlag(isBlueAlliance, coordinate);
                pawn.setPositionOfPawn(flag);
            } else if (pawn.getValue() == 0) { 
                placeBomb(pawn, coordinate, proposalsForBomb, flag, bombs, isBlueAlliance);
                bombs--;
            } else if (pawn.getValue() == 2) { 
                placeSpy(pawn, coordinate, spies, isBlueAlliance);
                spies--;
            } else { 
                placeOtherPawn(pawn, coordinate, isBlueAlliance);
            }
            random.add(pawn); // Add the pawn by random order
        }
    
        // Set pawns on the board
        for (final var pawn : random) {
            this.board.setPawnOnBoard(pawn.getPositionOfPawn(), pawn);
        }
    
        this.board.toStringBoard(); // Get the coordinates 
        this.setActivePawns(random);
        return random; // Return the random values
    }
    
    // Helper method to place the flag
    private int placeFlag(boolean isBlueAlliance, List<Integer> coordinate) {
        int cor = (isBlueAlliance ? 0 : 80) + randomGenerator.nextInt(isBlueAlliance ? 19 : 20);
        coordinate.remove(Integer.valueOf(cor)); // Remove selected coordinate
        return cor;
    }
    
    // Helper method to place bombs
    private void placeBomb(Pawn pawn, List<Integer> coordinate, int[] proposalsForBomb, int flag, int bombs, boolean isBlueAlliance) {
        int cor;
        if (bombs > 0) {
            do {
                cor = proposalsForBomb[randomGenerator.nextInt(proposalsForBomb.length)] + flag;
            } while (!coordinate.contains(cor)); // Find valid position
            pawn.setPositionOfPawn(cor);
        } else {
            do {
                cor = (isBlueAlliance ? 0 : 60) + randomGenerator.nextInt(40);
            } while (!coordinate.contains(cor)); // Find valid position
            pawn.setPositionOfPawn(cor);
        }
        coordinate.remove(Integer.valueOf(cor)); // Remove selected coordinate
    }
    
    // Helper method to place spies
    private void placeSpy(Pawn pawn, List<Integer> coordinate, int spies, boolean isBlueAlliance) {
        int cor;
        if (spies > 0) {
            do {
                cor = (isBlueAlliance ? 0 : 60) + randomGenerator.nextInt(40);
            } while (!coordinate.contains(cor) || (isBlueAlliance ? cor < 20 : cor >= 80)); // Ensure valid spy position
            pawn.setPositionOfPawn(cor);
        } else {
            do {
                cor = (isBlueAlliance ? 0 : 60) + randomGenerator.nextInt(40);
            } while (!coordinate.contains(cor)); // Find valid position
            pawn.setPositionOfPawn(cor);
        }
        coordinate.remove(Integer.valueOf(cor)); // Remove selected coordinate
    }
    
    // Helper method to place other pawns
    private void placeOtherPawn(Pawn pawn, List<Integer> coordinate, boolean isBlueAlliance) {
        int cor;
        do {
            cor = (isBlueAlliance ? 0 : 60) + randomGenerator.nextInt(40);
        } while (!coordinate.contains(cor)); // Find valid position
        pawn.setPositionOfPawn(cor);
        coordinate.remove(Integer.valueOf(cor)); // Remove selected coordinate
    }    

    //Method that sets the position and the coordinates of the pawns on the panel of the game
    public void setCoordinateOfPawn(int pos, int coordinate, int panel, boolean start) {
        int position;
        if (panel == 0) {
            // In case of panel 0, find the pawn and update its coordinate
            position = findPawnByPosition(pos);
        } else {
            // In case of other panels, handle based on 'start' flag
            if (start) {
                activePawns.get(pos).setPositionOfPawn(coordinate);
                return;
            } else {
                position = findPawnByCalculatedPosition(pos);
            }
        }
        if (position >= 0) {
            activePawns.get(position).setPositionOfPawn(coordinate);
        }
    }
    
    // Helper method to find a pawn by its position
    private int findPawnByPosition(int pos) {
        for (int i = 0; i < activePawns.size(); i++) {
            if (activePawns.get(i).getPositionOfPawn() == pos) {
                return i;  // Return the index of the pawn
            }
        }
        return -1;  // Return -1 if not found
    }
    
    // Helper method to find a pawn by a calculated position
    private int findPawnByCalculatedPosition(int pos) {
        for (int i = 0; i < activePawns.size(); i++) {
            if ((activePawns.get(i).getPositionOfPawn() + 1) * (-1) == pos) {
                return i;  // Return the index of the pawn
            }
        }
        return -1;  // Return -1 if not found
    }

    private boolean isLegalMove(final Move move) {
        return legalMoves.contains(move);
    }

    /**
     *
     * @param move , we check the move if is illegal or done .
     * @return MoveTransition , a move with the new Board and the new status of
     * the move.
     */
    public MoveTransition makeMove(final Move move) {
        if (!isLegalMove(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionedBoard = move.execute();
        return new MoveTransition(this.board, transitionedBoard, move, MoveStatus.DONE);
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
    public Move botMove(List<Tile> tiles) {
        // Check if pawn (value 1) is near an opponent's pawn (value 10) and perform move
        Move move = checkOpponentProximity(tiles);
        if (move != null) {
            return move;
        }
    
        // If no opponent proximity found, attempt to make a random attack
        return makeRandomAttackMove(tiles);
    }
    
    // Helper method to check if a pawn (value 1) is near an opponent's pawn (value 10)
    private Move checkOpponentProximity(List<Tile> tiles) {
        for (final var m : legalMoves) {
            if (tiles.get(m.getDestinationCoordinate()).isTileOccupied()) {
                Pawn destinationPawn = tiles.get(m.getDestinationCoordinate()).getPawn();
                if (m.getPawn().getValue() == 1 && destinationPawn.getValue() == 10) {
                    if (destinationPawn.isFlipped()) {
                        return m; // Return move if opponent's pawn (value 10) is on the same side
                    } else {
                        break; // Stop checking if pawn is on the opposite side
                    }
                }
            }
        }
        return null; // No valid move found in this case
    }
    
    // Helper method to make a random attack move based on certain chances
    private Move makeRandomAttackMove(List<Tile> tiles) {
        int a, chance;
        while (true) {
            a = randomGenerator.nextInt(this.legalMoves.size());
            Tile destinationTile = tiles.get(legalMoves.get(a).getDestinationCoordinate());
            
            // Check if destination tile is occupied
            if (destinationTile.isTileOccupied()) {
                Pawn destinationPawn = destinationTile.getPawn();
                Pawn movingPawn = legalMoves.get(a).getPawn();
                
                // Attack condition: moving pawn value is less than the opponent's pawn
                if (destinationPawn.getValue() > movingPawn.getValue()) {
                    chance = randomGenerator.nextInt(10);
    
                    // Chance-based logic to decide if the move should be performed
                    if (shouldBreakChance(chance)) {
                        break;
                    }
                } else {
                    break; // No attack if the pawn is stronger or equal
                }
            } else {
                break; // If the tile is not occupied, move can be performed
            }
        }
        return legalMoves.get(a); // Return the selected move
    }
    
    // Helper method to determine if the move should be made based on the random chance
    private boolean shouldBreakChance(int chance) {
        return chance == 2 || chance == 4 || chance == 8;
    }
    
    /**
     * Caclulate all legal Moves of the player ,updating the legalPawns and the
     * legalMoves of the Player. We use a list so each time we calculate
     * the pawns and the moves we need to clear the it and start again .
     * Otherwise the old legal Moves and pawns will remain .
     *
     * @param board
     */
    public void calculateLegalMoves(Board board) {
        legalPawns.clear();
        legalMoves.clear();
        final var legals = new ArrayList<Move>();
        for (final var pawn : getActivePawns()) {
            final var m = pawn.calculateValidMoves(board);
            if (!m.isEmpty()) {
                legalPawns.add(pawn);
                legals.addAll(m);
            }
        }
        this.legalMoves = legals;
    }

    public Pawn getPawnOfStack(int pos) {
        return activePawns.get(pos);
    }

    public void deletePawn(Pawn p) {
        activePawns.remove(p);
    }
}
