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
    private double [] weights_for_target = {    0.78
                                                ,1.13
                                                ,2.58
                                                ,0.50
                                                ,3.40
                                                ,1.68
                                                ,4.60
                                                ,0.63
                                                ,7.69
                                                ,1.60
                                                ,5.42
                                                ,1.94
                                                ,1.01
                                                ,1.21
                                                ,0.34
                                                ,6.39
                                                ,0.12
                                                ,6.62
                                                ,-0.48
                                                ,2.17
                                                ,-0.58
                                                ,0.21
                                                ,-2.20
                                                ,0.36
                                                ,0.10
                                                ,-0.55
                                                ,-4.01
                                                ,1.54
                                                ,-3.18
                                                ,1.33
                                                ,-4.19
                                                ,-1.35
                                                ,0.69
                                                ,-0.59
                                                ,-1.33
                                                ,-5.75
                                                ,1.09
                                                ,-6.14
                                                ,-1.81
                                                ,-2.71
                                                ,1.37};
    private double [] weights_for_station = {   1.00
                                                ,0.71
                                                ,1.06
                                                ,0.34
                                                ,0.94
                                                ,1.22
                                                ,5.00
                                                ,-0.42
                                                ,5.99
                                                ,0.81
                                                ,0.72
                                                ,1.50
                                                ,0.94
                                                ,0.88
                                                ,0.64
                                                ,3.41
                                                ,1.42
                                                ,5.61
                                                ,0.54
                                                ,5.49
                                                ,0.70
                                                ,0.78
                                                ,-1.41
                                                ,0.95
                                                ,-0.74
                                                ,1.50
                                                ,-5.30
                                                ,-0.40
                                                ,-5.27
                                                ,0.18
                                                ,-1.89
                                                ,-0.81
                                                ,1.08
                                                ,-0.02
                                                ,1.53
                                                ,-4.01
                                                ,0.41
                                                ,-2.58
                                                ,1.73
                                                ,-3.36
                                                ,1.09};
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
