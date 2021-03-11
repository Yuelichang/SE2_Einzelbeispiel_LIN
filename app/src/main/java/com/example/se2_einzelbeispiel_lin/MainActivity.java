package com.example.se2_einzelbeispiel_lin;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;
import java.net.*;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button serverButton;
    EditText numText;
    Button sortButton;

    String userInput;
    String serverText;
    String nullText = "Bitte gib deine Matrikelnummer ein";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverButton = (Button) findViewById(R.id.serverButton);
        numText = (EditText) findViewById(R.id.numText);
        textView = (TextView) findViewById(R.id.answerText);
        sortButton = (Button) findViewById(R.id.sortButton);

        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(createThread());
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException ie) {
                }
                textView.setText(serverText);
            }
        });


            sortButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String output = bubbleSortAndDeletePrim();
                    textView.setText(output);
                }
            });
        }


    public Runnable createThread() {
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
        return runnable;
    }

    public String bubbleSortAndDeletePrim() {
        String output = "";
        char help;
        userInput = numText.getText().toString();
        if (userInput == null) {
            textView.setText(nullText);
        } else {
            char[] input = userInput.toCharArray();
            for (int i = 1; i < input.length; i++) {
                for (int j = 0; j < input.length-i; j++) {
                    if (input[j] > input[j+1]) {
                        help = input[j];
                        input[j] = input[j+1];
                        input[j+1] = help;
                    }
                }
            }
            output = Arrays.toString(input);
            output = output.substring(1,output.length()-1);
            output = output.replaceAll("[2357]" +  "[,]" + " "  , "");
        }
        return output;
    }
}