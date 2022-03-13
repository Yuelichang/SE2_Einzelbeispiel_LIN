package com.example.se2_einzelbeispiel_lin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button serverBtn;
    Button calcBtn;
    EditText mNrTxt;
    TextView textView;
    TextView calcView;
    TextView serverView;

    String userInput;
    String serverText;
    String nullText = "Bite gib deine Matrikelnummer ein";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverBtn = (Button) findViewById(R.id.serverBtn);
        calcBtn = (Button) findViewById(R.id.calcBtn);
        mNrTxt = (EditText) findViewById(R.id.mNrTxt);
        textView = (TextView) findViewById(R.id.textView);
        calcView = (TextView) findViewById(R.id.calcView);
        serverView = (TextView) findViewById(R.id.serverView);

//--------------------OnClick ServerBtn---------------------

        serverBtn.setOnClickListener(view -> {
            Thread thread = new Thread(createThread());
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException ie) {
            }
            serverView.setText(serverText);
        });


//------------------OnClick CalcBtn-----------------------------

        calcBtn.setOnClickListener(view -> {
            String output = toBinary(checksum());
            calcView.setText(output);
        });
    }

//-------------------create Thread------------------------------

    public Runnable createThread() {
        Runnable runnable = () -> {
            try {
                userInput = mNrTxt.getText().toString();
                Socket clientSocket = new Socket("se2-isys.aau.at", 53212);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes(userInput + "\n");
                serverText = inFromServer.readLine();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return runnable;
    }

//-----------------Quersumme berechnen-------------------------

    public String checksum() {
        String output = "";
        userInput = mNrTxt.getText().toString();
        char[] userInputArray = userInput.toCharArray();
        int sum = 0;
        for (int i = 0; i < userInputArray.length; i++) {
            sum += Character.getNumericValue(userInputArray[i]);
        }
        return String.valueOf(sum);
    }

//----------------toBinary----------------------------------

    public String toBinary(String sum) {
        StringBuilder result = new StringBuilder();
        int number = Integer.parseInt(sum);
        while (number!=0) {
            result.append(number % 2);
            number = number /2;
        }
        result.reverse();
        if (result.length() != 0 ) { return result.toString(); }
        else return nullText;
    }
}

