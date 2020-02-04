


import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

public class Buffer {
    
    private ArrayList<String> buffer;
    private int n;
    int rowCount = 0;
    private JProgressBar bar;
    private DefaultTableModel tableConsumer;
    private DefaultTableModel tableProducer;
    
    public Buffer(int n, JProgressBar bar, JLabel label, DefaultTableModel tableConsumer, DefaultTableModel tableProducer) {
        buffer = new ArrayList<String>();
        this.n = n;
        this.bar = bar;
        this.bar.setMinimum(0);
        this.bar.setMaximum(n);
        this.tableConsumer = tableConsumer;
        this.tableProducer = tableProducer;
        
    }
    
    synchronized String consume(int id) {
        String product = "";
        
        if(buffer.size() == 0) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try{
            this.tableProducer.removeRow(0);
            this.bar.setValue(this.buffer.size());
            product = this.buffer.remove(0);
            String res, op, op1, op2;
            if(product.length() == 7){
                    //jLabel.setText(Long.toString(counter++));
                    op = String.valueOf(product.charAt(1));
                    op1 = String.valueOf(product.charAt(3));
                    op2 = String.valueOf(product.charAt(5));
                    res = eval(op, op1, op2);
                    this.tableConsumer.addRow(new Object[] {id, product, res });
            }
            
        }catch(Exception ex){}
        notify();
        
        return product;
    }
    
    synchronized void produce(String product, int id) {
        if(buffer.size() > n) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.buffer.add(product);
        this.tableProducer.addRow(new Object[] {id, product});
        this.bar.setValue(this.buffer.size());
        
        notify();
    }
    
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
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
   

}
