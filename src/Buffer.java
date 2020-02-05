import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wally
 */
public class Buffer {
    private LinkedList<String> buffer;
    private int maxSize, min, max, sleepProduce, sleepConsumer, consumeProducts, bufferCount; 
    private static String[] operadores = {"+", "-", "*", "/"};
    private static String operandos = "0123456789";
    private Random r;
    DefaultTableModel modelConsumidor;
    DefaultTableModel modelProductor;
    private JLabel count;
    private JProgressBar bufferBar;
    
    
    public Buffer (int maxSize, int min, int max, int sleepProduce, int sleepConsumer, DefaultTableModel modelConsumidor, 
            DefaultTableModel modelProductor, JLabel count, JProgressBar bufferBar ){
        this.buffer = new LinkedList<>();
        this.maxSize = maxSize;
        this.min = min;
        this.max = max;
        this.sleepProduce = sleepProduce;
        this.sleepConsumer = sleepConsumer;
        this.modelConsumidor = modelConsumidor;
        this.modelProductor = modelProductor;
        this.consumeProducts = 0;
        this.bufferBar = bufferBar;
        this.bufferBar.setMaximum(maxSize);
        this.bufferBar.setMinimum(0);
        this.bufferBar.setValue(0);
        this.bufferCount = 0;
        this.count = count;
        this.r = new Random(System.currentTimeMillis());
        operandos = max==9?operandos.substring(min):operandos.substring(min, max+1);
    }
    public void produce(int id) throws InterruptedException {
        synchronized (this.buffer) {
            while (this.buffer.size() == maxSize) {
                 this.buffer.wait();
            }
            String operador = operadores[r.nextInt(4)];
            String operando1 = String.valueOf(operandos.charAt(ThreadLocalRandom.current().nextInt(min, max + 1)%operandos.length()));
            String operando2 = String.valueOf(operandos.charAt(ThreadLocalRandom.current().nextInt(min, max + 1)%operandos.length()));
            String producedItem =  "(" + operador + " " + operando1 + " " + operando2 + ")"; 

            this.modelProductor.addRow(new Object[] {id, producedItem});
            this.buffer.add(producedItem);
            this.bufferCount++;
            this.bufferBar.setValue(this.bufferCount);
            Thread.sleep(this.sleepConsumer);
            this.buffer.notify();
        }
    }

    public void consume(int id) throws InterruptedException {
        synchronized (this.buffer) {
            while (this.buffer.size() == 0) {
                this.buffer.wait();
            }
            Thread.sleep(this.sleepProduce);
            String producedItem = this.buffer.remove(0);
            this.bufferCount++;
            this.bufferBar.setValue(this.bufferCount);
            this.consumeProducts++;
            this.count.setText(""+this.consumeProducts);
            System.out.println(Thread.currentThread().getName()+", CONSUMED : "+ producedItem);
            this.modelProductor.removeRow(0);
            this.modelConsumidor.addRow(new Object[] {id, producedItem});
            this.buffer.notify();
        }
    }
    /*public static void main(String[] args){
       /* Buffer buffer = new Buffer(1,0,9);
        
        Producer producer0 = new Producer(buffer, 0);
        Consumer consumer0 = new Consumer(buffer, 0);
        
        producer0.start();
        consumer0.start();
     
        System.out.println("MID");
     
        Producer producer1=new Producer(buffer, 1);
        Consumer consumer1=new Consumer(buffer,1);

        producer1.start();
        consumer1.start();
    }*/
}
