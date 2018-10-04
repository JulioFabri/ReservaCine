package com.example.julio.sistemareserva;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.julio.sistemareserva.LoginActivity.getBitmapFromURL;

public class ReservaAsiento extends AppCompatActivity {
    private int maxFila=2;
    private int maxColumna=10;
    private Button [] [] matrizBotones=new Button[maxFila][maxColumna];
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_asiento);


        matrizBotones[0][0]= (Button) findViewById(R.id.a1);
        matrizBotones[0][1]= (Button) findViewById(R.id.a2);
        matrizBotones[0][2]= (Button) findViewById(R.id.a3);
        matrizBotones[0][3]= (Button) findViewById(R.id.a4);
        matrizBotones[0][4]= (Button) findViewById(R.id.a5);
        matrizBotones[1][0]= (Button) findViewById(R.id.b1);
        matrizBotones[1][1]= (Button) findViewById(R.id.b2);
        matrizBotones[1][2]= (Button) findViewById(R.id.b3);
        matrizBotones[1][3]= (Button) findViewById(R.id.b4);
        matrizBotones[1][4]= (Button) findViewById(R.id.b5);
        matrizBotones[0][5]= (Button) findViewById(R.id.a6);
        matrizBotones[0][6]= (Button) findViewById(R.id.a7);
        matrizBotones[0][7]= (Button) findViewById(R.id.a8);
        matrizBotones[0][8]= (Button) findViewById(R.id.a9);
        matrizBotones[0][9]= (Button) findViewById(R.id.a10);
        matrizBotones[1][5]= (Button) findViewById(R.id.b6);
        matrizBotones[1][6]= (Button) findViewById(R.id.b7);
        matrizBotones[1][7]= (Button) findViewById(R.id.b8);
        matrizBotones[1][8]= (Button) findViewById(R.id.b9);
        matrizBotones[1][9]= (Button) findViewById(R.id.b10);

        try {
            cargaEstadoBoton(matrizBotones);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //FUNCION DE LOS BOTONES
        setReserva();


        // Boton confirma TODOS LOS ASIENTOS MARCADOS

        Button confirmar= (Button) findViewById(R.id.btnConfirmar);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=getIntent().getExtras();
                ArrayList <modelCartelera> listaCartelera=new ArrayList<>();
                boolean flag=false;
                for (int i=0; i<maxFila; i++) {
                    for(int j=0; j<maxColumna; j++) {
                        ColorDrawable color=(ColorDrawable) matrizBotones[i][j].getBackground();
                        if( color.getColor() == Color.parseColor("#FF99CC00") ) {
                            flag=true;
                            try {
                                 funcionBotonConfirma(bundle.getString("userID"), bundle.getString("fechaID"), bundle.getString("salaID"), matrizBotones[i][j].getHint().toString());
                                 //Toast.makeText(getApplicationContext(), "tiene q haber algo", Toast.LENGTH_SHORT).show();
                            }
                            catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (flag) {
                    //Intent i=new Intent(getApplicationContext(), grillaCartelera.class);
                    //i=new Intent(getApplicationContext(), grillaCartelera.class);
                    //i.putExtra("userID", bundle.getString("userID"));
                    //if(datosJson==null) {Toast.makeText(getApplicationContext(), "tiene q haber algo", Toast.LENGTH_SHORT).show();}
                    //i.putExtra("listaCartelera", datosJson);
                    //startActivity(i);
                    /*Gson gson=new Gson();
                    String datosJson=gson.toJson(listaCartelera);
                    i=new Intent(getApplicationContext(), grillaCartelera.class);
                    i.putExtra("listaCartelera", datosJson);
                    i.putExtra("userID", bundle.getString("userID"));*/
                    //startActivity(i);

                    Toast.makeText(getApplicationContext(), "La reserva se realizo con exito", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No selecciono ningun asiento", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //ELIGE LAS BUTACAS DISPONIBLES

    public void setReserva() {
        for(int i=0; i<maxFila; i++) {
            for(int j=0; j<maxColumna; j++) {
                matrizBotones[i][j].setOnClickListener(new clsClickListener(i,j));
            }
        }
    }

    private class clsClickListener implements View.OnClickListener {
        private int x, y;
        public clsClickListener(int x, int y) {
            this.x = x; this.y = y; }
        public void onClick(View view) {
            ColorDrawable color=(ColorDrawable) matrizBotones[x][y].getBackground();
            if(color.getColor() == Color.parseColor("#FF99CC00")) {
                matrizBotones[x][y].setBackgroundColor(Color.parseColor("#c8e1dc"));
            }else {
                matrizBotones[x][y].setBackgroundColor(Color.parseColor("#FF99CC00"));
            }
        }
    }


    //Asignacion botones en matriz DISPONIBLES-OCUPADOS

    public void cargaEstadoBoton(final Button [][] matrizBoton) throws UnsupportedEncodingException {
        RequestQueue rq= Volley.newRequestQueue(getApplicationContext());
        JsonRequest jrq;

        Bundle bundle=getIntent().getExtras();
        String fecha=bundle.getString("fechaID");

        String urlCodificada= URLEncoder.encode(fecha, "UTF-8");
        String url= "http://miusuariojf.000webhostapp.com/reservaAndroid/vistaReservas.php?sala="+bundle.getString("salaID")+"&fecha="+urlCodificada;
        //String url= "http://192.168.0.185/android/vistaReservas.php?sala="+bundle.getString("salaID")+"&fecha="+urlCodificada;
        jrq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray=response.optJSONArray("datos");
                JSONObject jsonObject=null;

                try {
                    for(int i=0; i<jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        compara(matrizBoton, jsonObject.optString("numFila"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(jrq);
    }

    //COMPARA LOS HINTS DE LOS BOTONES- PONE ROJO Y NO DISPONIBLES PARA PRESIONAR LOS ASIENTOS OCUPADOS
    public void compara(Button [][] matrizBoton, String asiento) {
        for(int i=0; i<maxFila; i++) {
            for(int j=0; j<maxColumna; j++) {
                if(matrizBoton [i][j].getHint().equals(asiento)) {
                    matrizBoton [i][j].setBackgroundColor(Color.parseColor("#FFFF4444"));
                    matrizBoton [i][j].setEnabled(false);
                }
            }
        }
    }


    // INSERCION EN LA BASE DE DATOS
    private void funcionBotonConfirma(final String user, String fecha, String sala, String asiento) throws UnsupportedEncodingException {
        RequestQueue rq= Volley.newRequestQueue(getApplicationContext());
        final ArrayList<modelCartelera> carteleras=new ArrayList<>();

        // PARA QUITAR ESPACIO EN BLANCO ENTRE FECHA Y HORA
        String urlCodificada= URLEncoder.encode(fecha, "UTF-8");
        String url="http://miusuariojf.000webhostapp.com/reservaAndroid/insertarReserva.php?userID="+user+"&fechaID="+urlCodificada+"&salaID="+sala+"&asiento="+asiento;
        //String url="http://192.168.0.185/android/insertarReserva.php?userID="+user+"&fechaID="+urlCodificada+"&salaID="+sala+"&asiento="+asiento;


        /*StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/


        JsonRequest jrq=new JsonObjectRequest(Request.Method.GET, url, null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray=response.optJSONArray("datosCartelera");
                    JSONObject jsonObject=null;
                    for(int i=0; i<jsonArray.length(); i++) {
                        jsonObject=jsonArray.getJSONObject(i);
                        modelCartelera cart=new modelCartelera(jsonObject.optString("nombre"), jsonObject.optString("descripcion"), jsonObject.optInt("IDpelicula"), jsonObject.optString("imagen"));
                        //String urlFilm = jsonObject.optString("imagen");
                        //Bitmap bit=getBitmapFromURL(urlFilm);
                        //cart.setImagenCartelera(jsonObject.optString("imagen"));

                        //int imagen = R.drawable.avengers;
                        //carteleras.add(new modelCartelera(jsonObject.optString("nombre"), jsonObject.optString("descripcion"), jsonObject.optInt("IDpelicula") ,imagen) );
                        //carteleras.add(new modelCartelera(jsonObject.optString("nombre"), jsonObject.optString("descripcion"), jsonObject.optInt("IDpelicula"), bit) );
                        carteleras.add(cart);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), "bien ahi "+carteleras.get(1).getNombre(), Toast.LENGTH_SHORT).show();
                Gson gson=new Gson();
                String datosJson=gson.toJson(carteleras);
                i=new Intent(getApplicationContext(), grillaCartelera.class);
                i.putExtra("listaCartelera", datosJson);
                i.putExtra("userID", user);
                startActivity(i);
                //if(datosJson!=null) {Toast.makeText(getApplicationContext(), "vamooooooooooo", Toast.LENGTH_SHORT).show();}

                //i.putExtra("listaCartelera", datosJson);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "mal ahi", Toast.LENGTH_SHORT).show();
            }
        });
        //i.putExtra("userID", user);
        rq.add(jrq);
    }

    public static Bitmap getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }



}
