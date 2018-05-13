/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jni;

/**
 *
 * @author Kun
 */
public class localEnvironment {

    //private int[] mapSize = {400, 400};
    public int[] actionSpace = {0, 1, 2, 3};
    public int featureLen = 10*4;//feature length 10 × the action length 4
    private int[][] locA = {{100, 100},{100, 300}};//location of the agent
    private int[][] locS = {{100, 100},{100, 300}};//location of the station
    private int[][] locG = new int[2][2];//location of the goal
    private int[][] locT = {{200, 200},{300, 200}};//location of the target

    public localEnvironment( ){
        this.locG = this.locT;
    }
    
    public int set_current_location(int[][] agent_locations){
        //System.out.println("location: " + agent_locations[0][1] + ',' +  + agent_locations[0][0] + ';' + agent_locations[1][0] + ',' + agent_locations[1][1]);
        int rtVal = 0;
        double dist1 = Math.sqrt(((this.locG[0][0]-agent_locations[0][0])*(this.locG[0][0]-agent_locations[0][0]))+((this.locG[0][1]-agent_locations[0][1])*(this.locG[0][1]-agent_locations[0][1])));
        double dist2 = Math.sqrt(((this.locG[1][0]-agent_locations[1][0])*(this.locG[1][0]-agent_locations[1][0]))+((this.locG[1][1]-agent_locations[1][1])*(this.locG[1][1]-agent_locations[1][1])));
        //System.out.println("dist1: " + dist1);
        //System.out.println("dist2: " + dist2);
        if(dist1 < 10){
            if(dist2 < 10){
                rtVal = 1;
                return rtVal;
            }
        }
        dist1 = Math.sqrt(((this.locG[1][0]-agent_locations[0][0])*(this.locG[1][0]-agent_locations[0][0]))+((this.locG[1][1]-agent_locations[0][1])*(this.locG[1][1]-agent_locations[0][1])));
        dist2 = Math.sqrt(((this.locG[0][0]-agent_locations[1][0])*(this.locG[0][0]-agent_locations[1][0]))+((this.locG[0][1]-agent_locations[1][1])*(this.locG[0][1]-agent_locations[1][1])));
        //System.out.println("dist1: " + dist1);
        //System.out.println("dist2: " + dist2);
        if(dist1 < 10){
            if(dist2 < 10){
                rtVal = 1;
                return rtVal;
            }
        }
        double dist3 = Math.sqrt(((agent_locations[1][0]-agent_locations[0][0])*(agent_locations[1][0]-agent_locations[0][0]))+((agent_locations[1][1]-agent_locations[0][1])*(agent_locations[1][1]-agent_locations[0][1])));
        if(dist3 <  10){
            rtVal = -1;
            return rtVal;
        }
        this.locA[0][0] = agent_locations[0][0];
        this.locA[0][1] = agent_locations[0][1];
        this.locA[1][0] = agent_locations[1][0];
        this.locA[1][1] = agent_locations[1][1];
        return rtVal;
    }
    public void set_goal_to_station(){
        this.locG = this.locS;
    }
    public void set_goal_to_target(){
        this.locG = this.locT;
    }
    
    public double [][] get_scaled_policy_features(){
        
        double max_angle = 3.14;
        double a1_to_goal1 = this.clc_angle(this.locG[0][0] - this.locA[0][0],this.locG[0][1] - this.locA[0][1]);
        double a1_to_goal2 = this.clc_angle(this.locG[1][0] - this.locA[0][0],this.locG[1][1] - this.locA[0][1]);
        double a2_to_goal1 = this.clc_angle(this.locG[0][0] - this.locA[1][0],this.locG[0][1] - this.locA[1][1]);
        double a2_to_goal2 = this.clc_angle(this.locG[1][0] - this.locA[1][0],this.locG[1][1] - this.locA[1][1]);
        double a1_to_a2 = this.clc_angle(this.locA[1][0] - this.locA[0][0],this.locA[1][1] - this.locA[0][1]);
        
        double sin_a1_goal1 = Math.sin(a1_to_goal1)/max_angle;
        double cos_a1_goal1 = Math.cos(a1_to_goal1)/max_angle;
        double sin_a1_goal2 = Math.sin(a1_to_goal2)/max_angle;
        double cos_a1_goal2 = Math.cos(a1_to_goal2)/max_angle;
        double sin_a2_goal1 = Math.sin(a2_to_goal1)/max_angle;
        double cos_a2_goal1 = Math.cos(a2_to_goal1)/max_angle;
        double sin_a2_goal2 = Math.sin(a2_to_goal2)/max_angle;
        double cos_a2_goal2 = Math.cos(a2_to_goal2)/max_angle;
        double sin_a1_a2 = Math.sin(a1_to_a2)/max_angle;
        double cos_a1_a2 = Math.cos(a1_to_a2)/max_angle;
        
        double [][]features = new double [this.actionSpace.length][this.featureLen+1];
        // initialize the feature values as 0
        for(int i=0; i<this.actionSpace.length; i++){
            for(int j=0; j<this.featureLen+1; j++){
                features[i][j] = 0;
            }
        }
        
        for(int action=0; action < this.actionSpace.length;  action++)
        {
            features[action][0] = 1;
            features[action][1+action*10] = sin_a1_goal1;
            features[action][2+action*10] = cos_a1_goal1;
            features[action][3+action*10] = sin_a1_goal2;
            features[action][4+action*10] = cos_a1_goal2;
            features[action][5+action*10] = sin_a2_goal1;
            features[action][6+action*10] = cos_a2_goal1;
            features[action][7+action*10] = sin_a2_goal2;
            features[action][8+action*10] = cos_a2_goal2;
            features[action][9+action*10] = sin_a1_a2;
            features[action][10+action*10] = cos_a1_a2;
        }
        return features;
    }
    
    
    /**************
     * calculate the angle between two points
     * @param x differences in x axis
     * @param y differences in y axis
     * @return angle between 0 - 2*pi
     */
    public double clc_angle(int x, int y){
        //System.out.println("\033[32m" + "differences: " + x + ", " + y  + "\033[0m");
        double angle;
        if(x == 0){//when x == 0, we cannot use atan2to calc
            if(y  == 0){
               angle = 0;     
            }
            else{
                if (y > 0){
                    angle = 1.57;// pi/2
                }
                else{
                    angle = -1.57;// -pi/2
                }
            }
        }else{
            angle = Math.atan2(y, x);
        }
        return angle;  
    }
}
