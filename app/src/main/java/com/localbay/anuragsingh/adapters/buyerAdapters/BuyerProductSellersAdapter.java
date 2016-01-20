package com.localbay.anuragsingh.adapters.buyerAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.models.StockProductModel;

import java.util.List;

/**
 * Created by anuragsingh on 19/01/16.
 */
public class BuyerProductSellersAdapter extends ArrayAdapter {

    List<StockProductModel> availableStock;
    TextView sellerPrice;
    TextView shippingCost;
    TextView sellerName;

    public BuyerProductSellersAdapter(Context context, int resource, List<StockProductModel> availableStock) {
        super(context, resource);
        this.availableStock = availableStock;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        StockProductModel currentSPM = availableStock.get(position);

        sellerPrice = (TextView) row.findViewById(R.id.sellerPrice);
        StringBuilder priceText = new StringBuilder();
        priceText.append("Rs. ");
        priceText.append(Integer.toString(currentSPM.getPriceModel().getCostPrice()));
        sellerPrice.setText(priceText.toString());

        shippingCost = (TextView) row.findViewById(R.id.shippingCost);
        StringBuilder shippingText = new StringBuilder();
        if (currentSPM.getPriceModel().getShippingCost() == 0)
            shippingText.append("FREE");
        else {
            shippingText.append("Rs. ");
            shippingText.append(Integer.toString(currentSPM.getPriceModel().getShippingCost()));
        }

        sellerName = (TextView) row.findViewById(R.id.sellerName);
        sellerName.setText(currentSPM.getParseUser().getString("fullname").toString());
        return row;
    }

}
