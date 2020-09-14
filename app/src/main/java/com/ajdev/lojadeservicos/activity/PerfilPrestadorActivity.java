package com.ajdev.lojadeservicos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.model.Prestador;

public class PerfilPrestadorActivity extends AppCompatActivity {

    private Prestador usuarioSelecionado;
    private Button buttonAcaoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_prestador);

        //Inicializar Componenetes
        buttonAcaoPerfil = findViewById(R.id.buttonEnviarMensagem);
        buttonAcaoPerfil.setText("Enviar Mensagem");

        //Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //Recuperar usuario selecionadao
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usuarioSelecionado = (Prestador) bundle.getSerializable("usuarioSelecionado");

            //Configura nome do usuário na toolbar
            //getSupportActionBar().setTitle(usuarioSelecionado.getNome());

        }

        //Abrir activity de mensagem
        buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaChat();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    public void abrirTelaChat(){
        startActivity(new Intent(this, ChatActivity.class));
        finish();
    }
}