/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import behaviours.handleMoving;
import behaviours.handleRequestInRobots;
import behaviours.handleUDP;
import jade.core.behaviours.ThreadedBehaviourFactory;
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
public class robot extends BasicAgent{
    private handleRequestInRobots hdlRequest = null;
    private handleMoving hdlMoving = null;
    private handleUDP hdlUDP = null;
    
    @Override
    protected void setup() {
        super.setup();
        System.out.println("\033[32mRobot Agent " + getLocalName() + " starting\033[0m");
        //Register agents as a broker
        this.registerwithDF("robot");
        //Wait for incoming requests from broker agents.
        MessageTemplate template = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
        
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        hdlUDP = new handleUDP();
        addBehaviour(tbf.wrap(hdlUDP));
        tbf.resume(hdlUDP);
        
        hdlRequest = new handleRequestInRobots(this);
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

    public int[][] get_agent_locations(){
        return this.hdlUDP.get_agent_locations();
    }
}