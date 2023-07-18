package com.example.group6.finalproject_fitnesstracker;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject
{
    @PrimaryKey
    private String uuid;

    private String username;

    private String password;


    
}
