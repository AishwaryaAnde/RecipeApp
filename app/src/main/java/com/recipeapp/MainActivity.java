package com.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<FoodData> myFoodList;
    FoodData mFoodData;
    MyAdapter myAdapter;

    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;

    ProgressDialog progressDialog;
    EditText txt_SearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        txt_SearchText = findViewById(R.id.txt_SearchText);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading item....");

        myFoodList = new ArrayList<>();


        myAdapter = new MyAdapter(MainActivity.this, myFoodList);
        recyclerView.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");

        progressDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myFoodList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    FoodData foodData = itemSnapshot.getValue(FoodData.class);
                    foodData.setKey(itemSnapshot.getKey());
                    myFoodList.add(foodData);
                }
                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        txt_SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<FoodData> filterList = new ArrayList<>();
        for (FoodData item : myFoodList)
        {
            if (item.getItemName().toLowerCase().contains(text.toLowerCase()))
            {
                filterList.add(item);
            }
        }

        myAdapter.filteredList(filterList);
    }

    public void btn_upload_image(View view) {

        startActivity(new Intent(this, Upload_Recipe.class));
    }
}


/*
 //add this below myFoodList = new ArrayList<>();
 mFoodData = new FoodData("Chicken Biryani",
                "I confess I reached a fairly ripe age before I learned to love chickpeas. When I was growing up in the US, they were called garbanzo beans, and my mother kept a tin of them in case she needed to make three-bean salad, which I never ate because of the name. To a child, “three-bean salad” sounds three times worse than “one-bean salad”, which sounds bad enough."
                ,"Rs.500",R.drawable.biryani);
        myFoodList.add(mFoodData);

        mFoodData = new FoodData("Palak Paneer",
                "I confess I reached a fairly ripe age before I learned to love chickpeas. When I was growing up in the US, they were called garbanzo beans, and my mother kept a tin of them in case she needed to make three-bean salad, which I never ate because of the name. To a child, “three-bean salad” sounds three times worse than “one-bean salad”, which sounds bad enough.",
                "Rs.340",R.drawable.palak_paneer);
        myFoodList.add(mFoodData);

        mFoodData = new FoodData("Aloo ka Paratha",
                "I confess I reached a fairly ripe age before I learned to love chickpeas. When I was growing up in the US, they were called garbanzo beans, and my mother kept a tin of them in case she needed to make three-bean salad, which I never ate because of the name. To a child, “three-bean salad” sounds three times worse than “one-bean salad”, which sounds bad enough."
                ,"Rs.175",R.drawable.paratha);
        myFoodList.add(mFoodData);

        mFoodData = new FoodData("Chicken souce Pizza",
                "I confess I reached a fairly ripe age before I learned to love chickpeas. When I was growing up in the US, they were called garbanzo beans, and my mother kept a tin of them in case she needed to make three-bean salad, which I never ate because of the name. To a child, “three-bean salad” sounds three times worse than “one-bean salad”, which sounds bad enough.",
                "Rs.345",R.drawable.pizza);
        myFoodList.add(mFoodData);

        mFoodData = new FoodData("Chole Puri",
                "I confess I reached a fairly ripe age before I learned to love chickpeas. When I was growing up in the US, they were called garbanzo beans, and my mother kept a tin of them in case she needed to make three-bean salad, which I never ate because of the name. To a child, “three-bean salad” sounds three times worse than “one-bean salad”, which sounds bad enough.",
                "Rs.273",R.drawable.poori_recipe);
        myFoodList.add(mFoodData);*/
