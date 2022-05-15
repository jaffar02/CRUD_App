package com.jaffar.firebasedatabase;

public class Employee {
    String name;
    String key;
    String position;

    public Employee(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public Employee(){ }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
