/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartan;

import spartan.gui.MainMenu;

/**
 *
 * @author pante
 */
public class Spartan {
   static MainMenu a;
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              a=  new MainMenu();
              a.setVisible(true);
              a.main();
            }
        });
    }
}
