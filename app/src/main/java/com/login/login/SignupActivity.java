package com.login.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {
    Button signup;
    String emailpattern="[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    EditText email,password,name,contact,cpassword,dob;
//    RadioButton male,female;
    RadioGroup gender;
    Spinner city;
//    String[] cityArray = {"Ahmedabad","Surat","Gandhinagar","Vadodara","Rajkot"};
    ArrayList<String> arrayList;
    String sCity;
    String sGender;
    Calendar calendar;

    SQLiteDatabase db;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = findViewById(R.id.button2);
        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.editTextTextEmailAddress);
        contact = findViewById(R.id.signup_contact);
        password = findViewById(R.id.editTextNumberPassword);
        cpassword = findViewById(R.id.signup_confirm_Password);
//        male = findViewById(R.id.signup_male);
//        female = findViewById(R.id.signup_female);
        city = findViewById(R.id.signup_city);
        dob = findViewById(R.id.signup_dob);
        gender = findViewById(R.id.radio_group);


        calendar = Calendar.getInstance();

        db = openOrCreateDatabase("Sign_up",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT VARCHAR(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                dob.setText(sdf.format(calendar.getTime()));

            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this,dateClick,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        arrayList = new ArrayList<>();

        arrayList.add("Select City");
        arrayList.add("Gandhinagar");
        arrayList.add("Rajkot");
        arrayList.add("Ahmedabad");
        arrayList.add("Mehsana");
        arrayList.add("Surat");

        ArrayAdapter adapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_1, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new CommonMethod(SignupActivity.this, arrayList.get(i));
                if (i == 0) {
                    sCity = "";
                } else {
                    sCity = arrayList.get(i);
                    new CommonMethod(SignupActivity.this, sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        male.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new CommonMethod(SignupActivity.this, "Male");
//            }
//        });
//        female.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new CommonMethod(SignupActivity.this, "Female");
//            }
//        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i); //i = R.id.signup_male,R.id.signup_female;
                new CommonMethod(SignupActivity.this, radioButton.getText().toString());
                sGender = radioButton.getText().toString();
                new CommonMethod(SignupActivity.this, sGender);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
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
                else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                }
                else if (password.getText().toString().trim().length()<6){
                    password.setError("Enter 6 character Password");
                }
                else if (cpassword.getText().toString().trim().equals("")) {
                    cpassword.setError("Confirm Password Required");
                }
                else if (cpassword.getText().toString().trim().length()<6){
                    cpassword.setError("Enter 6 character Confirm Password");
                }
                else if(!cpassword.getText().toString().trim().matches(password.getText().toString().trim())){
                    cpassword.setError("Password Doesn't Match");
                }
                else if(sCity.equals("")){
                    new CommonMethod(SignupActivity.this,"Please Select City");
                }
                else if(dob.getText().toString().trim().equals("")){
                    dob.setError("Please Select Date of Birth");
                }
                else {

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL= '"+email.getText().toString()+"' ";
                    Cursor cursor = db.rawQuery(selectQuery,null);

                    if(cursor.getCount()>0){
                        new CommonMethod( SignupActivity.this,"Email Id Already Registerd");
                    }
                    else {
                        String insertQuery = "INSERT INTO USERS VALUES('"+name.getText().toString()+"','"+email.getText().toString()+"','"+contact.getText().toString()+"','"+password.getText().toString()+"','"+sGender+"','"+sCity+"','"+dob.getText().toString()+"')";
                        db.execSQL(insertQuery);

//                    Toast.makeText(SignupActivity.this, "Sign Up Succesfully", Toast.LENGTH_SHORT).show();
                        new CommonMethod(SignupActivity.this, "Sign Up Successfully");
//                    Snackbar.make(view,"Login Successfully",Snackbar.LENGTH_LONG).show();
//                    new CommonMethod(view,"Login Successfully");
//                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
//                    startActivity(intent);
                        onBackPressed();
                    }
                }
            }
        });
    }
}