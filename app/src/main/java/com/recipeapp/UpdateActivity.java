package com.recipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

public class UpdateActivity extends AppCompatActivity {

    Uri uri;
    EditText edit_recipe_name,edit_recipe_desc,edit_recipe_price;
    Button btn_Select,btn_Update;
    String imageUrl;
    ImageView imageUpdate;
    String key,oldImageUrl;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    String recipeName,recipeDesc,recipePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        imageUpdate = findViewById(R.id.image_Update);

        edit_recipe_name = findViewById(R.id.edit_update_name);
        edit_recipe_desc = findViewById(R.id.edit_update_desc);
        edit_recipe_price = findViewById(R.id.edit_update_price);

        btn_Select = findViewById(R.id.btn_Update_Select);
        btn_Update = findViewById(R.id.btn_Update_Recipe);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            Glide.with(UpdateActivity.this)
                    .load(bundle.getString("oldImageUrl"))
                    .into(imageUpdate);

            edit_recipe_name.setText(bundle.getString("nameKey"));
            edit_recipe_desc.setText(bundle.getString("descKey"));
            edit_recipe_price.setText(bundle.getString("priceKey"));
            key = bundle.getString("key");
            oldImageUrl = bundle.getString("oldImageUrl");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe").child(key);

        btn_Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecipe();
            }
        });

    }


    private void selectImage() {

        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            uri = data.getData();
            imageUpdate.setImageURI(uri);
        }
        else
        {
            Toast.makeText(this, "you have not picked image", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRecipe() {

        recipeName = edit_recipe_name.getText().toString().trim();
        recipeDesc = edit_recipe_desc.getText().toString().trim();
        recipePrice = edit_recipe_price.getText().toString();

        //StorageReference reference = storageReference.child("RecipeImage/" + UUID.randomUUID().toString());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Updating...");
        progressDialog.show();

        storageReference = FirebaseStorage.getInstance().getReference().child("RecipeImage")
                .child(uri.getLastPathSegment());

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();

                uploadRecipe();

                progressDialog.dismiss();

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();

               // Toast.makeText(UpdateActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                //Toast.makeText(UpdateActivity.this, "Image Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void  uploadRecipe()
    {


        FoodData foodData = new FoodData(
                recipeName,recipeDesc,recipePrice,imageUrl
        );

        databaseReference.setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                StorageReference storageReferenceNew = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                storageReferenceNew.delete();
                Toast.makeText(UpdateActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
