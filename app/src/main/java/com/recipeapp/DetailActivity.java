package com.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView foodDescription, foodName, foodPrice;
    ImageView imageView;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        foodName = findViewById(R.id.txtDetailName);
        foodDescription = findViewById(R.id.txtDetailDescription);
        foodPrice = findViewById(R.id.txtDetailPrice);
        imageView = findViewById(R.id.imgDetail);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            foodName.setText(bundle.getString(""));
            foodName.setText(bundle.getString("Name"));
            foodDescription.setText(bundle.getString("Description"));
            foodPrice.setText(bundle.getString("Price"));
            key = bundle.getString("KeyValue");
            imageUrl = bundle.getString("Image");

            //imageView.setImageResource(bundle.getInt("Image"));

            Glide.with(this)
                    .load(bundle.getString("Image"))
                    .into(imageView);
        }
    }

    public void btnDeleteRecipe(View view) {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipe");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                reference.child(key).removeValue();
                Toast.makeText(DetailActivity.this, "Recipe Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    public void btnUpdate(View view) {

        startActivity(new Intent(getApplicationContext(), UpdateActivity.class)
                .putExtra("nameKey", foodName.getText().toString())
                .putExtra("descKey", foodDescription.getText().toString())
                .putExtra("priceKey", foodPrice.getText().toString())
                .putExtra("oldImageUrl", imageUrl)
                .putExtra("key", key));

        finish();
    }
}
