package com.ajdev.lojadeservicos.helper;

import android.location.Location;

import androidx.annotation.NonNull;

import com.ajdev.lojadeservicos.config.ConfiguracaoFirebase;
import com.ajdev.lojadeservicos.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class Localizacao {

    private static double latitude;
    private static double longitude;

    public Localizacao() {
    }

    public static void recuperLatLng() {

        DatabaseReference usuarioRef;
        String identificadorUsuario;

        usuarioRef = ConfiguracaoFirebase.getFirebaseDataBase();
        identificadorUsuario = UsuarioFirebase.getIdentficadorUsuario();

        //Recuperar informações do perfil
        DatabaseReference databaseReference = usuarioRef
                .child("usuarios")
                .child(identificadorUsuario);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                   Usuario usuario = dataSnapshot.getValue(Usuario.class);

                   latitude = usuario.getLatitude();
                   longitude = usuario.getLongitude();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public static float calcularDistancia(Double prestadorLat, Double prestadorLng) {


       recuperLatLng();

        Location locationCliente = new Location("Local cliente");
        locationCliente.setLatitude(getLatitude());
        locationCliente.setLongitude(getLongitude());

        Location locationPrestador = new Location("Local prestador");
        locationPrestador.setLatitude(prestadorLat);
        locationPrestador.setLongitude(prestadorLng);

        //Calcula distancia - Resultado em Metros
        // dividir por 1000 para converter em KM
        float distancia = locationCliente.distanceTo(locationPrestador) / 1000;

        return distancia;
    }

    public static String formatarDistancia(float distancia) {

        String distanciaFormatada;
        if (distancia < 1) {
            distancia = distancia * 1000;//em Metros
            distanciaFormatada = Math.round(distancia) + " M ";
        } else {
            DecimalFormat decimal = new DecimalFormat("0.0");
            distanciaFormatada = decimal.format(distancia) + " KM ";
        }

        return distanciaFormatada;

    }


    public static double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
