package com.tamboraagungmakmur.winwin.Model;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Tambora on 29/11/2016.
 */
public class TabModel {

    private ImageView imgIcon;
    private TextView txTitle;
    private LinearLayout lyTab;

    public TabModel(ImageView imgIcon, TextView txTitle, LinearLayout lyTab) {
        this.imgIcon = imgIcon;
        this.txTitle = txTitle;
        this.lyTab = lyTab;
    }

    public ImageView getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(ImageView imgIcon) {
        this.imgIcon = imgIcon;
    }

    public TextView getTxTitle() {
        return txTitle;
    }

    public void setTxTitle(TextView txTitle) {
        this.txTitle = txTitle;
    }

    public LinearLayout getLyTab() {
        return lyTab;
    }

    public void setLyTab(LinearLayout lyTab) {
        this.lyTab = lyTab;
    }
}
