package edu.eci.arsw.highlandersim;

public class Semaforo {

    private boolean bandera;

    public synchronized boolean getBandera(){
        return bandera;
    }

    public synchronized void setBandera(boolean bandera){
        this.bandera = bandera;
    }
    
}
