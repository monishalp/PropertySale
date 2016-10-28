package com.example.harser777.a10_26_dagger2_1;

import javax.inject.Inject;

/**
 * Created by harser777 on 10/26/2016.
 */

public class Vehicle {

    private Motor motor;

    @Inject
    public Vehicle(Motor motor){
        this.motor = motor;
    }

    public void increaseSpeed(int value){
        motor.accelerate(value);
    }

    public void stop(){
        motor.brake();
    }

    public int getSpeed(){
        return motor.getRpm();
    }
}
