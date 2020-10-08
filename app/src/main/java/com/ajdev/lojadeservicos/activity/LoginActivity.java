package com.ajdev.lojadeservicos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private TextView recuperarSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoEntrar = findViewById(R.id.botaoEntrar);
        recuperarSenha = findViewById(R.id.textEsqueceuSenha);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (!textoEmail.isEmpty()) {
                    if (!textoSenha.isEmpty()) {
                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin();

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha o campo senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o campo e-mail!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        recuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = campoEmail.getText().toString();
                if (!emailAddress.isEmpty()) {
                    recuperarSenha(emailAddress);
                } else {
                    Toast.makeText(LoginActivity.this,
                            "preencha o campo email!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void validarLogin() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            abrirTelaPrincipal();

                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "E-mail e senha não correspondem a um usuário cadastrado!";
                            } catch (FirebaseAuthInvalidUserException e) {
                                excecao = "Usuário não cadastrado!";
                            } catch (Exception e) {
                                excecao = "Erro ao logar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(LoginActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void recuperarSenha(String email) {

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("tag", "Email enviado.");
                            Toast.makeText(LoginActivity.this,
                                    "Um link foi enviado para seu email!",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(LoginActivity.this,
                                    "Email não cadastrado!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}