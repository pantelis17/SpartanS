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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
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
 *
 * @author pante
 */
public class Server extends JFrame implements Serializable {

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
    private JPanel StartPane;
    private JPanel OpponentPane;
    private JPanel MovePane;
    ArrayList<TilePanel> tiles;
    ArrayList<TilePanel> tilesred;
    ArrayList<TilePanel> tilesoppent;
    ArrayList<JLabel> moves;
    private int min1 = 4, sec1 = 60;
    private JLabel nith;
    private JLabel min;
    private JLabel sec;
    private JLabel message;
    private Move move;
    Timer timer;
    boolean flag = true;//for timer
    boolean isClicked = false;//for start click
    boolean timeIsUp = false;
    boolean EndGame = false;
    //  boolean PlayersTurn = true;
    private boolean Winner = false;
    private boolean Draw = false;
    private JPanel scrollPane1;
    private JScrollPane scrol;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private DataInputStream indata;
    private DataOutputStream outdata;
    private ServerSocket server;
    private Socket connection;
    private static MainMenu main;
    private Load load;
    private Server ser;

    public Server(MainMenu m, int port) throws IOException {
        initComponents();
        main = m;
        server = new ServerSocket(port);
        server.setSoTimeout(60000);
        connection = server.accept();
        main.setVisible(false);
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
        outdata = new DataOutputStream(connection.getOutputStream());
        indata = new DataInputStream(connection.getInputStream());
        this.alliance = Alliance.RED;
        setTitle("Spartan");
        Start = true;
        sourceTile = null;
        destinationTile = null;
        MovedPawn = null;
        bluePlayer = board.getBluePlayer();
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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jPanel1.setLayout(new GridLayout(10, 10));
        jPanel1.setBackground(new Color(0, 0, 0, 0));
        tiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i * 10 + j == 42 || i * 10 + j == 43 || i * 10 + j == 46 || i * 10 + j == 47 || i * 10 + j == 52 || i * 10 + j == 53 || i * 10 + j == 56 || i * 10 + j == 57) {
                    tiles.add(new TilePanel(i * 10 + j, null, 0));
                    tiles.get(i * 10 + j).setLayout(new BorderLayout());
                    tiles.get(i * 10 + j).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    tiles.get(i * 10 + j).setBackground(new Color(0, 0, 0, 50));
                } else {
                    tiles.add(new TilePanel(i * 10 + j, null, 0));
                    tiles.get(i * 10 + j).setLayout(new BorderLayout());
                    tiles.get(i * 10 + j).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    tiles.get(i * 10 + j).setBackground(new Color(0, 0, 0, 0));
                }
                jPanel1.add(tiles.get(i * 10 + j));
            }
        }
        jPanel1.revalidate();
        jPanel1.repaint();
        setLocation(-3, 0);
        StartPane = new JPanel();
        if (!alliance.isBlue()) {
            StartPane.setBackground(new Color(204, 0, 0, 255));
        } else {
            StartPane.setBackground(new Color(0, 0, 204, 255));
        }
        StartPane.setVisible(true);
        StartPane.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3);
        StartPane.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2 + 1, 0);
        StartPane.setLayout(new GridLayout(4, 10));
        JButton start = new JButton("Start");
        start.setEnabled(false);
        start.setPreferredSize(new Dimension(80, 80));
        tilesred = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                if (!alliance.isBlue()) {
                    tilesred.add(new TilePanel(i * 10 + j, this.redPlayer.getPawnOfStack(i * 10 + j), 1));
                    this.redPlayer.setCordinateofPawn(i * 10 + j, (i * 10 + j + 1) * (-1), 1, !isClicked);
                    tilesred.get(i * 10 + j).setLayout(new BorderLayout());
                    tilesred.get(i * 10 + j).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    this.redPlayer.getPawnOfStack(i * 10 + j).setSide(true);
                    tilesred.get(i * 10 + j).add(new JLabel(this.redPlayer.getPawnOfStack(i * 10 + j).getImage()));
                    tilesred.get(i * 10 + j).setBackground(new Color(0, 0, 0, 0));
                } else {
                    tilesred.add(new TilePanel(i * 10 + j, this.bluePlayer.getPawnOfStack(i * 10 + j), 1));
                    this.bluePlayer.setCordinateofPawn(i * 10 + j, (i * 10 + j + 1) * (-1), 1, !isClicked);
                    tilesred.get(i * 10 + j).setLayout(new BorderLayout());
                    tilesred.get(i * 10 + j).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    this.bluePlayer.getPawnOfStack(i * 10 + j).setSide(true);

                    tilesred.get(i * 10 + j).add(new JLabel(this.bluePlayer.getPawnOfStack(i * 10 + j).getImage()));
                    tilesred.get(i * 10 + j).setBackground(new Color(0, 0, 0, 0));
                }
                StartPane.add(tilesred.get(i * 10 + j));
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
                    return;
                }
                try {
                    Random(alliance);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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
                try {
                    Close();
                } catch (Exception ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                main.setVisible(true);
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
                if ((board.getCurrentPlayer().getAlliance().isBlue() == alliance.isBlue()) && !Start) {
                    Move move = board.getCurrentPlayer().BotMove(board.getGameBoard());
                    tiles.get(move.getCurrentCoordinate()).setBackground(new Color(255, 0, 0, 50));
                    tiles.get(move.getDestinationCoordinate()).setBackground(new Color(255, 0, 0, 50));
                    jPanel1.removeAll();
                    for (final TilePanel boardTile : tiles) {
                        if (boardTile.getPos() != move.getCurrentCoordinate() && boardTile.getPos() != move.getDestinationCoordinate()) {
                            boardTile.drawTile();
                        }
                        jPanel1.add(boardTile);
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
        // scrollPane1.setLayout();
        moves = new ArrayList<>();
        OptionPane.add(random);
        OptionPane.add(help);
        OptionPane.add(exit);

        MovePane = new JPanel();
        MovePane.setBackground(Color.DARK_GRAY);
        MovePane.setVisible(true);
        MovePane.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 4, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 10);
        MovePane.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2 + 1, (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 3) * 2);
        min = new JLabel("0" + (min1 + 1));
        sec = new JLabel("0" + (sec1 - 60));
        nith = new JLabel(":");
        if ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() > 1500) {
            nith.setFont(new Font("Serif", Font.BOLD, 30));
            min.setFont(new Font("Serif", Font.BOLD, 30));
            sec.setFont(new Font("Serif", Font.BOLD, 30));
            message = new JLabel(" Your Turn => ");
            message.setFont(new Font("Serif", Font.BOLD, 30));
        } else {
            nith.setFont(new Font("Serif", Font.BOLD, 20));
            min.setFont(new Font("Serif", Font.BOLD, 20));
            sec.setFont(new Font("Serif", Font.BOLD, 20));
            message = new JLabel(" Your Turn  ");
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

        //      MovePane.add(scroll);
        start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isClicked) {
                    start.setEnabled(false);
                    try {
                        outdata.writeBoolean(true);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        isClicked = (boolean) indata.readBoolean();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    start.setText("Ready");
                    timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (board.boardFull(alliance)) {
                                start.setEnabled(true);
                                start.setBackground(Color.green);

                            } else {
                                start.setEnabled(false);
                            }
                            sec.setForeground(Color.WHITE);
                            min.setForeground(Color.WHITE);
                            nith.setForeground(Color.WHITE);
                            message.setForeground(Color.WHITE);
                            if (sec1 == 0) {
                                sec1 = 60;
                                min1--;

                            }

                            if (min1 < 0) {
                                //TODO more
                                // timeIsUp = true;
                                if (!board.boardFull(alliance) && Start) {
                                    try {
                                        Random(alliance);
                                    } catch (IOException ex) {
                                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                                if (Start) {
                                    start.setEnabled(false);
                                    try {
                                        Random(Alliance.BLUE);
                                        out.writeObject(redPlayer.getActivePawns());

                                    } catch (IOException ex) {
                                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    timeIsUp = true;
                                    min1 = 1;
                                    sec1 = 60;
                                    min.setText("0" + (min1 + 1));
                                    sec.setText("0" + (sec1 - 60));
                                    message.setText("Your Turn =>");
                                    MovePane.remove(start);
                                    Start = false;

                                    timer.start();
                                    GamePlay();
                                } else {
                                    EndGame = true;
                                    //MovePane.setVisible(false);
                                    timer.stop();
                                    if (board.getCurrentPlayer().getAlliance().isBlue() == alliance.isBlue()) {
                                        JOptionPane.showMessageDialog(getThisJFrame(), "You Lose.... Time is up", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                                        updateComponentTreeUI(getThisJFrame());

                                    } else {
                                        JOptionPane.showMessageDialog(getThisJFrame(), "You Win!! Time is up for the opponent.", "END GAME", JOptionPane.INFORMATION_MESSAGE);
                                        updateComponentTreeUI(getThisJFrame());

                                    }
                                }
                            } else {
                                if (!Start) {
                                    if (board.getCurrentPlayer().getAlliance().isBlue() == alliance.isBlue()) {
                                        try {
                                            outdata.writeInt(0);
                                        } catch (IOException ex) {
                                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } else {
                                        try {
                                            if (indata.readInt() != 0) {
                                                OpponentMove();
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (ClassNotFoundException ex) {
                                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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
                } else {
                    start.setEnabled(false);
                    // min.setText("");
                    // sec.setText("");
                    // nith.setText("");
                    // message.setText("Wait for Opponent");
                    try {
                        Random(Alliance.BLUE);
                        out.writeObject(redPlayer.getActivePawns());

                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    timeIsUp = true;
                    min1 = 1;
                    sec1 = 60;
                    min.setText("0" + (min1 + 1));
                    sec.setText("0" + (sec1 - 60));
                    message.setText("Your Turn =>");
                    MovePane.remove(start);
                    Start = false;

                    timer.start();
                    GamePlay();

                }
            }
        });
        add(MovePane);
        add(StartPane);
        add(OpponentPane);
        add(OptionPane);
        add(scrollPane1);
    }

    private void Random(Alliance a) throws IOException {
        ArrayList<Pawn> rand = new ArrayList<>();
        int min, max;
        if (!a.isBlue()) {
            rand = redPlayer.Random();
            min = 60;
            max = 100;
            StartPane.removeAll();
            for (int i = 0; i < 40; i++) {
                tilesred.set(i, new TilePanel(i, null, 1));
            }
            for (final TilePanel boardTile : tilesred) {
                StartPane.add(boardTile);
            }
            StartPane.validate();
            StartPane.repaint();
            StartPane.updateUI();
        } else {
            try {
                rand = (ArrayList<Pawn>) in.readObject();

                for (Pawn pawn : rand) {
                    pawn.setSide(false);
                }
                bluePlayer.setStack(rand);
                for (Pawn pawn : rand) {
                    this.board.setPawnOnBoard(pawn.getPositionOfPawn(), pawn);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            min = 0;
            max = 40;
        }

        jPanel1.removeAll();
        for (int i = min; i < max; i++) {
            tiles.remove(rand.get(i - min).getPositionOfPawn());
            tiles.add(rand.get(i - min).getPositionOfPawn(), new TilePanel(rand.get(i - min).getPositionOfPawn(), rand.get(i - min), 0));
            tiles.get(rand.get(i - min).getPositionOfPawn()).add(new JLabel(rand.get(i - min).getImage()));
        }
        for (final TilePanel boardTile : tiles) {
            jPanel1.add(boardTile);
        }
        jPanel1.validate();
        jPanel1.repaint();
        jPanel1.updateUI();
        updateComponentTreeUI(getThisJFrame());
    }

    private Runnable waitfor() throws InterruptedException {
        Thread.sleep((long) 5000);
        return null;
    }

    private void OpponentMove() throws IOException, ClassNotFoundException, InterruptedException {
        // board.changeCurrentPlayer();
        GamePlay();
        final Move move = (Move) in.readObject();
        final MoveTransition transition = board.getCurrentPlayer().makeMove(move);
        board = transition.getToBoard();
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

                tilesred.set(i, new TilePanel(i, temp.getPawn(), 1));
                tilesred.get(i).add(new JLabel(temp.getPawn().getImage()));
                for (final TilePanel boardTile : tilesred) {
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
                tilesred.set(i, new TilePanel(i, temp.getPawn(), 1));
                tilesred.get(i).add(new JLabel(temp.getPawn().getImage()));
                for (final TilePanel boardTile : tilesred) {
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
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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
                String s = new String("You: From " + (a + 1) + " To " + (b + 1) + "            ");
                JLabel tem = new JLabel(s);
                tem.setForeground(Color.RED);
                if ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() < 1500) {
                    tem.setFont(new Font("Serif", Font.BOLD, 13));
                } else {
                    tem.setFont(new Font("Serif", Font.BOLD, 18));
                }
                moves.add(0, tem);
            } else {
                String s = new String("Opponent: From " + (a + 1) + " To " + (b + 1) + "         ");
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
            tilesred.set(a, new TilePanel(a, null, 1));
            StartPane.validate();
            StartPane.repaint();
            StartPane.updateUI();
        } else {
            if (!swap) {
                tiles.set(a, new TilePanel(a, null, 0));
                board.setPawnOnBoard(a, null);
                if(tiles.get(b).getPawn() == null){
                    board.setPawnOnBoard(b, null);
                }
            } 
        }
        jPanel1.removeAll();
        for (final TilePanel boardTile : tiles) {
            if (!swap) {
                if (boardTile.getPos() == b ) {
                    if (boardTile.getPawn() != null) {
                        boardTile.add(new JLabel(boardTile.getPawn().getImage()));
                        if (boardTile.getPos() == b) {
                            boardTile.getTile().getPawn().setCordinateOfPawn(b);
                            board.setPawnOnBoard(b, boardTile.getPawn());

                        } else {
                            boardTile.getTile().getPawn().setCordinateOfPawn(a);
                            board.setPawnOnBoard(a, boardTile.getPawn());
                        }
                    }
                } else {
                    boardTile.drawTile();
                }
                jPanel1.add(boardTile);
                jPanel1.validate();
                jPanel1.updateUI();
                updateComponentTreeUI(this);
            } else {
                if (boardTile.getPos() == b || boardTile.getPos() == a) {
                    if (boardTile.getPawn() != null) {
                        boardTile.add(new JLabel(boardTile.getPawn().getImage()));
                        if (boardTile.getPos() == b) {
                            boardTile.getTile().getPawn().setCordinateOfPawn(b);
                            board.setPawnOnBoard(b, boardTile.getPawn());

                        } else {
                            boardTile.getTile().getPawn().setCordinateOfPawn(a);
                            board.setPawnOnBoard(a, boardTile.getPawn());
                        }
                    }
                } else {
                    boardTile.drawTile();
                }
                jPanel1.add(boardTile);
            }
        }
        jPanel1.validate();
        jPanel1.updateUI();
        updateComponentTreeUI(this);

        if (!Start) {
            GamePlay();
            if (board.getCurrentPlayer().getAlliance().isBlue()) {
                tiles.get(a).setBackground(new Color(0, 255, 0, 50));
                tiles.get(a).setBorder(BorderFactory.createLineBorder(Color.RED));
                tiles.get(b).setBackground(new Color(0, 255, 0, 50));
                tiles.get(b).setBorder(BorderFactory.createLineBorder(Color.RED));
                CheckEnd();
            }

        }
    }

    private JFrame getThisJFrame() {
        return this;
    }

    private void setCurrentMove(Move move) {
        this.move = move;
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
            tilesred.set(a, new TilePanel(a, null, 1));
            StartPane.remove(a);
            StartPane.add(tilesred.get(a), a);

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
            //    board.toStringBoard();
        }
        StartPane.removeAll();
        tilesred.set(b, new TilePanel(b, pawn, 1));
        tilesred.get(b).add(new JLabel(pawn.getImage()));
        for (final TilePanel boardTile : tilesred) {
            if (boardTile.getPos() == b) {
                this.redPlayer.setCordinateofPawn(a, (b + 1) * (-1), p, !isClicked);
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

    private void CheckEnd() {
        if (!Start) {
            GamePlay();
            if (!(board.getCurrentPlayer().getAlliance().isBlue())) {
                if (EndGame) {
                    //MovePane.setVisible(false);
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

                    // OpponentMove();
                }
                //this.PlayersTurn = !this.PlayersTurn;
            } else {

                //message.setText("Your Turn");
                //  this.PlayersTurn = !this.PlayersTurn;
                //  GamePlay();
                if (EndGame) {
                    // MovePane.setVisible(false);
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
            }
        }
    }

    private void GamePlay() {
        this.board.getCurrentPlayer().calculateLegalMoves(board);//.setLegalMoves(this.board.getCurrentPlayer().calculateLegalMoves(this.board.getCurrentPlayer().getActivePawns().getStack(), board));
        this.board.getCurrentPlayer().getOpponent().calculateLegalMoves(board);//.setLegalMoves(this.board.getCurrentPlayer().getOpponent().calculateLegalMoves(this.board.getCurrentPlayer().getOpponent().getActivePawns().getStack(), board));
        if (redPlayer.getAvailablePawns().isEmpty() && bluePlayer.getAvailablePawns().isEmpty()) {
            Draw = true;
            EndGame = true;
        } else if (redPlayer.getAvailablePawns().isEmpty()) {
            EndGame = true;
            Winner = false;
        } else if (bluePlayer.getAvailablePawns().isEmpty()) {
            EndGame = true;
            Winner = true;
        }
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

    public void Close() throws Exception {
        if (isClicked) {
            timer.stop();
        }
        out.close();
        in.close();
        server.close();
        connection.close();
    }

    private Runnable test(int a, int b) {
        for (final TilePanel boardTile : tiles) {
            if (boardTile.getPos() == b || boardTile.getPos() == a) {
                if (boardTile.getPawn() != null) {
                    boardTile.add(new JLabel(boardTile.getPawn().getImage()));
                    if (boardTile.getPos() == b) {
                        boardTile.getTile().getPawn().setCordinateOfPawn(b);
                        board.setPawnOnBoard(b, boardTile.getPawn());
                    } else {
                        boardTile.getTile().getPawn().setCordinateOfPawn(a);
                        board.setPawnOnBoard(a, boardTile.getPawn());
                    }
                }
            } else {
                boardTile.drawTile();
            }
            jPanel1.add(boardTile);
            jPanel1.validate();
            jPanel1.updateUI();
            updateComponentTreeUI(this);
        }
        return null;
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
                                    sourceTile = tilesred.get(getPos()).getTile();
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
                                destinationTile = tilesred.get(getPos()).getTile();
                                if (!tilesred.get(destinationTile.getTileCordinates()).getTile().isTileOccupied()) {
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
                                                tilesred.set(k, new TilePanel(k, move.getPawn(), 1));
                                                tilesred.get(k).add(new JLabel(move.getPawn().getImage()));
                                                for (final TilePanel boardTile : tilesred) {
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
                                                tilesred.set(k, new TilePanel(k, move.getPawn(), 1));
                                                tilesred.get(k).add(new JLabel(move.getPawn().getImage()));
                                                for (final TilePanel boardTile : tilesred) {
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
                                                /*                                                try {
                                                    drawBoard(sourceTile.getTileCordinates(), destinationTile.getTileCordinates(), NumPanel, false);
                                                } catch (InterruptedException ex) {
                                                    Logger.getLogger(SinglePlayer.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (IOException ex) {
                                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                                } catch (ClassNotFoundException ex) {
                                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                 */ try {
                                                    outdata.writeInt(1);
                                                    out.writeObject(move);
                                                    CheckEnd();
                                                    board.changeCurrentPlayer();
                                                    //  OpponentMove();
                                                } catch (IOException ex) {
                                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                                sourceTile = null;
                                                destinationTile = null;
                                                MovedPawn = null;
                                                NumPanel = -1;
                                                //board.toStringBoard();

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
                tilesred.set(a, new TilePanel(a, pawn, 1));
                tilesred.get(a).setLayout(new BorderLayout());
                tilesred.get(a).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                tilesred.get(a).add(new JLabel(pawn.getImage()));
                tilesred.get(a).setBackground(new Color(0, 0, 0, 0));
                StartPane.add(tilesred.get(a), a);
                StartPane.validate();
                StartPane.repaint();
                StartPane.updateUI();
            } else if (p == 0) {
                jPanel1.remove(a);
                tiles.set(a, new TilePanel(a, pawn, 0));
                tiles.get(a).setLayout(new BorderLayout());
                tiles.get(a).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                tiles.get(a).add(new JLabel(pawn.getImage()));
                tiles.get(a).setBackground(new Color(0, 0, 0, 0));
                jPanel1.add(tiles.get(a), a);
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
