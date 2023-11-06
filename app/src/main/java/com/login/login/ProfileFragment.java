package com.login.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ProfileFragment extends Fragment {
    
    Button update,logout,edit;
    String emailpattern="[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    EditText email,name,contact,dob;
    RadioButton male,female;
    RadioGroup gender;
    Spinner city;
    
    ArrayList<String> arrayList;
    String sCity;
    String sGender;
    Calendar calendar;

    SQLiteDatabase db;
    SharedPreferences sp;
    
    public ProfileFragment() {
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        sp=getActivity().getSharedPreferences(ConstantSp.PREF, Context.MODE_PRIVATE);

//        name = findViewById(R.id.home_name);
//        name.setText(sp.getString(ConstantSp.NAME, "")+"\n"+
//                sp.getString(ConstantSp.EMAIL,"")+"\n"+
//                sp.getString(ConstantSp.CONTACT,"")+"\n"+
//                sp.getString(ConstantSp.PASSWORD,"")+"\n"+
//                sp.getString(ConstantSp.GENDER,"")+"\n"+
//                sp.getString(ConstantSp.CITY,"")+"\n"+
//                sp.getString(ConstantSp.DOB,""));
        update = v.findViewById(R.id.update_profile);
        logout = v.findViewById(R.id.logout);
        name = v.findViewById(R.id.home_name);
        email = v.findViewById(R.id.home_email);
        contact = v.findViewById(R.id.home_contact);
        city = v.findViewById(R.id.home_city);
        dob = v.findViewById(R.id.home_dob);
        gender = v.findViewById(R.id.radio_group);
        male = v.findViewById(R.id.home_male);
        female = v.findViewById(R.id.home_female);


        calendar = Calendar.getInstance();

        db = getActivity().openOrCreateDatabase("Sign_up",Context.MODE_PRIVATE,null);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),dateClick,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
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

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                new CommonMethod(getActivity(), arrayList.get(i));
                if (i == 0) {
                    sCity = "";
                } else {
                    sCity = arrayList.get(i);
                    new CommonMethod(getActivity(), sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        male.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new CommonMethod(HomeActivity.this, "Male");
//            }
//        });
//        female.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new CommonMethod(HomeActivity.this, "Female");
//            }
//        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = v.findViewById(i); //i = R.id.signup_male,R.id.signup_female;
                new CommonMethod(getActivity(), radioButton.getText().toString());
                sGender = radioButton.getText().toString();
                new CommonMethod(getActivity(), sGender);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
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
                else if(sCity.equals("")){
                    new CommonMethod(getActivity(),"Please Select City");
                }
                else if(dob.getText().toString().trim().equals("")){
                    dob.setError("Please Select Date of Birth");
                }
                else {

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL= '"+email.getText().toString()+"' ";
                    Cursor cursor = db.rawQuery(selectQuery,null);

                    if(cursor.getCount()>0){
                        String updateQuery = "UPDATE USERS SET NAME ='"+name.getText().toString()+"',CONTACT ='"+contact.getText().toString()+"',GENDER = '"+sGender+"',CITY = '"+sCity+"',DOB ='"+dob.getText().toString()+"' WHERE EMAIL = '"+email.getText().toString()+"'";
                        db.execSQL(updateQuery);
                        new CommonMethod(getActivity(),"Update Succesfully");

                        sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                        sp.edit().putString(ConstantSp.CITY,sCity).commit();
                        sp.edit().putString(ConstantSp.DOB,dob.getText().toString()).commit();

                        setData(false);
                    }
                    else {
                        new CommonMethod(getActivity(),"Invalid User");
                    }
                }
            }
        });

        edit = v.findViewById(R.id.edit_profile);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        setData(false);

        return v;
    }

    private void setData(boolean isEnable){

        name.setEnabled(isEnable);
        email.setEnabled(false);
        contact.setEnabled(isEnable);
        dob.setEnabled(isEnable);
        male.setEnabled(isEnable);
        female.setEnabled(isEnable);
        city.setEnabled(isEnable);

        if(isEnable == true){
            edit.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
        }
        else{
            edit.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);
        }

        name.setText(sp.getString(ConstantSp.NAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));
        dob.setText(sp.getString(ConstantSp.DOB,""));

        sGender = sp.getString(ConstantSp.GENDER,"");
        if(sGender.equalsIgnoreCase("MALE")){
            male.setChecked(true);
        } else if (sGender.equalsIgnoreCase("FEMALE")) {
            female.setChecked(true);
        }else {
            
        }
        
        sCity = sp.getString(ConstantSp.CITY,"");
        int icitypos = 0;
        for (int i=0;i<arrayList.size();i++){
            if(sCity.equalsIgnoreCase(arrayList.get(i))){
                icitypos = i;
            }
        }
        city.setSelection(icitypos);
    }

}