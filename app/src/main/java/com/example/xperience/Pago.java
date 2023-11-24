package com.example.xperience;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Pago extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("precioTotal")) {
            double precioTotal = intent.getDoubleExtra("precioTotal", 0.0);

            TextView precioTotalTextView = findViewById(R.id.precioTextView2);
            precioTotalTextView.setText("Precio Total: $" + precioTotal);
        }
    }

    public void procederPago(View view) {
        if (validarCampos()) {
            showToast("Campos validados correctamente");
            // Si los campos son válidos, redirigir al activity "Cancelado"
            Intent intent = new Intent(this, Cancelado.class);
            startActivity(intent);
        }
    }

    private boolean validarCampos() {
        EditText nombreEditText = findViewById(R.id.nombreEditText);
        EditText tarjetaEditText = findViewById(R.id.Codigo);
        EditText fechaVencimientoEditText = findViewById(R.id.fecha);
        EditText cvvEditText = findViewById(R.id.cvv);

        String nombre = obtenerTexto(nombreEditText);
        String tarjeta = obtenerTexto(tarjetaEditText);
        String fechaVencimiento = obtenerTexto(fechaVencimientoEditText);
        String cvv = obtenerTexto(cvvEditText);

        if (nombre.isEmpty()) {
            showToast("El campo 'Nombre' no puede estar vacío");
            return false;
        }

        if (!validarTarjeta(tarjeta)) {
            showToast("Ingrese un código de tarjeta válido (16 dígitos)");
            return false;
        }

        if (!validarFechaVencimiento(fechaVencimiento)) {
            showToast("Ingrese una fecha de vencimiento válida (MM/YY)");
            return false;
        }

        if (!validarCVV(cvv)) {
            showToast("Ingrese un CVV válido (3 dígitos)");
            return false;
        }

        return true;
    }

    private boolean validarTarjeta(String tarjeta) {
        String regex = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";
        return Pattern.matches(regex, tarjeta);
    }

    private boolean validarFechaVencimiento(String fechaVencimiento) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy", Locale.getDefault());
            dateFormat.setLenient(false);
            Date fecha = dateFormat.parse(fechaVencimiento);

            Date fechaActual = new Date();
            Date fechaLimiteInicio = dateFormat.parse("01/24");
            Date fechaLimiteFin = dateFormat.parse("12/26");

            return fecha.after(fechaActual) && fecha.after(fechaLimiteInicio) && fecha.before(fechaLimiteFin);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarCVV(String cvv) {
        return cvv.matches("\\d{3}");
    }

    private String obtenerTexto(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}