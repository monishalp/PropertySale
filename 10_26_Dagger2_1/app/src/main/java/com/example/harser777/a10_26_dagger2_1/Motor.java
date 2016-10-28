package com.example.harser777.a10_26_dagger2_1;

/**
 * Created by harser777 on 10/26/2016.
 */

public class Motor {

    private int rpm;

    public Motor(){
        this.rpm=0;
    }

    public int getRpm(){
        return rpm;
    }

    public void accelerate(int value){
        rpm=rpm+value;
    }

    public void brake(){
        rpm=0;
    }
}
