package se.stolbygge.stolbygge;

import java.util.ArrayList;

public class Step {

    ArrayList<Part> parts;
    private String stepName;
    private int stepNr;
    private String imgName;
    private boolean checked;

    public Step() {
        this.stepName = "Steg 1000";
        this.stepNr = 1000;
    }

    public Step(String stepName, int stepNr, String imgName, ArrayList<Part> parts) {
        this.stepName = stepName;
        this.stepNr = stepNr;
        this.parts = parts;
        this.imgName = imgName;
        this.checked = false;
    }

    public ArrayList<Part> getParts() {
        return parts;
    }

    public String getStepName() {
        return stepName;
    }

    public int getStepNr() {
        return stepNr;
    }

    public String getImgName() {
        return imgName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setPart(ArrayList<Part> parts) {
        this.parts = parts;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public void setStepNr(int stepNr) {
        this.stepNr = stepNr;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
