/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.gateway;
import jade.core.behaviours.CyclicBehaviour;

/**
 *
 * @author Kun
 */
enum MoveState{
    TO_LOCATION,WAIT,INFORM,TO_STATION,NO_MOVE
}
public class handleMoving extends CyclicBehaviour{
    private gateway myAgent = null;

    private MoveState state = MoveState.NO_MOVE; //current state of Sphero
    
    private int debugTest = 0;
    

    
    public handleMoving(gateway a) {
        this.myAgent = a;
    }
    
    @Override
    public void action() {
        switch(this.state){
            case TO_LOCATION:
                System.out.println("\033[32m" + this.getAgent().getName() 
                        + " is moving to location"+ "\033[0m");
                this.debugTest++;
                this.myAgent.doWait(1000);
                if(this.debugTest > 10)
                {
                    this.debugTest = 0;
                    this.state = MoveState.WAIT;
                }

            break;
            case WAIT:
                System.out.println("\033[32m" + this.getAgent().getName() 
                        + " is at a location"+ "\033[0m");
                this.debugTest++;
                this.myAgent.doWait(1000);
                if(this.debugTest > 3)
                {
                    this.debugTest = 0;
                    this.state = MoveState.INFORM;
                } 
            break;
            case INFORM:
                System.out.println("\033[32m" + this.getAgent().getName() 
                        + " has done the job"+ "\033[0m");
                
                this.myAgent.set_jobDone();//set the jobDone flag in the hdlRequest
                this.state = MoveState.TO_STATION;
            break;
            case TO_STATION:
                System.out.println("\033[32m" + this.getAgent().getName() 
                        + " is moving back to station"+ "\033[0m");
                this.debugTest++;
                this.myAgent.doWait(1000);
                if(this.debugTest > 10)
                {
                    this.debugTest = 0;
                    this.state = MoveState.NO_MOVE;
                }        
            break;
            case NO_MOVE:
                System.out.println("\033[32m" + this.getAgent().getName() 
                        + " is standing by"+ "\033[0m");
                this.myAgent.doWait(1000);
            break;
            default:
                System.out.println("\033[32m" + this.getAgent().getName() 
                        + " is standing by"+ "\033[0m");
                this.myAgent.doWait(1000);
        }
    }
   
    public void set_state_to_location(){
        this.state = MoveState.TO_LOCATION;
    }
}