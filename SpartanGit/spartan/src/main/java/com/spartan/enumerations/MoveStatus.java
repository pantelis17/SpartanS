package com.spartan.enumerations;

/**
 * Enumeration of the moveStatus,
 * implements the abstact method of isDone.
 */
public enum MoveStatus {

    DONE { // legal move
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE { // illegal move
        public boolean isDone() {
            return false;
        }
    };

    public abstract boolean isDone();

}