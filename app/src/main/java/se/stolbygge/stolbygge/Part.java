package se.stolbygge.stolbygge;

import java.io.Serializable;

public class Part implements Serializable {

    private String name;
    private String artNo;
    private String imgName;
    private int amount;

    // noob constructor for starters
    public Part() {
        this.name = "Skruv";
        this.artNo = "1337";
        this.amount = 7;
    }

    public Part(String name, String artNo, String imgName, int amount) {
        this.name = name;
        this.artNo = artNo;
        this.imgName = imgName;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getArtNo() {
        return artNo;
    }

    public String getImgName() {
        return imgName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtNo(String artNo) {
        this.artNo = artNo;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
