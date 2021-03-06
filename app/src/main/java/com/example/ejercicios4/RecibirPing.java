package com.example.ejercicios4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RecibirPing extends AppCompatActivity {

    private String ping;
    private ListView pingPintar;
    private Button regresarBoton;
    private ArrayList<String> pings = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String host;
    private String ipHost;
    private boolean isHostOrIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibir_ping);
        pingPintar = findViewById(R.id.pingPintar);
        regresarBoton = findViewById(R.id.regresarBoton);

        ping = getIntent().getExtras().getString("pasarIP");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pings);

        host = getIntent().getExtras().getString("pasarHost");
        isHostOrIP = getIntent().getBooleanExtra("isIP", true);
        if (isHostOrIP){
            obtenerPing();
        } else {
            obtenerHost();
        }

        regresarBoton.setOnClickListener(
                (v) -> {
                finish();
                }
        );

    }

    public void obtenerPing (){
        new Thread(
                ()->{

                    try {
                        String ip = ping; //IP DEL CELULAR AL QUE LE HARÉ PING
                        InetAddress inet = InetAddress.getByName(ip);
                        for (int i=0; i<5; i++){
                            boolean conectado = inet.isReachable(5000); //1000 es el tiempo que espera a que responda
                            //Log.e(">>>", "Conectado: "+ conectado);
                            runOnUiThread(
                                    ()->{
                                        //PINTAR LOS PINGS
                                        pings.add("Conectado: " + conectado);
                                        adapter.notifyDataSetChanged();
                                        pingPintar.setAdapter(adapter);
                                    }
                            );
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    } //
    public void obtenerHost () {
        //HILO DEL HOST
        new Thread(
                () ->{
                    //MI IP : 192.168.1.12
                    // MASCARA: 255.255.255.0
                    String base = host ;
                    for (int i=1; i< 255; i++){
                        ipHost = base + i;
                        InetAddress inet;
                        try {
                            inet = InetAddress.getByName(ipHost);
                            boolean conectado = inet.isReachable(5000);
                            runOnUiThread(
                                    () -> {
                                        if (conectado) {
                                            pings.add("Conectado: " + ipHost);
                                            Log.e("<<<", "" + pings.get(0));
                                            adapter.notifyDataSetChanged();
                                            pingPintar.setAdapter(adapter);
                                        }
                                    }
                            );
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }
} //