


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;

public class Consumer extends Thread {
    Buffer buffer;
    int sleep;
    int id;
    volatile boolean end = false;
    static Long counter = 1L;
    static JLabel jLabel;
    static DefaultTableModel model;
    
    Consumer(int id, Buffer buffer, int sleep, JLabel jLabel9, DefaultTableModel model) {
        this.buffer = buffer;
        this.sleep = sleep;
        this.id = id;
        this.jLabel = jLabel9;
        this.model = model;
    }
    
    public synchronized void finish(){
        this.end = true;
    }
    @Override
    public synchronized void run() {
        System.out.println("Running Consumer...");
        
        
        while(!this.end) {
            this.buffer.consume(id);
            counter++;
            this.jLabel.setText(String.valueOf(counter));
            
                try{
                    //notify();
                }catch(Exception ex){
                    Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                    //System.exit(0);
                }
            
            try {
                Thread.sleep(this.sleep);
            } catch (InterruptedException ex) {
                this.finish();
            }
        }
    }
}
