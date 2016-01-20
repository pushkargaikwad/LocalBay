package com.localbay.anuragsingh.adapters.buyerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.models.CatalogModel;

import java.util.List;

/**
 * Created by anuragsingh on 13/01/16.
 */
public class BuyerProductListAdapter extends ArrayAdapter {

    List<CatalogModel> products;
    Context context;

    public BuyerProductListAdapter(Context context, int resource, List<CatalogModel> products) {
        super(context, resource);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.buyer_single_product_item, parent, false);

        CatalogModel currentProduct = products.get(position);
        StringBuilder product_name = new StringBuilder();
        product_name.append(currentProduct.getManufacturer());
        product_name.append(" ");
        product_name.append(currentProduct.getTitle());
        product_name.append(" ");
        product_name.append(currentProduct.getVariant());

        TextView productName = (TextView) row.findViewById(R.id.productTitle);
        productName.setText(product_name.toString());

        TextView productDesc = (TextView) row.findViewById(R.id.product_short_desc);
        productDesc.setText(currentProduct.getShortDescription());

        ImageView productImage = (ImageView) row.findViewById(R.id.productImage);
        String productUrl = currentProduct.getImageUrl();

        Ion.with(productImage)
                .placeholder(R.drawable.ic_image_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .load(productUrl);

        return row;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public CatalogModel getItem(int position) {
        return products.get(position);
    }
}
