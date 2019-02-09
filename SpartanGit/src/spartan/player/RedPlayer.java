package spartan.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import spartan.Alliance;
import spartan.board.Board;
import spartan.pieces.Pawn;
import spartan.pieces.StackPawns;

/**
 *
 * @author user
 */
public class RedPlayer extends Player implements Serializable {

    ArrayList<Pawn> activeRedPawns;
    ArrayList<Integer> startMove;

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
        return this.board.getBluePlayer();
    }

    @Override
    public ArrayList<Pawn> Random() {
        ArrayList<Pawn> random = new ArrayList<>();
        ArrayList<Integer> cordinate = new ArrayList<>();
        int[] proposalsForBomb = {-1, 1, -10, 10, 9, 11, -9, -11};
        int cor;
        int flag = -1;
        int bombs = 3;
        int spies = 4;
        for (int i = 60; i < 100; i++) {
            cordinate.add(i);
        }
        Random r = new Random();
        for (Pawn pawn : activeRedPawns) {
            if (pawn.getValue() == -1) {
                cor = 80 + r.nextInt(20);
                cordinate.remove(new Integer(cor));
                flag = cor;
                pawn.setCordinateOfPawn(cor);
            } else if (pawn.getValue() == 0) {
                if (bombs > 0) {
                    do {
                        cor = proposalsForBomb[r.nextInt(proposalsForBomb.length)] + flag;
                        if (cordinate.contains(cor)) {
                            pawn.setCordinateOfPawn(cor);
                        }
                    } while (!cordinate.contains(cor));
                    cordinate.remove(new Integer(cor));
                    bombs--;
                } else {
                    do {
                        cor = 60 + r.nextInt(40);
                        if (cordinate.contains(cor)) {
                            pawn.setCordinateOfPawn(cor);
                        }
                    } while (!cordinate.contains(cor));
                    cordinate.remove(new Integer(cor));

                }

            } else if (pawn.getValue() == 2) {
                if (spies > 0) {
                    do {
                        cor = 60 + r.nextInt(40);
                        if (cordinate.contains(cor) && cor < 80) {
                            pawn.setCordinateOfPawn(cor);
                        }
                    } while (!cordinate.contains(cor) || cor >= 80);
                    cordinate.remove(new Integer(cor));
                    spies--;
                } else {
                    do {
                        cor = 60 + r.nextInt(40);
                        if (cordinate.contains(cor)) {
                            pawn.setCordinateOfPawn(cor);
                        }
                    } while (!cordinate.contains(cor));
                    cordinate.remove(new Integer(cor));

                }
            } else {
                do {
                    cor = 60 + r.nextInt(40);
                    if (cordinate.contains(cor)) {
                        pawn.setCordinateOfPawn(cor);
                    }
                } while (!cordinate.contains(cor));
                cordinate.remove(new Integer(cor));
            }
            random.add(pawn);
        }
        for (Pawn pawn : random) {
            this.board.setPawnOnBoard(pawn.getPositionOfPawn(), pawn);
        }
        this.board.toStringBoard();
        this.setStack(random);
        return random;
    }

    @Override
    public void setCordinateofPawn(int pos, int cordinate, int panel, boolean Start) {
        if (panel == 0) {
            int position = 0;
            for (Pawn pawn : this.activeRedPawns) {
                if (pawn.getPositionOfPawn() == pos) {
                    break;
                } else {
                    position++;
                }
            }
            this.activeRedPawns.get(position).setCordinateOfPawn(cordinate);
        } else {
            if (Start) {
                this.activeRedPawns.get(pos).setCordinateOfPawn(cordinate);
            } else {

                int position = 0;
                for (Pawn pawn : this.activeRedPawns) {
                    if ((pawn.getPositionOfPawn() + 1) * (-1) == pos) {
                        break;
                    } else {
                        position++;
                    }
                }
                this.activeRedPawns.get(position).setCordinateOfPawn(cordinate);
            }
        }
    }

    @Override
    public void setStack(ArrayList<Pawn> p) {
        this.activeRedPawns = p;
    }

    public Pawn getPawnOfStack(int pos) {
        //Pawn removedPawn = this.stack.get(pos)
        return this.activeRedPawns.get(pos);
    }

    @Override
    public void DeletePawn(Pawn p) {
        this.activeRedPawns.remove(p);
    }

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
