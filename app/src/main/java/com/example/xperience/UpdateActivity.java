package com.example.xperience;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class UpdateActivity extends AppCompatActivity {

    ImageView editarI;
    Button editarP;
    EditText editarT, editarC, editarS, editarH, editarD;
    String titulo, categoria, sinopsis, hora, duracion, language;
    String imagenUrl;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    private static final int REQUEST_CODE_IMAGE_PICKER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        editarI = findViewById(R.id.editarImagen);
        editarP = findViewById(R.id.editarpelicula);
        editarT = findViewById(R.id.editartitulopelicula);
        editarC = findViewById(R.id.editarcategoria);
        editarS = findViewById(R.id.editarsinopsis);
        editarH = findViewById(R.id.editarhora);
        editarD = findViewById(R.id.editarduracion);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            editarI.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateActivity.this, "No se ha seleccionado una imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(UpdateActivity.this).load(bundle.getString("Image")).into((editarI));
            editarT.setText(bundle.getString("Titulo"));
            editarC.setText(bundle.getString("Categoria"));
            editarS.setText(bundle.getString("Sinopsis"));
            editarH.setText(bundle.getString("Hora"));
            editarD.setText(bundle.getString("Duracion"));
            key = bundle.getString("key");
            oldImageURL = bundle.getString("Imagen");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Android");
        if (key != null) {
            databaseReference = databaseReference.child(key);
        } else {
            // Manejar el caso en el que key es nulo
            return;
        }

        editarI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });



        editarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveData() {
        if (uri != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Android").child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageReference.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    imagenUrl = uri.toString();
                                    updateData();
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    // Manejar el caso en que la obtención de la URL de descarga falla
                                    dialog.dismiss();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Manejar el caso en que la carga del archivo falla
                        dialog.dismiss();
                    });
        } else {
            // Manejar el caso en que uri es nulo
            Toast.makeText(UpdateActivity.this, "No se ha seleccionado una imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateData() {
        titulo = editarT.getText().toString().trim();
        categoria = editarC.getText().toString().trim();
        sinopsis = editarS.getText().toString().trim();
        hora = editarH.getText().toString().trim();
        duracion = editarD.getText().toString().trim();

        DataClass dataClass = new DataClass(titulo, categoria, sinopsis, hora, duracion, imagenUrl);

        databaseReference.setValue(dataClass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                        reference.delete();
                        Toast.makeText(UpdateActivity.this, "Pelicula Editada", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show());
    }
}
