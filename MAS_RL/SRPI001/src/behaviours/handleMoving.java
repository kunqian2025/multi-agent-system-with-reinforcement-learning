/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agents.robot;
import jade.core.behaviours.CyclicBehaviour;
import jni.Sphero;
import jni.localEnvironment;
import jni.policyEstimator;

/**
 *
 * @author Kun
 */
enum MoveState{
    TO_LOCATION,WAIT,INFORM,TO_STATION,NO_MOVE
}
public class handleMoving extends CyclicBehaviour{
    private robot myAgent = null;
    
    private localEnvironment env = new localEnvironment();//current environment
    private policyEstimator myPE = new policyEstimator(env);//estimate values
    
    private Sphero mySphero = new Sphero('b');//new Spheron('b');

    private MoveState state = MoveState.NO_MOVE; //current state of Sphero
    
    private int debugTest = 0;
    
    private int status = 0; //detect if hit the obstacle or reach the goal
    private int[][] agent_locations = new int[2][2];
    private double[] probabilities = new double[env.actionSpace.length];//probabilities for each action
    private int action_idx = 0;//chosen action index
    private int[] hrad = {0,90,180,255};//heading angle
    
    public handleMoving(robot a) {
        this.myAgent = a;
    }
    
    @Override
    public void action() {
        switch(this.state){
            case TO_LOCATION:
                System.out.println("\033[32m" + this.getAgent().getName() 
                        + " is moving to location"+ "\033[0m");
                this.agent_locations = this.myAgent.get_agent_locations();
                System.out.println("\033[32m" + "location: " + this.agent_locations[0][0] + ", " + this.agent_locations[0][1] + "; " + this.agent_locations[1][0] + ", " + this.agent_locations[1][1] + "\033[0m");
                
                this.status = this.env.set_current_location(this.agent_locations);
                //System.out.println("\033[32m" + "status: "+ this.status + "\033[0m");
                if(this.status == 1){
                    //this.mySphero.stop();
                    this.state = MoveState.WAIT;
                }
                else{
                    if(this.status == -1){
                        //this.mySphero.stop();
                        this.state = MoveState.INFORM;
                    } 
                }
                this.probabilities = this.myPE.predict(this.agent_locations);
                this.action_idx =  this.randsample(this.env.actionSpace, this.probabilities);
                System.out.println("\033[32m" + "action_idx: "+ this.action_idx + "\033[0m");
                this.mySphero.move(this.hrad[this.action_idx]);
                this.myAgent.doWait(50);
                this.mySphero.stop();

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
                this.env.set_goal_to_station();//next goal is going to be station
                this.myPE.set_weights_for_station();
            break;
            case TO_STATION:
                System.out.println("\033[32m" + this.getAgent().getName() 
                        + " is moving back to station"+ "\033[0m");
                this.agent_locations = this.myAgent.get_agent_locations();
                System.out.println("\033[32m" + "location: " + this.agent_locations[0][0] + ", " + this.agent_locations[0][1] + "; " + this.agent_locations[1][0] + ", " + this.agent_locations[1][1] + "\033[0m");
                
                this.status = this.env.set_current_location(this.agent_locations);
                if(this.status == 1){
                    //this.mySphero.stop();
                    this.state = MoveState.NO_MOVE;
                }
                else{
                    if(this.status == -1){
                        //this.mySphero.stop();
                        this.state = MoveState.TO_STATION;
                    } 
                }
                this.probabilities = this.myPE.predict(this.agent_locations);
                this.action_idx =  this.randsample(this.env.actionSpace, this.probabilities);
                System.out.println("\033[32m" + "action_idx: "+ this.action_idx + "\033[0m");
                this.mySphero.move(this.hrad[this.action_idx]);
                this.myAgent.doWait(50);
                this.mySphero.stop();        
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
        this.env.set_goal_to_target();
        this.myPE.set_weights_for_target();
    }
    
     /*______________________________________________________________________*/
    /************
     * random sample one value in the values array based on the probability distribution
     * @param values
     * @param pdf is the probability distribution
     * @return index of the value chosen
     */
    public int randsample(int[] values, double [] pdf) {
        double randomValue = Math.random();
        double[] cdf = new double[pdf.length];
        cdf[0] = pdf[0];
        for(int i=1; i<pdf.length; i++) {
            cdf[i] = cdf[i-1] + pdf[i];
        }
        int results;
        int currentPosition = 0;
        while(randomValue > cdf[currentPosition] && currentPosition < cdf.length) {
            currentPosition++; //Check the next one.
        }
        if(currentPosition < cdf.length) {
            results = values[currentPosition];
        } else {
            results = values[cdf.length-1]; 
        }
        return results;
    }
}