package com.login.login;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Locale;


public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    String[] idArray = {"1","2","3","4","5"};
    String[] nameArray = {"Sofa","Chair","Table","Closet","Bed"};
    int[] imageArray = {R.drawable.sofa,R.drawable.chair,R.drawable.table,R.drawable.closet,R.drawable.bed};
    String[] priceArray = {"9999","599","499","8999","14999"};


    RecyclerView categoryRecyclerview;
    String[] categoryNameArray = {"Living Room","Bedroom","Kitchen","Lights","Decoration"};
    int[] catrgoryImageArray = {R.drawable.livingroom,R.drawable.bedroom,R.drawable.kitchen,R.drawable.light,R.drawable.decoration};

    ArrayList<CategoryList> arrayList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.home_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ProductAdapter adapter = new ProductAdapter(getActivity(),nameArray,imageArray,priceArray,idArray);
        recyclerView.setAdapter(adapter);


        categoryRecyclerview = view.findViewById(R.id.home_recyclerview_category);

        categoryRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        categoryRecyclerview.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for (int i=0;i<categoryNameArray.length;i++){
            CategoryList list = new CategoryList();
            list.setName(categoryNameArray[i]);
            list.setImage(catrgoryImageArray[i]);
            arrayList.add(list);
        }


        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(),arrayList);
        categoryRecyclerview.setAdapter(categoryAdapter);

        return view;
    }
}