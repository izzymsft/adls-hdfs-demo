package com.microsoft.ocp.models;

public class Student {

    private String firstName;

    private String lastName;

    private double gpa;

    private double score;

    private boolean married;

    private int age;

    public Student()
    {
        this.age = 18;
    }

    public String getFirstName() {
        return firstName;
    }

    public Student setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Student setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public double getGpa() {
        return gpa;
    }

    public Student setGpa(double gpa) {
        this.gpa = gpa;
        return this;
    }

    public double getScore() {
        return score;
    }

    public Student setScore(double score) {
        this.score = score;
        return this;
    }

    public boolean isMarried() {
        return married;
    }

    public Student setMarried(boolean married) {
        this.married = married;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Student setAge(int age) {
        this.age = age;
        return this;
    }
}
