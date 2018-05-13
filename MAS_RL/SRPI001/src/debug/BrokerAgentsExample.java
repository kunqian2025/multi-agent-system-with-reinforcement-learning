/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package debug;

import jade.Boot;

/**
 * This demonstrates how JADE can be started using the Java main class.
 * For debugging purposes, this allows for more convenient 
 * starting of multiple agents. To make this more generic, 
 * one could add some arguments to the main class, to define
 * the type and number of agents to be stared. 
 * 
 * @author brehm
 */
public class BrokerAgentsExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //This defines how many BROKER agents should be started.
        Integer numBrokerAgents = 0; 
        
        
        // A string is concatenated for the BROKER agents, as an argument to the Jade.Boot command.
        String brokerAgents = "";  
        for(Integer i=0; i < numBrokerAgents; i++) {
            brokerAgents=brokerAgents+"broker"+i.toString()+":agents.broker;";
        }
        
        String robotAgent = "robot0:agents.robot;";    
        
        // Start JADE with the above defined agents agents.
        String[] parameters = new String[] { 
                 //"-gui",                        //Starts the agent management tools
                 "-container",                //Uncomment if you want to run a remote container on a different host 
                 "-host", "192.168.1.12",    //Replace this with your local IP.
                 "-port", "12345",              //The port number for inter-platform communication
                 brokerAgents+robotAgent};
        
        Boot.main(parameters);	
    }
    
}
