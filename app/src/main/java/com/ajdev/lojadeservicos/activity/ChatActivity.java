package com.ajdev.lojadeservicos.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.helper.UsuarioFirebase;
import com.ajdev.lojadeservicos.model.Mensagem;
import com.ajdev.lojadeservicos.model.Usuario;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Usuario usuarioSelecionado;
    private EditText editMensagem;

    //Identificador usuarios remetente e destinatarios
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

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
        editMensagem = findViewById(R.id.editTextMensagem);

        //Recuperar dados do usuario remetente
        idUsuarioRemetente = UsuarioFirebase.getIdentficadorUsuario();

        //Recuperar dados do prestador
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            usuarioSelecionado = (Usuario) bundle.getSerializable("chatMensagem");
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

            //Recuperar dados do destinatario
            idUsuarioDestinatario = usuarioSelecionado.getIdUsuario();

        }

    }

    public void enviarMensagem(View view){
        String textoMensagem = editMensagem.getText().toString();
        if(!textoMensagem.isEmpty()){

            Mensagem msg = new Mensagem();
            msg.setIdUsuario(idUsuarioRemetente);
            msg.setMensagem(textoMensagem);

            //Salvar Mensagem para o remetente
            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, msg);

        }else{
            Toast.makeText(ChatActivity.this,
            "Digite uma mensagem para enviar",
                    Toast.LENGTH_LONG).show();
        }
    }

    private  void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){

        DatabaseReference databaseRef = ConfiguracaoFirebase.getFirebaseDataBase();
        DatabaseReference mensagemRef = databaseRef.child("mensagens");

        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(mensagem);

        //LimparTexto
        editMensagem.setText("");


    }

}