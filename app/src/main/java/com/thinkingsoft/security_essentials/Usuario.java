package com.thinkingsoft.security_essentials;

public class Usuario {

    String uid;
    String email;
    String name;

    public Usuario(String uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

    public Usuario() {
    }

    public String getName() {
        return name;
    }
    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }
}
