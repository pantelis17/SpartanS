/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartan.pieces;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.ImageIcon;

import com.spartan.board.*;
import com.spartan.enumerations.Alliance;

import lombok.Getter;
import lombok.Setter;

/**
 * This is the parent class which contains functions vital for the right work  of the pawns.
 */
@Getter
@Setter
public abstract class Pawn {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    protected final Alliance pawnAlliance; // the color of the pawn
    protected final ImageIcon front; // the front image of this pawn 
    protected final ImageIcon back; // the back image . Same for all the pawns in the same alliance
    protected final int tablePosition; // position in the hand
    protected final int value; 
    protected int positionOfPawn;// the position in the board
    private boolean flipped; // if we see the back or the front image. false for the back

    Pawn(final int positionOfPawn, final Alliance pawnOfAlliance, final int value, final int pos) {
        this.positionOfPawn = positionOfPawn; //initialize the variables 
        this.pawnAlliance = pawnOfAlliance;
        this.value = value;
        flipped = false;
        this.tablePosition = pos;
        final var pawnSize = (int) SCREEN_SIZE.getWidth() / 24;
        var color = "red.png";
        if (this.pawnAlliance.isBlue()) {
            color = "blue.png";
        }
        final var img0 = new ImageIcon(getClass().getResource(String.format("/images/%s", color)));
        final var img1 = img0.getImage().getScaledInstance(pawnSize, pawnSize, Image.SCALE_SMOOTH); // set the size based on the screen size 
        final var img2 = new ImageIcon(getClass().getResource(String.format("/images/%s%s", value, color)));
        final var img3 = img2.getImage().getScaledInstance(pawnSize, pawnSize, Image.SCALE_SMOOTH);// set the size based on the screen size 
        this.back = new ImageIcon(img1);
        this.front = new ImageIcon(img3);
    }

    public ImageIcon getImage() {
        if (flipped) {
            return front;
        } else {
            return back;
        }
    }

    public abstract List<Move> calculateValidMoves(final Board board);// calculate all the valid moves for this pawn.
}
