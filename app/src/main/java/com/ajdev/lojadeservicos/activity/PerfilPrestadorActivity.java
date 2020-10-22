package com.ajdev.lojadeservicos.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.model.Usuario;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilPrestadorActivity extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private TextInputEditText nome, telefone, email, endereco, categoria, atuacao, descricao;
    private TextInputLayout textInputCategoria, textInputAtuacao, textInputDescricao;
    private CircleImageView fotoPerfil;
    private FloatingActionButton buttonAcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_prestador);

        //Inicializar Componenetes
        inicializarComponentes();
        buttonAcao.setImageDrawable(getResources().getDrawable(R.drawable.ic_mensagem));

        //Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //Recuperar usuario selecionadao
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioSelecionado = (Usuario) bundle.getSerializable("prestadorSelecionado");

            //Configura nome do usuário na toolbar
            getSupportActionBar().setTitle(usuarioSelecionado.getNome());

            //Configura dados do usuario.
            nome.setVisibility(View.GONE);
            telefone.setText(usuarioSelecionado.getTelefone());
            email.setText(usuarioSelecionado.getEmail());
            endereco.setText(usuarioSelecionado.getCEP());
            textInputCategoria.setVisibility(View.VISIBLE);
            categoria.setText(usuarioSelecionado.getCategoria());
            textInputAtuacao.setVisibility(View.VISIBLE);
            atuacao.setText(usuarioSelecionado.getAtuacao());
            textInputDescricao.setVisibility(View.VISIBLE);
            descricao.setText(usuarioSelecionado.getDescricao());

            //Configura foto de perfil
            String foto = usuarioSelecionado.getCaminhoFoto();
            if (foto != null) {
                Glide.with(PerfilPrestadorActivity.this)
                        .load(foto)
                        .into(fotoPerfil);
            } else {
                fotoPerfil.setImageResource(R.drawable.avatar);
            }

        }

        //Abrir activity de mensagem
        buttonAcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaChat();
            }
        });


    }

    public void inicializarComponentes() {
        buttonAcao = findViewById(R.id.fabPerfil);
        fotoPerfil = findViewById(R.id.imagePerfil);
        nome = findViewById(R.id.textViewNomePerfil);
        telefone = findViewById(R.id.textViewTelefonePerfil);
        email = findViewById(R.id.textViewEmailPerfil);
        endereco = findViewById(R.id.textViewCepPerfil);
        categoria = findViewById(R.id.textViewCategoriaPerfil);
        atuacao = findViewById(R.id.textViewAtuacaoPerfil);
        descricao = findViewById(R.id.textViewDescricaoPerfil);

        textInputCategoria = findViewById(R.id.textInputCategoriaPerfil);
        textInputAtuacao = findViewById(R.id.textInputAtuacaoPerfil);
        textInputDescricao = findViewById(R.id.textInputDescricaoPerfil);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    public void abrirTelaChat() {

        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("chatMensagem", usuarioSelecionado);
        startActivity(i);

        finish();
    }
}