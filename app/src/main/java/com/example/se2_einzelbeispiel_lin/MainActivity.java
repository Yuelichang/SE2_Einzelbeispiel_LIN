package com.example.se2_einzelbeispiel_lin;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button serverButton;
    EditText numText;
    Button sortButton;

    String userInput;
    String serverText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverButton = (Button) findViewById(R.id.serverButton);
        numText = (EditText) findViewById(R.id.numText);
        textView = (TextView) findViewById(R.id.answerText);
        sortButton = (Button) findViewById(R.id.sortButton);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    userInput = numText.getText().toString();
                    Socket clientSocket = new Socket("se2-isys.aau.at", 53212);
                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outToServer.writeBytes(userInput + "\n");
                    serverText = inFromServer.readLine();
                    clientSocket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(runnable);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException ie) {
                }
                textView.setText(serverText);

            }
        });


    }
}