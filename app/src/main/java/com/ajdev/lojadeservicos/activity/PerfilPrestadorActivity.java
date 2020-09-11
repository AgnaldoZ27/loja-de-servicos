package com.ajdev.lojadeservicos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

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
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        buttonAcaoPerfil.setText("Enviar Mensagem");

        //Configurar toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        //Recuperar usuario selecionadao
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usuarioSelecionado = (Prestador) bundle.getSerializable("usuarioSelecionado");

            //Configura nome do usuário na toolbar
            getSupportActionBar().setTitle(usuarioSelecionado.getNome());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}