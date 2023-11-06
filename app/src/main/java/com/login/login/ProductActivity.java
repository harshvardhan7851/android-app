package com.login.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    ImageView iamgeView;
    TextView name,price;
    SharedPreferences sp;
    Button payment;
    Button addCart,removeCart,wishlist,removeWishlist;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        db = openOrCreateDatabase("Sign_up",MODE_PRIVATE,null);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT VARCHAR(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID VARCHAR(10),PRODUCTID VARCHAR(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTPRICE VARCHAR(20),PRODUCTQTY INTEGER(10),TOTALPRICE VARCHAR(20))";
        db.execSQL(cartTableQuery);

        String wishListTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,PRODUCTID VARCHAR(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTPRICE VARCHAR(20))";
        db.execSQL(wishListTableQuery);

        sp=getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        name = findViewById(R.id.product_detail_name);
        iamgeView = findViewById(R.id.product_detail_image);
        price = findViewById(R.id.product_detail_price);
        payment = findViewById(R.id.payment);
        addCart = findViewById(R.id.add_cart);
        removeCart = findViewById(R.id.remove_cart);
        wishlist = findViewById(R.id.add_wishlist);
        removeWishlist = findViewById(R.id.remove_wishlist);

        name.setText(sp.getString(ConstantSp.PRODUCT_NAME,""));
        iamgeView.setImageResource(sp.getInt(ConstantSp.PRODUCT_IMAGE,0));
        price.setText(ConstantSp.PRODUCT_PRICE_SYMBOL+sp.getString(ConstantSp.PRODUCT_PRICE,""));

        String selectCartQuery = "SELECT * FROM CART WHERE PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID,"")+"' AND ORDERID='0 ' ";
        Cursor cursorCart = db.rawQuery(selectCartQuery,null);
        if(cursorCart.getCount()>0){
            addCart.setVisibility(View.GONE);
            removeCart.setVisibility(View.VISIBLE);
        }
        else {
            addCart.setVisibility(View.VISIBLE);
            removeCart.setVisibility(View.GONE);
        }

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectCartQuery = "SELECT * FROM CART WHERE PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID,"")+"' AND ORDERID = '0'";
                Cursor cursor = db.rawQuery(selectCartQuery,null);
                if(cursor.getCount()>0){
                    new CommonMethod(ProductActivity.this,"Already Exists In Cart");
                }
                else {
                    int iQty = 1;
                    int iTotalPrice = Integer.parseInt(sp.getString(ConstantSp.PRODUCT_PRICE, "")) * iQty;
                    String insertCartQuery = "INSERT INTO CART VALUES(NULL,'0','" + sp.getString(ConstantSp.PRODUCT_ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_NAME, "") + "','" + sp.getInt(ConstantSp.PRODUCT_IMAGE, 0) + "','" + sp.getString(ConstantSp.PRODUCT_PRICE, "") + "','" + iQty + "','" + iTotalPrice + "')";
                    db.execSQL(insertCartQuery);

                    new CommonMethod(ProductActivity.this, "Added To Cart");

                    addCart.setVisibility(View.GONE);
                    removeCart.setVisibility(View.VISIBLE);
                }
            }
        });

        removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deletQuery = "DELETE FROM CART WHERE PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"' AND ORDERID='0'";
                db.execSQL(deletQuery);

                new CommonMethod(ProductActivity.this,"Product removed from Cart");

                addCart.setVisibility(View.VISIBLE);
                removeCart.setVisibility(View.GONE);
            }
        });


        String selectWishlistQuery = "SELECT * FROM WISHLIST WHERE PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID,"")+"' ";
        Cursor cursor = db.rawQuery(selectWishlistQuery,null);
        if(cursor.getCount()>0){
            wishlist.setVisibility(View.GONE);
            removeWishlist.setVisibility(View.VISIBLE);
        }
        else {
            wishlist.setVisibility(View.VISIBLE);
            removeWishlist.setVisibility(View.GONE);
        }

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectWishlistQuery = "SELECT * FROM WISHLIST WHERE PRODUCTID = '"+sp.getString(ConstantSp.PRODUCT_ID,"")+"' ";
                Cursor cursor = db.rawQuery(selectWishlistQuery,null);
                if(cursor.getCount()>0){
                    new CommonMethod(ProductActivity.this,"Already Exists In Wishlist");
                }
                else {

                    String insertCartQuery = "INSERT INTO WISHLIST VALUES(NULL,'" + sp.getString(ConstantSp.PRODUCT_ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_NAME, "") + "','" + sp.getInt(ConstantSp.PRODUCT_IMAGE, 0) + "','" + sp.getString(ConstantSp.PRODUCT_PRICE, "") + "')";
                    db.execSQL(insertCartQuery);

                    new CommonMethod(ProductActivity.this, "Added To Wishlist");

                    wishlist.setVisibility(View.GONE);
                    removeWishlist.setVisibility(View.VISIBLE);
                }
            }
        });

        removeWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deletQuery = "DELETE FROM WISHLIST WHERE PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"'";
                db.execSQL(deletQuery);

                new CommonMethod(ProductActivity.this,"Product removed from wishlist");

                wishlist.setVisibility(View.VISIBLE);
                removeWishlist.setVisibility(View.GONE);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(ProductActivity.this,"Payment Done");
            }
        });

    }
}