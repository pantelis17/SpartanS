/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spartan.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.spartan.enumerations.Alliance;
import com.spartan.networksocket.Client;
import com.spartan.networksocket.Server;

/**
 * This class is the JFrame of the start, Contains the modes of the game options
 * and help.
 */
public class MainMenu extends javax.swing.JFrame {

    private SinglePlayer single;
    private Alliance alliance;

    /**
     * Creates new form NewJFrame2
     */
    public MainMenu() {
        initComponents();
        setTitle("Spartan");
        setLocation(-3, 0);
        alliance = Alliance.RED;//when the game start the player in the single mode have the red pawns
        Image logo = new ImageIcon(getClass().getResource("/images/logo.png")).getImage();
        setAlwaysOnTop(true);
        setResizable(false);
        Image t = new ImageIcon(getClass().getResource("/images/cursor.png")).getImage();
        final var resizedCursorImage = t.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Set desired size here
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor cursor = toolkit.createCustomCursor(resizedCursorImage, new Point(5, 5), "Custom Cursor");
        setCursor(cursor);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        jLabel1.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() );
        jLabel1.setLocation(-3, 0);
        jPanel1.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() );
        jPanel1.setLocation(-3, 0);
        jPanel1.setBackground(new Color(0, 0, 0, 0));// so in will be transparenti
        setIconImage(logo);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon(getClass().getResource("/images/map.png"));
        Image img1 = img.getImage().getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() , Image.SCALE_SMOOTH);
        jLabel1.setIcon(new ImageIcon(img1));
        JButton Single = new JButton("Single");
        ImageIcon button2 = new ImageIcon(getClass().getResource("/images/singleplayerbutton.png"));
        Image button3 = button2.getImage().getScaledInstance((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4 + 45, 150, Image.SCALE_SMOOTH);
        Single.setIcon(new ImageIcon(button3));
        Single.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Load load = new Load();// create a load object. THis is the splash screen
                load.setVisible(true);
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        getThisJFrame().setVisible(false);//set visible false
                        single = new SinglePlayer(alliance, getThisJFrame());
                        single.setVisible(true);//when it is loadid set this jFrame visible
                        load.dispose();//clear the splash screen and free the memory
                    }
                });
            }
        });
        Single.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 3);
        Single.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 27) * 10, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 7);
        JButton Multi = new JButton("Multi");
        ImageIcon button = new ImageIcon(getClass().getResource("/images/multiplayerbutton.png"));
        Image button1 = button.getImage().getScaledInstance((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4 + 45, 150, Image.SCALE_SMOOTH);
        Multi.setIcon(new ImageIcon(button1));
        Multi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Load l = new Load();
                Server server = null;
                Random r = new Random();
                int port;
                Object[] options = {"Create Game",
                    "Join Game"};//this will be the button in th joptionPane
                int n = JOptionPane.showOptionDialog(getThisJFrame(),
                        "Would you like to create Game or to Join in another game?",
                        "MultiPlayer",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, //do not use a custom Icon
                        options, //the titles of buttons
                        options[0]); //default button title
                if (n == JOptionPane.YES_OPTION) {//if the user choose create game then

                    do {
                        port = r.nextInt(9000);
                        if (port > 4000) {//choose a random port between 4000 and 9000 
                            break;
                        }
                    } while (true);
                    try {
                        String ip = null;//search for the ip
                        try {
                            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();//get every ip mask etc
                            while (interfaces.hasMoreElements()) {
                                NetworkInterface iface = interfaces.nextElement();
                                // filters out 127.0.0.1 and inactive interfaces
                                if (iface.isLoopback() || !iface.isUp()) {
                                    continue;//if the ip is 127.0.0.1 
                                }

                                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                                while (addresses.hasMoreElements()) {
                                    InetAddress addr = addresses.nextElement();

                                    // *EDIT*
                                    if (addr instanceof Inet6Address) {
                                        continue;//if the ip is IPV6
                                    }

                                    if (iface.getDisplayName().contains("VirtualBox")) {
                                        continue;//if it isnt the physical address
                                    }
                                    ip = addr.getHostAddress();
                                    System.out.println(iface.getDisplayName() + " " + ip);
                                }
                            }
                        } catch (SocketException ef) {
                            throw new RuntimeException(ef);
                        }
                        System.out.println("Current IP address : " + ip);
                        System.out.println(port);
                        JOptionPane.showMessageDialog(getThisJFrame(), "Your Address " + ip + "\nYour Password  " + String.valueOf(port), "INFO", 1);//show the ip and the port of the server
                        l.run();//start the load screen
                        l.setVisible(true);
                        getThisJFrame().setVisible(false);
                        server = new Server(getThisJFrame(), port);
                        server.setVisible(true);
                        l.dispose();
                    } catch (SocketTimeoutException ex) {
                        l.dispose();
                        getThisJFrame().setVisible(true);
                    } catch (IOException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (n == JOptionPane.NO_OPTION) {// if he press join 
                    JTextField username = new JTextField();
                    JTextField password = new JPasswordField();
                    Object[] message = {// he must add  the ip and the port
                        "Address:", username,
                        "Password:", password
                    };

                    int option = JOptionPane.showConfirmDialog(getThisJFrame(), message, "Login", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {// if he press ok the the client programm start
                        Client client;

                        l.run();
                        l.setVisible(true);
                        getThisJFrame().setVisible(false);
                        int nym = 0;
                        try {
                            nym = Integer.parseInt(password.getText());// change the port from string to integer
                            client = new Client(getThisJFrame(), username.getText(), nym); //getThisJFrame().setVisible(false);
                            client.setVisible(true);
                            l.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(getThisJFrame(), "Invalid Input", "ERROR", JOptionPane.ERROR_MESSAGE);
                            getThisJFrame().dispose();
                            new MainMenu().setVisible(true);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(getThisJFrame(), "Invalid Input", "ERROR", JOptionPane.ERROR_MESSAGE);
                            getThisJFrame().dispose();
                            new MainMenu().setVisible(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(getThisJFrame(), "Canceled");
                    }
                }
                l.dispose();
            }
        });
        Multi.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 3);
        Multi.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 27) * 10, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 11);
        JButton Exit = new JButton("Exit");
        ImageIcon button4 = new ImageIcon(getClass().getResource("/images/exitbutton.png"));
        Image button5 = button4.getImage().getScaledInstance((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4 + 45, 150, Image.SCALE_SMOOTH);
        Exit.setIcon(new ImageIcon(button5));
        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        Exit.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 3);
        Exit.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 27) * 10, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 19);

        JButton Options = new JButton("Options");
        ImageIcon button8 = new ImageIcon(getClass().getResource("/images/optionbutton.png"));
        Image button9 = button8.getImage().getScaledInstance((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4 + 50, 150, Image.SCALE_SMOOTH);
        Options.setIcon(new ImageIcon(button9));
        Options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Red Alliance",
                    "Blue Alliance"};
                int n = JOptionPane.showOptionDialog(getThisJFrame(),
                        "Choose your Alliance",
                        "Options",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, //do not use a custom Icon
                        options, //the titles of buttons
                        options[0]); //default button title
                if (n == JOptionPane.YES_OPTION) {//if he choose red player
                    alliance = Alliance.RED;
                } else if (n == JOptionPane.NO_OPTION) {//if he choose blue player
                    alliance = Alliance.BLUE;
                }
            }
        });
        Options.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 3);
        Options.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 27) * 10, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 15);

        JButton Help = new JButton("Help");
        ImageIcon button6 = new ImageIcon(getClass().getResource("/images/helpbutton.png"));
        Image button7 = button6.getImage().getScaledInstance((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 9, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 20) * 2, Image.SCALE_SMOOTH);
        Help.setIcon(new ImageIcon(button7));
        Help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getThisJFrame(), "All movable pieces, with the exception of the Scout, may move only one step to any adjacent space vertically or horizontally (but not diagonally).\n A piece may not move onto a space occupied by a like color piece. "
                        + "\nBomb and Flag pieces are not moveable. The Scout may move any number of spaces in a straight line"
                        + " \n(such as the rook in chess). "
                        + "\nIn the older versions of Stratego the Scout could not move and strike in the same turn; in newer versions this was allowed. \nEven before that, sanctioned play usually amended the original Scout movement to allow moving and striking in the same turn because it facilitates gameplay. \nNo piece can move back and forth between the same two spaces"
                        + " \nfor more than three consecutive turns (two square rule),"
                        + "\n nor can a piece endlessly chase a piece it has no hope of capturing (more square rule).\n\n"
                        + "\nWhen the player wants to attack, they move their piece onto a square occupied by an opposing piece.\n Both players then reveal their piece's rank; the weaker piece (see exceptions below) is removed from the board. \nIf the engaging pieces are of equal rank, both are removed. \nA piece may not move onto a square already occupied unless it attacks."
                        + " \nTwo pieces have special attack powers. "
                        + "\nOne special piece is the Bomb which only Miners can defuse. "
                        + "\nIt immediately eliminates any other piece striking it, "
                        + "\nwithout itself being destroyed. Each player also has one Spy, which succeeds only if it attacks the Marshal, or the Flag. "
                        + "\nIf the Spy attacks any other piece, or is attacked by any piece (including the Marshal), the Spy is defeated. The original rules contained a provision that following a strike,\n the winning piece immediately occupies the space vacated by the losing piece. \nThis makes sense when the winning piece belongs to the player on move,"
                        + "\nbut no sense when the winning piece belongs to the player not on move. "
                        + "\nThe latter part of the rule has been quietly ignored in most play.", "Rules", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        Help.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 12, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 2);
        Help.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 27) * 22, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 27) * 22);

        jPanel1.add(Options);
        jPanel1.add(Exit);
        jPanel1.add(Single);
        jPanel1.add(Multi);
        jPanel1.add(Help);
    }

    public MainMenu getThisJFrame() {
        return this;
    }

    public static void music() {
        try {

            InputStream input = MainMenu.class.getResource("/music/4o_cut.wav").openStream();// load the music file
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(input);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);//start the audio
            clip.loop(Clip.LOOP_CONTINUOUSLY);//make the audio to play constandly in a loop

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                }
            });
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 212, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 155, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(49, 27, 212, 155);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(266, 27, 212, 155);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public void main() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // music();
                //        new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
