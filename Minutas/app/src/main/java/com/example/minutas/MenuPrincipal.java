package com.example.minutas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipal extends AppCompatActivity {
    public TextView TVNombre;
    // El menu principal de la app, se entra al hacer login, aqui se puede acceder al modulo minutas o al modulo usuarios.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuprincipal);


        String nombreUsuario = getIntent().getStringExtra("NOMBRE_USUARIO");
        TVNombre = findViewById(R.id.TVnombre);


        if (nombreUsuario != null) {
            String mensajeBienvenida = "Bienvenido, " + nombreUsuario;
            TVNombre.setText(mensajeBienvenida);
        } else {
            TVNombre.setText("Bienvenido");
        }

        Button btnUsuario = findViewById(R.id.BtnUsuarios);
        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipal.this, UsuarioModuloPrincipal.class);

                startActivity(intent);
            }

        });
        Button btnMinuta = findViewById(R.id.BtnMinuta);
        btnMinuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipal.this, MinutaModuloPrincipal.class);
                startActivity(intent);
            }

        });
    }
}
