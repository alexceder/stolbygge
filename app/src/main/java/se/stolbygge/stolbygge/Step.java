package se.stolbygge.stolbygge;


import java.util.ArrayList;

public class Step {

    ArrayList<Part> parts;
    private String step_name;
    private int step_nr;
    private String imgName;

    /* Behöver ha stegnamn och stegnr för att kunna "hämta" steg
    *
    * Behöver innehålla en tillhörande bild och en lista med delar som behöver användas.
    * Ha en array av parts. Borde kunna hämtas från den tidigare parts-arrayen.
    *
    * */

    public Step(){
        this.step_name = "Steg 1000";
        this.step_nr = 1000;
    }

    public Step(String step_name, int step_nr, String imgName, ArrayList<Part> parts){
        this.step_name = step_name;
        this.step_nr = step_nr;
        this.parts = parts;
        this.imgName = imgName;
    }

    public String getName(){ return step_name; }

    public int getNr(){ return step_nr; }

    public ArrayList<Part> getParts(){ return parts;}

    public String getImgName(){ return imgName;}

    public void setName(String step_name){ this.step_name = step_name;}

    public void setNr(int step_nr){ this.step_nr = step_nr;}

    public void setPart(ArrayList<Part> parts){ this.parts = parts;}

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }


}
