package com.example.user.smsender;

/**
 * Created by user on 09.02.15.
 */
public class SendEvent {
    public final String phoneNumber;
    public final String message;
    public SendEvent(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
}
