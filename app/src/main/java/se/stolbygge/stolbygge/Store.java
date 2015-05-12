package se.stolbygge.stolbygge;

import java.util.ArrayList;

public final class Store {

    private final static Store instance = new Store();

    private ArrayList<Part> parts;

    private ArrayList<Step> steps;

    private Store() {
        // TODO: Read this from persistent store.

        // Create the parts
        parts = new ArrayList<>();
        parts.add(new Part("Insexskruv", "1", "insexskruv", "insexskruv", 6, false, false));
        parts.add(new Part("Insexnyckel", "1", "insexnyckel", "insexnyckel", 1, false, false));
        parts.add(new Part("Vänster benpar", "1", "vanster_benpar", "sida", 1, true, true));
        parts.add(new Part("Plugg", "1", "plugg", "plugg", 2, true, false));
        parts.add(new Part("Höger benpar", "1", "hoger_benpar", "sida", 1, true, true));
        parts.add(new Part("Sits", "1", "sitts", "sits", 1, true, true));
        parts.add(new Part("Ryggstöd", "1", "ryggstod", "ryggtopp", 1, true, true));
        parts.add(new Part("Ryggstödsdekoration", "1", "ryggstodsdekoration", "ryggstod", 1, true, true));

        // NOTE: The parts list in each step is a SHALLOW copy -- this is intended.
        // NOTE: You might think we need a getByName or something and extend the ArrayList
        //       so that this could be done easier. But manipulation like this should not
        //       be necessary outside of the adapter.
        // Create the steps
        steps = new ArrayList<>();
        ArrayList<Part> temp = new ArrayList<>();
        temp.add(parts.get(6));
        temp.add(parts.get(7));
        temp.add(parts.get(3));
        steps.add(new Step("Steg 1", 1, "steg_1", new ArrayList<>(temp)));
        temp.clear();

        temp.add(parts.get(5));
        steps.add(new Step("Steg 2", 2, "steg_2", new ArrayList<>(temp)));
        temp.clear();

        temp.add(parts.get(2));
        steps.add(new Step("Steg 3", 3, "steg_3", new ArrayList<>(temp)));
        temp.clear();

        temp.add(parts.get(2));
        steps.add(new Step("Steg 4", 4, "steg_4", new ArrayList<>(temp)));
        temp.clear();

        temp.add(parts.get(0));
        temp.get(0).setAmount(3);
        steps.add(new Step("Steg 5", 5, "steg_5", new ArrayList<>(temp)));
        temp.clear();

        temp.add(parts.get(0));
        temp.get(0).setAmount(3);
        steps.add(new Step("Steg 6", 6, "steg_6", new ArrayList<>(temp)));
        temp.clear();
    }

    public static Store getInstance() {
        return instance;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public ArrayList<Part> getParts() {
        return parts;
    }

    public ArrayList<Part> getFindableParts() {
        ArrayList<Part> identifiableParts = new ArrayList<>();

        for (Part p : parts) {
            if (p.isFindable()) {
                identifiableParts.add(p);
            }
        }

        return identifiableParts;
    }

    public ArrayList<Part> getBigParts() {
        ArrayList<Part> bigParts = new ArrayList<>();

        for (Part p : parts) {
            if (p.isBig()) {
                bigParts.add(p);
            }
        }

        return bigParts;
    }
}
