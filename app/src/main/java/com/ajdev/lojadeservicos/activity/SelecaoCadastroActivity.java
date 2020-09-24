package com.ajdev.lojadeservicos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ajdev.lojadeservicos.R;

public class SelecaoCadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecao_tipo_cadastro);

    }
    public void btCliente(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
        finish();
    }

    public void btPrestador(View view) {
        startActivity(new Intent(this, CadastroPrestadorActivity.class));
        finish();
    }
}