


import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;

public class Buffer {
    
    private ArrayList<String> buffer;
    private int n;
    
    public Buffer(int n) {
        buffer = new ArrayList<String>();
        this.n = n;
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
        
        notify();
    }
    
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }
    
}
