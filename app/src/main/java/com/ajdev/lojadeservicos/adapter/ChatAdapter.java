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
    private static final int TIPO_REMETENTE_TEXTO = 0;
    private static final int TIPO_DESTINATARIO_TEXTO = 1;
    private static final int TIPO_REMETENTE_FOTO = 2;
    private static final int TIPO_DESTINATARIO_FOTO = 3;


    public ChatAdapter(List<Mensagem> listaMensagens, Context c) {
        this.mensagens = listaMensagens;
        this.context = c;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = null;
        if (viewType == TIPO_REMETENTE_TEXTO) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente_texto, parent, false);
        } else if (viewType == TIPO_REMETENTE_FOTO) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente_foto, parent, false);
        }else if (viewType == TIPO_DESTINATARIO_TEXTO) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario_texto, parent, false);
        } else if (viewType == TIPO_DESTINATARIO_FOTO) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario_foto, parent, false);
        }
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        String imagem = mensagem.getImagem();

        if (imagem != null) {
            Uri url = Uri.parse(imagem);
            Glide.with(context)
                    .load(url)
                    .into(holder.imagem);


            //Esconder o texto
            //holder.mensagem.setVisibility(View.GONE);
        } else {

            holder.mensagem.setText(msg);

            //Esconder a imagem
            //holder.imagem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mensagem = mensagens.get(position);

        String msg = mensagem.getMensagem();
        String idUsuario = UsuarioFirebase.getIdentficadorUsuario();


        if (idUsuario.equals(mensagem.getIdUsuario())) {
            if (!msg.equals("imagem.jpeg")) {
                return TIPO_REMETENTE_TEXTO;
            } else {
                return TIPO_REMETENTE_FOTO;
            }
        }
        if (!msg.equals("imagem.jpeg")) {
            return TIPO_DESTINATARIO_TEXTO;
        } else {
            return TIPO_DESTINATARIO_FOTO;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mensagem;
        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagem = itemView.findViewById(R.id.imageMensagemFoto);
            mensagem = itemView.findViewById(R.id.textNomeExibicao);
        }
    }
}
