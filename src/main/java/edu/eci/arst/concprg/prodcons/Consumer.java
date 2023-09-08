/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread{
    
    private BlockingQueue<Integer> queue;
    
    
    public Consumer(BlockingQueue<Integer> queue){
        this.queue=queue;        
    }
    
    @Override
    public void run() {
        while (true) {

            //Usamos el metodo take para evitar que se haga la pausa activa
            int elem;
            try{
                //El consumidor utiliza el método take para bloquear el hilo hasta que haya elementos disponibles en la cola. 
                //Cuando un elemento esté disponible, take lo retirará de la cola y lo almacenará en la variable elem.
                elem = queue.take();
                System.out.println("Consumer consumes "+elem);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            /*if (queue.size() > 0) {
                int elem=queue.poll();
                System.out.println("Consumer consumes "+elem);                                
            }*/
            
        }
    }
}
