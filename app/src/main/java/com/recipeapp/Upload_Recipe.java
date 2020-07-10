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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Upload_Recipe extends AppCompatActivity {

    ImageView imageUpload;
    Uri uri;
    EditText edit_recipe_name,edit_recipe_desc,edit_recipe_price;
    String imageUrl;
    Button btn_upload,btn_select;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__recipe);

        imageUpload = findViewById(R.id.image_Upload);

        edit_recipe_name = findViewById(R.id.edit_recipe_name);
        edit_recipe_desc = findViewById(R.id.edit_recipe_desc);
        edit_recipe_price = findViewById(R.id.edit_recipe_price);

        btn_upload = findViewById(R.id.btn_upload);
        btn_select = findViewById(R.id.btn_select);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

                //uploadRecipe();
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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
            imageUpload.setImageURI(uri);
        }
    else
        {
            Toast.makeText(this, "you have not picked image", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImage()
    {
        StorageReference reference = storageReference.child("RecipeImage/" + UUID.randomUUID().toString());

         /*storageReference = FirebaseStorage.getInstance().getReference()
                .child("RecipeImage").child(uri.getLastPathSegment());*/

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Uploading...");
        progressDialog.show();

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();

                uploadRecipe();

                progressDialog.dismiss();

               //Toast.makeText(Upload_Recipe.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(Upload_Recipe.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

               // Toast.makeText(Upload_Recipe.this, "Image Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void  uploadRecipe()
    {


        FoodData foodData = new FoodData(
                edit_recipe_name.getText().toString(),
                edit_recipe_desc.getText().toString(),
                edit_recipe_price.getText().toString(),
                imageUrl
        );

        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("Recipe")
                .child(myCurrentDateTime).setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Upload_Recipe.this, "Recipe Uploaded", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_Recipe.this, "Recipe Failed", Toast.LENGTH_SHORT).show();

                Toast.makeText(Upload_Recipe.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
