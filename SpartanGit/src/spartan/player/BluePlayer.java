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
public class BluePlayer extends Player implements Serializable {

    ArrayList<Pawn> activeBluePawns;
    ArrayList<Integer> startMove;

    public BluePlayer(final Board board) {
        super(board);
        this.activeBluePawns = new StackPawns(Alliance.BLUE).getStack();
        this.startMove = calculateStartMove();
    }
    
    @Override
    public ArrayList<Pawn> getActivePawns() {
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

    @Override
    public ArrayList<Pawn> Random() {
        ArrayList<Pawn> random = new ArrayList<>();
        ArrayList<Integer> cordinate = new ArrayList<>();
        int[] proposalsForBomb = {-1, 1, -10, 10, 9, 11, -9, -11};
        int cor;
        int flag = -1;
        int bombs = 3;
        int spies = 4;
        for (int i = 0; i < 40; i++) {
            cordinate.add(i);
        }
        Random r = new Random();
        for (Pawn pawn : activeBluePawns) {
            if (pawn.getValue() == -1) {
                cor = r.nextInt(19);
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
                        cor = r.nextInt(40);
                        if (cordinate.contains(cor)) {
                            pawn.setCordinateOfPawn(cor);
                        }
                    } while (!cordinate.contains(cor));
                    cordinate.remove(new Integer(cor));

                }

            } else if (pawn.getValue() == 2) {
                if (spies > 0) {
                    do {
                        cor = r.nextInt(40);
                        if (cordinate.contains(cor) && cor >= 20) {
                            pawn.setCordinateOfPawn(cor);
                        }
                    } while (!cordinate.contains(cor) || cor < 20);
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
            for (Pawn pawn : this.activeBluePawns) {
                if (pawn.getPositionOfPawn() == pos) {
                    break;
                } else {
                    position++;
                }
            }
            this.activeBluePawns.get(position).setCordinateOfPawn(cordinate);

        } else {
            if (Start) {
                this.activeBluePawns.get(pos).setCordinateOfPawn(cordinate);
            } else {

                int position = 0;
                for (Pawn pawn : this.activeBluePawns) {
                    if ((pawn.getPositionOfPawn() + 1) * (-1) == pos) {
                        break;
                    } else {
                        position++;
                    }
                }
                this.activeBluePawns.get(position).setCordinateOfPawn(cordinate);

            }
        }
    }

    @Override
    public void setStack(ArrayList<Pawn> p) {
        this.activeBluePawns = p;
    }

    public Pawn getPawnOfStack(int pos) {
        //Pawn removedPawn = this.stack.get(pos)
        return this.activeBluePawns.get(pos);
    }

    @Override
    public ArrayList<Integer> calculateStartMove() {
        ArrayList<Integer> start = new ArrayList<>();

        for (int k = 0; k < 40; k++) {
            start.add(k);
        }
        return start;
    }

    @Override
    public void DeletePawn(Pawn p) {
        this.activeBluePawns.remove(p);
    }

    @Override
    public ArrayList<Integer> getStart() {
        return this.startMove;
    }
}
