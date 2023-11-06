package com.login.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class CartFragment extends Fragment {

    RecyclerView recyclerView;
    SharedPreferences sp;
    SQLiteDatabase db;
    ArrayList<CartList> arrayList;
    public static Button checkout;
    public  static int iTotalPrice = 0;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        db = getActivity().openOrCreateDatabase("Sign_up", Context.MODE_PRIVATE,null);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT VARCHAR(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID VARCHAR(10),PRODUCTID VARCHAR(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTPRICE VARCHAR(20),PRODUCTQTY INTEGER(10),TOTALPRICE VARCHAR(20))";
        db.execSQL(cartTableQuery);

        String wishListTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,PRODUCTID VARCHAR(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTPRICE VARCHAR(20))";
        db.execSQL(wishListTableQuery);

        recyclerView = view.findViewById(R.id.card_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        sp = getActivity().getSharedPreferences(ConstantSp.PREF,Context.MODE_PRIVATE);

        checkout = view.findViewById(R.id.cart_checkout);

        String selectQuery = "SELECT * FROM CART WHERE ORDERID='0'";
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.getCount()>0){

            checkout.setVisibility(View.VISIBLE);

            arrayList =  new ArrayList<>();
            while (cursor.moveToNext()){

                CartList list = new CartList();

                list.setCartId(cursor.getString(0));
                list.setProductId(cursor.getString(2));
                list.setProductName(cursor.getString(3));
                list.setProductImage(cursor.getString(4));
                list.setProductPrice(cursor.getString(5));
                list.setProductQty(cursor.getString(6));
                list.setTotalPrice(cursor.getString(7));
                arrayList.add(list);

                iTotalPrice += Integer.parseInt(cursor.getString(7));
            }
            CartAdapter adapter = new CartAdapter(getActivity(),arrayList);
            recyclerView.setAdapter(adapter);

            checkout.setText("Checkout  "+ ConstantSp.PRODUCT_PRICE_SYMBOL+iTotalPrice);
        }
        else {
            checkout.setVisibility(View.GONE);
        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),ShippingActivity.class);
                startActivity(intent);

            }
        });
        return  view;
    }
}