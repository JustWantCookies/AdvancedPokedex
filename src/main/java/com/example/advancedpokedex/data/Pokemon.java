package com.example.advancedpokedex.data;

public class Pokemon {
    //TODO fill data here
    private String name;

    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Pokemon (String name, int level){
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
