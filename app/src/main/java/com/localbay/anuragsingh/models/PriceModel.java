package com.localbay.anuragsingh.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by anuragsingh on 03/01/16.
 */
public class PriceModel implements Parcelable {


    private int costPrice;
    private int shippingCost;

    public PriceModel() {
        costPrice = -1;
        shippingCost = -1;
    }

    public ParseObject savePrice() throws ParseException {
        ParseObject po = new ParseObject("Price");

        po.put("costPrice", this.costPrice);
        po.put("shippingCost", this.shippingCost);

        try {
            po.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return po;
    }

    public PriceModel(int costPrice, int shippingCost) {
        this.costPrice = costPrice;
        this.shippingCost = shippingCost;
    }

    public int getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(int costPrice) {
        this.costPrice = costPrice;
    }

    public int getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(int shippingCost) {
        this.shippingCost = shippingCost;
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
        dest.writeInt(costPrice);
        dest.writeInt(shippingCost);
    }

    private PriceModel(Parcel in) {
        costPrice = in.readInt();
        shippingCost = in.readInt();
    }

    public static final Parcelable.Creator<PriceModel> CREATOR = new Parcelable.Creator<PriceModel>() {
        @Override
        public PriceModel createFromParcel(Parcel source) {
            PriceModel price = new PriceModel(source);
            return price;
        }

        public PriceModel[] newArray(int size) {
            return new PriceModel[size];
        }
    };

}
