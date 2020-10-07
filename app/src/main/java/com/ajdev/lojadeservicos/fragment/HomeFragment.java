package com.ajdev.lojadeservicos.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.activity.PerfilPrestadorActivity;
import com.ajdev.lojadeservicos.adapter.PesquisaAdapter;
import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.config.Permissoes;
import com.ajdev.lojadeservicos.helper.RecyclerItemClickListener;
import com.ajdev.lojadeservicos.helper.UsuarioFirebase;
import com.ajdev.lojadeservicos.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewHome;
    private DatabaseReference usuarioRef;
    private PesquisaAdapter adapter;
    private List<Usuario> listaPrestador = new ArrayList<>();
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION};
    private String identificadorUsuario;
    private FirebaseUser usuarioAtual;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Configurações inicias
        recyclerViewHome = view.findViewById(R.id.recyclerViewHome);
        usuarioRef = ConfiguracaoFirebase.getFirebaseDataBase().child("usuarios");
        identificadorUsuario = UsuarioFirebase.getIdentficadorUsuario();
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this.getActivity(), 1);

        //Configurações do adapter
        adapter = new PesquisaAdapter(listaPrestador, getActivity());

        //Configura RecyclerView
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PesquisaAdapter(listaPrestador, getActivity());
        recyclerViewHome.setAdapter(adapter);

        //

        //Recupera prestadores
        recuperarPrestadores();

        //Configurar evento de clique
        recyclerViewHome.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewHome,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Usuario usuarioSelecionado = listaPrestador.get(position);
                        Intent i = new Intent(getActivity(), PerfilPrestadorActivity.class);
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

    public void recuperarPrestadores() {
        listaPrestador.clear();
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Usuario usuario = ds.getValue(Usuario.class);
                    if (usuario.getTipoCadastro().equals("PRESTADOR")) {
                        String emailUsuarioAtual = usuarioAtual.getEmail();
                        if(!emailUsuarioAtual.equals(usuario.getEmail())){
                            listaPrestador.add(usuario);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}