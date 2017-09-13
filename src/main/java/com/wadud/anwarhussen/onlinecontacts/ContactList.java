package com.wadud.anwarhussen.onlinecontacts;

/**
 * Created by Anwar Hussen on 14-Aug-16.
 */
public class ContactList {

    private String name;
    private String phoneNUmber;


    private String id;
    public ContactList(String id, String name, String phoneNUmber){
        this.id = id;
        this.name = name;
        this.phoneNUmber = phoneNUmber;
    }
    public ContactList(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNUmber() {
        return phoneNUmber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNUmber(String phoneNUmber) {
        this.phoneNUmber = phoneNUmber;
    }
}
