/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.spartan;

import main.java.spartan.gui.MainMenu;

/**
 *This is the main class where we start the game.
 */
public class Spartan {

    static MainMenu a;// create a MainMenu item

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                a = new MainMenu();//initialize this item
                a.setVisible(true);//set the jFrame visible
                a.main();//call the main mathod from the MainMenu class
            }
        });
    }
}
