package com.ajdev.lojadeservicos.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.helper.Localizacao;
import com.ajdev.lojadeservicos.model.Usuario;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPesquisa extends RecyclerView.Adapter<AdapterPesquisa.MyViewHolder> {

    private List<Usuario> listaUsuario;
    private Context context;
    private Localizacao localizacao;


    public AdapterPesquisa(List<Usuario> l, Context c) {
        this.listaUsuario = l;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pesquisa_prestador, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = listaUsuario.get(position);

        holder.nome.setText(usuario.getNome());
        holder.categoria.setText(usuario.getCategoria());
        holder.atuacao.setText(usuario.getAtuacao());

        float distancia = localizacao.calcularDistancia(usuario.getLatitude(), usuario.getLongitude());
        String distanciaFormatada = localizacao.formatarDistancia(distancia);
        holder.distancia.setText(distanciaFormatada + "- aproximadamente");

        //Configuração de foto
        String foto = usuario.getCaminhoFoto();
        if (foto != null) {
            Uri uri = Uri.parse(usuario.getCaminhoFoto());
            Glide.with(context).load(uri).into(holder.foto);
        } else {
            holder.foto.setImageResource(R.drawable.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return listaUsuario.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome, categoria, atuacao, distancia;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageFotoPesquisa);
            nome = itemView.findViewById(R.id.textNomePesquisa);
            categoria = itemView.findViewById(R.id.textCategoriaPesquisa);
            atuacao = itemView.findViewById(R.id.textAtuacaoPesquisa);
            distancia = itemView.findViewById(R.id.textViewDistancia);

        }
    }
}
