/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wally
 */
class Consumer extends Thread {
    private Buffer buffer;
    int producerNo;
    private boolean end;
    
    public Consumer(Buffer buffer, int producerNo) {
     this.buffer = buffer;
     this.producerNo = producerNo;
     this.end = false;
    }
    
    public synchronized void finish(){
        this.end = true;
    }
    @Override
    public void run() {
        while (!this.end) {
            try {
                this.buffer.consume(this.producerNo);
                Thread.sleep(100);
            } catch (InterruptedException e) { 
                this.finish();
            }
        }
    }
}
