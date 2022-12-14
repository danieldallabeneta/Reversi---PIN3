package Controller;

import Model.Board;

/**
 *
 * @author danie
 */
public class Heuristic {
    
    public static int playerPrincipal;
    
    static int heuristicValue(Board board) {
        return (25 * mobilityHeuristic(board)) + (25 * coinParityHeuristic(board)) + (50 * stabilityHeuristic(board));
    }
    
    static int mobilityHeuristic(Board board) {
        int moves = board.getValidMoves(playerPrincipal).size();
        int oppMoves = board.getValidMoves(playerPrincipal == 1 ? 2 : 1).size();        
        if (moves + oppMoves != 0) {
            return 100 * ((moves - oppMoves) / (moves + oppMoves));
        }
        return 0;
    }   
    
    static int coinParityHeuristic(Board board) {
        int countMax = board.getCountCoin(playerPrincipal);
        int oppCoin = board.getCountCoin(playerPrincipal == 1 ? 2 : 1);        
        if (countMax + oppCoin != 0) {
            return 100 * (countMax - oppCoin) / (countMax + oppCoin);
        }
        return 0;
    }

    static int stabilityHeuristic(Board board) {
        int max = board.getCountWeigth(playerPrincipal);
        int min = board.getCountWeigth(playerPrincipal == 1 ? 2 : 1);        
        if (max + min != 0) {
            return 100 * (max - min) / (max + min);
        }
        return 0;
    }
    
    
}
