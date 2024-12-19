package com.spartan.player;

import java.util.ArrayList;
import java.util.List;

import com.spartan.board.Board;
import com.spartan.enumerations.Alliance;
import com.spartan.pieces.Pawn;
import com.spartan.pieces.StackPawns;

/**
 *  This class contain every function we need for the redPlayer.
 */
public class RedPlayer extends Player {

    private List<Pawn> activeRedPawns;// is the pawn which is still in the game.
    private List<Integer> startMove; // contains the move which the player can make in the set of his pawn

    public RedPlayer(final Board board) {
        super(board);
        this.activeRedPawns = new StackPawns(Alliance.RED).getStack();
        this.startMove = this.calculateStartMove();
    }
    
    @Override
    public List<Pawn> getActivePawns() {
        return this.activeRedPawns;
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBluePlayer();
    }

    /**
     * This function is used to create the set of the board for the redPlayer
     * 
     * @return a new arraylist with the new coordinates of the pawns
     */
    @Override
    public List<Pawn> random() {
        final var random = new ArrayList<Pawn>();//create the arraylist with the pawn after the change of their coordinates
        final var coordinate = new ArrayList<Integer>();//here we have all the coordinates where we can add a pawn.
        int[] proposalsForBomb = {-1, 1, -10, 10, 9, 11, -9, -11}; // here are some proposal positions for the bombs around the flag
        int cor;
        int flag = -1;//this is for the coordinate of the flag
        int bombs = 3;// we want at least 3 bombs around the flag
        int spies = 4;//we want at least 4 spies in the 2 from lines
        for (var i = 60; i < 100; i++) {
            coordinate.add(i);//initialize the coordinate arraylist
        }
        for (final var pawn : activeRedPawns) {// for each active pawn 
            if (pawn.getValue() == -1) {// if the pawn is the flag
                cor = 80 + randomGenerator.nextInt(20);// we need the flag to be in the back 2 lines
                coordinate.remove(Integer.valueOf(cor));//remove the coordinate of the flag from the available coordinates
                flag = cor;
                pawn.setCoordinateOfPawn(cor);//set the coordinate to the flag
            } else if (pawn.getValue() == 0) {// if the pawn if bomb
                if (bombs > 0) {//if we do not have 3 bombs close to the flag
                    do {
                        cor = proposalsForBomb[randomGenerator.nextInt(proposalsForBomb.length)] + flag;//create a possible coordinate
                        if (coordinate.contains(cor)) {// check if this coordinate exist in the arraylist
                            pawn.setCoordinateOfPawn(cor);// if it exist set the pawn on it
                        }
                    } while (!coordinate.contains(cor));
                    coordinate.remove(Integer.valueOf(cor));//remove the coordinate from the arraylist
                    bombs--;// minus the number of the bombs we still need close to the flag
                } else {// if the flag has 3 bombs around it we come here
                    do {
                        cor = 60 + randomGenerator.nextInt(40);// choose a random position for the bomb
                        if (coordinate.contains(cor)) {//if it is available 
                            pawn.setCoordinateOfPawn(cor);//set the bomb on it
                        }
                    } while (!coordinate.contains(cor));//else make a loop until we find an available coordinate
                    coordinate.remove(Integer.valueOf(cor));//remove the coordinate from the arraylist

                }

            } else if (pawn.getValue() == 2) {//if the pawn is scout
                if (spies > 0) {// the if we have less than 4 scouts in the 2 front lines
                    do {
                        cor = 60 + randomGenerator.nextInt(40);// choose random coordinate
                        if (coordinate.contains(cor) && cor < 80) {//check is it exist and if it is in the 2 front lines
                            pawn.setCoordinateOfPawn(cor);//if yes set the coordinate to the pawn
                        }
                    } while (!coordinate.contains(cor) || cor >= 80);//loop until to find a legal coordinate
                    coordinate.remove(Integer.valueOf(cor));//remove the coordinate from the arraylist
                    spies--;//minus the scouts we need in the front line
                } else {//if we have at least 4 scouts in the front line
                    do {
                        cor = 60 + randomGenerator.nextInt(40);//choose random coordinate
                        if (coordinate.contains(cor)) {//check if it is exist
                            pawn.setCoordinateOfPawn(cor);//set the coordinate to the pawn
                        }
                    } while (!coordinate.contains(cor));
                    coordinate.remove(Integer.valueOf(cor));// remove the coordinate

                }
            } else {//if it is any other pawn
                do {
                    cor = 60 + randomGenerator.nextInt(40);//choose a random coordinate
                    if (coordinate.contains(cor)) {//check if it is exist
                        pawn.setCoordinateOfPawn(cor);//if yes set the coordinate to the pawn
                    }
                } while (!coordinate.contains(cor));
                coordinate.remove(Integer.valueOf(cor));
            }
            random.add(pawn);//add the pawn with it final coordinate to the arraylist with the set pawns
        }
        for (final var pawn : random) {
            this.board.setPawnOnBoard(pawn.getPositionOfPawn(), pawn);//set every pawn on the board
        }
        this.board.toStringBoard();//print the board
        this.setStack(random);//set the stack of the pawns
        return random;
    }

    /**
     * This function is used to change the coordinate of a pawn.
     * 
     * @param pos is the current coordinate
     * @param coordinate is the coordinate we want to go
     * @param panel is the panel where the pawn was
     * @param Start show us if we are still on the set of the board or if we play
     */
    @Override
    public void setCoordinateOfPawn(int pos, int coordinate, int panel, boolean start) {
        if (panel == 0) {//if the pawn is on the board
            int position = 0;
            for (Pawn pawn : this.activeRedPawns) {//for each active pawn
                if (pawn.getPositionOfPawn() == pos) {//search for the pawn we need
                    break;
                } else {
                    position++;
                }
            }
            this.activeRedPawns.get(position).setCoordinateOfPawn(coordinate);//set the pawn on the new coordinate
        } else {// if the pawn is on the hand of the player
            if (start) {// if we are in the start
                this.activeRedPawns.get(pos).setCoordinateOfPawn(coordinate);//set the coordinate
            } else {

                int position = 0;
                for (Pawn pawn : this.activeRedPawns) {//search for the position of the pawn
                    if ((pawn.getPositionOfPawn() + 1) * (-1) == pos) {
                        break;
                    } else {
                        position++;
                    }
                }
                this.activeRedPawns.get(position).setCoordinateOfPawn(coordinate);//add ot to the new coordinate
            }
        }
    }

    @Override
    public void setStack(List<Pawn> p) {
        this.activeRedPawns = p;
    }

    public Pawn getPawnOfStack(int pos) {
        return this.activeRedPawns.get(pos);
    }

    @Override
    public void deletePawn(Pawn p) {
        this.activeRedPawns.remove(p);
    }

    /**
     * The red player can set his pawn in the coordinate 60-100 because he is in the lower side of the board.
     * 
     * @return every move the player can make in the set of the board
     */
    @Override
    public ArrayList<Integer> calculateStartMove() {
        ArrayList<Integer> start = new ArrayList<>();
        for (int k = 60; k < 100; k++) {
            start.add(k);
        }
        return start;
    }

    @Override
    public List<Integer> getStart() {
        return this.startMove;
    }

}
