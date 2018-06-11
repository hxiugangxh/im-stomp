package com.us.example.bean;

import java.security.Principal;

public final class MyPrincipal implements Principal {

    private final String name;

    public MyPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}