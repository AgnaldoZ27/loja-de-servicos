package com.ajdev.lojadeservicos.model;

import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Prestador extends Usuario implements Serializable {

    private String CPF_CNPJ;
    private String categoria;
    private String descricao;

    public Prestador() {
    }

    public Prestador(String idUsuario, String tipoCadastro, String nome, String CEP, String telefone, String email, String senha, String caminhoFoto, String CPF_CNPJ, String categoria, String descricao) {
        super(idUsuario, tipoCadastro, nome, CEP, telefone, email, senha, caminhoFoto);
        this.CPF_CNPJ = CPF_CNPJ;
        this.categoria = categoria;
        this.descricao = descricao;
    }

    public void salvarPrestador() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase();
        firebase.child("usuarios")
                .child("prestador")
                .child(this.getIdUsuario())
                .setValue(this);
    }

    public void atualizarPrestador() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDataBase();
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child("prestador")
                .child(getIdUsuario());

        Map<String, Object> valoresUsuario = convertParaMap();
        usuarioRef.updateChildren(valoresUsuario);
    }

    public Map<String, Object> convertParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("tipoCadastro", getTipoCadastro());
        usuarioMap.put("email",getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("telefone", getTelefone());
        usuarioMap.put("CPF_CNPJ", getCPF_CNPJ());
        usuarioMap.put("categoria", getCategoria());
        usuarioMap.put("descricao", getDescricao());
        usuarioMap.put("idPrestador", getIdUsuario());
        usuarioMap.put("caminhoFoto", getCaminhoFoto());

        return usuarioMap;
    }

    public String getCPF_CNPJ() {
        return CPF_CNPJ;
    }

    public void setCPF_CNPJ(String CPF_CNPJ) {
        this.CPF_CNPJ = CPF_CNPJ;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

