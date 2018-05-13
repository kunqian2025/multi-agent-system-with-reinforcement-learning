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

public class Sphero{
    CppWrapper myWrapper;//Sphero API 
    static {
        //System.loadLibrary("Spheron");
        try {
            System.load("/home/pi/Desktop/n/libJniWrapper.so");
        }
        catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
        }
    }
    public Sphero(char color)
    {
        myWrapper = new CppWrapper();
        //"CD:72:26:BA:E7:32"
        //"C5:A2:54:D0:39:C9"
        myWrapper.mconnect("C5:A2:54:D0:39:C9");
        //myWrapper.setcolour(color);
        //System.out.println("test");
   
    }
    public void move(int heading)
    {      
        this.myWrapper.row(heading,20);
    }   
    public void stop(){
        this.myWrapper.row(0,0);
    }
}


