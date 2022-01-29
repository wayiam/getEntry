package com.example.qrscanner;

public class Visitors {

    private  String Name, Id, Sem, branch,time;

    public Visitors(String name, String id, String sem, String branch,String t) {
        this.Name = name;
        this.Id = id;
        this.Sem = sem;
        this.time = t;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
