package com.localbay.anuragsingh.buyer;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.adapters.buyerAdapters.BuyerProductSellersAdapter;
import com.localbay.anuragsingh.models.CatalogModel;
import com.localbay.anuragsingh.models.PriceModel;
import com.localbay.anuragsingh.models.StockProductModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerProductFragment extends Fragment {

    CatalogModel selectedCatalog;
    List<StockProductModel> availableStock;
    View view;
    ProgressBar progressBar;

    TextView productName;
    TextView productDesc;
    ImageView productImage;
    TextView oos_flag;

    ListView productSellers;
    BuyerProductSellersAdapter bpsa;

    ParseObject current_stock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedCatalog = getArguments().getParcelable("LB_SELECTED_BUYER_PRODUCT");

        availableStock = new ArrayList<StockProductModel>();
    }

    /**
     * GET ALL AVAILABLE STOCK
     *
     * @return
     */
    public void getAvailableStock() {

        /**
         * PARSE GET STOCK FOR CURRENT CATALOG PRODUCT
         */
        ParseObject catalogParse = selectedCatalog.getParseObject();
        progressBar.setVisibility(View.VISIBLE);


        final ParseObject[] object = new ParseObject[1];
        ParseQuery pq = ParseQuery.getQuery("Catalog");

        pq.whereEqualTo("manufacturer", selectedCatalog.getManufacturer());
        pq.whereEqualTo("title", selectedCatalog.getTitle());
        pq.whereEqualTo("variant", selectedCatalog.getVariant());

        pq.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    System.out.println(list.size());

                    if (list.size() != 0) {
                        ParseObject parseCatalog = list.get(0);
                        /**
                         * GET STOCK DETAILS
                         */
                        ParseQuery<ParseObject> getStockDetails = new ParseQuery<ParseObject>("StockProduct");
                        getStockDetails.whereEqualTo("catalog_product", parseCatalog);

                        getStockDetails.findInBackground(new FindCallback<ParseObject>() {

                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e != null){
                                    e.printStackTrace();
                                }
                                else if (e == null) {
                                    progressBar.setVisibility(View.GONE);

                                    if (objects.size() != 0) {
                                        for (ParseObject current_stock_object : objects) {

                                            current_stock = current_stock_object;
                                            final StockProductModel current_stock_product = new StockProductModel();

                                            current_stock_product.setDispatchTime(Integer.parseInt(current_stock.get("dispatch_time").toString()));
                                            current_stock_product.setStock_units(Integer.parseInt(current_stock.get("stock_units").toString()));
                                            current_stock_product.setCondition(current_stock.getString("condition"));

                                            System.out.println(current_stock_object.getObjectId());
                                            /**
                                             * GET SELLER FOR CURRENT PRODUCT
                                             */

                                            ParseRelation<ParseUser> getSellerRelation = current_stock_object.getRelation("sold_by");
                                            ParseQuery getSeller = getSellerRelation.getQuery();

                                            getSeller.findInBackground(new FindCallback<ParseUser>() {

                                                @Override
                                                public void done(List<ParseUser> users, ParseException e) {
                                                    if (e!=null){
                                                        e.printStackTrace();
                                                    }
                                                    Log.d("USER SIZE", Integer.toString(users.size()));
                                                }
                                            });

                                            ParseRelation<ParseObject> getPriceRelation = current_stock_object.getRelation("prices");
                                            ParseQuery getPrice = getPriceRelation.getQuery();
                                            getPrice.findInBackground(new FindCallback<ParseObject>() {

                                                @Override
                                                public void done(List<ParseObject>prices, ParseException e) {
                                                    Log.d("SIZE", Integer.toString(prices.size()));
                                                    System.out.println(prices.get(0).get("costPrice"));
                                                }
                                            });

//                                            try {
//                                                List<ParseUser> sellers = getSeller.find();
//                                                if (sellers.size() == 0) {
//                                                    System.out.println("NO USER FOUND");
//                                                }
//                                                else{
//                                                    ParseUser seller = (ParseUser) objects.get(0);
//                                                    current_stock_product.setParseUser(seller);
//                                                }
//
//                                            } catch (ParseException e1) {
//                                                e1.printStackTrace();
//                                            }


//                                            ParseQuery getPrice = current_stock.getRelation("prices").getQuery();
//
//                                            try {
//                                                List<ParseObject> prices = getPrice.find();
//                                                if (prices.size() == 0){
//                                                    System.out.println("NO PRICE FOUND");
//                                                }
//                                                else{
//
//                                                    PriceModel price = new PriceModel();
//                                                    ParseObject price_current = prices.get(0);
//                                                    price.setShippingCost(price_current.getInt("shippingCost"));
//                                                    price.setCostPrice(price_current.getInt("costPrice"));
//
//                                                    current_stock_product.setPriceModel(price);
//                                                    current_stock_product.setCatalogModel(selectedCatalog);
//                                                    availableStock.add(current_stock_product);
//                                                    bpsa.notifyDataSetChanged();
//
//                                                }
//                                            } catch (ParseException e1) {
//                                                e1.printStackTrace();
//                                            }
                                        }
                                    } else {
                                        oos_flag.setVisibility(View.VISIBLE);
                                        productSellers.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
                    } else {
                        System.out.println("STOCK");
                    }

                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_buyer_product, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        productName = (TextView) view.findViewById(R.id.productName);
        StringBuilder productTitle = new StringBuilder();
        productTitle.append(selectedCatalog.getManufacturer()).append(" ").append(selectedCatalog.getTitle()).append(" ");
        productTitle.append(selectedCatalog.getVariant());

        productName.setText(productTitle.toString());

        productImage = (ImageView) view.findViewById(R.id.productImage);
        String productUrl = selectedCatalog.getImageUrl();

        productDesc = (TextView) view.findViewById(R.id.productDesc);
        productDesc.setText(selectedCatalog.getShortDescription());

        Ion.with(productImage)
                .placeholder(R.drawable.ic_image_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .load(productUrl);

        /**
         * LOAD SELLERS
         */

        productSellers = (ListView) view.findViewById(R.id.productSellers);
        oos_flag = (TextView) view.findViewById(R.id.oos_flag);
        bpsa = new BuyerProductSellersAdapter(getActivity().getBaseContext(), R.id.productSellersWrapper, availableStock);
        productSellers.setAdapter(bpsa);

        getAvailableStock();

        return view;
    }

}
