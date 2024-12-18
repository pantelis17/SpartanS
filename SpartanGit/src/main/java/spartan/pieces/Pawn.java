/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.spartan.pieces;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.List;
import javax.swing.ImageIcon;

import main.java.spartan.Alliance;
import main.java.spartan.board.*;

/**
 * This is the parent class which contains functions vital for the right work  of the pawns.
 * 
 * @author Pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos 2980
 */
public abstract class Pawn implements Serializable {

    protected int positionOfPawn;// the position in the board
    protected final Alliance pawnAlliance; // the color of the pawn
    protected final ImageIcon front; // the front image of this pawn 
    protected final ImageIcon back; // the back image . Same for all the pawns in the same alliance
    protected final int tablePosition; // position in the hand
    protected final int Value; 
    private boolean flipped; // if we see the back or the front image. false for the back

    Pawn(final int positionOfPawn, final Alliance pawnOfAlliance, final int value, final int pos) {
        this.positionOfPawn = positionOfPawn;//initialiaze the variables 
        this.pawnAlliance = pawnOfAlliance;
        this.Value = value;
        flipped = false;
        this.tablePosition = pos;
        if (this.pawnAlliance.isBlue()) {
            ImageIcon img0 = new ImageIcon(getClass().getResource("/main/resources/images/blue.png"));
            Image img1 = img0.getImage().getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, Image.SCALE_SMOOTH); // set the size depense on the screen size 
            ImageIcon img2 = new ImageIcon(getClass().getResource("/main/resources/images/" + value + "blue.png"));
            Image img3 = img2.getImage().getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, Image.SCALE_SMOOTH);// set the size depense on the screen size 
            this.back = new ImageIcon(img1);
            this.front = new ImageIcon(img3);
        } else {
            ImageIcon img0 = new ImageIcon(getClass().getResource("/main/resources/images/red.png"));
            Image img1 = img0.getImage().getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, Image.SCALE_SMOOTH);// set the size depense on the screen size 
            ImageIcon img2 = new ImageIcon(getClass().getResource("/main/resources/images/" + value + "red.png"));
            Image img3 = img2.getImage().getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, Image.SCALE_SMOOTH);// set the size depense on the screen size 
            this.back = new ImageIcon(img1);
            this.front = new ImageIcon(img3);
        }
    }

    public int getTablePos() {
        return this.tablePosition;
    }

    public void setCordinateOfPawn(int cordinate) {
        this.positionOfPawn = cordinate;
    }

    public boolean getSide(){
        return flipped;
    }
    
    public ImageIcon getImage() {
        if (flipped) {
            return this.front;
        } else {
            return this.back;
        }
    }

    public ImageIcon getFront() {
        return this.front;
    }

    public int getPositionOfPawn() {
        return this.positionOfPawn;
    }

    public int getValue() {
        return this.Value;
    }

    public Alliance getAlliance() {
        return this.pawnAlliance;
    }

    public void setSide(boolean side) {
        flipped = side;
    }

    public abstract List<Move> calculateValidMoves(final Board board);// calculate all the valid moves for this pawn.
}
