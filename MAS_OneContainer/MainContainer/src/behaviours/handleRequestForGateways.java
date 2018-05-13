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
public class handleRequestForGateways extends CyclicBehaviour{

    private Agent myAgent;
    private DFAgentDescription[] gatewayAgents;
    
    private int rqstCnt = 0;
    
    private boolean rqstSent = false;
    
    public handleRequestForGateways(Agent a, DFAgentDescription[] result) {
        myAgent = a;    
        gatewayAgents = result;
    }
    
    @Override
    public void action() {
        //if (gatewayAgents.length == 0) return;
        
        if(rqstSent)
        {
            ACLMessage msg = myAgent.receive();
            if(msg != null)
            {
                //rqstSent = false;
                if(msg.getPerformative() == ACLMessage.REFUSE)
                {
                    rqstSent = false;
                    System.out.println("\033[34m" + this.getAgent().getName() 
                                + " received -> 'REFUSE' from " 
                                + msg.getSender().getName() + "\033[0m");
                }
                if(msg.getPerformative() == ACLMessage.AGREE)
                {
                    //rqstCnt = gatewayAgents.length;
                    System.out.println("\033[34m" + this.getAgent().getName() 
                                + " received -> 'AGREE' from " 
                                + msg.getSender().getName()+ "\033[0m");
                    
                } 
                if(msg.getPerformative() == ACLMessage.INFORM)
                {
                    rqstSent = false;
                    rqstCnt = gatewayAgents.length;
                    System.out.println("\033[34m" + this.getAgent().getName() 
                        + " received -> 'INFORM': " + msg.getContent() + "from " 
                        + msg.getSender().getName()+ "\033[0m");    
                    myAgent.doWait(30000);
                }
            }
        }
        else
        {
            if(rqstCnt < gatewayAgents.length)
            {
                rqstSent = true;
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
                myAgent.doWait(30000);
                rqstCnt = 0;
            }
        }
        
    }
    
}
