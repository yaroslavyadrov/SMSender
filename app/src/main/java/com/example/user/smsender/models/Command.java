package com.example.user.smsender.models;

/**
 * Created by user on 25.01.15.
 */
public class Command {
    public int id;
    public String phoneNumber;
    public String name;
    public String text;
    public String lastDate;
    public String color;
    public int getID(){
        return this.id;
    }
}
