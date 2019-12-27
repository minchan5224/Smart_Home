package com.example.minch.smart_home_mate_rsapberry;

import android.graphics.drawable.Drawable;

/**
 * Created by minch on 2018-11-11.
 */

public class ListViewBtnItem {
    private Drawable iconDrawable;
    private String titleStr;
    private String descStr;
    public void setIcon(Drawable icon){
        iconDrawable = icon;
    }
    public void setTitle(String title){
        titleStr = title;
    }
    public void setDesc(String desc){
        descStr = desc;
    }
    public Drawable getIcon(){
        return this.iconDrawable;
    }
    public String getTitle(){
        return this.titleStr;
    }
    public String getDesc(){
        return this.descStr;
    }

}
