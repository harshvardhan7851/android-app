package com.login.login;

import android.content.Context;
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

import java.util.ArrayList;


public class WishlistFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<WishlistList> arrayList;
    SQLiteDatabase db;

    public WishlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wishlist, container, false);

        db = getActivity().openOrCreateDatabase("Sign_up", Context.MODE_PRIVATE,null);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT VARCHAR(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID VARCHAR(10),PRODUCTID VARCHAR(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTPRICE VARCHAR(20),PRODUCTQTY INTEGER(10),TOTALPRICE VARCHAR(20))";
        db.execSQL(cartTableQuery);

        String wishListTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,PRODUCTID VARCHAR(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTPRICE VARCHAR(20))";
        db.execSQL(wishListTableQuery);

        recyclerView = view.findViewById(R.id.wishlist_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String selectQuery = "SELECT * FROM WISHLIST";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.getCount()>0){
            arrayList =  new ArrayList<>();
            while (cursor.moveToNext()){
                WishlistList list = new WishlistList();
                list.setWishlistId(cursor.getString(0));
                list.setProductId(cursor.getString(1));
                list.setProductName(cursor.getString(2));
                list.setProductImage(cursor.getString(3));
                list.setProductPrice(cursor.getString(4));

                arrayList.add(list);
            }
            WishlistAdapter adapter = new WishlistAdapter(getActivity(),arrayList);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}