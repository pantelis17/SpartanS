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
        int[] proposalsForBomb = { -1, 1, -10, 10, 9, 11, -9, -11 }; //Array with candidate places for the bomb
        int cor;
        int flag = -1;
        int bombs = 3; //Initializing the number of the pawns
        int spies = 4;
        for (var i = startDeck; i < endDeck; i++) {
            coordinate.add(i); //Adding the pawns in the coordinate ArrayList
        }
        for (final var pawn : activePawns) { // // Iteration to calculate the places of the pawns
            if (pawn.getValue() == -1) { //  calculate the place of the  flag
                cor = (isBlueAlliance ? 0 : 80) + randomGenerator.nextInt(isBlueAlliance ? 19 : 20);
                coordinate.remove(Integer.valueOf(cor));
                flag = cor;
                pawn.setCoordinateOfPawn(cor);
            } else if (pawn.getValue() == 0) { // calculate the place of the bomb
                if (bombs > 0) {
                    do {
                        cor = proposalsForBomb[randomGenerator.nextInt(proposalsForBomb.length)] + flag;
                        if (coordinate.contains(cor)) { // Placing the bombs in the candidate positions 
                            pawn.setCoordinateOfPawn(cor);
                        }
                    } while (!coordinate.contains(cor));
                    coordinate.remove(Integer.valueOf(cor)); //Placing all the bombs until all are placed
                    bombs--;
                } else {
                    do {
                        cor = (isBlueAlliance ? 0 : 60) + randomGenerator.nextInt(40);
                        if (coordinate.contains(cor)) {
                            pawn.setCoordinateOfPawn(cor);
                        }
                    } while (!coordinate.contains(cor));
                    coordinate.remove(Integer.valueOf(cor));

                }

            } else if (pawn.getValue() == 2) { // calculate the place of the spies 
                if (spies > 0) {
                    do {
                        cor = (isBlueAlliance ? 0 : 60) + randomGenerator.nextInt(40);
                        if (coordinate.contains(cor) && (isBlueAlliance ? cor >= 20 : cor < 80)) {
                            pawn.setCoordinateOfPawn(cor);
                        }
                    } while (!coordinate.contains(cor) || (isBlueAlliance ? cor < 20 : cor >= 80)); //Placing all the spies until all are placed
                    coordinate.remove(Integer.valueOf(cor));
                    spies--;
                } else {
                    do {
                        cor = (isBlueAlliance ? 0 : 60) + randomGenerator.nextInt(40);
                        if (coordinate.contains(cor)) {
                            pawn.setCoordinateOfPawn(cor);
                        }
                    } while (!coordinate.contains(cor));
                    coordinate.remove(Integer.valueOf(cor));

                }
            } else {
                do {
                    cor = (isBlueAlliance ? 0 : 60) + randomGenerator.nextInt(40);
                    if (coordinate.contains(cor)) {
                        pawn.setCoordinateOfPawn(cor);
                    }
                } while (!coordinate.contains(cor));
                coordinate.remove(Integer.valueOf(cor));
            }
            random.add(pawn); // Adding the pawns by random order
        }
        for (final var pawn : random) {
            this.board.setPawnOnBoard(pawn.getPositionOfPawn(), pawn); //Iteration of placing the pawns on the board
        }
        this.board.toStringBoard(); //Getting the coordinates 
        this.setActivePawns(random);
        return random; //returns the random values
    }

    //Method that sets the position and the coordinates of the pawns on the panel of the game
    public void setCoordinateOfPawn(int pos, int coordinate, int panel, boolean start) {
        if (panel == 0) {
            var position = 0;
            for (final var pawn : activePawns) {
                if (pawn.getPositionOfPawn() == pos) { //Calculating the coordinates
                    break;
                } else {
                    position++;
                }
            }
            this.activePawns.get(position).setCoordinateOfPawn(coordinate); //Getting the position and the coordinates
        } else {
            if (start) {
                this.activePawns.get(pos).setCoordinateOfPawn(coordinate);
            } else {
                var position = 0;
                for (final var pawn : this.activePawns) {
                    if ((pawn.getPositionOfPawn() + 1) * (-1) == pos) { // numerical operation to calculate the position
                        break;
                    } else {
                        position++;
                    }
                }
                this.activePawns.get(position).setCoordinateOfPawn(coordinate); //Getting the position and the coordinates

            }
        }
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
        //check if 1 is near the 10 of opponent.
        for (final var m : legalMoves) {
            if (tiles.get(m.getDestinationCoordinate()).isTileOccupied()
                    && m.getPawn().getValue() == 1
                    && tiles.get(m.getDestinationCoordinate()).getPawn().getValue() == 10) {
                if (tiles.get(m.getDestinationCoordinate()).getPawn().getSide()) {
                    return m;
                } else {
                    break;
                }
            }
        }
        //a simple way to make an attack .
        //chance can break the loop and return the value of a (the random move) . The move should be legal.
        int a, chance = 0;
        while (true) {
            a = randomGenerator.nextInt(this.legalMoves.size());
            if (tiles.get(legalMoves.get(a).getDestinationCoordinate()).isTileOccupied()) {
                if (tiles.get(legalMoves.get(a).getDestinationCoordinate()).getPawn().getValue() > legalMoves.get(a)
                        .getPawn().getValue()) {
                    chance = randomGenerator.nextInt(10);

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
