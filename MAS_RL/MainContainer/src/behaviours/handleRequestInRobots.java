/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.robot;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * this action is to deal with the received message
 * 
 * @author Kun
 */

public class handleRequestInRobots  extends CyclicBehaviour{
    private robot myAgent = null;
    private boolean rqstEnabled = false;//if already issued a requst
    private boolean jobDone = false;//if the agreed job is done
    private ACLMessage senderMsg = null;//record the sender
    
    private int debugTest = 0;
    
    public handleRequestInRobots(robot a) {
        this.myAgent = a;
    }
    
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(this.rqstEnabled)//if already agree
        {
            if(msg != null)
            {
                if(msg.getPerformative() == ACLMessage.REQUEST)//block other requests
                {
                    //send back refurse
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.REFUSE);
                    this.myAgent.send(reply);
                    //msg = null;
                }
            }

            if(this.jobDone)//if job is done, send an information to the agreed request sender
            {
                System.out.println("\033[32m" + this.getAgent().getName() 
                        + " send -> 'INFORM': "  + " to " 
                        + senderMsg.getSender().getName()+ "\033[0m");   
                ACLMessage inform = senderMsg.createReply();
                inform.setContent("Job is done!");
                inform.setPerformative(ACLMessage.INFORM);
                this.myAgent.send(inform);

                this.rqstEnabled = false;	
                this.jobDone = false;
            }
        }
        else//have not agreed with any request
        {
            if(msg != null)
            {
                if(msg.getPerformative() == ACLMessage.REQUEST)//if there is a request, agree with it
                {
                    this.rqstEnabled = true;
                    this.senderMsg = msg;

                    ACLMessage agree = senderMsg.createReply();
                    agree.setPerformative(ACLMessage.AGREE);
                    this.myAgent.send(agree); //send agree back
                    
                    this.myAgent.set_state_to_location();//change the state of Sphero in hdlMoving
                }
            }
        }	
        //System.out.println("\033[32m" + "gateway request action count: " + (debugTest++)+ "\033[0m");
    }
    public void set_jobDone(){
        this.jobDone = true;
    }
}
