package com.example.xperience;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Asientos extends AppCompatActivity {

    private boolean[][] asientosSeleccionados;
    private int asientosDisponibles = 30;
    private double precioBase = 3.25;
    private double precioTotal = 0.0;
    private TextView textoAsientoSeleccionado;
    private TextView precioTextView;
    private ImageButton paymentButton;

    private static final int CODIGO_DE_PAGO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asientos);

        asientosSeleccionados = new boolean[6][5];

        textoAsientoSeleccionado = findViewById(R.id.textoAsientoSeleccionado);

        precioTextView = findViewById(R.id.precioTextView);

        paymentButton = findViewById(R.id.paymentButton);

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (asientosDisponibles < 30) {
                    redirigirAPago();
                } else {
                    // ...
                }
            }
        });

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof ImageView) {
                final ImageView asiento = (ImageView) child;
                final int fila = i / gridLayout.getColumnCount();
                final int columna = i % gridLayout.getColumnCount();

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(fila);
                params.columnSpec = GridLayout.spec(columna);
                asiento.setLayoutParams(params);

                final String abreviatura = obtenerAbreviatura(fila, columna);

                asiento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!asientosSeleccionados[fila][columna]) {
                            asiento.setColorFilter(Color.RED);
                            asientosSeleccionados[fila][columna] = true;
                            asientosDisponibles--;
                            precioTotal += precioBase;  // Sumar precio del asiento
                            actualizarInfoAsientos();
                        } else {
                            mostrarDialogoDeseleccion(asiento, fila, columna);
                        }
                    }
                });
            }
        }
    }

    private String obtenerAbreviatura(int fila, int columna) {
        char letraFila = (char) ('A' + fila);
        int numeroColumna = columna + 1;
        return letraFila + String.valueOf(numeroColumna);
    }

    private void mostrarDialogoDeseleccion(final ImageView asiento, final int fila, final int columna) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Deseas deseleccionar este asiento?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    asiento.clearColorFilter();
                    asientosSeleccionados[fila][columna] = false;
                    asientosDisponibles++;
                    precioTotal -= precioBase;  // Restar precio del asiento
                    actualizarInfoAsientos();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void actualizarInfoAsientos() {
        StringBuilder texto = new StringBuilder("<b>Su Asiento es: </b>");

        for (int i = 0; i < asientosSeleccionados.length; i++) {
            for (int j = 0; j < asientosSeleccionados[i].length; j++) {
                if (asientosSeleccionados[i][j]) {
                    texto.append(obtenerAbreviatura(i, j)).append(", ");
                }
            }
        }

        if (texto.length() > 0) {
            texto.delete(texto.length() - 2, texto.length());
        }
        texto.append("\n");

        textoAsientoSeleccionado.setText(Html.fromHtml(texto.toString(), Html.FROM_HTML_MODE_COMPACT));

        TextView asientosDisponiblesTextView = findViewById(R.id.asientosDisponiblesTextView);
        TextView asientosOcupadosTextView = findViewById(R.id.asientosOcupadosTextView);

        asientosDisponiblesTextView.setText("Disponibles: " + asientosDisponibles);
        asientosOcupadosTextView.setText("Ocupados: " + (30 - asientosDisponibles));


        precioTextView.setText("Precio Total: $" + precioTotal);


        if (asientosDisponibles < 30) {
            paymentButton.setVisibility(View.VISIBLE);
        } else {
            paymentButton.setVisibility(View.INVISIBLE);
        }
    }

    private void redirigirAPago() {

        Intent intentPago = new Intent(this, Pago.class);


        intentPago.putExtra("precioTotal", precioTotal);


        startActivityForResult(intentPago, CODIGO_DE_PAGO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CODIGO_DE_PAGO && resultCode == RESULT_OK) {

        }
    }
}

