/*
 * This class is about of the move . It contains some useful components of Board in order to adjust the move in Board.
 */
package com.spartan.player;

import com.spartan.board.Board;
import com.spartan.board.Move;
import com.spartan.enumerations.MoveStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * The fields are the Board (from and to) . Each move changes the structure of
 * Board so we need from and to. Move Status indicates what kind of Move was.
 * For example ,if it is Done and if is Illegal .
 */
@Getter
@AllArgsConstructor
public class MoveTransition {

    private final Board fromBoard;
    private final Board toBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;
}
