package com.example.xperience;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

public class DetallesActivity extends AppCompatActivity {

    TextView detaTitulo, detaCategoria, detaSinopsis, detaHora, detaDuracion;
    ImageView detaImagen;
    FloatingActionButton deleteButton;
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d("DetallesActivity", "Titulo: " + bundle.getString("Titulo"));
            Log.d("DetallesActivity", "Categoria: " + bundle.getString("Categoria"));
            Log.d("DetallesActivity", "Sinopsis: " + bundle.getString("Sinopsis"));
            Log.d("DetallesActivity", "Hora: " + bundle.getString("Hora"));
            Log.d("DetallesActivity", "Duracion: " + bundle.getString("Duracion"));
            Log.d("DetallesActivity", "Imagen: " + bundle.getString("Imagen"));

            detaTitulo.setText(bundle.getString("Titulo"));
            detaCategoria.setText(bundle.getString("Categoria"));
            detaSinopsis.setText(bundle.getString("Sinopsis"));
            detaHora.setText(bundle.getString("Hora"));
            detaDuracion.setText(bundle.getString("Duracion"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Imagen")).into(detaImagen);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                       reference.child(key).removeValue();
                        Toast.makeText(DetallesActivity.this,"Pelicula Elimianda", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                });
            }
        });
    }
}