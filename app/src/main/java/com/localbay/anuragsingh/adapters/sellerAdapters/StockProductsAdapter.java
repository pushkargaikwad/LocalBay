package com.localbay.anuragsingh.adapters.sellerAdapters;

import android.content.Context;
import android.util.Log;
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
import com.localbay.anuragsingh.models.StockProductModel;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by anuragsingh on 06/01/16.
 * ADAPTER FOR THE STOCK PRODUCT LISTING
 * CALLING CLASS: SellerCategoryProductsFragment
 */
public class StockProductsAdapter extends ArrayAdapter {

    private Context context;
    private List<StockProductModel> productStockModels;
    private RelativeLayout product_stock_details;

    public StockProductsAdapter(Context context, int resource, List<StockProductModel> productStockModels) {
        super(context, resource, productStockModels);
        this.context = context;
        this.productStockModels = productStockModels;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CatalogModel catalogModel = productStockModels.get(position).getCatalogModel();
        StockProductModel stockProductModel = productStockModels.get(position);

        Log.d("CATALOG TITLE", catalogModel.getTitle());

        LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = mInflater.inflate(R.layout.seller_product_listing_item, parent, false);

        TextView productTitle = (TextView) row.findViewById(R.id.productTitle);
        productTitle.setText(catalogModel.getTitle());

        TextView productCategory = (TextView) row.findViewById(R.id.subCategory);
        productCategory.setText(catalogModel.getSubCategory());

        TextView productBrand = (TextView) row.findViewById(R.id.brand);
        productBrand.setText(catalogModel.getManufacturer());

        TextView productVariant = (TextView) row.findViewById(R.id.variant);
        productVariant.setText(catalogModel.getVariant());

        ImageView productImage = (ImageView) row.findViewById(R.id.productImage);
        String productUrl = catalogModel.getImageUrl();

        Ion.with(productImage)
                .placeholder(R.drawable.ic_image_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .load(productUrl);

        product_stock_details = (RelativeLayout) row.findViewById(R.id.product_stock_details);
        product_stock_details.setVisibility(View.VISIBLE);

        TextView productQuantity = (TextView) row.findViewById(R.id.quantity);
        productQuantity.setText(Integer.toString(stockProductModel.getStock_units()));

        TextView price = (TextView) row.findViewById(R.id.price);
        price.setText(Integer.toString(stockProductModel.getPriceModel().getCostPrice()));

        TextView shippingCost = (TextView) row.findViewById(R.id.shippingCost);
        shippingCost.setText(Integer.toString(stockProductModel.getPriceModel().getShippingCost()));

        return row;
    }
}
