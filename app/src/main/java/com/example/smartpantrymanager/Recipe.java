package com.example.smartpantrymanager;

public class Recipe {
    private long id;
    private String name;
    private String steps;

    public Recipe() {}

    public Recipe(long id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }
}