package com.example.minutas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UsuarioModuloPrincipal extends AppCompatActivity {
    // Es basicamente menu principal del modulo usuarios.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modulousuario);


        Button btnUsuario = findViewById(R.id.btnUsuario);
        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsuarioModuloPrincipal.this, RegistroUsuario.class);

                startActivity(intent);
            }
        });

        Button btnVer = findViewById(R.id.btnVer);
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsuarioModuloPrincipal.this, UsuariosLista.class);
                startActivity(intent);
            }
        });
        Button btnEmail = findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsuarioModuloPrincipal.this, EnviarCorreo.class);

                startActivity(intent);
            }
        });
    }
}
