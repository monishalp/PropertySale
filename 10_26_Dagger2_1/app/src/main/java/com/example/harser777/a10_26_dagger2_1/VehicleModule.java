package com.example.harser777.a10_26_dagger2_1;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by harser777 on 10/26/2016.
 */

@Module
public class VehicleModule {

    @Provides
    @Singleton
    Motor provideMotor(){
        return new Motor();
    }

    @Provides @Singleton
    Vehicle provideVehicle(){
        return new Vehicle(new Motor());
    }
}
