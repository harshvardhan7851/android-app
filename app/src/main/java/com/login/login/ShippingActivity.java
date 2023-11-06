package com.login.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ShippingActivity extends AppCompatActivity {

    Button continueButton;
    String emailpattern="[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    EditText email,name,contact,address;
    RadioGroup paymentType;
    Spinner city;
    //    String[] cityArray = {"Ahmedabad","Surat","Gandhinagar","Vadodara","Rajkot"};
    ArrayList<String> arrayList;
    String sCity;
    String spaymentType;
    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        name = findViewById(R.id.shipping_name);
        email = findViewById(R.id.eshipping_email);
        contact = findViewById(R.id.shipping_contact);
        address = findViewById(R.id.shipping_address);
        city = findViewById(R.id.shipping_city);
        paymentType = findViewById(R.id.radio_group);
        continueButton = findViewById(R.id.shipping_continue);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("Sign_up",MODE_PRIVATE,null);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT VARCHAR(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID VARCHAR(10),PRODUCTID VARCHAR(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTPRICE VARCHAR(20),PRODUCTQTY INTEGER(10),TOTALPRICE VARCHAR(20))";
        db.execSQL(cartTableQuery);

        String wishListTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,PRODUCTID VARCHAR(10),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTPRICE VARCHAR(20))";
        db.execSQL(wishListTableQuery);

        String orderTableQuery = "CREATE TABLE IF NOT EXISTS SHIPPING_ORDER(ORDERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT VARCHAR(10),ADDRESS TEXT,CITY VARCHAR(50),TOTALAMOUNT VARCHAR(20),PAYMENTTYPE VARCHAR(20),TRANSACTIONID VARCHAR(50))";
        db.execSQL(orderTableQuery);

        name.setText(sp.getString(ConstantSp.NAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));

        arrayList = new ArrayList<>();

        arrayList.add("Select City");
        arrayList.add("Gandhinagar");
        arrayList.add("Rajkot");
        arrayList.add("Ahmedabad");
        arrayList.add("Mehsana");
        arrayList.add("Surat");

        ArrayAdapter adapter = new ArrayAdapter(ShippingActivity.this, android.R.layout.simple_list_item_1, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sCity = "";
                } else {
                    sCity = arrayList.get(i);
                    new CommonMethod(ShippingActivity .this, sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        paymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                new CommonMethod(ShippingActivity.this, radioButton.getText().toString());
                spaymentType = radioButton.getText().toString();
                new CommonMethod(ShippingActivity.this, spaymentType);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")){
                    name.setError("Name Required");
                }
                else if (email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if (!email.getText().toString().trim().matches(emailpattern)) {
                    email.setError("Enter Valid Email");
                }
                else if (contact.getText().toString().trim().length()<10) {
                    contact.setError("Enter Valid Contact Number");
                }
                else if (address.getText().toString().trim().equals("")) {
                    address.setError("Address Required");
                }
                else if(sCity.equals("")){
                    new CommonMethod(ShippingActivity.this,"Please Select City");
                }
                else if(paymentType.getCheckedRadioButtonId() == -1){
                    new CommonMethod(ShippingActivity.this,"Please Select Payment Type");
                }
                else {

                    if(spaymentType.equalsIgnoreCase("Cash On Delivery")){
                        String insertQuery = "INSERT INTO SHIPPING_ORDER VALUES(NULL,'"+name.getText().toString()+"','"+email.getText().toString()+"','"+contact.getText().toString()+"','"+address.getText().toString()+"','"+CartFragment.iTotalPrice+"','"+sCity+"','"+spaymentType+"','')";
                        db.execSQL(insertQuery);

                        String selectquery = "SELECT MAX(ORDERID) FROM SHIPPING_ORDER LIMIT 1";
                        Cursor cursor = db.rawQuery(selectquery,null);

                        if(cursor.getCount()>0){
                            while(cursor.moveToNext()){
                                String lastOrderId = cursor.getString(0);

                                String updateCartQuery = "UPDATE CART SET ORDERID = '"+lastOrderId+"' WHERE ORDERID='0' ";
                                db.execSQL(updateCartQuery);

                            }
                        }

                        new CommonMethod(ShippingActivity.this,"Order Placed Successfully");

                        Intent intent = new Intent(ShippingActivity.this,DashboardActivity.class);
                        startActivity(intent);
                    }
                    else{

                    }
                }
            }
        });
    }
}
