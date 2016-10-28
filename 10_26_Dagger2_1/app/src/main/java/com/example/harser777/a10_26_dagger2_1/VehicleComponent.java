package com.example.harser777.a10_26_dagger2_1;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by harser777 on 10/26/2016.
 */

@Singleton
@Component(modules = {VehicleModule.class})
public interface VehicleComponent {

    Vehicle provideVehicles();
}
