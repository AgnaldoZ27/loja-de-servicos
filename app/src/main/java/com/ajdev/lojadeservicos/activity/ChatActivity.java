package com.ajdev.lojadeservicos.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.model.Prestador;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Prestador usuarioSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Configurações da toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações Iniciais
        textViewNome = findViewById(R.id.textNomeMensagem);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);

        //Recuperar dados do prestador
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            usuarioSelecionado = (Prestador) bundle.getSerializable("chatMensagem");
            textViewNome.setText(usuarioSelecionado.getNome());
            String foto = usuarioSelecionado.getCaminhoFoto();
            if (foto != null) {
                Uri url = Uri.parse(usuarioSelecionado.getCaminhoFoto());
                Glide.with(ChatActivity.this)
                        .load(url)
                        .into(circleImageViewFoto);
            } else {
                circleImageViewFoto.setImageResource(R.drawable.avatar);
            }
        }

    }
}