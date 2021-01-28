package com.example.borarachar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    EditText editValue, editPeople;
    TextView result;
    FloatingActionButton share, listen;
    TextToSpeech ttsPlayer;
    int quantityPeople = 2;
    double valueBill = 0.00;
    String formattedResult = "R$ 0,00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editValue = (EditText) findViewById(R.id.billValue);
        editPeople = (EditText) findViewById(R.id.peopleQuantity);
        result = (TextView) findViewById(R.id.textViewResult);

        share = (FloatingActionButton) findViewById(R.id.floatingActionButtonShare);
        listen = (FloatingActionButton) findViewById(R.id.floatingActionButtonListen);

        TextWatcherObserver textWatcherObserver = new TextWatcherObserver(this);
        editValue.addTextChangedListener(textWatcherObserver);
        editPeople.addTextChangedListener(textWatcherObserver);
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttsPlayer != null) {
                    ttsPlayer.speak("O valor, que cada pessoa deve pagar, é de " + formattedResult + " reais.", TextToSpeech.QUEUE_FLUSH, null, "ID1");
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "A conta de R$ " + valueBill + " ficou de " + result.getText().toString() + " reais por pessoa.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 3496);
    }

    protected void calculate() {
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            valueBill = Double.parseDouble(editValue.getText().toString());
            quantityPeople = Integer.parseInt(editPeople.getText().toString());
            if (quantityPeople != 0) {
                formattedResult = df.format(valueBill / quantityPeople);
                result.setText("R$ " + formattedResult);
            } else {
                result.setText("R$ 0,00");
            }
        } catch (Exception e) {
            result.setText("R$ 0,00");
            Log.v("PDM", "ERRO: Valor incorreto");
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS ativado...", Toast.LENGTH_LONG).show();
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "TTS não habilitado...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3496) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // the user has the necessary data - create the TTS
                ttsPlayer = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);
            } else {
                // no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
}