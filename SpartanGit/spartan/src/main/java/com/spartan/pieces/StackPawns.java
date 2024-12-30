/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartan.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.spartan.enumerations.Alliance;

import lombok.Getter;

/**
 * This class is used to create the hands for the both players.
 */
@Getter
public class StackPawns {

    private final List<Pawn> stack;

    /**
     * 
     * @param alliance is the color of the player  
     */
    public StackPawns(Alliance alliance) {
        stack = new ArrayList<>();
        addFlag(alliance);
        addBombs(alliance);
        addNormalPawns(alliance);
    }
    
    private void addFlag(Alliance alliance) {
        stack.add(new Flag(-1, alliance));
    }
    
    private void addBombs(Alliance alliance) {
        for (int i = 0; i < 6; i++) {
            stack.add(new Bomb(-1, alliance, i + 1));
        }
    }
    
    private void addNormalPawns(Alliance alliance) {
        addPawns(alliance, 1, 7, 1);   // 1 spy
        addPawns(alliance, 2, 8, 8);  // 8 scouts
        addPawns(alliance, 3, 16, 5); // 5 pawns of rank 3
        addPawns(alliance, 4, 21, 4); // 4 pawns of rank 4
        addPawns(alliance, 5, 25, 4); // 4 pawns of rank 5
        addPawns(alliance, 6, 29, 4); // 4 pawns of rank 6
        addPawns(alliance, 7, 33, 3); // 3 pawns of rank 7
        addPawns(alliance, 8, 36, 2); // 2 pawns of rank 8
        addPawns(alliance, 9, 38, 1); // 1 pawn of rank 9
        addPawns(alliance, 10, 39, 1); // 1 pawn of rank 10
    }
    
    private void addPawns(Alliance alliance, int rank, int startId, int count) {
        for (int i = 0; i < count; i++) {
            stack.add(new NormalPawn(-1, alliance, rank, startId + i));
        }
    }  
}
