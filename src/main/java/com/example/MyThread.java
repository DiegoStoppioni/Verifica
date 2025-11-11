package com.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    ArrayList<Integer> disponibilita = new ArrayList<>();
 
    public MyThread(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        disponibilita.add(10); // Gold
        disponibilita.add(30); // Pit
        disponibilita.add(60); // Parterre

     

        try {
            out.println("WELCOME");

            boolean flag = false;
            while (!flag) {
                
                String messaggio = in.readLine();
                if (messaggio == null || messaggio.equals("") ) {
                    out.println("ERR LOGINREQUIRED");
                    
                }else{
                   String[] login = messaggio.split(" ", 2);

                    if (login.length == 2 && login[0].equals("LOGIN") && !(login[1] == "")) {
                       
                        flag = true;
                        out.println("OK");
                    } else {
                        out.println("ERR LOGINREQUIRED");
                    } 
                }      
            }

            while (true) {
              
                String scelta = in.readLine();

                if (scelta.equals("QUIT")){
                    out.println("BYE");
                    socket.close();
                    break;
                }
                
                switch(scelta){

                    
                    case "N":
                        out.println("AVAIL Gold: " + disponibilita.get(0)+ " Pit:" + disponibilita.get(1) + " Parterre: " + disponibilita.get(2));
                        break;
                    case "BUY":
                        String messaggio = in.readLine();
                        String[] biglietti = messaggio.split(" ", 2);
                        int quantita = Integer.parseInt(biglietti[1]);
                        
                        if(biglietti.length == 2 && biglietti[0].equals("Gold") && !(biglietti[1] == "")){                         
                            if(quantita <= disponibilita.get(0)){
                                disponibilita.set(0, (disponibilita.get(0) - quantita));
                                out.println("OK");
                            }else{
                                out.println("KO");
                            }
                        }else if(biglietti.length == 2 && biglietti[0].equals("Pit") && !(biglietti[1] == "")){
                            if(quantita <= disponibilita.get(1)){
                                disponibilita.set(1, (disponibilita.get(1) - quantita));
                                out.println("OK");
                            }else{
                                out.println("KO");
                            }
                        }else if(biglietti.length == 2 && biglietti[0].equals("Partere") && !(biglietti[1] == "")){
                            if(quantita <= disponibilita.get(2)){
                                disponibilita.set(2, (disponibilita.get(2) - quantita));
                                out.println("OK");
                            }else{
                                out.println("KO");
                            }
                        }else{
                            out.println("ERR UNKNOWNTYPE");
                        }

                        if(disponibilita.get(0) == 0){
                            out.println("GOLD esaurito");
                        }else if(disponibilita.get(1) == 0){
                            out.println("PIT esaurito");
                        }else if(disponibilita.get(2) == 0){
                            out.println("PARTERE esaurito");
                        }
                        break;

                    default:
                        out.println("ERR UNKNOWNCMD");
                        break;

                }
            }
        }catch (IOException e) {
            System.err.println("Errore nel thread: " + e.getMessage());
        }
    }
}
