package com.velvetcase.app.material.Models;

import java.util.ArrayList;

/**
 * Created by Prashant Patil on 09-06-2015.
 */
public class AllProductsModelBase {
    private static AllProductsModelBase instance;
    private  int ProductID;
    private  String ProductBGImg;
    private  String TemplateID;
    private  int DesignerID;
    private String TemplateSelectionFlag;
    private String ProductSelectionFlag;

    public String getProductSelectionFlag() {
        return ProductSelectionFlag;
    }

    public void setProductSelectionFlag(String productSelectionFlag) {
        this.ProductSelectionFlag = productSelectionFlag;
    }

    public String getTemplateSelectionFlag() {
        return TemplateSelectionFlag;
    }
    public void setTemplateSelectionFlag(String templateSelectionFlag) {
        TemplateSelectionFlag = templateSelectionFlag;
    }

    public static AllProductsModelBase getInstance() {
        if (instance == null) {
            instance = new AllProductsModelBase();
        }
        return instance;
    }

    public static void setInstance(AllProductsModelBase instance) {
        AllProductsModelBase.instance = instance;
    }
    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }
    public String getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(String TemplateID) {
        this.TemplateID = TemplateID;
    }
    public int getDesignerID() {
        return DesignerID;
    }

    public void setDesignerID(int DesignerID) {
        this.DesignerID = DesignerID;
    }
    public String getProductBGImg() {
        return ProductBGImg;
    }

    public void setProductBGImg(String ProductBGImg) {
        this.ProductBGImg = ProductBGImg;
    }

    public void GetProductDataFromServer(String response) {
        ProductResponseParser.getInstance().parseProductsResponse(response);
    }
    public ArrayList<Products> getProductsList() {
        return ProductResponseParser.getInstance().getResultProducts();
    }
    public void GetDesignerDataFromServer(String response) {
        ProductResponseParser.getInstance().parseDesignerResponse(response);
    }
    public ArrayList<Designer> getDesignerList() {
        return ProductResponseParser.getInstance().getResultDesignerList();
    }
    public void GetCreateDataFromServer(String response) {
        ProductResponseParser.getInstance().parseCreateResponse(response);
    }
    public ArrayList<Create> getCreateList() {
        return ProductResponseParser.getInstance().getResultCreateList();
    }
    public void GetLooksDataFromServer(String response) {
        ProductResponseParser.getInstance().parseLooksResponse(response);
    }
    public ArrayList<Template> getLooksList() {
        return ProductResponseParser.getInstance().getResultLooksList();
    }
    public void GetTemplateDataFromServer(String response) {
        ProductResponseParser.getInstance().parseTemplateResponse(response);
    }
    public ArrayList<Template> getTemplateList() {
        return ProductResponseParser.getInstance().getResultTemplateList();
    }

    public void GetSingleDesignerDataFromServer(String response) {
        ProductResponseParser.getInstance().parseSingleDesignerListResponse(response);
    }
    public ArrayList<Products> getSingleDesignerList() {
        return ProductResponseParser.getInstance().getResultSingleDesigner();
    }


    public void GetSingleProductDataFromServer(String response) {
        ProductResponseParser.getInstance().parseSingleProductDataResponse(response);
    }
    public ArrayList<Products> getSingleProductData() {
        return ProductResponseParser.getInstance().getResultSingleProdcut();
    }

    public void GetDiscoverNewDataFromServer(String response) {
        ProductResponseParser.getInstance().parseDiscoverNewProductDataResponse(response);
    }
    public ArrayList<Products> getDiscoverNewProductData() {
        return ProductResponseParser.getInstance().getResultDiscoverNewProduct();
    }
    public void GetDiscoverSimilarDataFromServer(String response) {
        ProductResponseParser.getInstance().parseDiscoverSimilarProductDataResponse(response);
    }
    public ArrayList<Products> getDiscoverSimilarProductData() {
        return ProductResponseParser.getInstance().getResultDiscoverSimilarProduct();
    }

    public void GetSavedLooksDataFromServer(String response) {
        ProductResponseParser.getInstance().parseSavedLooksProductDataResponse(response);
    }
    public ArrayList<Template> getSavedLooksProductData() {
        return ProductResponseParser.getInstance().getResultSavedLooksProduct();
    }

    public void UpdateListData(int prod_id , boolean flag,String product_type){
        ProductResponseParser.getInstance().UpdateProductList(prod_id,flag,product_type);
    }

    public void UpdateDesignListData(int designer_id , boolean flag){
        ProductResponseParser.getInstance().UpdateDesignerList(designer_id,flag);
    }

    public void UpdateSingleDesignerListData(int pos , boolean flag){
        ProductResponseParser.getInstance().UpdateSingleDesignerList(pos,flag);
    }
}
