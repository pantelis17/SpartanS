/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartan.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import spartan.Alliance;

/**
 * This class is used to create the hands for the both players.
 * 
 * @author Pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos 2980
 */
public class StackPawns implements Serializable{

    private ArrayList<Pawn> stack;

    /**
     * 
     * @param alliance is the color of the player  
     */
    public StackPawns(Alliance alliance) {
        stack = new ArrayList<>();// create a new arraylist which is the hand 
        int value;
        for (value = -1; value < 11; value++) { // make a loop for all the value we have in the game
            switch (value) {
                case -1:
                    // value -1 means that it is a flag
                    Flag f = new Flag(-1, alliance);// so create a flag with the color of the alliance
                    stack.add(f);// add it to the hand
                    break;
                case 0:// value 0 means that it is a bomb and we have 6 bombs in the game
                    for (int i = 0; i < 6; i++) {//make a loop to create 6 bombs
                        Bomb b = new Bomb(-1, alliance,1+i);
                        stack.add(b);//add the bombs to the hand
                    }   break;
                case 1: 
                    NormalPawn as = new NormalPawn(-1, alliance, value,7);
                    stack.add(as);
                    break;
                case 2:// value 2 means that it is a scout and we have 8 scouts in the game
                    for (int i = 0; i < 8; i++) {// make a loop to create 8 scouts
                        Scout s = new Scout(-1, alliance,8+i);
                        stack.add(s);// add them to the hand
                    }   break;
                case 3:
                    for (int i = 0; i < 5; i++) {
                        NormalPawn a = new NormalPawn(-1, alliance, value,16+i);
                        
                        stack.add(a);
                    }   break;
                case 4:
                    for (int i = 0; i < 4; i++) {
                        NormalPawn c = new NormalPawn(-1, alliance, value,21+i);
                        
                        stack.add(c);
                    }   break;
                case 5:
                    for (int i = 0; i < 4; i++) {
                        NormalPawn c = new NormalPawn(-1, alliance, value,25+i);
                        
                        stack.add(c);
                    }   break;
                case 6:
                    for (int i = 0; i < 4; i++) {
                        NormalPawn c = new NormalPawn(-1, alliance, value,29+i);
                        
                        stack.add(c);
                    }   break;
                case 7:
                    for (int i = 0; i < 3; i++) {
                        NormalPawn d = new NormalPawn(-1, alliance, value,33+i);
                        
                        stack.add(d);
                    }   break;
                case 8:
                    NormalPawn f1 = new NormalPawn(-1, alliance, value,36);
                    stack.add(f1);
                    NormalPawn f2 = new NormalPawn(-1, alliance, value,37);
                    stack.add(f2);
                    break;
                case 9:
                    {
                        NormalPawn t = new NormalPawn(-1, alliance, value,38);
                        stack.add(t);
                        break;
                    }
                default:
                    {
                        NormalPawn t = new NormalPawn(-1, alliance, value,39);
                        stack.add(t);
                        break;
                    }
            }

        }
    }

    public ArrayList<Pawn> getStack() {
        return this.stack;
    }

    public Pawn getPawnOfStack(int pos) {
        return this.stack.get(pos);
    }  
}
