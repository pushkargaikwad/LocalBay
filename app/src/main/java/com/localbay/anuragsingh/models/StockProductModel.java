package com.localbay.anuragsingh.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by anuragsingh on 04/01/16.
 */
public class StockProductModel implements Parcelable {

    ParseUser parseUser;
    CatalogModel catalogModel;
    PriceModel priceModel;


    ParseObject parseCatalogModel;

    private Integer stock_units;
    private String condition;
    private Integer dispatchTime;

    /**
     * CONSTRUCTOR
     */
    public StockProductModel() {
        parseUser = ParseUser.getCurrentUser();
    }

    /**
     * SAVE STOCK PRODUCT
     */
    public ParseObject saveStockProduct() throws ParseException {

        ParseObject productObject = new ParseObject("StockProduct");
        productObject.put("stock_units", stock_units);
        productObject.put("condition", condition);
        productObject.put("dispatch_time", dispatchTime);

        ParseObject priceObject = priceModel.savePrice();
        ParseRelation priceRelation = productObject.getRelation("prices");
        priceRelation.add(priceObject);

        ParseObject parseCatalogModel = getParseCatalogModel();
        ParseRelation catalogRelation = productObject.getRelation("catalog_product");
        catalogRelation.add(parseCatalogModel);

        ParseRelation userRelation = productObject.getRelation("sold_by");
        userRelation.add(parseUser);

        productObject.save();

        return productObject;
    }

    /**
     * UPDATE STOCK PRODUCT
     *
     * @return success/failure boolean
     */
    public boolean updateStockProduct() throws ParseException {
        return false;
    }

    public ParseUser getParseUser() {
        return parseUser;
    }

    public void setParseUser(ParseUser parseUser) {
        this.parseUser = parseUser;
    }


    public ParseObject getParseCatalogModel() {
        return parseCatalogModel;
    }

    public void setParseCatalogModel(ParseObject parseCatalogModel) {
        this.parseCatalogModel = parseCatalogModel;
    }

    public CatalogModel getCatalogModel() {
        return catalogModel;
    }

    public void setCatalogModel(CatalogModel catalogModel) {
        this.catalogModel = catalogModel;
    }

    public PriceModel getPriceModel() {
        return priceModel;
    }

    public void setPriceModel(PriceModel priceModel) {
        this.priceModel = priceModel;
    }

    public Integer getStock_units() {
        return stock_units;
    }

    public void setStock_units(Integer stock_units) {
        this.stock_units = stock_units;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(int dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    /**
     * PARCELLABLE METHODS
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stock_units);
        dest.writeString(condition);
        dest.writeInt(dispatchTime);
    }

    private StockProductModel(Parcel in) {
        stock_units = in.readInt();
        condition = in.readString();
        dispatchTime = in.readInt();
    }

    public static final Parcelable.Creator<StockProductModel> CREATOR = new Parcelable.Creator<StockProductModel>() {
        @Override
        public StockProductModel createFromParcel(Parcel source) {
            StockProductModel product = new StockProductModel(source);
            return product;
        }

        public StockProductModel[] newArray(int size) {
            return new StockProductModel[size];
        }
    };
}
