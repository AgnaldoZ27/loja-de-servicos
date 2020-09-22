package com.ajdev.lojadeservicos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.helper.UsuarioFirebase;
import com.ajdev.lojadeservicos.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroPrestadorActivity extends AppCompatActivity {

    private EditText campoNome, campoCep, campoTelefone, campoEmail, campoSenha, campoCategoria, campoDescricao;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private ProgressBar progressBar;
    private String tipoCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_prestador);

        inicializarComponentes();

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tipoCadastro = "PRESTADOR";
                String textoNome = campoNome.getText().toString();
                String textoCep = campoCep.getText().toString();
                String textoTelefone = campoTelefone.getText().toString();
                String textoCategoria = campoCategoria.getText().toString();
                String textoDescricao = campoDescricao.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();


                //validar campos

                if (!textoNome.isEmpty()) {
                    if (!textoCep.isEmpty()) {
                        if (!textoTelefone.isEmpty()) {
                            if (!textoEmail.isEmpty()) {
                                if (!textoSenha.isEmpty()) {

                                    usuario = new Usuario();
                                    usuario.setTipoCadastro(tipoCadastro);
                                    usuario.setNome(textoNome);
                                    usuario.setCEP(textoCep);
                                    usuario.setTelefone(textoTelefone);
                                    usuario.setCategoria(textoCategoria);
                                    usuario.setDescricao(textoDescricao);
                                    usuario.setEmail(textoEmail);
                                    usuario.setSenha(textoSenha);
                                    cadastrarUsuario();


                                } else {
                                    Toast.makeText(CadastroPrestadorActivity.this,
                                            "Preencha o campo senha!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CadastroPrestadorActivity.this,
                                        "Preencha o campo email!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CadastroPrestadorActivity.this,
                                    "Preencha o campo telefone!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CadastroPrestadorActivity.this,
                                "Preencha o campo CEP!",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CadastroPrestadorActivity.this,
                            "Preencha o campo nome!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressBar.setVisibility(View.VISIBLE);

                            //Salvar dados do usuario no Firebase.
                            String idUsuario = task.getResult().getUser().getUid();
                            usuario.setIdUsuario(idUsuario);
                            usuario.salvar();

                            //Salvar dados do profile no Firebase.
                            UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                            Toast.makeText(CadastroPrestadorActivity.this,
                                    "Cadastro com Sucesso",
                                    Toast.LENGTH_SHORT).show();

                            finish();
                        } else {

                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                excecao = "Digite uma senha mais forte!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "Por favor, digite um e-mail válido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                excecao = "Esta conta já foi cadastrada";
                            } catch (Exception e) {
                                excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroPrestadorActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void inicializarComponentes() {

        campoNome = findViewById(R.id.editNome);
        campoCep = findViewById(R.id.editCEP);
        campoTelefone = findViewById(R.id.editTelefone);
        campoCategoria = findViewById(R.id.editTextCategoria);
        campoDescricao = findViewById(R.id.editTextDescricao);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.botaoCadastrar);
        progressBar = findViewById(R.id.progressBarCadastro);

    }
}