/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.robot;
import jade.core.behaviours.SimpleBehaviour;
import java.util.logging.Level;
import java.util.logging.Logger;
import jni.udp_client;

/**
 *
 * @author Kun
 */
public class handleUDP extends SimpleBehaviour{
    
    private udp_client myClient = null;//UDP client to get position info 
    private int[][]agent_locations = {{100,100},{300,100}};;
    
    public handleUDP() {
        this.myClient = new udp_client();
        this.myClient.setColor('b');
    }
    
    @Override
    public void action() {
        while(true){
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(robot.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.myClient.getPacket();
            //System.out.println("\033[32m" + "gateway UDP action count: " + (debugTest++)+ "\033[0m");
            this.agent_locations = this.myClient.get_Sphero_locations();
        }
    }
    public int[][] get_agent_locations(){
        return this.agent_locations;
    }
    @Override
    public boolean done() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
