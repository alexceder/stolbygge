package se.stolbygge.stolbygge;

import java.io.Serializable;

public class Part implements Serializable {

    private String name;
    private String artNo;
    private String imgName;
    private String geometry;
    private int amount;
    private boolean findable;
    private boolean found;

    public Part() {
        //
    }

    public Part(String name, String artNo, String imgName, int amount) {
        this.name = name;
        this.artNo = artNo;
        this.imgName = imgName;
        this.amount = amount;
        this.findable = true;
        this.found = false;
    }

    public Part(String name, String artNo, String imgName, String geometry, int amount) {
        this.name = name;
        this.artNo = artNo;
        this.imgName = imgName;
        this.geometry = geometry;
        this.amount = amount;
        this.findable = true;
        this.found = false;
    }

    public Part(String name, String artNo, String imgName, String geometry, int amount, boolean findable) {
        this.name = name;
        this.artNo = artNo;
        this.imgName = imgName;
        this.geometry = geometry;
        this.amount = amount;
        this.findable = findable;
        this.found = false;
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

    public String getGeometry() {
        return geometry;
    }

    public boolean isFound() {
        return found;
    }

    public boolean isFindable() {
        return findable;
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

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public void setFindable(boolean findable) {
        this.findable = findable;
    }
}
