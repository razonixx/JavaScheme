/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author r
 */
public class NewClass extends Thread{
    public int id;
    public int t;
    
    public static String[] operadores = {"+", "-", "*", "/"};
    public static String[] operandos = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    
    public NewClass(int id, int t) {
        this.id = id;
        this.t = t;
    }
    
    
    @Override
    public void run() {
        for(int i = 0; i < t; i++)
        {
            double a = (Math.random() * ((3 - 0) + 1)) + 0;
            double b = (Math.random() * ((9 - 0) + 1)) + 0;
            double c = (Math.random() * ((9 - 0) + 1)) + 0;
            System.out.println("Thread: " + id);
            System.out.println("Scheme: (" + operadores[(int)Math.floor(a)] + " " + operandos[(int)Math.floor(b)]+ " " + operandos[(int)Math.floor(c)] + ")");
        }
    }
}
