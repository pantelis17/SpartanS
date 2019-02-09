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
 *
 * @author pante
 */
public class StackPawns implements Serializable{

    private ArrayList<Pawn> stack;

    public StackPawns(Alliance alliance) {
        stack = new ArrayList<>();
        int value;
        for (value = -1; value < 11; value++) {
            if (value == -1) {
                Flag f = new Flag(-1, alliance);
                stack.add(f);
            } else if (value == 0) {

                for (int i = 0; i < 6; i++) {
                    Bomb b = new Bomb(-1, alliance,1+i);
                    stack.add(b);
                }
            } else if (value == 1) {
                NormalPawn as = new NormalPawn(-1, alliance, value,7);
                stack.add(as);
            } else if (value == 2) {

                for (int i = 0; i < 8; i++) {
                    Scout s = new Scout(-1, alliance,8+i);
                    stack.add(s);
                }
            } else if (value == 3) {
                for (int i = 0; i < 5; i++) {
                    NormalPawn a = new NormalPawn(-1, alliance, value,16+i);

                    stack.add(a);
                }
            } else if (value == 4 ) {
                for (int i = 0; i < 4; i++) {
                    NormalPawn c = new NormalPawn(-1, alliance, value,21+i);

                    stack.add(c);
                }
            }else if(value == 5 ){
                for (int i = 0; i < 4; i++) {
                    NormalPawn c = new NormalPawn(-1, alliance, value,25+i);

                    stack.add(c);
                }
            }else if(value == 6){
                for (int i = 0; i < 4; i++) {
                    NormalPawn c = new NormalPawn(-1, alliance, value,29+i);

                    stack.add(c);
                }
            } else if (value == 7) {
                for (int i = 0; i < 3; i++) {
                    NormalPawn d = new NormalPawn(-1, alliance, value,33+i);

                    stack.add(d);
                }
            } else if (value == 8) {
                NormalPawn f1 = new NormalPawn(-1, alliance, value,36);
                stack.add(f1);
                NormalPawn f2 = new NormalPawn(-1, alliance, value,37);
                stack.add(f2);
            } else if(value == 9) {
                NormalPawn t = new NormalPawn(-1, alliance, value,38);
                stack.add(t);
            }
            else{
                 NormalPawn t = new NormalPawn(-1, alliance, value,39);
                stack.add(t);
            }

        }
    }

    public ArrayList<Pawn> getStack() {
        return this.stack;
    }

    public Pawn getPawnOfStack(int pos) {
        //Pawn removedPawn = this.stack.get(pos)
        return this.stack.get(pos);
    }

    
   
    
    public void setPawn(int newpos, Pawn p) {
        for (Pawn pawn : this.stack) {
            if (pawn.getValue() == p.getValue() && pawn.getPositionOfPawn() == p.getPositionOfPawn()) {

            }
        }
    }
}
