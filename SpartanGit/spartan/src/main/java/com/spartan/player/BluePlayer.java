package com.spartan.player;

import java.util.ArrayList;
import java.util.List;

import com.spartan.board.Board;
import com.spartan.enumerations.Alliance;
import com.spartan.pieces.Pawn;
import com.spartan.pieces.StackPawns;

/**
 *  This class contain every function we need for the bluePlayer.
 */
public class BluePlayer extends Player {

    private List<Pawn> activeBluePawns; // ArrayList of the pawns of the BluePlayer
    private List<Integer> startMove;//Arraylist of initial move of the Player

    public BluePlayer(final Board board) { //Constractor of the Class
        super(board); // Inherits the board og the game
        this.activeBluePawns = new StackPawns(Alliance.BLUE).getStack(); // Creates the pawns of the BluePlayer
        this.startMove = calculateStartMove(); // Calculates the initial move
    }

    @Override
    public List<Pawn> getActivePawns() { 
        return this.activeBluePawns;
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getRedPlayer();
    }

    /**
     * This function is used to create the set of the board for the bluePlayer
     * 
     * @return a new arraylist with the new coordinates of the pawns
     */
    @Override
    public List<Pawn> random() {
        final var random = new ArrayList<Pawn>();
        final var coordinate = new ArrayList<Integer>(); // Arraylist of the coordinates 
        int[] proposalsForBomb = { -1, 1, -10, 10, 9, 11, -9, -11 }; //Array with candidate places for the bomb
        int cor;
        int flag = -1;
        int bombs = 3; //Initializing the number of the pawns
        int spies = 4;
        for (var i = 0; i < 40; i++) {
            coordinate.add(i); //Adding the pawns in the coordinate ArrayList
        }
        for (final var pawn : activeBluePawns) { // // Iteration to calculate the places of the pawns
            if (pawn.getValue() == -1) { //  calculate the place of the  flag
                cor = randomGenerator.nextInt(19);
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
                        cor = randomGenerator.nextInt(40);
                        if (coordinate.contains(cor)) {
                            pawn.setCoordinateOfPawn(cor);
                        }
                    } while (!coordinate.contains(cor));
                    coordinate.remove(Integer.valueOf(cor));

                }

            } else if (pawn.getValue() == 2) { // calculate the place of the spies 
                if (spies > 0) {
                    do {
                        cor = randomGenerator.nextInt(40);
                        if (coordinate.contains(cor) && cor >= 20) {
                            pawn.setCoordinateOfPawn(cor);
                        }
                    } while (!coordinate.contains(cor) || cor < 20); //Placing all the spies until all are placed
                    coordinate.remove(Integer.valueOf(cor));
                    spies--;
                } else {
                    do {
                        cor = randomGenerator.nextInt(40);
                        if (coordinate.contains(cor)) {
                            pawn.setCoordinateOfPawn(cor);
                        }
                    } while (!coordinate.contains(cor));
                    coordinate.remove(Integer.valueOf(cor));

                }
            } else {
                do {
                    cor = randomGenerator.nextInt(40);
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
        this.setStack(random);
        return random; //returns the random values
    }

    //Method that sets the position and the coordinates of the pawns on the panel of the game
    @Override
    public void setCoordinateOfPawn(int pos, int coordinate, int panel, boolean start) {
        if (panel == 0) {
            int position = 0;
            for (Pawn pawn : this.activeBluePawns) {
                if (pawn.getPositionOfPawn() == pos) { //Calculating the coordinates
                    break;
                } else {
                    position++;
                }
            }
            this.activeBluePawns.get(position).setCoordinateOfPawn(coordinate); //Getting the position and the coordinates

        } else {
            if (start) {
                this.activeBluePawns.get(pos).setCoordinateOfPawn(coordinate);
            } else {

                int position = 0;
                for (Pawn pawn : this.activeBluePawns) {
                    if ((pawn.getPositionOfPawn() + 1) * (-1) == pos) { // numerical operation to calculate the position
                        break;
                    } else {
                        position++;
                    }
                }
                this.activeBluePawns.get(position).setCoordinateOfPawn(coordinate); //Getting the position and the coordinates

            }
        }
    }

    @Override
    public void setStack(List<Pawn> p) { //setter method for the blue pawns 
        this.activeBluePawns = p;
    }

    public Pawn getPawnOfStack(int pos) { //getter method the position of the pawns 
        return this.activeBluePawns.get(pos);
    }

    @Override
    public List<Integer> calculateStartMove() { // method of calculating the arraylist of  the start move 
        final var start = new ArrayList<Integer>();

        for (int k = 0; k < 40; k++) {
            start.add(k); //Adding the values
        }
        return start;
    }

    @Override
    public void deletePawn(Pawn p) { //method to delete a pawn from the available blue pawns
        this.activeBluePawns.remove(p);
    }

    @Override
    public List<Integer> getStart() { //getter method for the start move
        return this.startMove;
    }
}
