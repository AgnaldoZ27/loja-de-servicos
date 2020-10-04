package com.ajdev.lojadeservicos.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.helper.UsuarioFirebase;
import com.ajdev.lojadeservicos.model.Usuario;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView imageEditarPerfil;
    private TextView textAlterarFoto;
    private TextInputEditText editNomePerfil, editCepPerfil, editTelefonePerfil, editEmailPerfil, editCategoriaPerfil, editAtuacaoPerfil, editDescricaoPerfil;
    private TextInputLayout textInputCategoria, textInputAtuacao, textInputDescricao;
    private Button buttonSalvarAlteracoes;
    private static final int SELECAO_GALERIA = 1;
    private StorageReference storageRef;
    private Usuario usuario;
    private DatabaseReference firebaseRef;
    private String identificadorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //configurações iniciais
        storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDataBase();
        identificadorUsuario = UsuarioFirebase.getIdentficadorUsuario();

        //Configuração toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //inicializar componentes
        inicializarComponentes();

        //recuperar dados do usuario.
        DatabaseReference databaseReference = firebaseRef
                .child("usuarios")
                .child(identificadorUsuario);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                    editNomePerfil.setText(usuario.getNome());
                    editCepPerfil.setText(usuario.getCEP());
                    editEmailPerfil.setText(usuario.getEmail());
                    editTelefonePerfil.setText(usuario.getTelefone());
                    String tipo = usuario.getTipoCadastro();
                    if (tipo.equals("PRESTADOR")) {
                        textInputCategoria.setVisibility(View.VISIBLE);
                        editCategoriaPerfil.setText(usuario.getCategoria());
                        textInputAtuacao.setVisibility(View.VISIBLE);
                        editAtuacaoPerfil.setText(usuario.getAtuacao());
                        textInputDescricao.setVisibility(View.VISIBLE);
                        editDescricaoPerfil.setText(usuario.getDescricao());
                    }

                    String foto = usuario.getCaminhoFoto();
                    if (foto != null) {
                        Glide.with(EditarPerfilActivity.this)
                                .load(foto)
                                .into(imageEditarPerfil);
                    } else {
                        imageEditarPerfil.setImageResource(R.drawable.avatar);
                    }

                }

                //salvar alterações
                buttonSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String nome = editNomePerfil.getText().toString();
                        String cep = editCepPerfil.getText().toString();
                        String telefone = editTelefonePerfil.getText().toString();
                        String email = editEmailPerfil.getText().toString();
                        String categoria = editCategoriaPerfil.getText().toString();
                        String atuacao = editAtuacaoPerfil.getText().toString();
                        String descricao = editDescricaoPerfil.getText().toString();


                        //atualizar nome no perfil.
                        UsuarioFirebase.atualizarNomeUsuario(nome);

                        usuario.setNome(nome);
                        usuario.setCEP(cep);
                        usuario.setTelefone(telefone);
                        usuario.setEmail(email);
                        String tipo = usuario.getTipoCadastro();
                        if (tipo.equals("PRESTADOR")) {
                            usuario.setCategoria(categoria);
                            usuario.setAtuacao(atuacao);
                            usuario.setDescricao(descricao);
                        }

                        //atualizar no banco de dados.
                        usuario.atualizar();

                        Toast.makeText(EditarPerfilActivity.this,
                                "Dados alterados com Sucesso",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Alterar foto do usuário
        textAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Bitmap imagem = null;

            try {
                //Selecao apenas da galeria
                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                //Caso tenha sido escolhido uma imagem
                if (imagem != null) {

                    //Configurar imagem na tela
                    imageEditarPerfil.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();


                    //Salvar imagem no firebase.
                    final StorageReference imageRef = storageRef
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario + ".jpeg");
                    UploadTask uploadTask = imageRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditarPerfilActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Recuper local da foto
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizarFotoUsuario(url);
                                }
                            });


                            Toast.makeText(EditarPerfilActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void atualizarFotoUsuario(Uri url) {
        //Atualizar foto no perfil
        UsuarioFirebase.atualizarFotoUsuario(url);

        Toast.makeText(EditarPerfilActivity.this,
                "Sua foto foi atualizada!",
                Toast.LENGTH_SHORT).show();
    }

    public void inicializarComponentes() {

        imageEditarPerfil = findViewById(R.id.imagePerfil);
        textAlterarFoto = findViewById(R.id.textAlterarFoto);
        editNomePerfil = findViewById(R.id.editAlterarNome);
        editCepPerfil = findViewById(R.id.editAlterarCep);
        editTelefonePerfil = findViewById(R.id.editAlterarTelefone);
        editEmailPerfil = findViewById(R.id.editAlterarEmail);
        editCategoriaPerfil = findViewById(R.id.editAlterarCategoria);
        editAtuacaoPerfil = findViewById(R.id.editAlterarAtuacao);
        editDescricaoPerfil = findViewById(R.id.editAlterarDescricao);
        buttonSalvarAlteracoes = findViewById(R.id.buttonSalvarAlteracoes);
        editEmailPerfil.setFocusable(false);

        textInputCategoria = findViewById(R.id.textInputCategoria);
        textInputAtuacao = findViewById(R.id.textInputAtuacao);
        textInputDescricao = findViewById(R.id.textInputAtuacao);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

}
