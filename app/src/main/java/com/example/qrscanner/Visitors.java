package com.example.qrscanner;

public class Visitors {

    private  String Name, Id, Sem, branch;

    public Visitors(String name, String id, String sem, String branch) {
        Name = name;
        Id = id;
        Sem = sem;
        this.branch = branch;
    }

    public Visitors() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getSem() {
        return Sem;
    }

    public void setSem(String sem) {
        this.Sem = sem;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
