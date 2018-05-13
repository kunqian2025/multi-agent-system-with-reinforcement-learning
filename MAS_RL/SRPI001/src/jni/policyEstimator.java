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
public class policyEstimator {
    
    localEnvironment localEnv = null;
    private double [] weights_for_target = {    0.46
                                                ,0.99
                                                ,4.01
                                                ,0.21
                                                ,6.98
                                                ,0.40
                                                ,2.69
                                                ,0.84
                                                ,2.92
                                                ,0.50
                                                ,-3.17
                                                ,4.64
                                                ,0.37
                                                ,7.59
                                                ,-0.92
                                                ,0.74
                                                ,0.47
                                                ,1.48
                                                ,-1.23
                                                ,-1.84
                                                ,0.62
                                                ,1.22
                                                ,-3.47
                                                ,0.56
                                                ,-2.46
                                                ,1.33
                                                ,-0.67
                                                ,0.70
                                                ,1.92
                                                ,1.52
                                                ,6.22
                                                ,-4.66
                                                ,0.66
                                                ,-6.15
                                                ,-1.22
                                                ,-0.22
                                                ,-0.55
                                                ,-1.02
                                                ,-0.95
                                                ,1.88
                                                ,-0.59};
    private double [] weights_for_station = {   0.69
                                                ,1.10
                                                ,6.59
                                                ,-0.07
                                                ,4.76
                                                ,0.27
                                                ,1.32
                                                ,0.44
                                                ,1.32
                                                ,0.78
                                                ,-0.83
                                                ,3.90
                                                ,0.98
                                                ,4.93
                                                ,2.18
                                                ,2.60
                                                ,0.25
                                                ,0.26
                                                ,1.43
                                                ,-4.00
                                                ,1.99
                                                ,2.16
                                                ,-5.18
                                                ,0.45
                                                ,-5.35
                                                ,-0.10
                                                ,-0.07
                                                ,1.69
                                                ,-0.29
                                                ,0.22
                                                ,2.04
                                                ,-4.94
                                                ,-0.63
                                                ,-2.52
                                                ,0.02
                                                ,-0.31
                                                ,0.43
                                                ,-1.10
                                                ,0.37
                                                ,5.05
                                                ,-1.39};
    private double [] weights = new double[41];
    public policyEstimator(localEnvironment env ){
        this.localEnv = env;
        this.weights = this.weights_for_target;
    }
    public void set_weights_for_station(){
        this.weights = this.weights_for_station;
    }
    public void set_weights_for_target(){
        this.weights = this.weights_for_target;
    }
    
    public double [] predict(int[][] agent_locations){
        double [] preferences_exp = new double[this.localEnv.actionSpace.length];
        double preferences_sum = 0;
        double [] probabilities = new double[this.localEnv.actionSpace.length];
        this.localEnv.set_current_location(agent_locations);
        double [][]features = this.localEnv.get_scaled_policy_features();
        int feature_len = this.localEnv.featureLen;
        
        for(short i=0; i<this.localEnv.actionSpace.length; i++)
        {
            double tem_val = 0;
            for(int j=0; j<feature_len+1; j++){
                tem_val = tem_val + features[i][j]*this.weights[j];
            }
            preferences_exp[i] = Math.exp(tem_val);  
            preferences_sum += preferences_exp[i];
        }
        for(short i=0; i<this.localEnv.actionSpace.length; i++)
        {
            probabilities[i] = preferences_exp[i]/preferences_sum;  
            System.out.println(i + "th probability:" + probabilities[i]);
        }
        return probabilities;
    }
}
