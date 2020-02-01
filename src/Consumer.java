


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

    public String eval(String op, String op1, String op2)
    {
        int res;
        String resString;
        switch(op)
        {
            case "+":
                res = Integer.parseInt(op1) + Integer.parseInt(op2);
                return String.valueOf(res);
            case "-":
                res = Integer.parseInt(op1) - Integer.parseInt(op2);
                return String.valueOf(res);
            case "*":
                res = Integer.parseInt(op1) * Integer.parseInt(op2);
                return String.valueOf(res);
            case "/":
                if(op2.equals("0"))
                {
                    return "Error: Division by 0";
                }
                resString = op1 + "/" + op2;
                return resString;
        }
        return "Error in evaluating the string";
    }
    
    public synchronized void writeTable(int id, String product, String res) throws Exception
    {
        Object add[] = {id, product, res};
        model.addRow(add);
        //wait();
    }
    
    public synchronized void finish(){
        this.end = true;
    }
    @Override
    public synchronized void run() {
        System.out.println("Running Consumer...");
        String product;
        String res, op, op1, op2;
        
        while(!this.end) {
            product = this.buffer.consume();
            if(product.length() == 7){
                //jLabel.setText(Long.toString(counter++));
                op = String.valueOf(product.charAt(1));
                op1 = String.valueOf(product.charAt(3));
                op2 = String.valueOf(product.charAt(5));
                res = eval(op, op1, op2);
                try{
                    this.writeTable(id, product, res);
                    //notify();
                }catch(Exception ex){
                    System.out.println(product + " = " + res);
                    Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                    //System.exit(0);
                }
            }
            try {
                Thread.sleep(this.sleep);
            } catch (InterruptedException ex) {
                this.finish();
            }
        }
    }
}
