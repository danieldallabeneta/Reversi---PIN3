package View;

import Controller.MiniMax;
import Model.Board;
import Model.ModelParameters;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author danie
 */
public class ViewPrincipal extends javax.swing.JFrame {

    JButton[][] grid;
    Board tabuleiro;
    int[][] gamestate;
    ModelParameters parameters;
    int x, y, player;

    public ViewPrincipal(Map<String, Object> parametros) throws Throwable {
        parameters = ModelParameters.getInstance().getInstance();
        parameters.setParametros(parametros);
        initComponents();
        lbPlayer1.setText(parameters.getNome1());
        lbPlayer2.setText(parameters.getNome2());
        tfPlayer1.setText("0");
        tfPlayer2.setText("0");
        initialize();
        initialState();
        btAvancar.addActionListener(movimentar());
        jButton1.addActionListener(finalizar());
        tabuleiro = new Board(gamestate);
        paintboard();
        player = 2;
        suggestMoves();
    }

    public void initialState() {
        gamestate = new int[8][8];
        gamestate[3][3] = 1;
        gamestate[4][4] = 1;
        gamestate[3][4] = 2;
        gamestate[4][3] = 2;
    }

    public void initialize() {
        this.getContentPane().setForeground(Color.BLUE);
        this.criaTabuleiro();
    }

    private void criaTabuleiro() {
        grid = new JButton[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[j][i] = new JButton("");
                grid[j][i].setSize(new Dimension(60, 60));
                grid[j][i].setBounds(20 + 53 * j, 20 + 53 * i, 50, 50);
                grid[j][i].setOpaque(true);
                grid[j][i].setBorderPainted(false);
                jPanel1.add(grid[j][i]);
            }
        }
    }

    void paintboard() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                setColor(x, y, tabuleiro.gamestate[x][y]);
            }
        }
    }

    public void setColor(int x, int y, int c) {
        switch (c) {
            case 0:
                grid[x][y].setBackground(new Color(50, 110, 70));
                break;
            case 1:
                grid[x][y].setBackground(Color.BLACK);
                break;
            case 2:
                grid[x][y].setBackground(Color.WHITE);
                break;
        }
    }

    public void suggestMoves() throws Throwable {
        ArrayList<Board> moves = tabuleiro.getValidMoves(player);

        for (Board i : moves) {
            grid[i.movedX][i.movedY].setBackground(new Color(50, 160, 70));
        }
    }

    private void playIa() throws Throwable {
        int depht = player == 1 ? parameters.getProfundidade1() : parameters.getProfundidade2();
        int[][] incpy = Board.cloneGrid(tabuleiro.gamestate);
        MiniMax.heuristic1 = parameters.getHeuristic1();
        MiniMax.heuristic2 = parameters.getHeuristic2();
        MiniMax.playerPrincipal = this.player;
        MiniMax.startTime = System.currentTimeMillis();
        MiniMax.maxTime = 3000;
        MiniMax.cutOff = false;

        for (int i = 1; i <= depht && !MiniMax.cutOff; i++) {
            MiniMax.maxLevel = i;
            MiniMax.minimax(true, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, new Board(incpy));

            tabuleiro = MiniMax.best;
            
            
            if (MiniMax.cutOff) {
                break;
            } 
        }

        this.x = this.tabuleiro.movedX;
        this.y = this.tabuleiro.movedY;
        if (player == 1) {
            player = 2;
        } else {
            player = 1;
        }
        paintboard();
        suggestMoves();
        setPontuacao();
    }

    public JFrame getFrame() {
        return this;
    }

    public void setPontuacao() throws Throwable {
        int ponto1 = tabuleiro.calculateValue(1);
        int ponto2 = tabuleiro.calculateValue(2);

        tfPlayer1.setText(Integer.toString(ponto1));
        tfPlayer2.setText(Integer.toString(ponto2));

    }

    private void isFimPartida() {
        int ponto1 = tabuleiro.calculateValue(1);
        int ponto2 = tabuleiro.calculateValue(2);

        if (ponto1 + ponto2 == 64) {
            finalizaPartida(ponto1, ponto2);
        }
    }

    public ActionListener movimentar() {
        ActionListener acaoConfirmar = (ActionEvent e) -> {            
            try {
                playIa();
            } catch (Throwable ex) {
                Logger.getLogger(ViewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("Selecionado (" + x + " , " + y + ")");
            paintboard();

            try {
                suggestMoves();
            } catch (Throwable ex) {
                Logger.getLogger(ViewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!validaJogada()) {
                if (player == 1) {
                    player = 2;
                } else {
                    player = 1;
                }
                if (!validaJogada()) {
                    int ponto1 = tabuleiro.calculateValue(1);
                    int ponto2 = tabuleiro.calculateValue(2);
                    finalizaPartida(ponto1, ponto2);
                }
            }
            isFimPartida();
        };
        return acaoConfirmar;
    }

    public ActionListener finalizar() {
        ActionListener acaoConfirmar = (ActionEvent e) -> {

            int difPoint = 0;

            while (difPoint != 64) {               
                try {
                    playIa();
                } catch (Throwable ex) {
                    Logger.getLogger(ViewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println("Selecionado (" + x + " , " + y + ")");
                paintboard();

                try {
                    suggestMoves();
                } catch (Throwable ex) {
                    Logger.getLogger(ViewPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
                int ponto1 = tabuleiro.calculateValue(1);
                int ponto2 = tabuleiro.calculateValue(2);
                difPoint = ponto1 + ponto2;
                if (!validaJogada()) {
                    if (player == 1) {
                        player = 2;
                    } else {
                        player = 1;
                    }
                    if (!validaJogada()) {
                        finalizaPartida(ponto1, ponto2);
                        difPoint = 64;
                    }
                }
            }

            isFimPartida();
        };
        return acaoConfirmar;
    }

    private boolean validaJogada() {
        ArrayList<Board> moves = tabuleiro.getValidMoves(player);
        return moves.size() > 0 ? true : false;
    }

    private void finalizaPartida(int ponto1, int ponto2) {
        try {
            String vencedor;
            if (ponto1 > ponto2) {
                vencedor = parameters.getNome1();
            } else {
                vencedor = parameters.getNome2();
            }
            String texto = "Fim de jogo \n O Jogador :"
                    + vencedor + " foi o vencedor \n "
                    + parameters.getNome1() + " " +Integer.toString(ponto1) + "\n"
                    + parameters.getNome2() + " " +Integer.toString(ponto2);
            JOptionPane.showMessageDialog(this, texto, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
            player = 2;
            initialState();
            tabuleiro = new Board(gamestate);
            paintboard();
            tfPlayer1.setText("0");
            tfPlayer2.setText("0");
            suggestMoves();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        lbPlayer2 = new javax.swing.JLabel();
        lbPlayer1 = new javax.swing.JLabel();
        btAvancar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        tfPlayer1 = new javax.swing.JTextField();
        tfPlayer2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lbPlayer2.setBackground(new java.awt.Color(255, 255, 51));
        lbPlayer2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbPlayer2.setForeground(new java.awt.Color(255, 0, 0));
        lbPlayer2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPlayer2.setText("Player 2");

        lbPlayer1.setBackground(new java.awt.Color(102, 255, 255));
        lbPlayer1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbPlayer1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPlayer1.setText("Player 1");

        btAvancar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btAvancar.setText("Avançar");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setText("Finalizar");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 718, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 634, Short.MAX_VALUE)
        );

        tfPlayer1.setEditable(false);
        tfPlayer1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        tfPlayer2.setEditable(false);
        tfPlayer2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 18, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(btAvancar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbPlayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfPlayer1, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                            .addComponent(tfPlayer2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btAvancar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbPlayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfPlayer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAvancar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbPlayer1;
    private javax.swing.JLabel lbPlayer2;
    private javax.swing.JTextField tfPlayer1;
    private javax.swing.JTextField tfPlayer2;
    // End of variables declaration//GEN-END:variables

}
