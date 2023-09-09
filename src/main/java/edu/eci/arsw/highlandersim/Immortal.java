package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private AtomicInteger health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    private Semaforo semaforo;

    private Semaforo semaforoStop;

    private boolean estoyVivo;


    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb ,Semaforo semaforo, Semaforo semaforoStop) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = new AtomicInteger(health);
        this.defaultDamageValue=defaultDamageValue;
        this.semaforo = semaforo;
        this.semaforoStop = semaforoStop;
        estoyVivo = true;
    }

    public void run() {

        while (estoyVivo && semaforoStop.getBandera())  {
           
            Immortal im;

            if(!semaforo.getBandera()){
                synchronized(semaforo){
                    try {
                        semaforo.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            int myIndex = -1;
            int nextFighterIndex = 0;

            synchronized(immortalsPopulation){
                myIndex = immortalsPopulation.indexOf(this);
                if(!immortalsPopulation.isEmpty()){
                    nextFighterIndex = r.nextInt(immortalsPopulation.size());
                }

                //avoid self-fight
                if (nextFighterIndex == myIndex) {
                    nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
                }
            }

            if(nextFighterIndex != myIndex){

                im = immortalsPopulation.get(nextFighterIndex);

                this.fight(im);

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    public void fight(Immortal i2) {

        //synchronized(this){

        synchronized(immortalsPopulation){
            if (i2.getHealth() > 0 && estoyVivo) {
                i2.changeHealth(- defaultDamageValue);
                this.changeHealth(defaultDamageValue);
                updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
            } else if(estoyVivo){
                    updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
                    if(immortalsPopulation.size() > 1){
                        //Se elimina el inmortal muerto de la lista
                        immortalsPopulation.remove(i2);
                    }
            }else{
                if(immortalsPopulation.size() > 1){
                    //Se elimina el inmortal muerto de la lista
                    immortalsPopulation.remove(this);
                }
            }

        }

        //}

    }

    public void changeHealth(int v) {
        //Obtiene el valor actual en memoria (variable), lo setea y lo vuelve a agregar
        estoyVivo = health.addAndGet(v)>0;
    }

    public int getHealth() {
        //Permite retornar el AtomicInteger como un entero
        return health.intValue();
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
