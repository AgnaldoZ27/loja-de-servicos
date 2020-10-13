package com.ajdev.lojadeservicos.model;

import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.helper.Localizacao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable, Comparable<Usuario>{

    private Localizacao localizacao;

    private String idUsuario;
    private String tipoCadastro;
    private String nome;
    private String CEP;
    private String telefone;
    private String email;
    private String senha;
    private String caminhoFoto;
    private String CPF_CNPJ;
    private String categoria;
    private String atuacao;
    private String atuacaoMinusculo;
    private String descricao;
    private Double latitude;
    private Double longitude;

    public Usuario() {

    }

    public void salvar() {
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase();
        firebase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this);

    }

    public void atualizar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDataBase();
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(getIdUsuario());

        Map<String, Object> valoresUsuario = convertParaMap();
        usuarioRef.updateChildren(valoresUsuario);
    }

    public Map<String, Object> convertParaMap() {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("tipoCadastro", getTipoCadastro());
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("cep", getCEP());
        usuarioMap.put("telefone", getTelefone());
        usuarioMap.put("idUsuario", getIdUsuario());
        usuarioMap.put("caminhoFoto", getCaminhoFoto());
        usuarioMap.put("CPF_CNPJ", getCPF_CNPJ());
        usuarioMap.put("categoria", getCategoria());
        usuarioMap.put("atuacao", getAtuacao());
        usuarioMap.put("atuacaoMinusculo", getAtuacaoMinusculo());
        usuarioMap.put("descricao", getDescricao());
        usuarioMap.put("latitude", getLatitude());
        usuarioMap.put("longitude", getLongitude());

        return usuarioMap;
    }

    public Usuario(String idUsuario, String tipoCadastro, String nome, String CEP, String telefone, String email, String senha, String caminhoFoto, String CPF_CNPJ, String categoria, String atuacao, String atuaocaoMinusculo, String descricao, Double latitude, Double longitude) {
        this.idUsuario = idUsuario;
        this.tipoCadastro = tipoCadastro;
        this.nome = nome;
        this.CEP = CEP;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.caminhoFoto = caminhoFoto;
        this.CPF_CNPJ = CPF_CNPJ;
        this.categoria = categoria;
        this.atuacao = atuacao;
        this.atuacaoMinusculo = atuaocaoMinusculo;
        this.descricao = descricao;
        this.latitude = latitude;
        this.longitude = longitude;

    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoCadastro() {
        return tipoCadastro;
    }

    public void setTipoCadastro(String tipoCadastro) {
        this.tipoCadastro = tipoCadastro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
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

    public String getAtuacao() {
        return atuacao;
    }

    public void setAtuacao(String atuacao) {
        this.atuacao = atuacao;
    }

    public String getAtuacaoMinusculo() {
        return atuacaoMinusculo;
    }

    public void setAtuacaoMinusculo(String atuacaoMinusculo) {
        this.atuacaoMinusculo = atuacaoMinusculo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int compareTo(Usuario o) {
        float distancia = localizacao.calcularDistancia(this.latitude, this.longitude);
        float distancia2 = localizacao.calcularDistancia(o.getLatitude(), o.getLongitude());
        if(distancia > distancia2){
            return 1;
        }
        if(distancia < distancia2){
            return -1;
        }
        return 0;
    }
}
