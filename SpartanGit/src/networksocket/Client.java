/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networksocket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import static javax.swing.SwingUtilities.updateComponentTreeUI;
import javax.swing.Timer;
import spartan.Alliance;
import spartan.board.Board;
import spartan.board.Move;
import spartan.board.Tile;
import spartan.gui.Load;
import spartan.gui.MainMenu;
import spartan.gui.SinglePlayer;
import spartan.pieces.Pawn;
import spartan.player.BluePlayer;
import spartan.player.MoveTransition;
import spartan.player.RedPlayer;

/**
 * This class contain the functions and the logic for the client on the game.
 * 
 * @author Pantelis Ypsilanti 2962 , Odysseas Zagoras 2902 , Theodoros Mosxos 2980
 */
public class Client extends JFrame implements Serializable {

    private JPanel jPanel1;
    private JLabel jLabel1;
    private BluePlayer bluePlayer;
    private RedPlayer redPlayer;
    private Board board = new Board();
    private Tile sourceTile;
    private Tile destinationTile;
    private Pawn MovedPawn;
    private int NumPanel;
    private boolean Start;
    private final Alliance alliance;
    private JPanel StartPane;//player hand panel
    private JPanel OpponentPane;//opponent graveyard panel
    private JPanel MovePane;
    ArrayList<TilePanel> tiles;
    ArrayList<TilePanel> tilesblue;//hand of the player
    ArrayList<TilePanel> tilesoppent;//hand of the opponent
    ArrayList<JLabel> moves;
    private int min1 = 4, sec1 = 60;
    private JLabel nith;
    private JLabel min;
    private JLabel sec;
    private JLabel message;
    Timer timer;
    boolean flag = true;//for timer
    boolean isClicked = false;//for start click
    boolean timeIsUp = false;
    boolean EndGame = false;
    private boolean Winner = false;
    private boolean Draw = false;
    private JPanel scrollPane1;
    private JScrollPane scrol;
    private boolean firstRound = false;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private DataInputStream indata;
    private DataOutputStream outdata;
    private Socket connection;
    private MainMenu main;
    private Load load;
    private Move move;

    public Client(MainMenu m, String ip, int port) throws IOException {
        main = m;
        connection = new Socket(ip, port);//try to make connection with the server 
        OutputStream outToServer = connection.getOutputStream();
        out = new ObjectOutputStream(outToServer);//create new stream
        InputStream inFromServer = connection.getInputStream();
        in = new ObjectInputStream(inFromServer);//create new in stream
        outdata = new DataOutputStream(connection.getOutputStream());//create new out data stream
        indata = new DataInputStream(connection.getInputStream());//create new in data stream
        initComponents();
        this.alliance = Alliance.BLUE;//client is always the blue player
        board.changeCurrentPlayer();//change the currentplayer of the board so the blue player can set his pawn on the board
        setTitle("Spartan");
        Start = true;
        sourceTile = null;
        destinationTile = null;
        MovedPawn = null;
        bluePlayer = board.getBluePlayer();//initialize the players
        redPlayer = board.getRedPlayer();
        Image logo = new ImageIcon(getClass().getResource("/spartan/Images/logo.png")).getImage();
        setAlwaysOnTop(true);
        setResizable(false);
        Image t = new ImageIcon(getClass().getResource("/spartan/Images/cursor.png")).getImage();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor cursor = toolkit.createCustomCursor(t, new Point(5, 5), "Custom Cursor");
        setCursor(cursor);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() + 10, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() + 5);
        jLabel1.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() + 30) / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 10);
        jPanel1.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() + 12) / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 20);

        ImageIcon background = new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource("/spartan/Images/map.png"));
        Image map = background.getImage().getScaledInstance((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() + 10) / 2),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() + 10), Image.SCALE_SMOOTH);
        jLabel1.setIcon(new ImageIcon(map));
        setIconImage(logo);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//set the frame so it cant be closed

        jPanel1.setLayout(new GridLayout(10, 10));
        jPanel1.setBackground(new Color(0, 0, 0, 0));// make jPanel1 transaparansy
        tiles = new ArrayList<>();//this is the arraylist with the tiles of the board and what they contain
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i * 10 + j == 42 || i * 10 + j == 43 || i * 10 + j == 46 || i * 10 + j == 47 || i * 10 + j == 52 || i * 10 + j == 53 || i * 10 + j == 56 || i * 10 + j == 57) {//if we are in a column
                    tiles.add(new TilePanel(i * 10 + j, null, 0));// Tilepanel is a new class which has a tile object and other informations
                    tiles.get(i * 10 + j).setLayout(new BorderLayout());
                    tiles.get(i * 10 + j).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    tiles.get(i * 10 + j).setBackground(new Color(0, 0, 0, 50));//set a soft black color in this tile

                } else {
                    tiles.add(new TilePanel(i * 10 + j, null, 0));
                    tiles.get(i * 10 + j).setLayout(new BorderLayout());
                    tiles.get(i * 10 + j).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    tiles.get(i * 10 + j).setBackground(new Color(0, 0, 0, 0));
                }
            }
        }
        for (int i = 99; i >= 0; i--) {//because client is a blue player we load the tiles on in jPanel up side down so we can watch from the lower side of the board
            jPanel1.add(tiles.get(i));
        }
        jPanel1.revalidate();
        jPanel1.repaint();
        setLocation(-3, 0);
        StartPane = new JPanel();
        if (!alliance.isBlue()) {
            StartPane.setBackground(new Color(204, 0, 0, 255));//make it red
        } else {
            StartPane.setBackground(new Color(0, 0, 204, 255));// make it blue
        }
        StartPane.setVisible(true);
        StartPane.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3);
        StartPane.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2 + 1, 0);
        StartPane.setLayout(new GridLayout(4, 10));
        JButton start = new JButton("Start");
        start.setEnabled(false);
        start.setPreferredSize(new Dimension(80, 80));
        tilesblue = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                if (!alliance.isBlue()) {// this part will be used later if we decide to make the client to can be red player too
                    tilesblue.add(new TilePanel(i * 10 + j, this.redPlayer.getPawnOfStack(i * 10 + j), 1));
                    this.redPlayer.setCordinateofPawn(i * 10 + j, (i * 10 + j + 1) * (-1), 1, !isClicked);
                    tilesblue.get(i * 10 + j).setLayout(new BorderLayout());
                    tilesblue.get(i * 10 + j).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    this.redPlayer.getPawnOfStack(i * 10 + j).setSide(true);
                    tilesblue.get(i * 10 + j).add(new JLabel(this.redPlayer.getPawnOfStack(i * 10 + j).getImage()));
                    tilesblue.get(i * 10 + j).setBackground(new Color(0, 0, 0, 0));
                } else {// for now 
                    tilesblue.add(new TilePanel(i * 10 + j, this.bluePlayer.getPawnOfStack(i * 10 + j), 1));//add new Tile panels with the pawns of the blue's hand
                    this.bluePlayer.setCordinateofPawn(i * 10 + j, (i * 10 + j + 1) * (-1), 1, !isClicked);
                    tilesblue.get(i * 10 + j).setLayout(new BorderLayout());
                    tilesblue.get(i * 10 + j).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    this.bluePlayer.getPawnOfStack(i * 10 + j).setSide(true);

                    tilesblue.get(i * 10 + j).add(new JLabel(this.bluePlayer.getPawnOfStack(i * 10 + j).getImage()));
                    tilesblue.get(i * 10 + j).setBackground(new Color(0, 0, 0, 0));
                }
                StartPane.add(tilesblue.get(i * 10 + j));
            }
        }
        start.setEnabled(true);
        OpponentPane = new JPanel();
        if (!alliance.isBlue()) {
            OpponentPane.setBackground(new Color(0, 0, 204, 255));
        } else {
            OpponentPane.setBackground(new Color(204, 0, 0, 255));
        }

        OpponentPane.setVisible(true);
        OpponentPane.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3);
        OpponentPane.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2 + 1, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3);
        OpponentPane.setLayout(new GridLayout(4, 10));
        tilesoppent = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                tilesoppent.add(new TilePanel(i * 10 + j, null, 2));
                tilesoppent.get(i * 10 + j).setLayout(new BorderLayout());
                tilesoppent.get(i * 10 + j).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                tilesoppent.get(i * 10 + j).setBackground(new Color(0, 0, 0, 0));
                OpponentPane.add(tilesoppent.get(i * 10 + j));
            }
        }
        JPanel OptionPane = new JPanel();
        OptionPane.setBackground(Color.DARK_GRAY);
        OptionPane.setVisible(true);
        OptionPane.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3);
        OptionPane.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2 + (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4 + 1), (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3) * 2);

        ImageIcon button = new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource("/spartan/Images/randombutton.png"));
        Image button1 = button.getImage().getScaledInstance((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 12, Image.SCALE_SMOOTH);

        JButton random = new JButton("Random");
        random.setIcon(new ImageIcon(button1));
        random.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 5, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 15));
        random.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isClicked || timeIsUp) {
                    return;//is the player hasnt press the start or if time is up for the board set do nothing
                }
                try {
                    Random(alliance);//call the random and create a random set of your pawn in the board
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton exit = new JButton("Exit");

        ImageIcon button4 = new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource("/spartan/Images/exitbutton.png"));
        Image button5 = button4.getImage().getScaledInstance((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 12, Image.SCALE_SMOOTH);
        exit.setIcon(new ImageIcon(button5));
        exit.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 5, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 15));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (isClicked) {//is the player has already press the start button
                    timer.stop();
                }

                try {
                    Close();//close the connection and everything else we need to close
                } catch (Exception ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                main.setVisible(true);//bring back the menu
                getThisJFrame().dispose();

            }
        });

        JButton help = new JButton("Help");

        ImageIcon button6 = new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource("/spartan/Images/helpbutton.png"));
        Image button7 = button6.getImage().getScaledInstance((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 12, Image.SCALE_SMOOTH);
        help.setIcon(new ImageIcon(button7));
        help.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 5, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 15));
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((board.getCurrentPlayer().getAlliance().isBlue() == alliance.isBlue()) && !Start) {//can activate this button only when we are in the main game and its the blue player turn
                    Move move = board.getCurrentPlayer().BotMove(board.getGameBoard());//choose a random move 
                    tiles.get(move.getCurrentCoordinate()).setBackground(new Color(255, 0, 0, 50));//change the color so the player can see it
                    tiles.get(move.getDestinationCoordinate()).setBackground(new Color(255, 0, 0, 50));
                    jPanel1.removeAll();//refresh the jPanel
                    for (int i = 99; i >= 0; i--) {
                        if (tiles.get(i).getPos() != move.getCurrentCoordinate() && tiles.get(i).getPos() != move.getDestinationCoordinate()) {
                            tiles.get(i).drawTile();
                        }
                        jPanel1.add(tiles.get(i));
                    }
                    jPanel1.validate();
                    jPanel1.repaint();
                    updateComponentTreeUI(getThisJFrame());

                }
            }
        });
        scrollPane1 = new JPanel();
        scrollPane1.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3) * 2 + (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 10));
        scrollPane1.setBackground(Color.yellow);
        scrollPane1.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3) * 2 + (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        moves = new ArrayList<>();
        OptionPane.add(random);
        OptionPane.add(help);
        OptionPane.add(exit);
        MovePane = new JPanel();
        MovePane.setBackground(Color.DARK_GRAY);
        MovePane.setVisible(true);
        MovePane.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 10);
        MovePane.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2 + 1, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3) * 2);
        min = new JLabel("0" + (min1 + 1));//create the timer variables
        sec = new JLabel("0" + (sec1 - 60));
        nith = new JLabel(":");
        if ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() > 1500) {//initialiaze the size of the fond depense the size of the screen
            nith.setFont(new Font("Serif", Font.BOLD, 30));
            min.setFont(new Font("Serif", Font.BOLD, 30));
            sec.setFont(new Font("Serif", Font.BOLD, 30));
            message = new JLabel(" Your Turn => ");
            message.setFont(new Font("Serif", Font.BOLD, 30));
        } else {
            nith.setFont(new Font("Serif", Font.BOLD, 20));
            min.setFont(new Font("Serif", Font.BOLD, 20));
            sec.setFont(new Font("Serif", Font.BOLD, 20));
            message = new JLabel(" Your Turn => ");
            message.setFont(new Font("Serif", Font.BOLD, 20));
        }
        nith.setPreferredSize(new Dimension(23, 23));
        min.setPreferredSize(new Dimension(50, 50));
        sec.setPreferredSize(new Dimension(50, 50));
        MovePane.add(start);
        MovePane.add(message);
        MovePane.add(min);
        MovePane.add(nith);
        MovePane.add(sec);

        sec.setForeground(Color.WHITE);
        min.setForeground(Color.WHITE);
        nith.setForeground(Color.WHITE);
        message.setForeground(Color.WHITE);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isClicked) {
                    start.setEnabled(false);
                    try {
                        outdata.writeBoolean(true);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        isClicked = (boolean) indata.readBoolean();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    start.setText("Ready");
                    timer = new Timer(1000, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {//that actions are done every second
                            if (board.boardFull(alliance)) {//check if every pawn is set
                                start.setEnabled(true);//if yes activate the ready choise button
                                start.setBackground(Color.green);
                            } else {
                                start.setEnabled(false);
                            }
                            sec.setForeground(Color.WHITE);
                            min.setForeground(Color.WHITE);
                            nith.setForeground(Color.WHITE);
                            message.setForeground(Color.WHITE);
                            if (sec1 == 0) {//when the seconds ready the zero
                                sec1 = 60;
                                min1--;
                            }

                            if (min1 < 0) {//if the minites are less than 0 then time is up
                                if (!board.boardFull(alliance) && Start) {
                                    try {
                                        Random(alliance);
                                    } catch (IOException ex) {
                                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                if (Start) {
                                    start.setEnabled(false);
                                    try {
                                        out.writeObject(bluePlayer.getActivePawns());// send the final set of the board to the opponent
                                        Random(Alliance.RED);//initialize the opponent set on the board
                                    } catch (IOException ex) {
                                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    timeIsUp = true;
                                    min1 = 1;
                                    sec1 = 60;
                                    min.setText("0" + (min1 + 1));
                                    sec.setText("0" + (sec1 - 60));
                                    message.setText("Opponent Turn =>");
                                    MovePane.remove(start);

                                    Start = false;
                                    timer.start();

                                    GamePlay();//update the valid moves of the players and check if we have reach in the end of the game
                                    board.changeCurrentPlayer();//change the current player because red must play first
                                    firstRound = true;

                                } else {

                                    EndGame = true;//if time is up then someone have lose because he didnt make a move
                                    timer.stop();

                                    if (board.getCurrentPlayer().getAlliance().isBlue() == alliance.isBlue()) {//if it was  blue player's turn
                                        JOptionPane.showMessageDialog(getThisJFrame(), "You Lose.... Time is up", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                                        updateComponentTreeUI(getThisJFrame());

                                    } else {//if it was red player's turn
                                        JOptionPane.showMessageDialog(getThisJFrame(), "You Win!! Time is up for the opponent.", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                                        updateComponentTreeUI(getThisJFrame());
                                    }
                                }
                            } else {
                                if (!Start) {//if we are in the main game 
                                    if (firstRound) {//if we are in the first turn
                                        try {
                                            if (indata.readInt() == 0) {//if the input data is 0 the do nothing
                                            } else {
                                                firstRound = false;
                                                OpponentMove();//opponent has just send his move. go to execute it
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (ClassNotFoundException ex) {
                                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } else {
                                        if (board.getCurrentPlayer().getAlliance().isBlue() == alliance.isBlue()) {
                                            try {
                                                outdata.writeInt(0);// send a 0 every second in your turn until you make a move
                                            } catch (IOException ex) {
                                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        } else {
                                            try {
                                                if (indata.readInt() != 0) {//when the indata wont be 0 go to make the opponent move
                                                    OpponentMove();
                                                }
                                            } catch (IOException ex) {
                                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                            } catch (ClassNotFoundException ex) {
                                                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                }
                                sec1--;
                                if (sec1 < 10) {
                                    sec.setText("0" + sec1);
                                    flag = false;
                                }
                                if (min1 < 10) {
                                    min.setText("0" + min1);
                                    if (sec1 < 10) {
                                        sec.setText("0" + sec1);
                                    } else {
                                        sec.setText("" + sec1);
                                    }
                                    flag = false;
                                }
                                if (flag) {
                                    sec.setText("" + sec1);
                                    min.setText("" + min1);
                                }
                            }
                        }

                    });
                    timer.start();
                } else {// when the player click ready 
                    start.setEnabled(false);
                    try {
                        out.writeObject(bluePlayer.getActivePawns());//set the arraylist with the active pawns with their final cordinates
                        Random(Alliance.RED);//take the opponent hand 
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    timeIsUp = true;
                    min1 = 1;
                    sec1 = 60;
                    min.setText("0" + (min1 + 1));
                    sec.setText("0" + (sec1 - 60));
                    message.setText("Opponent Turn =>");
                    MovePane.remove(start);

                    Start = false;
                    timer.start();

                    GamePlay();// update the legal moves and check if we have a winner
                    board.changeCurrentPlayer();// change the current player
                    firstRound = true;
                }
            }
        });

        add(MovePane);
        add(StartPane);
        add(OpponentPane);
        add(OptionPane);
        add(scrollPane1);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        // jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 0, 0));
        setMinimumSize(new java.awt.Dimension(500, 500));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 880, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 650, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 880, 650);

        jLabel1.setBackground(new java.awt.Color(204, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/spartan/Images/map.png"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 880, 650);

        pack();
    }// </editor-fold>

    private JFrame getThisJFrame() {
        return this;
    }

    private void setCurrentMove(Move move) {
        this.move = move;
    }

    /**
     * use this function to terminate the conection between server and client
     * @throws IOException 
     */
    public void Close() throws IOException {
        if (isClicked) {
            timer.stop();
        }
        out.close();
        in.close();
        connection.close();
    }

    /**
     * Update the legal moves and check if we have a winner.
     */
    private void GamePlay() {
        this.board.getCurrentPlayer().calculateLegalMoves(board);
        this.board.getCurrentPlayer().getOpponent().calculateLegalMoves(board);
        if (redPlayer.getAvailablePawns().isEmpty() && bluePlayer.getAvailablePawns().isEmpty()) {// if both players dont have any move to make
            Draw = true;
            EndGame = true;
        } else if (redPlayer.getAvailablePawns().isEmpty()) {//if red player, the opponent has no move to make
            EndGame = true;

            Winner = true;

        } else if (bluePlayer.getAvailablePawns().isEmpty()) {//if blue player, you, have no moves to make

            EndGame = true;

            Winner = false;
        }
    }

    /**
     * Create random set on the board for the client or 
     * copy the set on the board of the server, depense the alliance.
     * 
     * @param a is the alliance 
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void Random(Alliance a) throws IOException, ClassNotFoundException {
        ArrayList<Pawn> rand = new ArrayList<>();
        int min, max;
        if (a.isBlue()) {
            rand = bluePlayer.Random();
            min = 0;//the minimun position on the board
            max = 40;//the maximun position on the board
            StartPane.removeAll();
            for (int i = 0; i < 40; i++) {
                tilesblue.set(i, new TilePanel(i, null, 1));
            }
            for (final TilePanel boardTile : tilesblue) {
                StartPane.add(boardTile);
            }
            StartPane.validate();
            StartPane.repaint();
            StartPane.updateUI();
        } else {
            rand = (ArrayList<Pawn>) in.readObject();//read the opponent arrayling 
            for (Pawn pawn : rand) {
                pawn.setSide(false);//set the opponent side
            }
            redPlayer.setStack(rand);

            for (Pawn pawn : rand) {
                this.board.setPawnOnBoard(pawn.getPositionOfPawn(), pawn);//set every pawn on the board
            }
            min = 60;
            max = 100;
        }

        jPanel1.removeAll();//refresh the jPanel ( board )
        for (int i = min; i < max; i++) {
            tiles.remove(rand.get(i - min).getPositionOfPawn());
            tiles.add(rand.get(i - min).getPositionOfPawn(), new TilePanel(rand.get(i - min).getPositionOfPawn(), rand.get(i - min), 0));
            tiles.get(rand.get(i - min).getPositionOfPawn()).add(new JLabel(rand.get(i - min).getImage()));
        }
        for (int i = 99; i >= 0; i--) {
            jPanel1.add(tiles.get(i));
        }

        jPanel1.validate();
        jPanel1.repaint();
        jPanel1.updateUI();
        updateComponentTreeUI(getThisJFrame());

    }

    /**
     * This is used to make the move of the opponent
     * 
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void OpponentMove() throws IOException, ClassNotFoundException {

        GamePlay();//update the legal moves and check for winner
        final Move move = (Move) in.readObject();//read the opponent move 
        final MoveTransition transition = board.getCurrentPlayer().makeMove(move);
        board = transition.getToBoard();//update the board
        TilePanel temp = tiles.get(move.getDestinationCoordinate());
        if (temp.getPawn() == null) {// update the tiles arraylist if the destination tile isnt occupied
            tiles.remove(move.getDestinationCoordinate());
            tiles.add(move.getDestinationCoordinate(), new TilePanel(move.getDestinationCoordinate(), move.getPawn(), 0));
        } else {//if it is occupied
            Pawn winner = board.Conflict(move.getPawn(), temp.getPawn());//check the winner of the conflict
            move.getPawn().setSide(true);//make both pawns to be flipped up
            temp.getPawn().setSide(true);
            if (winner == null) {// if the is no winner then both go out
                tiles.remove(move.getDestinationCoordinate());
                tiles.add(move.getDestinationCoordinate(), new TilePanel(move.getDestinationCoordinate(), null, 0));
                board.getCurrentPlayer().getOpponent().DeletePawn(temp.getPawn());
                board.getCurrentPlayer().DeletePawn(move.getPawn());
                OpponentPane.removeAll();//update the opponentPnae
                int k = move.getPawn().getTablePos();
                tilesoppent.set(k, new TilePanel(k, move.getPawn(), 1));
                tilesoppent.get(k).add(new JLabel(move.getPawn().getImage()));
                for (final TilePanel boardTile : tilesoppent) {
                    if (boardTile.getPos() == k) {
                        OpponentPane.add(boardTile);
                    } else {
                        boardTile.drawTile();
                        OpponentPane.add(boardTile);
                    }

                }
                OpponentPane.validate();
                OpponentPane.updateUI();
                updateComponentTreeUI(getThisJFrame());

                StartPane.removeAll();
                int i = temp.getPawn().getTablePos();

                tilesblue.set(i, new TilePanel(i, temp.getPawn(), 1));
                tilesblue.get(i).add(new JLabel(temp.getPawn().getImage()));
                for (final TilePanel boardTile : tilesblue) {
                    if (boardTile.getPos() == i) {
                        temp.getPawn().setCordinateOfPawn(-1);
                        StartPane.add(boardTile);
                    } else {
                        boardTile.drawTile();
                    }
                    StartPane.add(boardTile);
                }
                StartPane.validate();
                StartPane.updateUI();
                updateComponentTreeUI(getThisJFrame());

            } else if (winner == move.getPawn()) {
                tiles.remove(move.getDestinationCoordinate());
                tiles.add(move.getDestinationCoordinate(), new TilePanel(move.getDestinationCoordinate(), move.getPawn(), 0));
                board.getCurrentPlayer().getOpponent().DeletePawn(temp.getPawn());
                StartPane.removeAll();
                int i = temp.getPawn().getTablePos();
                tilesblue.set(i, new TilePanel(i, temp.getPawn(), 1));
                tilesblue.get(i).add(new JLabel(temp.getPawn().getImage()));
                for (final TilePanel boardTile : tilesblue) {
                    if (boardTile.getPos() == i) {
                        temp.getPawn().setCordinateOfPawn(-1);
                        StartPane.add(boardTile);
                    } else {
                        boardTile.drawTile();
                        StartPane.add(boardTile);
                    }
                }
                StartPane.validate();
                StartPane.updateUI();
                updateComponentTreeUI(getThisJFrame());

            } else {
                tiles.remove(move.getDestinationCoordinate());
                tiles.add(move.getDestinationCoordinate(), new TilePanel(move.getDestinationCoordinate(), temp.getPawn(), 0));

                board.getCurrentPlayer().DeletePawn(move.getPawn());
                OpponentPane.removeAll();
                int k = move.getPawn().getTablePos();
                tilesoppent.set(k, new TilePanel(k, move.getPawn(), 1));
                tilesoppent.get(k).add(new JLabel(move.getPawn().getImage()));
                for (final TilePanel boardTile : tilesoppent) {
                    if (boardTile.getPos() == k) {
                        //          move.getPawn().setCordinateOfPawn(-1);
                        OpponentPane.add(boardTile);
                    } else {
                        boardTile.drawTile();
                        OpponentPane.add(boardTile);

                    }
                }
                OpponentPane.validate();
                OpponentPane.updateUI();
                updateComponentTreeUI(getThisJFrame());

            }
            if (temp.getPawn().getValue() == -1) {
                EndGame = true;
                Winner = false;

            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    drawBoard(move.getCurrentCoordinate(), move.getDestinationCoordinate(), 0, false);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SinglePlayer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                board.toStringBoard();
                board.changeCurrentPlayer();
                GamePlay();

            }
        });
    }

    private void drawBoard(int a, int b, int numPanel, boolean swap) throws InterruptedException, IOException, ClassNotFoundException {

        if (!Start) {
            scrollPane1.removeAll();
            if ((board.getCurrentPlayer().getAlliance().isBlue() == alliance.isBlue())) {
                String s = new String("You: From " + (a + 1) + " To " + (b + 1) + "          ");
                JLabel tem = new JLabel(s);
                tem.setForeground(Color.RED);
                if ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() < 1500) {
                    tem.setFont(new Font("Serif", Font.BOLD, 13));
                } else {
                    tem.setFont(new Font("Serif", Font.BOLD, 18));
                }
                moves.add(0, tem);
            } else {
                String s = new String("Opponent: From " + (a + 1) + " To " + (b + 1) + "          ");
                JLabel tem = new JLabel(s);
                tem.setForeground(Color.BLUE);
                if ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() < 1500) {
                    tem.setFont(new Font("Serif", Font.BOLD, 13));
                } else {
                    tem.setFont(new Font("Serif", Font.BOLD, 18));
                }
                moves.add(0, tem);
            }
            for (JLabel temp : moves) {
                scrollPane1.add(temp);
            }
            scrollPane1.validate();
            scrollPane1.repaint();
            scrollPane1.updateUI();
            updateComponentTreeUI(this);

        }
        if (numPanel == 1) {
            StartPane.remove(a);
            StartPane.add(new TilePanel(a, null, 1), a);
            tilesblue.set(a, new TilePanel(a, null, 1));
            StartPane.validate();
            StartPane.repaint();
            StartPane.updateUI();
        } else {
            if (!swap) {
                tiles.set(a, new TilePanel(a, null, 0));
                board.setPawnOnBoard(a, null);
                if(tiles.get(b).getPawn() == null){
                    System.out.println("mpika gia null");
                    board.setPawnOnBoard(b, null);
                }
            } else {
                board.toStringBoard();
            }
        }
        jPanel1.removeAll();
        for (int i = 99; i >= 0; i--) {
            if (!swap) {
                if (tiles.get(i).getPos() == b || tiles.get(i).getPos() == a) {
                    if (tiles.get(i).getPawn() != null) {
                        tiles.get(i).add(new JLabel(tiles.get(i).getPawn().getImage()));
                        if (tiles.get(i).getPos() == b) {
                            tiles.get(i).getTile().getPawn().setCordinateOfPawn(b);
                            board.setPawnOnBoard(b, tiles.get(i).getPawn());

                        } else {
                            tiles.get(i).getTile().getPawn().setCordinateOfPawn(a);
                            board.setPawnOnBoard(a, tiles.get(i).getPawn());
                        }
                    }
                } else {
                    tiles.get(i).drawTile();
                }
                jPanel1.add(tiles.get(i));
                jPanel1.validate();
                jPanel1.updateUI();
                updateComponentTreeUI(this);
            } else {
                if (tiles.get(i).getPos() == b || tiles.get(i).getPos() == a) {
                    if (tiles.get(i).getPawn() != null) {
                        tiles.get(i).add(new JLabel(tiles.get(i).getPawn().getImage()));
                        if (tiles.get(i).getPos() == b) {
                            tiles.get(i).getTile().getPawn().setCordinateOfPawn(b);
                            board.setPawnOnBoard(b, tiles.get(i).getPawn());

                        } else {
                            tiles.get(i).getTile().getPawn().setCordinateOfPawn(a);
                            board.setPawnOnBoard(a, tiles.get(i).getPawn());
                        }
                    }
                } else {
                    tiles.get(i).drawTile();
                }
                jPanel1.add(tiles.get(i));
            }

        }
        jPanel1.validate();
        jPanel1.updateUI();
        updateComponentTreeUI(this);

        if (!Start) {
            GamePlay();
            if (!board.getCurrentPlayer().getAlliance().isBlue()) {
                tiles.get(a).setBackground(new Color(0, 255, 0, 50));
                tiles.get(a).setBorder(BorderFactory.createLineBorder(Color.RED));
                tiles.get(b).setBackground(new Color(0, 255, 0, 50));
                tiles.get(b).setBorder(BorderFactory.createLineBorder(Color.RED));
                CheckEnd();
            }

        }
    }

    private void CheckEnd() {
        if (!Start) {
            GamePlay();
            if (!(board.getCurrentPlayer().getAlliance().isBlue())) {
                if (EndGame) {
                    timer.stop();
                    if (Draw) {
                        JOptionPane.showMessageDialog(getThisJFrame(), "Draw", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                        updateComponentTreeUI(getThisJFrame());

                    } else {
                        if (Winner) {
                            JOptionPane.showMessageDialog(getThisJFrame(), "You Win", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                            updateComponentTreeUI(getThisJFrame());

                        } else {
                            JOptionPane.showMessageDialog(getThisJFrame(), "You Lose", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                            updateComponentTreeUI(getThisJFrame());

                        }
                    }

                } else {
                    min1 = 1;
                    sec1 = 60;
                    min.setText("0" + (min1 + 1));
                    sec.setText("0" + (sec1 - 60));
                    message.setText("Your Turn => ");
                }
            } else {
                if (EndGame) {
                    timer.stop();
                    if (Draw) {
                        JOptionPane.showMessageDialog(getThisJFrame(), "Draw", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                        updateComponentTreeUI(getThisJFrame());

                    } else {
                        if (Winner) {
                            JOptionPane.showMessageDialog(getThisJFrame(), "You Win", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                            updateComponentTreeUI(getThisJFrame());

                        } else {
                            JOptionPane.showMessageDialog(getThisJFrame(), "You Lose", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                            updateComponentTreeUI(getThisJFrame());

                        }
                    }

                } else {
                    min1 = 1;
                    sec1 = 60;
                    min.setText("0" + (min1 + 1));
                    sec.setText("0" + (sec1 - 60));
                    message.setText("Opponent Turn => ");
                }
            }
        }
    }

    private void highlightStartTiles() {
        if (alliance.isBlue()) {
            for (int i = 0; i < 40; i++) {
                if (!board.getTile(i).isTileOccupied()) {
                    tiles.get(i).setBackground(new Color(0, 0, 204, 50));
                }
            }
        } else {
            for (int i = 60; i < 100; i++) {
                if (!board.getTile(i).isTileOccupied()) {
                    tiles.get(i).setBackground(new Color(204, 0, 0, 50));
                }
            }
        }
        jPanel1.validate();
        jPanel1.updateUI();
        updateComponentTreeUI(this);

    }

    private void drawStartPanel(int a, int b, final Pawn pawn, int p) {
        if (p == 1) {
            tilesblue.set(a, new TilePanel(a, null, 1));
            StartPane.remove(a);
            StartPane.add(tilesblue.get(a), a);

            StartPane.validate();
            StartPane.repaint();
            StartPane.updateUI();
        } else if (p == 0) {
            jPanel1.removeAll();
            tiles.set(a, new TilePanel(a, null, 0));
            for (final TilePanel boardTile : tiles) {
                jPanel1.add(boardTile);
            }
            board.setPawnOnBoard(a, null);
            jPanel1.validate();
            jPanel1.updateUI();
            board.toStringBoard();
        }
        StartPane.removeAll();
        tilesblue.set(b, new TilePanel(b, pawn, 1));
        tilesblue.get(b).add(new JLabel(pawn.getImage()));
        for (final TilePanel boardTile : tilesblue) {
            if (boardTile.getPos() == b) {
                 this.bluePlayer.setCordinateofPawn(a, (b + 1) * (-1), p, !isClicked);
                StartPane.add(boardTile);
            } else {
                boardTile.drawTile();
            }
            StartPane.add(boardTile);
        }
        StartPane.validate();
        StartPane.updateUI();
        updateComponentTreeUI(this);
    }

    public class TilePanel extends JPanel {

        private Tile tile;

        public TilePanel(int pos, Pawn p, int numberOfPanel) {
            super();
            tile = new Tile(pos, p);
            setBackground(new Color(0, 0, 0, 0));
            setLayout(new BorderLayout());
            this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (Start) {
                        if (!isClicked || timeIsUp) {
                            return;
                        }
                    } else {
                        if (!(board.getCurrentPlayer().getAlliance().isBlue() == alliance.isBlue()) || EndGame) {
                            return;
                        }
                    }
                    if (numberOfPanel == 1) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (sourceTile != null) {
                                getThis().unhighLighted(sourceTile.getTileCordinates(), NumPanel, MovedPawn);
                                for (int i = 0; i < 100; i++) {
                                    tiles.get(i).drawTile();
                                }
                            }
                            sourceTile = null;
                            destinationTile = null;
                            MovedPawn = null;
                            NumPanel = -1;

                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            if (sourceTile == null) {
                                if (Start) {
                                    sourceTile = tilesblue.get(getPos()).getTile();
                                    NumPanel = numberOfPanel;
                                    MovedPawn = sourceTile.getPawn();
                                    if (MovedPawn == null) {
                                        sourceTile = null;
                                        NumPanel = -1;
                                    } else {
                                        highlightStartTiles();
                                        getThis().highLighted();
                                    }
                                }
                            } else {
                                destinationTile = tilesblue.get(getPos()).getTile();
                                if (!tilesblue.get(destinationTile.getTileCordinates()).getTile().isTileOccupied()) {
                                    if (Start) {
                                       SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                drawStartPanel(sourceTile.getTileCordinates(), destinationTile.getTileCordinates(), MovedPawn, NumPanel);
                                                sourceTile = null;
                                                destinationTile = null;
                                                MovedPawn = null;
                                                NumPanel = -1;
                                            }
                                        });

                                        for (TilePanel tile : tiles) {
                                            tile.drawTile();
                                        }
                                    }
                                }
                            }
                        }
                    } else if (numberOfPanel == 0) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (sourceTile != null) {
                                getThis().unhighLighted(sourceTile.getTileCordinates(), NumPanel, MovedPawn);
                                for (int i = 0; i < 100; i++) {
                                    tiles.get(i).drawTile();
                                }
                            }
                            sourceTile = null;
                            destinationTile = null;
                            MovedPawn = null;
                            NumPanel = -1;
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            if (sourceTile == null) {
                                if (Start) {
                                    sourceTile = tiles.get(getPos()).getTile();
                                    MovedPawn = sourceTile.getPawn();
                                    NumPanel = 0;
                                    if (MovedPawn == null) {
                                        sourceTile = null;
                                        NumPanel = -1;
                                    } else {
                                        getThis().highLighted();
                                    }

                                } else {
                                    sourceTile = board.getTile(getPos());
                                    NumPanel = 0;
                                    MovedPawn = sourceTile.getPawn();
                                    if (MovedPawn == null || !board.getCurrentPlayer().getAvailablePawns().contains(MovedPawn)) {
                                        sourceTile = null;
                                        NumPanel = -1;
                                    } else {
                                        getThis().highLighted();
                                        for (Move move : board.getCurrentPlayer().getLegalMoves()) {
                                            if (move.getCurrentCoordinate() == getThis().getPos()) {
                                                tiles.get(move.getDestinationCoordinate()).setBackground(new Color(255, 255, 0, 50));
                                            }
                                        }
                                        updateComponentTreeUI(getThisJFrame());
                                    }
                                }
                            } else {
                                destinationTile = board.getTile(getPos());
                                if (Start) {
                                    if (!tiles.get(destinationTile.getTileCordinates()).getTile().isTileOccupied()) {
                                        if (board.getCurrentPlayer().getStart().contains(destinationTile.getTileCordinates())) {
                                          tiles.remove(destinationTile.getTileCordinates());
                                            tiles.add(destinationTile.getTileCordinates(), new TilePanel(destinationTile.getTileCordinates(), sourceTile.getPawn(), 0));
                                            SwingUtilities.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        drawBoard(sourceTile.getTileCordinates(), destinationTile.getTileCordinates(), NumPanel, false);
                                                    } catch (InterruptedException ex) {
                                                        Logger.getLogger(SinglePlayer.class.getName()).log(Level.SEVERE, null, ex);
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                                    } catch (ClassNotFoundException ex) {
                                                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                    sourceTile = null;
                                                    destinationTile = null;
                                                    MovedPawn = null;
                                                    NumPanel = -1;
                                                }
                                            });
                                            for (TilePanel tile : tiles) {
                                                tile.drawTile();
                                            }
                                        }
                                    } else {
                                        if (NumPanel == 0) {
                                            TilePanel temporary = tiles.get(destinationTile.getTileCordinates());
                                            tiles.remove(destinationTile.getTileCordinates());
                                            tiles.add(destinationTile.getTileCordinates(), new TilePanel(destinationTile.getTileCordinates(), sourceTile.getPawn(), 0));
                                            tiles.remove(sourceTile.getTileCordinates());
                                            tiles.add(sourceTile.getTileCordinates(), new TilePanel(sourceTile.getTileCordinates(), temporary.getPawn(), 0));
                                           try {
                                                drawBoard(sourceTile.getTileCordinates(), destinationTile.getTileCordinates(), NumPanel, true);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                            } catch (IOException ex) {
                                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                            } catch (ClassNotFoundException ex) {
                                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            SwingUtilities.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    sourceTile = null;
                                                    destinationTile = null;
                                                    MovedPawn = null;
                                                    NumPanel = -1;
                                                }
                                            });
                                        }
                                    }
                                } else {
                                     final Move move = Move.MoveFactory.createMove(board, sourceTile.getTileCordinates(), destinationTile.getTileCordinates());
                                    setCurrentMove(move);
                                    board.toStringBoard();
                                    final MoveTransition transition = board.getCurrentPlayer().makeMove(move);
                                    if (transition.getMoveStatus().isDone()) {
                                         TilePanel temp = tiles.get(move.getDestinationCoordinate());
                                        if (temp.getPawn() == null) {
                                            tiles.remove(move.getDestinationCoordinate());
                                            tiles.add(move.getDestinationCoordinate(), new TilePanel(move.getDestinationCoordinate(), move.getPawn(), 0));
                                        } else {
                                            Pawn winner = board.Conflict(move.getPawn(), temp.getPawn());
                                            move.getPawn().setSide(true);
                                            temp.getPawn().setSide(true);
                                           if (winner == null) {
                                                tiles.remove(move.getDestinationCoordinate());
                                                tiles.add(move.getDestinationCoordinate(), new TilePanel(move.getDestinationCoordinate(), null, 0));
                                                board.getCurrentPlayer().getOpponent().DeletePawn(temp.getPawn());
                                                board.getCurrentPlayer().DeletePawn(move.getPawn());
                                                OpponentPane.removeAll();
                                                int i = temp.getPawn().getTablePos();
                                                tilesoppent.set(i, new TilePanel(i, temp.getPawn(), 1));
                                                tilesoppent.get(i).add(new JLabel(temp.getPawn().getImage()));
                                                for (final TilePanel boardTile : tilesoppent) {
                                                    if (boardTile.getPos() == i) {
                                                        temp.getPawn().setCordinateOfPawn(-1);
                                                        OpponentPane.add(boardTile);
                                                    } else {
                                                        boardTile.drawTile();
                                                        OpponentPane.add(boardTile);

                                                    }
                                                }
                                                OpponentPane.validate();
                                                OpponentPane.updateUI();
                                                updateComponentTreeUI(getThisJFrame());
                                                StartPane.removeAll();
                                                int k = move.getPawn().getTablePos();
                                                tilesblue.set(k, new TilePanel(k, move.getPawn(), 1));
                                                tilesblue.get(k).add(new JLabel(move.getPawn().getImage()));
                                                for (final TilePanel boardTile : tilesblue) {
                                                    if (boardTile.getPos() == k) {
                                                        move.getPawn().setCordinateOfPawn(-1);
                                                        StartPane.add(boardTile);
                                                    } else {
                                                        boardTile.drawTile();
                                                        StartPane.add(boardTile);
                                                    }

                                                }
                                                StartPane.validate();
                                                StartPane.updateUI();
                                                updateComponentTreeUI(getThisJFrame());

                                            } else if (winner == move.getPawn()) {
                                                tiles.remove(move.getDestinationCoordinate());
                                                tiles.add(move.getDestinationCoordinate(), new TilePanel(move.getDestinationCoordinate(), move.getPawn(), 0));
                                                board.getCurrentPlayer().getOpponent().DeletePawn(temp.getPawn());
                                                OpponentPane.removeAll();
                                                int i = temp.getPawn().getTablePos();
                                                tilesoppent.set(i, new TilePanel(i, temp.getPawn(), 1));
                                                tilesoppent.get(i).add(new JLabel(temp.getPawn().getImage()));
                                                for (final TilePanel boardTile : tilesoppent) {
                                                    if (boardTile.getPos() == i) {
                                                        temp.getPawn().setCordinateOfPawn(-1);
                                                        OpponentPane.add(boardTile);
                                                    } else {
                                                        boardTile.drawTile();
                                                        OpponentPane.add(boardTile);
                                                    }

                                                }
                                                OpponentPane.validate();
                                                OpponentPane.updateUI();
                                                updateComponentTreeUI(getThisJFrame());
                                            } else {
                                                tiles.remove(move.getDestinationCoordinate());
                                                tiles.add(move.getDestinationCoordinate(), new TilePanel(move.getDestinationCoordinate(), temp.getPawn(), 0));
                                                board.getCurrentPlayer().DeletePawn(move.getPawn());
                                                StartPane.removeAll();
                                                int k = move.getPawn().getTablePos();
                                                tilesblue.set(k, new TilePanel(k, move.getPawn(), 1));
                                                tilesblue.get(k).add(new JLabel(move.getPawn().getImage()));
                                                for (final TilePanel boardTile : tilesblue) {
                                                    if (boardTile.getPos() == k) {
                                                        move.getPawn().setCordinateOfPawn(-1);
                                                        StartPane.add(boardTile);
                                                    } else {
                                                        boardTile.drawTile();
                                                        StartPane.add(boardTile);
                                                    }

                                                }
                                                StartPane.validate();
                                                StartPane.updateUI();
                                                updateComponentTreeUI(getThisJFrame());

                                            }
                                            if (temp.getPawn().getValue() == -1) {
                                                Winner = true;
                                                EndGame = true;
                                                //                         return;
                                            }
                                        }
                                        try {
                                            drawBoard(sourceTile.getTileCordinates(), destinationTile.getTileCordinates(), NumPanel, false);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IOException ex) {
                                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (ClassNotFoundException ex) {
                                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    outdata.writeInt(1);
                                                    out.writeObject(move);
                                                    CheckEnd();
                                                    board.changeCurrentPlayer();
                                                } catch (IOException ex) {
                                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                sourceTile = null;
                                                destinationTile = null;
                                                MovedPawn = null;
                                                NumPanel = -1;
                                                board.toStringBoard();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent me) {
                    return;
                }

                @Override
                public void mouseReleased(MouseEvent me) {
                    return;
                }

                @Override
                public void mouseEntered(MouseEvent me) {
                    return;
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    return;
                }
            }
            );

        }

        /*public JPanel replaceLabel(){
            JPanel a  = new JPanel();
            a.add(new JLabel(new ImageIcon(getClass().getResource("/spartan/Images/red.gif"))));
            return a;
        }
         */
        public Tile getTile() {
            return this.tile;
        }

        public int getPos() {
            return this.tile.getTileCordinates();
        }

        private TilePanel getThis() {
            return this;
        }

        public Pawn getPawn() {
            return this.tile.getPawn();
        }

        public void highLighted() {
            this.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            this.setBackground(Color.CYAN);
            validate();
            repaint();
            updateUI();
        }

        public void unhighLighted(int a, int p, Pawn pawn) {
            if (p == 1) {
                StartPane.remove(a);
                tilesblue.set(a, new TilePanel(a, pawn, 1));
                tilesblue.get(a).setLayout(new BorderLayout());
                tilesblue.get(a).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                tilesblue.get(a).add(new JLabel(pawn.getImage()));
                tilesblue.get(a).setBackground(new Color(0, 0, 0, 0));
                StartPane.add(tilesblue.get(a), a);
                StartPane.validate();
                StartPane.repaint();
                StartPane.updateUI();
            } else if (p == 0) {
                jPanel1.remove(99 - a);
                tiles.set(a, new TilePanel(a, pawn, 0));
                tiles.get(a).setLayout(new BorderLayout());
                tiles.get(a).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                tiles.get(a).add(new JLabel(pawn.getImage()));
                tiles.get(a).setBackground(new Color(0, 0, 0, 0));
                jPanel1.add(tiles.get(a), 99 - a);
                jPanel1.validate();
                jPanel1.repaint();
                jPanel1.updateUI();
                /* jPanel1.removeAll();
                tiles.set(a, new TilePanel(a, pawn, Start, 0));
                tiles.get(a).add(new JLabel(pawn.getFront()));
                for (final TilePanel boardTile : tiles) {
                    boardTile.drawTile();
                    jPanel1.add(boardTile);
                }
                jPanel1.validate();
                jPanel1.updateUI();*/
            }
            validate();
            repaint();
            updateUI();
            updateComponentTreeUI(getThisJFrame());
        }

        void drawTile() {
            if (!(getPos() == 42 || getPos() == 43 || getPos() == 46 || getPos() == 47
                    || getPos() == 52 || getPos() == 53 || getPos() == 56 || getPos() == 57)) {
                setLayout(new BorderLayout());
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
                setBackground(new Color(0, 0, 0, 0));
                validate();
            } else {
                setLayout(new BorderLayout());
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
                setBackground(new Color(0, 0, 0, 50));
                validate();
            }
        }
    }
}
