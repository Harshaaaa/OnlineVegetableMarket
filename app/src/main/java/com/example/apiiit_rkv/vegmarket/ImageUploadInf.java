package com.example.apiiit_rkv.vegmarket;

public class ImageUploadInf {

    String itemname,itemtitle,itemurl,key;
    int itemcost;
    public ImageUploadInf() {

    }

    public ImageUploadInf(String itemname, String itemtitle, String itemurl, int itemcost, String key) {
        this.itemname = itemname;
        this.itemtitle = itemtitle;
        this.itemcost=itemcost;
        this.key=key;
        this.itemurl = itemurl;
    }

    public void setItemtitle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getItemcost() {
        return itemcost;
    }

    public void setItemcost(int itemcost) {
        this.itemcost = itemcost;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemtitle() {
        return itemtitle;
    }

    public void setItemtitlle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    public String getItemurl() {
        return itemurl;
    }

    public void setItemurl(String itemurl) {
        this.itemurl = itemurl;
    }


}