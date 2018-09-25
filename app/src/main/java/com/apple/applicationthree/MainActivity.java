package com.apple.applicationthree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView txtResultado;
    EditText input1, input2;
    TimePicker timePicker1, timePicker2;
    Button botonSubmit;
    double valorHora, valorFraccion, res = 0;
    int minutoTime1, minutoTime2, horaTime1, horaTime2;
    boolean bandera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView para el resultado
        txtResultado = (TextView)findViewById(R.id.txtResultado);

        // Inputs para la hora
        input1 = (EditText)findViewById(R.id.input1);
        input2 = (EditText)findViewById(R.id.input2);

        // boton para el submit
        botonSubmit = (Button)findViewById(R.id.botonSubmit);

        // Inputs del TIme Picker
        timePicker1 = (TimePicker)findViewById(R.id.timePicker1);
        timePicker2 = (TimePicker)findViewById(R.id.timePicker2);
        // Seteamos valor por 24Hrs
        timePicker1.setIs24HourView(true);
        timePicker2.setIs24HourView(true);

        botonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valorFraccion = Double.parseDouble(input2.getText().toString());
                valorHora = Double.parseDouble(input1.getText().toString());
                // Obtenemos valores de los input
                horaTime1 = timePicker1.getHour();
                minutoTime1 = timePicker1.getMinute();
                horaTime2 = timePicker2.getHour();
                minutoTime2 = timePicker2.getMinute();

                if ( horaTime1 != horaTime2 ) {
                    if ( horaTime1 < horaTime2 ) { // Validacion para evitar negativos en horas
                        int horaDiferencia  = horaTime2 - horaTime1;
                        if( minutoTime1 == minutoTime2 ){
                            //Toast.makeText(MainActivity.this, "normalSalidaIgual", Toast.LENGTH_LONG).show();
                            res = salidaIgual( horaDiferencia );
                        }
                        if ( minutoTime1 < minutoTime2 ){
                            //Toast.makeText(MainActivity.this, "normalSalidaMayor", Toast.LENGTH_LONG).show();
                            res =  salidaMayor( horaDiferencia );
                        }
                        if ( minutoTime1 > minutoTime2 ){
                            //Toast.makeText(MainActivity.this, "normalSalidaMenor", Toast.LENGTH_LONG).show();
                            res = salidaMenor( horaDiferencia );
                        }
                    } else { // Cuando la hora de entrada es mayor que la hora de salida.
                        int horaDiferencias = 24 - (horaTime1 - horaTime2);

                        if( minutoTime1 == minutoTime2 ){
                            //Toast.makeText(MainActivity.this, "ElseMinutosiguales", Toast.LENGTH_LONG).show();
                            res = salidaIgual( horaDiferencias );
                        }
                        if ( minutoTime1 < minutoTime2 ){
                            //Toast.makeText(MainActivity.this, "ElseSalidaMayor", Toast.LENGTH_LONG).show();
                            res =  salidaMayor( horaDiferencias );
                        }
                        if ( minutoTime1 > minutoTime2 ){
                            //Toast.makeText(MainActivity.this, "ElseSalidaMenor", Toast.LENGTH_LONG).show();
                            res = salidaMenor( horaDiferencias );
                        }
                    }

                } else {
                    res = valorHora;
                }
                txtResultado.setText( "Total a pagar: "+ res );
            }
        });
    }

    public double salidaMayor( int horaDiferencia ){
        int auxMinutoTime1 = minutoTime1;
        double auxFraccionMinutos = 0;
        if( horaDiferencia == 1 ){
            for ( int i = 1; i <= 4 ; i++ ){
                auxMinutoTime1 = auxMinutoTime1 + 15;
                if( auxMinutoTime1 >= minutoTime2 ) {
                    auxFraccionMinutos = valorFraccion * i;
                    i = 5;
                }
            }
            res = valorHora + auxFraccionMinutos;
        } else { // Proceso para cuando estas mas de una hora
            double auxFraccionHoras = valorFraccion * ( ( horaDiferencia - 1 ) * 4 );
            for ( int i = 1; i <= 4 ; i++ ){ // Vamos a calcular las fracciones a cobrar
                auxMinutoTime1 = auxMinutoTime1 + 15;

                if( auxMinutoTime1 >= minutoTime2 ) {
                    auxFraccionMinutos = valorFraccion * i;
                    i = 5;
                }
            }
            res = valorHora + auxFraccionMinutos + auxFraccionHoras;
        }
        return res;
    }

    public double salidaIgual( int horaDiferencia ){
        if ( horaDiferencia == 1 ) {
            res = valorHora;
        } else {
            double auxFraccionMinutos = valorFraccion * ( ( horaDiferencia - 1 ) * 4 );
            res = valorHora + auxFraccionMinutos;
        }
        return res;
    }

    public double salidaMenor( int horaDiferencia ){
        if( horaDiferencia == 1 ){
            res = valorHora;
        } else {
            int auxMinutos = ( minutoTime2 + 60 ) - minutoTime1 ;
            double auxFraccionMinutos = 0;
            int horasReales = horaDiferencia - 1; // Siempre se le restara una hora por los minutos.

            for ( int i = 1; i <= 4 ; i++ ){ // Vamos a calcular las fracciones a cobrar
                auxMinutos = auxMinutos - 15;

                if( auxMinutos < 0 ) {
                    auxFraccionMinutos = valorFraccion * i;
                    i = 5;
                }
            }
            double auxFraccionHoras = valorFraccion * ( (horasReales - 1) * 4 );
            res = valorHora + auxFraccionMinutos + auxFraccionHoras;
        }
        return res;
    }
}
