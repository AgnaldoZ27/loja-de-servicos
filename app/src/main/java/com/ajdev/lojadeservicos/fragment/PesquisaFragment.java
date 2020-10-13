package com.ajdev.lojadeservicos.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajdev.lojadeservicos.R;
import com.ajdev.lojadeservicos.activity.PerfilPrestadorActivity;
import com.ajdev.lojadeservicos.adapter.PesquisaAdapter;
import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.helper.Localizacao;
import com.ajdev.lojadeservicos.helper.RecyclerItemClickListener;
import com.ajdev.lojadeservicos.helper.UsuarioFirebase;
import com.ajdev.lojadeservicos.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PesquisaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PesquisaFragment extends Fragment {

    //Widget
    private SearchView searchViewPesquisa;
    private RecyclerView recyclerViewPesquisa;

    private List<Usuario> listaPrestador;
    private DatabaseReference usuarioRef;
    private PesquisaAdapter adapterPesquisa;
    private FirebaseUser usuarioAtual;
    private Localizacao localizacao;

    private Button filtroButton;
    private String filtroDistancia = "Até 10Km";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PesquisaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PesquisaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PesquisaFragment newInstance(String param1, String param2) {
        PesquisaFragment fragment = new PesquisaFragment();
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
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        searchViewPesquisa = view.findViewById(R.id.searchViewPesquisa);
        recyclerViewPesquisa = view.findViewById(R.id.recyclerViewPesquisa);

        //Configurações iniciais
        listaPrestador = new ArrayList<>();
        usuarioRef = ConfiguracaoFirebase.getFirebaseDataBase()
                .child("usuarios");
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        //Configura RecyclerView
        recyclerViewPesquisa.setHasFixedSize(true);
        recyclerViewPesquisa.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterPesquisa = new PesquisaAdapter(listaPrestador, getActivity());
        recyclerViewPesquisa.setAdapter(adapterPesquisa);

        //Configurar evento de clique
        recyclerViewPesquisa.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerViewPesquisa,
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

        //Configura searchView
        searchViewPesquisa.setQueryHint("Buscar prestadores");
        searchViewPesquisa.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String textoDigitado = newText;
                pesquisarPrestador(textoDigitado.toLowerCase(), filtroDistancia);
                return true;
            }

        });

        //Configura evento de clique no botão de distância
        filtroButton = view.findViewById(R.id.buttonDistanciaPesquisa);
        filtroButton.setText("Até 10Km");

        final AlertDialog alerta = filtroDistancia();
        filtroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.show();
            }
        });


        return view;

    }

    private void pesquisarPrestador(final String texto, final String filtro) {

        //limpa lista
        listaPrestador.clear();

        //pesquisa usuários caso tenha texto na pesquisa
        if (texto.length() >= 1) {
            Query query = usuarioRef.orderByChild("atuacaoMinusculo")
                    .startAt(texto)
                    .endAt(texto + "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //limpa lista
                    listaPrestador.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Usuario usuario = ds.getValue(Usuario.class);
                        if (usuario.getTipoCadastro().equals("PRESTADOR")) {
                            float distancia = localizacao.calcularDistancia(usuario.getLatitude(), usuario.getLongitude());
                            switch (filtro) {
                                case "Até 10Km":
                                    if (distancia < 10) {
                                        String emailUsuarioAtual = usuarioAtual.getEmail();
                                        if (!emailUsuarioAtual.equals(usuario.getEmail())) {
                                            listaPrestador.add(usuario);
                                        }
                                    }
                                    break;
                                case "Até 25Km":
                                    if (distancia < 25) {
                                        String emailUsuarioAtual = usuarioAtual.getEmail();
                                        if (!emailUsuarioAtual.equals(usuario.getEmail())) {
                                            listaPrestador.add(usuario);
                                        }
                                    }
                                    break;
                                case "Até 50Km":
                                    if (distancia < 50) {
                                        String emailUsuarioAtual = usuarioAtual.getEmail();
                                        if (!emailUsuarioAtual.equals(usuario.getEmail())) {
                                            listaPrestador.add(usuario);
                                        }
                                    }
                                    break;
                                case "Até 100Km":
                                    if (distancia < 100) {
                                        String emailUsuarioAtual = usuarioAtual.getEmail();
                                        if (!emailUsuarioAtual.equals(usuario.getEmail())) {
                                            listaPrestador.add(usuario);
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    adapterPesquisa.notifyDataSetChanged();

                    //int total = listaUsuario.size();
                    //Log.i("totalUsuario", "total: " + total);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }



    public AlertDialog filtroDistancia() {
        AlertDialog.Builder dialogDistancia = new AlertDialog.Builder(this.getActivity());
        dialogDistancia.setTitle("Seleciona a distância desejada");

        //Configurar spinner
        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

        final Spinner spinnerDistancia = viewSpinner.findViewById(R.id.spinnerFiltro);
        String[] distancia = getResources().getStringArray(R.array.distancia);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.getActivity(), android.R.layout.simple_spinner_item,
                distancia
        );
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDistancia.setAdapter(adapter);

        dialogDistancia.setView(viewSpinner);

        dialogDistancia.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtroDistancia = spinnerDistancia.getSelectedItem().toString();
                filtroButton.setText(filtroDistancia);
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return dialogDistancia.create();

    }
}