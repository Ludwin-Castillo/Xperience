package com.example.xperience;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetallesActivity extends AppCompatActivity {

    TextView detaTitulo, detaCategoria, detaSinopsis, detaHora, detaDuracion, detaLang;
    ImageView detaImagen;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        detaTitulo = findViewById(R.id.detalleTitulo);
        detaCategoria = findViewById(R.id.detalleCategoria);
        detaSinopsis = findViewById(R.id.detalleSinopsis);
        detaHora = findViewById(R.id.detalleHora);
        detaDuracion = findViewById(R.id.detaleDuracion);
        detaImagen = findViewById(R.id.detalleImagen);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detaLang = findViewById(R.id.detalleLang);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detaTitulo.setText(bundle.getString("Titulo"));
            detaCategoria.setText(bundle.getString("Categoria"));
            detaSinopsis.setText(bundle.getString("Sinopsis"));
            detaHora.setText(bundle.getString("Hora"));
            detaDuracion.setText(bundle.getString("Duracion"));
            detaLang.setText(bundle.getString("Lang"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("Imagen");
            Glide.with(this).load(imageUrl).into(detaImagen);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarPelicula();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarPelicula();
            }
        });
    }

    private void eliminarPelicula() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mostrarMensaje("Película eliminada exitosamente");
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mostrarMensaje("Error al eliminar la película");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mostrarMensaje("Error al eliminar la imagen");
            }
        });
    }

    private void editarPelicula() {
        Intent intent = new Intent(DetallesActivity.this, UpdateActivity.class)
                .putExtra("Titulo", detaTitulo.getText().toString())
                .putExtra("Categoria", detaCategoria.getText().toString())
                .putExtra("Sinopsis", detaSinopsis.getText().toString())
                .putExtra("Hora", detaHora.getText().toString())
                .putExtra("Duracion", detaDuracion.getText().toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", key)
                .putExtra("Language", detaLang.getText().toString());

        startActivity(intent);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(DetallesActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
