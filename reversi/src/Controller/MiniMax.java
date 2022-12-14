package Controller;

import Model.Board;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author danie
 */
public class MiniMax {

    public static int maxLevel = 10;
    public static int playerPrincipal;
    public static int heuristic1;
    public static int heuristic2;
    public static int playerAtual;
    public static Board best;
    public static boolean cutOff = false;
    public static int maxTime = 1000;
    public static long startTime = 0;

    public static int minimax(boolean maximizingPlayer, int level, int alpha, int beta, Board b) {
        if (System.currentTimeMillis() - startTime >= maxTime) {
            cutOff = true;
            best = b;
            return b.calculateValueDiff();
        } else if (level > maxLevel) {
            return b.calculateValueDiff();
        }

        setPlayerAtual(maximizingPlayer);

        ArrayList<Board> moves = b.getValidMoves(playerAtual);

        if (level >= maxLevel || moves.size() == 0) {
            return getValueHeuristic(b);
        }

        if (maximizingPlayer) {
            int top = 0;
            List<Integer> bestScore = new ArrayList<>(3);
            for (int i = 0; i < moves.size(); i++) {
                int score = minimax(false, level + 1, alpha, beta, moves.get(i));

                if (score > alpha) {
                    alpha = score;
                    top = i;
                    int qtdBestScore = bestScore.size();
                    if(qtdBestScore < 3){
                        bestScore.add(i);
                    } else {
                        int min = 100;
                        int max = bestScore.size();
                        int aux = 0;
                        for (int j = 0; j < max; j++) {
                            if(bestScore.get(j) < min){
                                min = bestScore.get(j);
                                aux = j;
                            }
                        }
                        bestScore.remove(bestScore.get(aux));
                        bestScore.add(i);
                    }
                }
                if (alpha >= beta) {
                    break;
                }
            }
            if (level == 0) {
                Random random = new Random();
                int select = random.nextInt(bestScore.size());
                best = moves.get(select);
            }
            return alpha;
        } else {
            for (Board i : moves) {
                int score = minimax(true, level + 1, alpha, beta, i);

                if (score < beta) {
                    beta = score;
                }

                if (alpha >= beta) {
                    break;
                }
            }
            return beta;
        }
    }

    private static void setPlayerAtual(boolean maximizing) {
        if (!maximizing && playerPrincipal == 1) {
            playerAtual = 2;
        } else if (!maximizing && playerPrincipal == 2) {
            playerAtual = 1;
        } else {
            playerAtual = playerPrincipal;
        }
    }

    private static int getValueHeuristic(Board b) {
        int heuristica = playerPrincipal == 1 ? heuristic1 : heuristic2;
        Heuristic.playerPrincipal = playerPrincipal;
        switch (heuristica) {
            case 1:
                return Heuristic.mobilityHeuristic(b);
            case 2:
                return Heuristic.coinParityHeuristic(b);
            case 3:
                return Heuristic.stabilityHeuristic(b);
            case 4:
                return Heuristic.heuristicValue(b);
        }
        return 0;
    }
    
}
