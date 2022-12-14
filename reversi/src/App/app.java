package App;

import View.ViewParameters;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danie
 */
public class app {
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewParameters tela = new ViewParameters();
                    tela.setVisible(true);
                } catch (Throwable ex) {
                    Logger.getLogger(app.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
