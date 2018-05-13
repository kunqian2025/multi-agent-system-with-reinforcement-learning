/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;


import behaviours.handleRequestForGateways;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This implements a BROKER agent.
 * A broker agent contacts all available GATEWAY agents, 
 * in order to request them to do perform specific tasks.
 * Gateway agents can refuse the request or agree to them.
 * The BROKER agent handles the FIPA-REQUEST Interaction protocol
 * based communication via an AchieveREInitiator behaviour.
 * 
 *
 * @author brehm
 */
public class broker extends BasicAgent{
    private int gatewayNum = 2;
    private DFAgentDescription[] result = null;
    
    @Override
    protected void setup() {
        super.setup();   
        System.out.println("\033[34mBroker Agent " + getLocalName() + " starting\033[0m");
        
        // Register agent as a broker
        this.registerwithDF("broker");
        
        //this.doWait(1000);
        do{
            try {
                this.doWait(1000);
                this.result = this.searchAgents("gateway");
            } catch (FIPAException ex) {
                Logger.getLogger(broker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while(this.result.length < this.gatewayNum);
        this.doWait(5000);
        addBehaviour(new handleRequestForGateways(this, this.result));
    }
}