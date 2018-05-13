/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import behaviours.handleMoving;
import behaviours.handleRequestInGateways;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 * This implements a dummy gateway agent.
 * 
 * All the agent does is, registering as GATEWAY agent with the DF and 
 * then implementing a behaviour based on AchieveREResponder to handle
 * incoming requests.
 *
 * @author brehm
 */
public class gateway extends BasicAgent{
    private handleRequestInGateways hdlRequest = null;
    private handleMoving hdlMoving = null;
    
    @Override
    protected void setup() {
        super.setup();
        System.out.println("\033[32mGateway Agent " + getLocalName() + " starting\033[0m");
        //Register agents as a broker
        this.registerwithDF("gateway");
        //Wait for incoming requests from broker agents.
        MessageTemplate template = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
        hdlRequest = new handleRequestInGateways(this);
        addBehaviour(hdlRequest);

        hdlMoving = new handleMoving(this);
        addBehaviour(hdlMoving);
        
    }
    
    public void set_state_to_location(){
        this.hdlMoving.set_state_to_location();
    }
    public void set_jobDone(){
        this.hdlRequest.set_jobDone();
    }
}
