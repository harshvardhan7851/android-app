package com.login.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button login,signup;

    String emailpattern="[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";

    EditText email,password;

    SQLiteDatabase db;
    SharedPreferences sp;
    CheckBox rememberMe;
    ImageView passHide,passShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.button2);
        signup = findViewById(R.id.button);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextNumberPassword);
        rememberMe = findViewById(R.id.login_check);
        passHide = findViewById(R.id.main_password_hide);
        passShow = findViewById(R.id.main_password_show);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("Sign_up",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT VARCHAR(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        passHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passHide.setVisibility(View.GONE);
                passShow.setVisibility(View.VISIBLE);

                password.setTransformationMethod(null);
            }
        });
        passShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passHide.setVisibility(View.VISIBLE);
                passShow.setVisibility(View.GONE);

                password.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if (!email.getText().toString().trim().matches(emailpattern)) {
                    email.setError("Enter Valid Email");
                }
                else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                }
                else if (password.getText().toString().trim().length()<6){
                    password.setError("Enter 6 character Password");
                }
                else {

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' AND PASSWORD='"+password.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
//                    Log.d("CURSOR_COUNT",String.valueOf(cursor.getCount()));

                    if(cursor.getCount()>0) {
                        while (cursor.moveToNext()){
                            String sName = cursor.getString(0);
                            String sEmail = cursor.getString(1);
                            String sContact = cursor.getString(2);
                            String sPassword = cursor.getString(3);
                            String sGender = cursor.getString(4);
                            String sCity = cursor.getString(5);
                            String sDob = cursor.getString(6);

                            Log.d("USER_DATA",sName+"\n"+sEmail+"\n"+sContact+"\n"+sPassword+"\n"+sGender+"\n"+sCity+"\n"+sDob);

                            sp.edit().putString(ConstantSp.NAME,sName).commit();
                            sp.edit().putString(ConstantSp.EMAIL,sEmail).commit();
                            sp.edit().putString(ConstantSp.CONTACT,sContact).commit();
                            sp.edit().putString(ConstantSp.PASSWORD,sPassword).commit();
                            sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                            sp.edit().putString(ConstantSp.CITY,sCity).commit();
                            sp.edit().putString(ConstantSp.DOB,sDob).commit();

                            if(rememberMe.isChecked()) {
                                sp.edit().putString(ConstantSp.REMEMBER, "YES").commit();
                            }
                            else {
                                sp.edit().putString(ConstantSp.REMEMBER, "").commit();
                            }
                        }

//                  Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        new CommonMethod(MainActivity.this, "Login Successfully");
//                  Snackbar.make(view,"Login Successfully",Snackbar.LENGTH_LONG).show();
//                    new CommonMethod(view,"Login Successfully");
                    Intent intent5 = new Intent(MainActivity.this, ProfileFragment.class);
                    startActivity(intent5);

                    }
                    else {
                        new CommonMethod(MainActivity.this, "Login Unsuccessfull");
                    }
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Sign Up", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}