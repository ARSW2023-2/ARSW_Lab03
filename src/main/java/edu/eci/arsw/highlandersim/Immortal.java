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


    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb ,Semaforo semaforo) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = new AtomicInteger(health);
        this.defaultDamageValue=defaultDamageValue;
        this.semaforo = semaforo;
    }

    public void run() {

        while (true) {
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

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void fight(Immortal i2) {

        synchronized(this){
            //synchronized(i2){
            if (i2.getHealth() > 0) {
                i2.changeHealth(- defaultDamageValue);
                this.changeHealth(defaultDamageValue);
                updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
            } else {
                updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
            }

            //}
        }

    }

    public void changeHealth(int v) {
        //Obtiene el valor actual en memoria (variable), lo setea y lo vuelve a agregar
        health.addAndGet(v);
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
