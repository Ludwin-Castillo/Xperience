package com.example.xperience;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class UploadActivity extends AppCompatActivity {

    ImageView cargarImagen;
    Button guardarPelicula;
    EditText titulo, categoria, sinopsis, hora, duracion;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        cargarImagen = findViewById(R.id.cargarimagen);
        titulo = findViewById(R.id.titulodelapelicula);
        categoria = findViewById(R.id.categoria);
        sinopsis = findViewById(R.id.sinopsis);
        hora = findViewById(R.id.hora);
        duracion = findViewById(R.id.duracion);
        guardarPelicula = findViewById(R.id.guardarpelicula);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            cargarImagen.setImageURI(uri);
                        } else {
                            Toast.makeText(UploadActivity.this, "No has seleccionado una imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        cargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        guardarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDato();
            }
        });
    }

    public void guardarDato() {

        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android_imagenes").child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageURL = uri.toString();
                            cargarDato();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(UploadActivity.this, "Error al obtener la URL de la imagen", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(UploadActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(UploadActivity.this, "Selecciona una imagen primero", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarDato() {

        String Titulo = titulo.getText().toString();
        String Categoria = categoria.getText().toString();
        String Synopsis = sinopsis.getText().toString();
        String Hora = hora.getText().toString();
        String Duracion = duracion.getText().toString();

        DataClass dataClass = new DataClass(Titulo, Categoria, Synopsis, Hora, Duracion);

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("Android").child(Titulo).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UploadActivity.this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}