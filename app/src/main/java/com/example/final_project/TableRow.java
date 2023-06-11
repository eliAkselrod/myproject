package com.example.final_project;

public class TableRow {
    private String team;
    private int p;
    private int w;
    private int l;
    private int d;
    private int pts;

    public TableRow(String team, int p, int w, int l, int d, int pts) {
        this.team = team;
        this.p = p;
        this.w = w;
        this.l = l;
        this.d = d;
        this.pts = pts;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setP(int p) {
        this.p = p;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setL(int l) {
        this.l = l;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public String getTeam() {
        return team;
    }

    public int getP() {
        return p;
    }

    public int getW() {
        return w;
    }

    public int getL() {
        return l;
    }

    public int getD() {
        return d;
    }

    public int getPts() {
        return pts;
    }

}
