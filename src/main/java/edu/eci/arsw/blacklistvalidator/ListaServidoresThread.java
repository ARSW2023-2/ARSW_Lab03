package edu.eci.arsw.blacklistvalidator;

import java.lang.Thread;
import java.util.LinkedList;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class ListaServidoresThread extends Thread {

    private int inicioSeccion;
    private int finSeccion;
    //cantidad de ocurrencias de la ip en la lista negra
    private int cantOcurrencias;
    private String ipaddress;
    //Cantidad de servidores que se encuentran en la lista negra
    private int cantServidores;
    private LinkedList<Integer> blackListOcurrences;
    private Contador contador;

    public ListaServidoresThread(int inicioSeccion, int finSeccion, String ipaddress,Contador contador) {
        super();
        this.inicioSeccion = inicioSeccion;
        this.finSeccion = finSeccion;
        this.ipaddress = ipaddress;
        this.blackListOcurrences=new LinkedList<>();
        this.contador=contador;

    }

    public int getCanOcurrencias() {
        return cantOcurrencias;
    }

    public int getCantServidores() {
        return cantServidores;
    }

    public LinkedList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }

    @Override
    public void run(){
        int i = inicioSeccion;
        while(contador.getBandera() && i<finSeccion){
            if (HostBlacklistsDataSourceFacade.getInstance().isInBlackListServer(i, ipaddress)) {
                blackListOcurrences.add(i);
                cantOcurrencias++;
                contador.aumentarConteo();
            }
            cantServidores++;
            i++;

        }
    }
     
}
