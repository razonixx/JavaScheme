


import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Buffer {
    
    private ArrayList<String> buffer;
    private int n;
    private JProgressBar bar;
    private JLabel label;
    
    public Buffer(int n, JProgressBar bar, JLabel label) {
        buffer = new ArrayList<String>();
        this.n = n;
        this.label = label;
        this.label.setText(String.valueOf(this.buffer.size()));
        this.bar = bar;
        this.bar.setMinimum(0);
        this.bar.setMaximum(n);
        
    }
    
    synchronized String consume() {
        String product = "";
        
        if(buffer.size() == 0) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try{
        product = this.buffer.remove(0);
        this.bar.setValue(this.buffer.size());
        this.label.setText(String.valueOf(this.buffer.size()));
        }catch(Exception ex){}
        notify();
        
        return product;
    }
    
    synchronized void produce(String product) {
        if(buffer.size() > n) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.buffer.add(product);
        this.bar.setValue(this.buffer.size());
        this.label.setText(String.valueOf(this.buffer.size()));
        
        notify();
    }
    
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }
    
}
