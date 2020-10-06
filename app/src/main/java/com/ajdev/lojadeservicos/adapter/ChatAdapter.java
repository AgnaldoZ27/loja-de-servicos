package com.ajdev.lojadeservicos.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.helper.UsuarioFirebase;
import com.ajdev.lojadeservicos.model.Mensagem;
import com.bumptech.glide.Glide;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;


    public ChatAdapter(List<Mensagem> listaMensagens, Context c) {
        this.mensagens = listaMensagens;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = null;
        if ( viewType == TIPO_REMETENTE ){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente, parent, false);
        }else if( viewType == TIPO_DESTINATARIO ){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario, parent, false);
        }
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        String imagem = mensagem.getImagem();

        if (imagem != null) {
            Uri url = Uri.parse(imagem);
            Glide.with(context)
                    .load(url)
                    .into(holder.imagem);

            //Esconder o texto
            holder.mensagem.setVisibility(View.GONE);
        } else {
            holder.mensagem.setText(msg);

            //Esconder a imagem
            holder.imagem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mensagem = mensagens.get(position);

        String idUsuario = UsuarioFirebase.getIdentficadorUsuario();

        if (idUsuario.equals(mensagem.getIdUsuario())) {
            return TIPO_REMETENTE;
        }

        return TIPO_DESTINATARIO;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mensagem;
        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagem = itemView.findViewById(R.id.imageViewMensagem);
            mensagem = itemView.findViewById(R.id.textNomeMensagem);
        }
    }
}
