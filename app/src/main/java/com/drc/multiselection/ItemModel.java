package com.drc.multiselection;

public class ItemModel {

    public String name;
    private boolean isSelected;
    private boolean isExpanded;

    public ItemModel(String name) {
        this.name = name;
        this.isSelected = false; // Initialize isSelected to false
        this.isExpanded = false; // Initialize isExpanded to false
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }



}