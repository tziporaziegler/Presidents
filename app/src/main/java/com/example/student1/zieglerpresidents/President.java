package com.example.student1.zieglerpresidents;

import java.io.Serializable;

public class President implements Serializable {

    private int number;
    private String president;
    private int birthYear;
    private Integer death_year; //can be null

    //@SerializedName(took_office) = read took_office as tookOffice
    private String tookOffice;
    private String leftOffice;
    private String party;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birth_year) {
        this.birthYear = birth_year;
    }

    public Integer getDeathYear() {
        return death_year;
    }

    public void setDeathYear(int death_year) {
        this.death_year = death_year;
    }

    public String getTookOffice() {
        return tookOffice;
    }

    public void setTookOffice(String took_office) {
        this.tookOffice = took_office;
    }

    public String getLeftOffice() {
        return leftOffice;
    }

    public void setLeftOffice(String left_office) {
        this.leftOffice = left_office;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }
}
