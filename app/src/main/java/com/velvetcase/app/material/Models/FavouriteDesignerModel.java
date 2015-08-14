package com.velvetcase.app.material.Models;

/**
 * Created by Prashant Patil on 11-06-2015.
 */
public class FavouriteDesignerModel {

    String id;
    String designer_id;
    String designer_name;
    String designer_products;
    String designer_img;
    String designer_city;
    String flag_url;
    public String getdesigner_id() {
        return designer_id;
    }

    public void setdesigner_id(String designer_id) {
        this.designer_id = designer_id;
    }

    public String getId() {
        return id;
    }

    public String getFlag_url() {
        return flag_url;
    }

    public void setFlag_url(String flag_url) {
        this.flag_url = flag_url;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDesigner_city() {
        return designer_city;
    }

    public void setDesigner_city(String designer_city) {
        this.designer_city = designer_city;
    }

    public String getDesigner_img() {
        return designer_img;
    }

    public void setDesigner_img(String designer_img) {
        this.designer_img = designer_img;
    }

    public String getDesigner_products() {
        return designer_products;
    }

    public void setDesigner_products(String designer_products) {
        this.designer_products = designer_products;
    }

    public String getDesigner_name() {
        return designer_name;
    }

    public void setDesigner_name(String designer_name) {
        this.designer_name = designer_name;
    }
}
