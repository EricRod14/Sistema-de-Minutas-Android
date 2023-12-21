package com.example.minutas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MinutaModuloPrincipal extends AppCompatActivity {
    // Es basicamente menu principal del modulo minutas.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modulominutas);


        Button btnUsuario = findViewById(R.id.btnMinuta);
        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MinutaModuloPrincipal.this, RegistroMinuta.class);

                startActivity(intent);
            }
        });
        Button btnVer = findViewById(R.id.btnVer);
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MinutaModuloPrincipal.this, MinutasLista.class);

                startActivity(intent);
            }
        });
    }}
