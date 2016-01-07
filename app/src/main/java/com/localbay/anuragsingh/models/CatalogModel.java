package com.localbay.anuragsingh.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by anuragsingh on 03/01/16.
 */
public class CatalogModel implements Parcelable {


    public CatalogModel(String manufacturer, String title, String category, String subCategory, String variant, String url) {
        this.manufacturer = manufacturer;
        this.title = title;
        this.category = category;
        this.subCategory = subCategory;
        this.variant = variant;
        this.imageUrl = url;

        this.shortDescription = "Lorem Ipsum";
    }

    /**
     * Save Product on Parse
     */
    public boolean saveProduct() {
        CatalogModel catalogModel = this;
        Boolean saveStatus = false;

        ParseObject product = new ParseObject("Catalog");
        product.put("manufacturer", catalogModel.getManufacturer());
        product.put("title", catalogModel.getTitle());
        product.put("category", catalogModel.getCategory());
        product.put("subCategory", catalogModel.getSubCategory());
        product.put("imageUrl", catalogModel.getImageUrl());
        product.put("variant", catalogModel.getVariant());

        product.put("shortDescription", catalogModel.getShortDescription());

        product.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    e.printStackTrace();
            }
        });

        return saveStatus;
    }

    public CatalogModel getProduct() {
        return this;
    }

    public ParseObject getParseObject() {

        final ParseObject[] object = new ParseObject[1];
        ParseQuery pq = ParseQuery.getQuery("Catalog");
        pq.whereEqualTo("manufacturer", this.getManufacturer());
        pq.whereEqualTo("title", this.getTitle());
        pq.whereEqualTo("variant", this.getVariant());

        pq.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    if (list.size() != 0) object[0] = list.get(0);
                }
            }
        });

        return object[0];
    }

    private String lbsin; // LOCALBAY STANDARD IDENTIFICATION NUMBER - Parse ObjectId

    // ITEM ATTRIBUTES
    private String manufacturer;
    private String title;

    /**
     * CATEGORY DETERMINES THE TYPE OF ATTRIBUTES A PRODUCT MIGHT HAVE
     * EACH CATEGORY IS A DIFFERENT CLASS, AND HAS IT'S OWN ATTRIBUTES SET.
     */
    private String category;
    private String subCategory;
    // ITEM ATTRIBUTES ENDS

    /***
     * IMAGEURL ARRAY - LIST OF PRODUCT IMAGES
     */
    private String imageUrl;
    private String variant;

    /**
     * SHORT DESCRIPTION ABOUT THE PRODUCT
     */
    private String shortDescription;
    private String dispatchTime;

    /**
     * Product Price
     */
    private PriceModel price;

    public void setLbsin(String lbsin) {
        this.lbsin = lbsin;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(String dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public PriceModel getPrice() {
        return price;
    }

    public void setPrice(PriceModel price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public String getLbsin() {
        return lbsin;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getVariant() {
        return variant;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(manufacturer);
        dest.writeString(title);
        dest.writeString(variant);
        dest.writeString(category);

        dest.writeString(imageUrl);
        dest.writeString(lbsin);
        dest.writeString(shortDescription);
    }

    public static final Parcelable.Creator<CatalogModel> CREATOR = new Parcelable.Creator<CatalogModel>() {
        @Override
        public CatalogModel createFromParcel(Parcel source) {
            CatalogModel product = new CatalogModel(source);
            return product;
        }

        public CatalogModel[] newArray(int size) {
            return new CatalogModel[size];
        }
    };

    private CatalogModel(Parcel in) {
        manufacturer = in.readString();
        title = in.readString();
        variant = in.readString();
        category = in.readString();

        imageUrl = in.readString();
        lbsin = in.readString();
        shortDescription = in.readString();
    }
}



