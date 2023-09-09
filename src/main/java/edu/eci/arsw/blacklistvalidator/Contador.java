package edu.eci.arsw.blacklistvalidator;


public class Contador {
    private boolean bandera;
    private int conteoActual;
    private int conteoFinal;

    public Contador(boolean bandera, int conteoFinal){
        this.conteoActual = 0;
        this.conteoFinal = conteoFinal;
        this.bandera = bandera;
    }

    public synchronized boolean getBandera(){
        return bandera;
    }

    private synchronized void setBandera(boolean bandera){
        this.bandera = bandera;
    }

    public synchronized void aumentarConteo(){
        conteoActual++;
        if(conteoActual == conteoFinal){
            setBandera(false);
            
        }
    }
    
}
