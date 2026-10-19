package com.raichess.event;

public class PersonChanged {
    private final String type;
    private final int id;

    public PersonChanged(String type, int id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
