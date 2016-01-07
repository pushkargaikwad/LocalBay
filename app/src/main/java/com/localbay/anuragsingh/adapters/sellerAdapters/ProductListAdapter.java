package com.localbay.anuragsingh.adapters.sellerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.models.CatalogModel;

import java.util.List;

/**
 * Created by anuragsingh on 03/01/16.
 */
public class ProductListAdapter extends ArrayAdapter {

    private Context context;
    private List<CatalogModel> catalogModel;
    private RelativeLayout product_stock_details;
    public ProductListAdapter(Context context, int resource, List product) {
        super(context, resource, product);
        this.context = context;
        this.catalogModel = product;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = mInflater.inflate(R.layout.seller_product_listing_item, parent, false);
        TextView productTitle = (TextView) row.findViewById(R.id.productTitle);
        productTitle.setText(catalogModel.get(position).getTitle());

        TextView productCategory = (TextView) row.findViewById(R.id.subCategory);
        productCategory.setText(catalogModel.get(position).getSubCategory());

        TextView productBrand = (TextView) row.findViewById(R.id.brand);
        productBrand.setText(catalogModel.get(position).getManufacturer());

        TextView productVariant = (TextView) row.findViewById(R.id.variant);
        productVariant.setText(catalogModel.get(position).getVariant());

        ImageView productImage = (ImageView) row.findViewById(R.id.productImage);
        String productUrl = catalogModel.get(position).getImageUrl();

        Ion.with(productImage)
                .placeholder(R.drawable.ic_image_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .load(productUrl);

        product_stock_details = (RelativeLayout) row.findViewById(R.id.product_stock_details);
        product_stock_details.setVisibility(View.GONE);

        return row;
    }
}

