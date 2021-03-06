package com.example.ejercicios4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList <String> numerosIp;
    private EditText ipUno, ipDos, ipTres, ipCuatro;
    private Button botonPing;
    private TextView ipDispositivo;
    private Button botonHost;
    private String redGlobal;
    private boolean isHostOrIP;
    //ip
    private String userIPUno, userIPDos, userIPTres, userIPCuatro;
    private String ipCompleta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipUno = findViewById(R.id.ipUno);
        ipDos = findViewById(R.id.ipDos);
        ipTres = findViewById(R.id.ipTres);
        ipCuatro = findViewById(R.id.ipCuatro);
        botonPing = findViewById(R.id.botonPing);
        ipDispositivo = findViewById(R.id.ipDispositivo);
        botonHost = findViewById(R.id.botonHost);

        encontrarIp();


        //Boton
        botonHost.setOnClickListener(
                (v) -> {
                    Intent i = new Intent(this, RecibirPing.class);
                    i.putExtra("pasarHost", redGlobal);
                    isHostOrIP = false;
                    i.putExtra("isIP", isHostOrIP);
                    startActivity(i);
                }
        );

        botonPing.setOnClickListener(
            (v)->{
             getNumerosIp();
                Intent i = new Intent(this, RecibirPing.class);
                i.putExtra("pasarIP", ipCompleta);
                isHostOrIP = true;
                i.putExtra("isIP", isHostOrIP);
                startActivity(i);
            }
        );
    }//
    private void getNumerosIp(){
        userIPUno = ipUno.getText().toString();
        userIPDos = ipDos.getText().toString();
        userIPTres = ipTres.getText().toString();
        userIPCuatro = ipCuatro.getText().toString();

        ipCompleta = userIPUno + "." + userIPDos + "." + userIPTres + "." + userIPCuatro;
    } //
    public void encontrarIp() {
        new Thread(
                () -> {
                    Socket s = null;
                    try {
                        s = new Socket("google.com", 80);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String ip = s.getLocalAddress().getHostAddress();
                    runOnUiThread(
                            () -> {
                                ipDispositivo.setText(ip);
                            }
                    );
                    try {
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String[] ipH = ip.split("\\.");
                    redGlobal = ipH[0]+"."+ipH[1]+"."+ipH[2]+".";
                }
        ).start();
    }
}