package com.ajdev.lojadeservicos.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.activity.EditarPerfilActivity;
import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.helper.UsuarioFirebase;
import com.ajdev.lojadeservicos.model.Usuario;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    private CircleImageView imagePerfil;
    private TextView nomePerfil, telefonePerfil, emailPerfil, cepPerfil, categoriaPerfil, atuacaoPerfil, descricaoPerfil;
    private Button buttonAcaoPerfil;
    private DatabaseReference usuarioRef;
    private String identificadorUsuario;
    private Usuario usuario;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Configurações iniciais
        usuarioRef = ConfiguracaoFirebase.getFirebaseDataBase();
        identificadorUsuario = UsuarioFirebase.getIdentficadorUsuario();

        //Configurações dos componentes
        imagePerfil = view.findViewById(R.id.imagePerfil);
        nomePerfil = view.findViewById(R.id.textViewNomePerfil);
        emailPerfil = view.findViewById(R.id.textViewEmailPerfil);
        telefonePerfil = view.findViewById(R.id.textViewTelefonePerfil);
        cepPerfil = view.findViewById(R.id.textViewCepPerfil);
        categoriaPerfil = view.findViewById(R.id.textViewCategoriaPerfil);
        atuacaoPerfil = view.findViewById(R.id.textViewAtuacaoPerfil);
        descricaoPerfil = view.findViewById(R.id.textViewDescricaoPerfil);
        buttonAcaoPerfil = view.findViewById(R.id.buttonEditarPerfil);

        //Recuperar informações do perfil
        DatabaseReference databaseReference = usuarioRef
                .child("usuarios")
                .child(identificadorUsuario);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    usuario = dataSnapshot.getValue(Usuario.class);

                    nomePerfil.setText(usuario.getNome());
                    cepPerfil.setText(usuario.getCEP());
                    emailPerfil.setText(usuario.getEmail());
                    telefonePerfil.setText(usuario.getTelefone());
                    String tipo = usuario.getTipoCadastro();
                    if (tipo.equals("PRESTADOR")) {
                        categoriaPerfil.setVisibility(View.VISIBLE);
                        categoriaPerfil.setText(usuario.getCategoria());
                        atuacaoPerfil.setVisibility(View.VISIBLE);
                        atuacaoPerfil.setText(usuario.getAtuacao());
                        descricaoPerfil.setVisibility(View.VISIBLE);
                        descricaoPerfil.setText(usuario.getDescricao());
                    }

                    String foto = usuario.getCaminhoFoto();
                    if (foto != null) {
                        Glide.with(PerfilFragment.this)
                                .load(foto)
                                .into(imagePerfil);
                    } else {
                        imagePerfil.setImageResource(R.drawable.avatar);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Abre edição de perfil
        buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);

            }
        });

        return view;

    }


}