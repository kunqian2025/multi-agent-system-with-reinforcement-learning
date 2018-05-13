/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jni;

/**
 *
 * @author Rannum
 */
import java.io.*;
import java.net.*;
// import java.util.concurrent.Semaphore;

/**
 *
 * @author larry
 */
public class udp_client {

    DatagramSocket serverSocket = null;
    int port;
    byte[] receiveData;
    char color;
    int[][] Sphero_locations = {{100,100},{300,100}};
    //private final Semaphore mutex = new Semaphore(1, true);


    public udp_client( ){
        try {
            serverSocket = new DatagramSocket(55000);
            receiveData = new byte[23];
            serverSocket.setSoTimeout(1000);
        }
        catch (IOException e){
            System.err.println("IOException :udp" + e);
        }

    }
    public void setPort(int port){
        this.port = port;
    }
    public void getPacket() {
        DatagramPacket incoming = new DatagramPacket(this.receiveData, this.receiveData.length);
        try {
		//System.out.println("before blocking");
                this.serverSocket.receive(incoming);//timeout 1s
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());
                //System.out.println(incoming.getLength() + ", " + s);
                parseMessage(s);   
        }
        catch (IOException e){
            System.err.println("IOException: udp recieve " + e);
        }
    }

    public void parseMessage(String msg){
        String gString = null;
        String bString = null;
	//System.out.print("test");
        if ( !msg.isEmpty()) {
            gString = msg.substring(2, 11);
            bString = msg.substring(14, 23);
            if (!gString.substring(0, 4).equals("eeee")) {
                if(this.color == 'g'){
                    this.Sphero_locations[0][0] = Short.valueOf(gString.substring(0, 4));
                    this.Sphero_locations[0][1] = Short.valueOf(gString.substring(5, 9));
                }else{
                    this.Sphero_locations[1][0] = Short.valueOf(gString.substring(0, 4));
                    this.Sphero_locations[1][1] = Short.valueOf(gString.substring(5, 9));
                }
            }
            if (!bString.substring(0, 4).equals("eeee")) {
                if(this.color == 'g'){
                    this.Sphero_locations[1][0] = Short.valueOf(bString.substring(0, 4));
                    this.Sphero_locations[1][1] = Short.valueOf(bString.substring(5, 9));
                }else{
                    this.Sphero_locations[0][0] = Short.valueOf(bString.substring(0, 4));
                    this.Sphero_locations[0][1] = Short.valueOf(bString.substring(5, 9));
                }
            }
        }
        //System.out.println("\033[32m" + "location: " + this.Sphero_locations[0][0] + ", " + this.Sphero_locations[0][1] + "; " + this.Sphero_locations[1][0] + ", " + this.Sphero_locations[1][1] + "\033[0m");
    }
    public char getColor(){

        return this.color;
    }
    public void setColor(char color){

        this.color = color;
    }
    public int[][] get_Sphero_locations(){
        return this.Sphero_locations;
    }

}
