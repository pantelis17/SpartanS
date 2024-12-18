package main.java.spartan.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import main.java.spartan.Alliance;
import main.java.spartan.board.Board;
import main.java.spartan.pieces.Pawn;
import main.java.spartan.pieces.StackPawns;

/**
 *
 * @author Pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos 2980
 */
public class BluePlayer extends Player implements Serializable {

    ArrayList<Pawn> activeBluePawns; // ArrayList of the pawns of the BluePlayer
    ArrayList<Integer> startMove;//Arraylist of initial move of the Player

    public BluePlayer(final Board board) {       //Constractor of the Class
        super(board);     // Inherits the board og the game
        this.activeBluePawns = new StackPawns(Alliance.BLUE).getStack();  // Creates the pawns of the BluePlayer
        this.startMove = calculateStartMove();  // Calculates the initial move
    }
    
    // Abstruct methods of the class
    
    @Override
    public ArrayList<Pawn> getActivePawns() {   //getter method for the active pawns
        return this.activeBluePawns;
    }

    @Override
    public Alliance getAlliance() {       //getter method for the Blue alliance of the player
        return Alliance.BLUE;
    }

    @Override
    public Player getOpponent() {                //getter method for opponents move 
        return this.board.getRedPlayer();
    }

      // This method is in charge of  placing the pawns of the palyer in random order
    @Override
    public ArrayList<Pawn> Random() {            
        ArrayList<Pawn> random = new ArrayList<>();               // Arraylist of random pawns
        ArrayList<Integer> cordinate = new ArrayList<>();              // Arraylist of the cordinates 
        int[] proposalsForBomb = {-1, 1, -10, 10, 9, 11, -9, -11};     //Array with canditate places for the bomb
        int cor;
        int flag = -1;
        int bombs = 3;                 //Initializing the number of the pawns
        int spies = 4;
        for (int i = 0; i < 40; i++) {
            cordinate.add(i);                //Adding the pawns in the coordinate ArrayList
        }
        Random r = new Random();               // random object 
        for (Pawn pawn : activeBluePawns) {             // //Iterration to calculate the places of the pawns
            if (pawn.getValue() == -1) {              //  calculate the place of the  flag
                cor = r.nextInt(19);
                cordinate.remove(new Integer(cor));
                flag = cor;
                pawn.setCordinateOfPawn(cor);
            } else if (pawn.getValue() == 0) {            // calculate the place of the bomb
                if (bombs > 0) {
                    do {
                        cor = proposalsForBomb[r.nextInt(proposalsForBomb.length)] + flag;  
                        if (cordinate.contains(cor)) {                       // Placing the bombs in the canditate positions 
                            pawn.setCordinateOfPawn(cor);
                        }
                    } while (!cordinate.contains(cor));
                    cordinate.remove(new Integer(cor));            //Placing all the bombs until all are placed
                    bombs--;
                } else {
                    do {
                        cor = r.nextInt(40);
                        if (cordinate.contains(cor)) {
                            pawn.setCordinateOfPawn(cor);
                        }
                    } while (!cordinate.contains(cor));
                    cordinate.remove(new Integer(cor));

                }

            } else if (pawn.getValue() == 2) {         // calculate the place of the spies 
                if (spies > 0) {
                    do {
                        cor = r.nextInt(40);
                        if (cordinate.contains(cor) && cor >= 20) {
                            pawn.setCordinateOfPawn(cor);            
                        }
                    } while (!cordinate.contains(cor) || cor < 20);       //Placing all the spies until all are placed
                    cordinate.remove(new Integer(cor));
                    spies--;
                } else {
                    do {
                        cor = r.nextInt(40);
                        if (cordinate.contains(cor)) {
                            pawn.setCordinateOfPawn(cor);
                        }
                    } while (!cordinate.contains(cor));
                    cordinate.remove(new Integer(cor));

                }
            } else {
                do {
                    cor = r.nextInt(40);
                    if (cordinate.contains(cor)) {
                        pawn.setCordinateOfPawn(cor);
                    }
                } while (!cordinate.contains(cor));
                cordinate.remove(new Integer(cor));
            }
            random.add(pawn);    // Adding the pawns by random order
        }
        for (Pawn pawn : random) {
            this.board.setPawnOnBoard(pawn.getPositionOfPawn(), pawn);         //Iterration of placing the pawns on the board
        }
        this.board.toStringBoard();    //Getting the coordinates 
        this.setStack(random);
        return random;  //returns the random values
    }

    
    //Method that sets the position and the coordinates of the pawns on the panel of the game
    @Override
    public void setCordinateofPawn(int pos, int cordinate, int panel, boolean Start) {
        if (panel == 0) {
            int position = 0;
            for (Pawn pawn : this.activeBluePawns) {
                if (pawn.getPositionOfPawn() == pos) {                 //Calculating the coordinates
                    break;
                } else {
                    position++;
                }
            }
            this.activeBluePawns.get(position).setCordinateOfPawn(cordinate); //Getting the position and the coordinates

        } else {
            if (Start) {
                this.activeBluePawns.get(pos).setCordinateOfPawn(cordinate);
            } else {

                int position = 0;
                for (Pawn pawn : this.activeBluePawns) {
                    if ((pawn.getPositionOfPawn() + 1) * (-1) == pos) {  // numerical operation to calculate the position
                        break;
                    } else {
                        position++;
                    }
                }
                this.activeBluePawns.get(position).setCordinateOfPawn(cordinate); //Getting the position and the coordinates

            }
        }
    }

    
    @Override
    public void setStack(ArrayList<Pawn> p) {  //seter method for the blue pawns 
        this.activeBluePawns = p;
    }

    public Pawn getPawnOfStack(int pos) {            //geter method the position of the pawns 
        //Pawn removedPawn = this.stack.get(pos)
        return this.activeBluePawns.get(pos);
    }

    @Override
    public ArrayList<Integer> calculateStartMove() {      // method of calculating the arraylist of  the start move 
        ArrayList<Integer> start = new ArrayList<>();

        for (int k = 0; k < 40; k++) {
            start.add(k);        //Adding the values
        }
        return start;
    }

    @Override
    public void DeletePawn(Pawn p) {        //method to delete a pawn from the available blue pawns
        this.activeBluePawns.remove(p);
    }

    @Override
    public ArrayList<Integer> getStart() {   //getter method for the start move
        return this.startMove;
    }
}
