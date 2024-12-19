package com.spartan;

import com.spartan.gui.MainMenu;

public class Main {
    static MainMenu a;// create a MainMenu item
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                a = new MainMenu();//initialize this item
                a.setVisible(true);//set the jFrame visible
                a.main();//call the main method from the MainMenu class
            }
        });
    }
}