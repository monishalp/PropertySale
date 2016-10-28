package com.example.monisha.propertymarketing.Interface;

import com.example.monisha.propertymarketing.Constants;
import com.example.monisha.propertymarketing.Modules.NetworkConnectionModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by harser777 on 10/27/2016.
 */
@Singleton
@Component(modules={NetworkConnectionModule.class})
public interface LogInComponent {

    Constants provideConstants();
}
