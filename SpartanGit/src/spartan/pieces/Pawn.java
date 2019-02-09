/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartan.pieces;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.List;
import javax.swing.ImageIcon;
import spartan.Alliance;
import spartan.board.*;

public abstract class Pawn implements Serializable {

    protected int positionOfPawn;
    protected final Alliance pawnAlliance;
    //TODO gia eikones . tha xreiastei kai allo pedio gia eikones logika.
    protected final ImageIcon front;
    protected final ImageIcon back;
    protected final int tablePosition;
    protected final int Value;
    private boolean flipped;

    Pawn(final int positionOfPawn, final Alliance pawnOfAlliance, final int value, final int pos) {
        this.positionOfPawn = positionOfPawn;
        this.pawnAlliance = pawnOfAlliance;
        this.Value = value;
        flipped = false;
        this.tablePosition = pos;
        if (this.pawnAlliance.isBlue()) {
            ImageIcon img0 = new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource("/spartan/Images/blue.png"));
            Image img1 = img0.getImage().getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, Image.SCALE_SMOOTH);
            ImageIcon img2 = new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource("/spartan/Images/" + value + "blue.png"));
            Image img3 = img2.getImage().getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, Image.SCALE_SMOOTH);
            this.back = new ImageIcon(img1);
            this.front = new ImageIcon(img3);
        } else {
            ImageIcon img0 = new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource("/spartan/Images/red.png"));
            Image img1 = img0.getImage().getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, Image.SCALE_SMOOTH);
            ImageIcon img2 = new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource("/spartan/Images/" + value + "red.png"));
            Image img3 = img2.getImage().getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 24, Image.SCALE_SMOOTH);
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

    public abstract List<Move> calculateValidMoves(final Board board);//TODO 3 moves rule.

    /*/
    public enum Pawntype{
      SCOUT("S"),
      NORMALPAWN("N"),
      BOMB("B"),
      FLAG("F");

      private String pawnName;
      
      Pawntype(final String pawnName){
          this.pawnName = pawnName;
      }
      
      public String toSting(){
          return this.pawnName;
          
      }
        
    }/*/
}
