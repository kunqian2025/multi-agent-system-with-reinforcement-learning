/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import java.util.Date;


/**
 *
 * @author Kun
 */
public class handleRequestForRobots extends CyclicBehaviour{

    private Agent myAgent;
    private DFAgentDescription[] gatewayAgents;
    
    private int numGoal = 2;
    private int rqstAgreed = 0;
    private boolean rqstComplete = false;

    private int rqstInformed = 0;
    private boolean goalComplete = false;
    
    private int rqstCnt = 0;
    
    private int rqstSent = 0;
    
    public handleRequestForRobots(Agent a, DFAgentDescription[] result) {
        myAgent = a;    
        gatewayAgents = result;
    }
    
    @Override
    public void action() {
        if(rqstSent>0)
        {
            ACLMessage msg = myAgent.receive();
            if(msg != null)
            {
                if(msg.getPerformative() == ACLMessage.REFUSE)
                {
                    System.out.println("\033[34m" + this.getAgent().getName() 
                                + " received -> 'REFUSE' from " 
                                + msg.getSender().getName() + "\033[0m");
                    rqstSent -= 1;//decrease the count so that a new request will be sent
                }
                if(msg.getPerformative() == ACLMessage.AGREE)
                {
                    System.out.println("\033[34m" + this.getAgent().getName() 
                                + " received -> 'AGREE' from " 
                                + msg.getSender().getName()+ "\033[0m");
                    rqstAgreed += 1;
                    if (rqstAgreed == numGoal){//when two agrees has been received, it stops requesting
                        rqstComplete = true;
                    }  
                } 
                if(msg.getPerformative() == ACLMessage.INFORM)
                {
                    System.out.println("\033[34m" + this.getAgent().getName() 
                        + " received -> 'INFORM': " + msg.getContent() + "from " 
                        + msg.getSender().getName()+ "\033[0m");    
                    rqstInformed += 1;
                    if (rqstInformed == numGoal){//two inform has been received, succeed
                        rqstComplete = false;
                        rqstSent = 0;
                        rqstCnt = 0;
                        myAgent.doWait(10000);
                    }
                }
            }
        }
        if(!rqstComplete){
            if(rqstSent < numGoal){
                rqstSent += 1;//the number of reqeusts will have been sent
                if(rqstCnt < gatewayAgents.length)
                {
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);       
                    msg.addReceiver(gatewayAgents[rqstCnt].getName());

                    // Set the FIPA protocol to FIPA_REQUEST, this is important as a 
                    // parameter for the AchieveREInitiator behaviour 
                    msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                    msg.setReplyByDate(new Date(System.currentTimeMillis() + 15000));

                    // Set some dummy content, here the request could be specified, e.g. to deliver an item "screw" 
                    msg.setContent("Send you a FIPA_REQUEST");

                    myAgent.doWait(200);
                    myAgent.send(msg);
                    System.out.println("\033[34m" + myAgent.getName() + " send a request to " 
                                                    + gatewayAgents[rqstCnt].getName().getName() + "\033[0m");
                    rqstCnt++;
                }
                else
                {   
                    myAgent.doWait(3000);
                    rqstCnt = 0;
                }         
            }
        }
    }
    
}
