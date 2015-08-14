package com.velvetcase.app.material.Models;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prashant Patil on 09-06-2015.
 */
public class ProductResponseParser {

    public static ProductResponseParser instance = null;
    private JSONObject response;
    private ArrayList<Products> productList;
    private ArrayList<Designer> DesignerList;
    private ArrayList<Create> CreateList;
    private ArrayList<Template> LooksList;
    private ArrayList<Template> TemplatesList;
    private ArrayList<Template> SavedLooksList;
    private ArrayList<Products> SingleDesignerList;
    private ArrayList<Products> SingleProdcutList;
    private ArrayList<Products> DiscoverNewProductList;
    private ArrayList<Products> DiscoverSimilarProductList;


    public static ProductResponseParser getInstance() {
        if (instance == null) {
            instance = new ProductResponseParser();
        }
        return instance;
    }

    public void UpdateProductList(int product_id ,boolean flag,String product_type) {

        if (product_type.equalsIgnoreCase("Product")) {
            try {
                for (int i =0; i<productList.size(); i++){
                    Products p = productList.get(i);
                    if(p.getproduct_id() == product_id){

                        productList.get(i).getproductJsonobj().put("wishlist_flag",flag);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (product_type.equalsIgnoreCase("Designer")) {
            try {
                for (int i =0; i<SingleDesignerList.size(); i++){
                    Products p = SingleDesignerList.get(i);
                    if(p.getproduct_id() == product_id){

                        SingleDesignerList.get(i).getproductJsonobj().put("wishlist_flag",flag);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (product_type.equalsIgnoreCase("Looks")){
            try {
                for (int i =0; i<SingleProdcutList.size(); i++){
                    Products p = SingleProdcutList.get(i);
                    if(p.getproduct_id() == product_id){

                        SingleProdcutList.get(i).getproductJsonobj().put("wishlist_flag",flag);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (product_type.equalsIgnoreCase("SavedLooks")){
            try {
                for (int i =0; i<productList.size(); i++){
                    Products p = productList.get(i);
                    if(p.getproduct_id() == product_id){

                        productList.get(i).getproductJsonobj().put("wishlist_flag",flag);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



    public void UpdateSingleDesignerList(int pos ,boolean flag) {
        try {
            SingleDesignerList.get(pos).getproductJsonobj().put("wishlist_flag",flag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void UpdateDesignerList(int designer_id ,boolean flag) {

            try {
                for (int i =0; i<DesignerList.size(); i++){
                    Designer d = DesignerList.get(i);
                    if(d .getdesigner_id() == designer_id){

                        DesignerList.get(i).getdesignerJsonObj().put("wishlist_flag",flag);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

    }



    public void parseProductsResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultProducts(new JSONArray(response.getString("cartlist")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void parseDesignerResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultDesigner(new JSONArray(response.getString("Designer_List")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void parseCreateResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultCreate(new JSONArray(response.getString("Create_List")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void parseLooksResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultLooks(new JSONArray(response.getString("template_data")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseTemplateResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultTemplate(new JSONArray(response.getString("template_data")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseSingleDesignerListResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultSingleDesigner(new JSONArray(response.getString("cartlist")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void parseSingleProductDataResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultSingleProduct(new JSONArray(response.getString("cartlist")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseDiscoverNewProductDataResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultDiscoverNewProduct(new JSONArray(response.getString("cartlist")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseDiscoverSimilarProductDataResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultDiscoverSimilarProduct(new JSONArray(response.getString("cartlist")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseSavedLooksProductDataResponse(String _response) {
        try {
            this.response = new JSONObject(_response);
            setResultSavedLooksProduct(new JSONArray(response.getString("template_data")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setResultProducts(JSONArray _jsonArray) {
        productList = new ArrayList<Products>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Products offer_obj = new Products((JSONObject) JSONUtil.get(jsonArray, i));
            productList.add(offer_obj);
        }
    }
    public void setResultDesigner(JSONArray _jsonArray) {
        DesignerList = new ArrayList<Designer>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Designer obj = new Designer((JSONObject) JSONUtil.get(jsonArray, i));
            DesignerList.add(obj);
        }
    }
    public void setResultCreate(JSONArray _jsonArray) {
        CreateList = new ArrayList<Create>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Create obj = new Create((JSONObject) JSONUtil.get(jsonArray, i));
            CreateList.add(obj);
        }
    }
    public void setResultLooks(JSONArray _jsonArray) {
        LooksList = new ArrayList<Template>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Template obj = new Template((JSONObject) JSONUtil.get(jsonArray, i));
            LooksList.add(obj);
        }
    }
    public void setResultTemplate(JSONArray _jsonArray) {
        TemplatesList = new ArrayList<Template>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Template obj = new Template((JSONObject) JSONUtil.get(jsonArray, i));
            TemplatesList.add(obj);
        }
    }

    public void setResultSingleDesigner(JSONArray _jsonArray) {
        SingleDesignerList = new ArrayList<Products>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Products offer_obj = new Products((JSONObject) JSONUtil.get(jsonArray, i));
            SingleDesignerList.add(offer_obj);
        }
    }

    public void setResultSingleProduct(JSONArray _jsonArray) {
        SingleProdcutList = new ArrayList<Products>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Products offer_obj = new Products((JSONObject) JSONUtil.get(jsonArray, i));
            SingleProdcutList.add(offer_obj);
        }
    }
    public void setResultDiscoverNewProduct(JSONArray _jsonArray) {
        DiscoverNewProductList = new ArrayList<Products>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Products offer_obj = new Products((JSONObject) JSONUtil.get(jsonArray, i));
            DiscoverNewProductList.add(offer_obj);
        }
    }

    public void setResultDiscoverSimilarProduct(JSONArray _jsonArray) {
        DiscoverSimilarProductList = new ArrayList<Products>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Products offer_obj = new Products((JSONObject) JSONUtil.get(jsonArray, i));
            DiscoverSimilarProductList.add(offer_obj);
        }
    }

    public void setResultSavedLooksProduct(JSONArray _jsonArray) {
        SavedLooksList = new ArrayList<Template>();
        JSONArray jsonArray = _jsonArray;
        for (int i = 0; i < jsonArray.length(); i++) {
            Template offer_obj = new Template((JSONObject) JSONUtil.get(jsonArray, i));
            SavedLooksList.add(offer_obj);
        }
    }
    public ArrayList<Products> getResultProducts() {
        return productList;
    }
    public ArrayList<Designer> getResultDesignerList() {
        return DesignerList;
    }
    public ArrayList<Create> getResultCreateList() {
        return CreateList;
    }
    public ArrayList<Template> getResultLooksList() {
        return LooksList;
    }
    public ArrayList<Template> getResultTemplateList() {
        return TemplatesList;
    }
    public ArrayList<Products> getResultSingleDesigner() {
        return SingleDesignerList;
    }
    public ArrayList<Products> getResultSingleProdcut() {
        return SingleProdcutList;
    }
    public ArrayList<Products> getResultDiscoverNewProduct() {
        return DiscoverNewProductList;
    }
    public ArrayList<Products> getResultDiscoverSimilarProduct() {
        return DiscoverSimilarProductList;
    }
    public ArrayList<Template> getResultSavedLooksProduct() {
        return SavedLooksList;
    }
}
