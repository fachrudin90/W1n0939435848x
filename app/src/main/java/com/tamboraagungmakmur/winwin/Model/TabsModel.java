package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 29/11/2016.
 */
public class TabsModel {

    private int imgSelected;
    private int imgUnselected;
    private int bgColor;

    public TabsModel() {
    }

    public TabsModel(int imgSelected, int imgUnselected, int bgColor) {
        this.imgSelected = imgSelected;
        this.imgUnselected = imgUnselected;
        this.bgColor = bgColor;
    }

    public int getImgSelected() {
        return imgSelected;
    }

    public void setImgSelected(int imgSelected) {
        this.imgSelected = imgSelected;
    }

    public int getImgUnselected() {
        return imgUnselected;
    }

    public void setImgUnselected(int imgUnselected) {
        this.imgUnselected = imgUnselected;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}
