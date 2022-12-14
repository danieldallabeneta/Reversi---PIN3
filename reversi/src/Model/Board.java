package Model;

import java.util.ArrayList;

/**
 *
 * @author danie
 */
public class Board {

    public int[][] gamestate;
    public int movedX, movedY;
    static int weights[][] = {{ 4,-3, 2, 2, 2, 2,-3, 4},
                              {-3,-4,-1,-1,-1,-1,-4,-3},
                              { 2,-1, 1, 0, 0, 1,-1, 2},
                              { 2,-1, 0, 1, 1, 0,-1, 2},
                              { 2,-1, 0, 1, 1, 0,-1, 2},
                              { 2,-1, 1, 0, 0, 1,-1, 2},
                              {-3,-4,-1,-1,-1,-1,-4,-3},
                              { 4,-3, 2, 2, 2, 2,-3, 4}};

    public Board(int[][] gamestate) {
        this.gamestate = gamestate;
    }
    
    public boolean makeMove(int X, int Y, int player) {

        if (gamestate[X][Y] != 0) {
            return false;
        }

        boolean legalAtLeastOnce = false;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                boolean piecesToFlip = false, passedOpponent = false;
                int k = 1;

                while (X + j * k >= 0 && X + j * k < 8 && Y + i * k >= 0 && Y + i * k < 8) { // Stay inside Board

                    if (gamestate[X + j * k][Y + i * k] == 0 || (gamestate[X + j * k][Y + i * k] == player && !passedOpponent)) {
                        break;
                    }
                    if (gamestate[X + j * k][Y + i * k] == player && passedOpponent) {
                        piecesToFlip = true;
                        break;
                    } else if (gamestate[X + j * k][Y + i * k] == player % 2 + 1) {
                        passedOpponent = true;
                        k++;
                    }
                }

                if (piecesToFlip) {

                    gamestate[X][Y] = player;

                    for (int h = 1; h <= k; h++) {
                        gamestate[X + j * h][Y + i * h] = player;
                    }

                    legalAtLeastOnce = true;
                }
            }
        }

        this.movedX = X;
        this.movedY = Y;

        return legalAtLeastOnce;
    }

    public int calculateValueDiff() {
        return calculateValue(1) - calculateValue(2);
    }

    public int calculateValue(int player) {
        int v = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gamestate[j][i] == player) {
                    v++;
                }
            }
        }
        return v;
    }

    public ArrayList<Board> getValidMoves(int player) {

        ArrayList<Board> boardList = new ArrayList<Board>();
        Board b = new Board(cloneGrid(gamestate));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (b.makeMove(j, i, player)) {
                    boardList.add(b);
                    b = new Board(cloneGrid(gamestate)); 
                }
            }
        }

        return boardList;
    }
    
    public static int[][] cloneGrid(int[][] gamestate) {
        int[][] r = new int[8][];
        for (int i = 0; i < 8; i++) {
            r[i] = gamestate[i].clone();
        }
        return r;
    }

    public int getCountCoin(int player) {
        int coins = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.gamestate[i][j] == player) {
                    coins++;
                }
            }
        }
        return coins;
    }

    public int getCountWeigth(int player) {
        int max = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gamestate[i][j] == player) {
                    max += weights[i][j];
                }
            }
        }
        return max;
    }

}
