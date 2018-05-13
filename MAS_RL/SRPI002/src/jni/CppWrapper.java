/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jni;

/**
 *
 * @author Rannum
 */
public class CppWrapper {
    
    public native void mconnect(String addr);
    public native void row(int x , int y);
    public native void setcolour(char c);
    
    public CppWrapper(){
        
    }
    
}
