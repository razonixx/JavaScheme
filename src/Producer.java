


import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.table.DefaultTableModel;


public class Producer extends Thread {
    Random r;
    Buffer buffer;
    int sleep;
    int id;
    int min;
    int max;
    DefaultTableModel model;
    int buffSize;
    int rowCount = 0;
    public static String[] operadores = {"+", "-", "*", "/"};
    public static String operandos = "0123456789";
    
    Producer(int id, Buffer buffer, int sleep, int min, int max, DefaultTableModel model, int buffSize) {
        this.buffer = buffer;
        this.sleep = sleep;
        this.id = id;
        this.min = min;
        this.max = max;
        this.model = model;
        this.buffSize = buffSize;
        this.model.setNumRows(buffSize);
        r = new Random(System.currentTimeMillis());
        
        operandos = max==9?operandos.substring(min):operandos.substring(min, max+1);
    }
    
    public synchronized void writeTable(int id, String product)
    {
        model.setValueAt(id, rowCount%buffSize, 0);
        model.setValueAt(product, rowCount%buffSize, 1);
        rowCount++;
        notify();
    }
    
    
    @Override
    public void run() {
        System.out.println("Running Producer...");
        
        while(true) {
            String operador = operadores[r.nextInt(4)];
            String operando1 = String.valueOf(operandos.charAt(ThreadLocalRandom.current().nextInt(min, max + 1)%operandos.length()));
            String operando2 = String.valueOf(operandos.charAt(ThreadLocalRandom.current().nextInt(min, max + 1)%operandos.length()));
            String operacion = "";
            operacion = "(" + operador + " " + operando1 + " " + operando2 + ")";
            this.writeTable(id, operacion);
            this.buffer.produce(operacion);
            //Buffer.print("Producer " + id + " produced: " + operacion);
            
            try {
                Thread.sleep(this.sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
