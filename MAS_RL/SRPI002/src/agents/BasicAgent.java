/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * This is an abstract class for the agents to be implemented. 
 * We assume all agents share some common characteristics, 
 * behaviours and implement common methods.
 * 
 * @author brehm
 */
public abstract class BasicAgent extends Agent{
    
    public void registerwithDF(String type) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() ); 
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( type );
        sd.setName( getLocalName() );
        dfd.addServices(sd);
        try {  
            DFService.register(this, dfd );  
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
    }
        
    protected DFAgentDescription[] searchAgents(String type) throws FIPAException {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType(type);
        dfd.addServices(sd);

        DFAgentDescription[] result = DFService.search(this, dfd);

        return result;
    }
}
