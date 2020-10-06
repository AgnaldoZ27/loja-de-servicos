package com.ajdev.lojadeservicos.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.activity.ChatActivity;
import com.ajdev.lojadeservicos.activity.PerfilPrestadorActivity;
import com.ajdev.lojadeservicos.adapter.ChatAdapter;
import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.helper.RecyclerItemClickListener;
import com.ajdev.lojadeservicos.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MensagemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MensagemFragment extends Fragment {

    private RecyclerView recyclerViewMensagem;
    private ChatAdapter adapter;
    private ArrayList<Usuario> mensagens = new ArrayList<>();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerMensagens;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MensagemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MensagemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MensagemFragment newInstance(String param1, String param2) {
        MensagemFragment fragment = new MensagemFragment();
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
        View view = inflater.inflate(R.layout.fragment_mensagem, container, false);

        //Configurações iniciais
        recyclerViewMensagem = view.findViewById(R.id.recyclerViewMensagem);
        usuarioRef = ConfiguracaoFirebase.getFirebaseDataBase().child("usuarios");


        //Configurações do adapter
        //adapter = new MensagemAdapter(mensagens, getActivity());

        //Configurar Recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMensagem.setLayoutManager(layoutManager);
        recyclerViewMensagem.setHasFixedSize(true);
        //recyclerViewMensagem.setAdapter(adapter);

        //Configurar evento de clique no recyclerview
        recyclerViewMensagem.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewMensagem,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Usuario usuarioSelecionado = mensagens.get(position);
                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        i.putExtra("prestadorSelecionado", usuarioSelecionado);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerMensagens);
    }

    public void recuperarMensagens(){
       valueEventListenerMensagens = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Usuario usuario = ds.getValue(Usuario.class);
                    mensagens.add(usuario);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}