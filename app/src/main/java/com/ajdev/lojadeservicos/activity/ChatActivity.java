package com.ajdev.lojadeservicos.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.adapter.ChatAdapter;
import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.helper.UsuarioFirebase;
import com.ajdev.lojadeservicos.model.Mensagem;
import com.ajdev.lojadeservicos.model.Usuario;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Usuario usuarioDestinario;
    private ImageView imageCamera;
    private EditText editMensagem;
    private DatabaseReference database;
    private StorageReference storage;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;


    //Identificador usuarios remetente e destinatarios
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    private RecyclerView recyclerMensagens;
    private ChatAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Configurações da toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //Configurações Iniciais
        textViewNome = findViewById(R.id.textNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        editMensagem = findViewById(R.id.editTextMensagemChat);
        recyclerMensagens = findViewById(R.id.recyclerChat);
        imageCamera = findViewById(R.id.imageViewCamera);

        //Recuperar dados do usuario remetente
        idUsuarioRemetente = UsuarioFirebase.getIdentficadorUsuario();

        //Recuperar usuario selecionadao
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuarioDestinario = (Usuario) bundle.getSerializable("chatMensagem");

            //Configura nome do usuário na toolbar
            textViewNome.setText(usuarioDestinario.getNome());

            String foto = usuarioDestinario.getCaminhoFoto();
            if (foto != null) {
                Uri url = Uri.parse(usuarioDestinario.getCaminhoFoto());
                Glide.with(ChatActivity.this)
                        .load(url)
                        .into(circleImageViewFoto);
            } else {
                circleImageViewFoto.setImageResource(R.drawable.avatar);
            }

            //Recuperar dados do destinatario
            idUsuarioDestinatario = usuarioDestinario.getIdUsuario();

        }

        //Configuração do adapter
        adapter = new ChatAdapter(mensagens, getApplicationContext());

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);

        database = ConfiguracaoFirebase.getFirebaseDataBase();
        mensagensRef = database.child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

    }

    public void enviarMensagem(View view) {

        String textoMensagem = editMensagem.getText().toString();

        if (!textoMensagem.isEmpty()) {

            Mensagem msg = new Mensagem();
            msg.setIdUsuario(idUsuarioRemetente);
            msg.setMensagem(textoMensagem);

            //Salvar Mensagem para o remetente
            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, msg);

            //Salvar mensagem para o destinatario
            salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, msg);


        } else {
            Toast.makeText(ChatActivity.this,
                    "Digite uma mensagem para enviar",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem) {

        DatabaseReference databaseRef = ConfiguracaoFirebase.getFirebaseDataBase();
        DatabaseReference mensagemRef = databaseRef.child("mensagens");

        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);

        //LimparTexto
        editMensagem.setText("");


    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagens() {
        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}