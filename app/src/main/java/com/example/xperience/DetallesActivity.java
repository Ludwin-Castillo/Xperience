package com.example.xperience;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetallesActivity extends AppCompatActivity {

    TextView detaTitulo, detaCategoria, detaSinopsis, detaHora, detaDuracion;
    ImageView detaImagen;

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
            Glide.with(this).load(bundle.getString("Imagen")).into(detaImagen);
        }

    }
}