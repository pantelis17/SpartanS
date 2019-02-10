package spartan.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import spartan.Alliance;
import spartan.board.Board;
import spartan.pieces.Pawn;
import spartan.pieces.StackPawns;

/**
 *  This class contain every function we need for the redPlayer.
 * 
 * @author Pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos 2980
 */
public class RedPlayer extends Player implements Serializable {

    ArrayList<Pawn> activeRedPawns;// is the pawn which is still in the game.
    ArrayList<Integer> startMove; // contains the move which the player can make in the set of his pawn

    public RedPlayer(final Board board) {
        super(board);
        this.activeRedPawns = new StackPawns(Alliance.RED).getStack();
        this.startMove = this.calculateStartMove();
    }
    
    @Override
    public ArrayList<Pawn> getActivePawns() {
        return this.activeRedPawns;
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBluePlayer();//return who is the opponent of this player, so return the blue player
    }

    /**
     * This function is used to create the set of the board for the redPlayer
     * 
     * @return a new arraylist with the new cordinates of the pawns
     */
    @Override
    public ArrayList<Pawn> Random() {
        ArrayList<Pawn> random = new ArrayList<>();//create the arraylist with the pawn after the change of their cordinates
        ArrayList<Integer> cordinate = new ArrayList<>();//here we have all the cordinates where we can add a pawn.
        int[] proposalsForBomb = {-1, 1, -10, 10, 9, 11, -9, -11}; // here are some proposal positions for the bombs around the flag
        int cor;
        int flag = -1;//this is for the cordinate of the flag
        int bombs = 3;// we want at least 3 bombs around the flag
        int spies = 4;//we want at least 4 spies in the 2 from lines
        for (int i = 60; i < 100; i++) {
            cordinate.add(i);//initialize the cordiante arraylist
        }
        Random r = new Random();
        for (Pawn pawn : activeRedPawns) {// for each active pawn 
            if (pawn.getValue() == -1) {// if the pawn is the flag
                cor = 80 + r.nextInt(20);// we need the flag to be in the back 2 lines
                cordinate.remove(new Integer(cor));//remove the cordinate of the flag from the available cordinates
                flag = cor;
                pawn.setCordinateOfPawn(cor);//set the cordinate to the flag
            } else if (pawn.getValue() == 0) {// if the pawn if bomb
                if (bombs > 0) {//if we doesnt have 3 bombs close to the flag
                    do {
                        cor = proposalsForBomb[r.nextInt(proposalsForBomb.length)] + flag;//create a posible cordinate
                        if (cordinate.contains(cor)) {// check if this cordinate exist in the arraylist
                            pawn.setCordinateOfPawn(cor);// if it exist set the pawn on it
                        }
                    } while (!cordinate.contains(cor));
                    cordinate.remove(new Integer(cor));//remove the cordinate from the arraylist
                    bombs--;// minus the number of the bombs we still need close to the flag
                } else {// if the flag has 3 bombs aroung it we come here
                    do {
                        cor = 60 + r.nextInt(40);// choose a random position for the bomb
                        if (cordinate.contains(cor)) {//if it is available 
                            pawn.setCordinateOfPawn(cor);//set the bomb on it
                        }
                    } while (!cordinate.contains(cor));//else make a loop until we find an available cordinate
                    cordinate.remove(new Integer(cor));//remove the cordinate from the arraylist

                }

            } else if (pawn.getValue() == 2) {//if the pawn is scout
                if (spies > 0) {// the if we have less than 4 scouts in the 2 front lines
                    do {
                        cor = 60 + r.nextInt(40);// chooose random cordinate
                        if (cordinate.contains(cor) && cor < 80) {//check is it exist and if it is in the 2 front lines
                            pawn.setCordinateOfPawn(cor);//if yes set the cordinate to the pawn
                        }
                    } while (!cordinate.contains(cor) || cor >= 80);//loop until to find a legal cordinate
                    cordinate.remove(new Integer(cor));//remove the cordinate from the arraylist
                    spies--;//minus the scouts we need in the front line
                } else {//if we have at least 4 scouts in the front line
                    do {
                        cor = 60 + r.nextInt(40);//choose random cordinate
                        if (cordinate.contains(cor)) {//check if it is exist
                            pawn.setCordinateOfPawn(cor);//set the cordinate to the pawn
                        }
                    } while (!cordinate.contains(cor));
                    cordinate.remove(new Integer(cor));// remove the cordinate

                }
            } else {//if it is any other pawn
                do {
                    cor = 60 + r.nextInt(40);//choose a random cordinate
                    if (cordinate.contains(cor)) {//check if it is exist
                        pawn.setCordinateOfPawn(cor);//if yes set the cordinate to the pawn
                    }
                } while (!cordinate.contains(cor));
                cordinate.remove(new Integer(cor));
            }
            random.add(pawn);//add the pawn with it final cordinate to the arraylist with the set pawns
        }
        for (Pawn pawn : random) {
            this.board.setPawnOnBoard(pawn.getPositionOfPawn(), pawn);//set every pawn on the board
        }
        this.board.toStringBoard();//print the board
        this.setStack(random);//set the stack of the pawns
        return random;
    }

    /**
     * This function is used to change the cordinate of a pawn.
     * 
     * @param pos is the current cordinate
     * @param cordinate is the cordinate we want to go
     * @param panel is the panel where the pawn was
     * @param Start show us if we are still on the set of the board or if we play
     */
    @Override
    public void setCordinateofPawn(int pos, int cordinate, int panel, boolean Start) {
        if (panel == 0) {//if the pawn is on the board
            int position = 0;
            for (Pawn pawn : this.activeRedPawns) {//for each active pawn
                if (pawn.getPositionOfPawn() == pos) {//search for the pawn we need
                    break;
                } else {
                    position++;
                }
            }
            this.activeRedPawns.get(position).setCordinateOfPawn(cordinate);//set the pawn on the new cordinate
        } else {// if the pawn is on the hand of the player
            if (Start) {// if we are in the start
                this.activeRedPawns.get(pos).setCordinateOfPawn(cordinate);//set the cordinate
            } else {

                int position = 0;
                for (Pawn pawn : this.activeRedPawns) {//search for the position of the pawn
                    if ((pawn.getPositionOfPawn() + 1) * (-1) == pos) {
                        break;
                    } else {
                        position++;
                    }
                }
                this.activeRedPawns.get(position).setCordinateOfPawn(cordinate);//add ot to the new cordinate
            }
        }
    }

    @Override
    public void setStack(ArrayList<Pawn> p) {
        this.activeRedPawns = p;
    }

    public Pawn getPawnOfStack(int pos) {
        return this.activeRedPawns.get(pos);
    }

    @Override
    public void DeletePawn(Pawn p) {
        this.activeRedPawns.remove(p);
    }

    /**
     * The red player can set his pawn in the cordinate 60-100 because he is in the lower side of the board.
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
    public ArrayList<Integer> getStart() {
        return this.startMove;
    }

}
