package com.example.monisha.propertymarketing.Modules;

import com.example.monisha.propertymarketing.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by harser777 on 10/27/2016.
 */


@Module
public class NetworkConnectionModule {
    @Provides @Singleton
    Constants provideConstants(){
        return new Constants();
    }

}
