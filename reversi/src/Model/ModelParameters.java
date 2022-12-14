package Model;

import java.util.Map;

/**
 *
 * @author danie
 */
public class ModelParameters {
    
    private static ModelParameters instance;
    private int profundidade1;
    private int profundidade2;
    private int heuristic1;
    private int heuristic2;
    private String nome1;
    private String nome2;
    
    private ModelParameters(){}

    public static synchronized ModelParameters getInstance() {
        if (instance == null) {
            instance = new ModelParameters();
        }
        return instance;
    }
        
    public void setParametros(Map<String, Object> parametros){
        profundidade1 = (int)parametros.get("profundidade1");
        profundidade2 = (int)parametros.get("profundidade2");
        heuristic1    = (int)parametros.get("heuristica1");
        heuristic2    = (int)parametros.get("heuristica2");
        nome1         = (String)parametros.get("nome1");
        nome2         = (String)parametros.get("nome2");
    }

    public int getProfundidade1() {
        return profundidade1;
    }

    public int getProfundidade2() {
        return profundidade2;
    }

    public int getHeuristic1() {
        return heuristic1;
    }

    public int getHeuristic2() {
        return heuristic2;
    }

    public String getNome1() {
        return nome1;
    }

    public String getNome2() {
        return nome2;
    }
    
    
}
