package com.example.julio.sistemareserva;

import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class grillaCartelera extends AppCompatActivity {

    private RecyclerView recyclerViewCartelera;
    private recyclerViewAdaptador adaptadorCartelera;
    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grilla_cartelera);


        final Bundle bundle=getIntent().getExtras();

        recyclerViewCartelera=(RecyclerView)findViewById(R.id.recyclerCartelera);
        recyclerViewCartelera.setLayoutManager(new LinearLayoutManager(this));

        RecyclerViewOnItemClickListener popupFecha= new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

                //POPUP USANDO DIALOG
                Dialog dialog = new Dialog(grillaCartelera.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.ventana_fecha);
                dialog.setCanceledOnTouchOutside(true);

                TextView alertbox_title = (TextView) dialog.findViewById(R.id.alertbox_title);
                alertbox_title.setText("Seleccione fecha");
                TextView nombrePelicula= dialog.findViewById(R.id.namePelicula);
                nombrePelicula.setText(adaptadorCartelera.getCarteleraLista().get(position).getNombre());

                final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);

                RequestQueue rq= Volley.newRequestQueue(getApplicationContext());
                JsonRequest jrq;

                int idPeli=adaptadorCartelera.getCarteleraLista().get(position).getId();
                String film=String.valueOf(idPeli);

                String url= "http://miusuariojf.000webhostapp.com/reservaAndroid/selectFecha.php?film="+film;
                //String url= "http://192.168.0.185/android/selectFecha.php?film="+film;

                jrq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray= response.optJSONArray("datos");
                        JSONObject jsonObject= null;
                        final ArrayList<String> fechasPeliculas=new ArrayList<String>();
                        fechasPeliculas.add("Fecha-Hora");

                        try {
                            for(int i=0; i<jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                fechasPeliculas.add(jsonObject.optString("IDfecha")+" | SALA "+jsonObject.optString("IDsala") );
                            }
                            ArrayAdapter listaSpinner=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, fechasPeliculas);
                            spinner.setAdapter(listaSpinner);
                            spinner.setSelection(0);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position !=0) {
                                        Toast.makeText(getApplicationContext (), fechasPeliculas.get(position), Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(grillaCartelera.this, ReservaAsiento.class);
                                        i.putExtra("userID", bundle.getString("idUsuario"));
                                        //SEPARANDO CADENA FECHA-SALA
                                        String [] fecha=fechasPeliculas.get(position).split("\\|");
                                        i.putExtra("fechaID", fecha [0]);
                                        char salaPos=fecha[1].charAt(fecha[1].length()-1);
                                        String sala=String.valueOf(salaPos);
                                        i.putExtra("salaID", sala);
                                        startActivity(i);
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext (), "Se produjo un error inesperado "+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                rq.add(jrq);

                dialog.show();
                //FIN POPUP
            }
        };


        if(bundle.getString("listaCartelera")!=null) {
            Gson gson=new Gson();
            String datosCartelera=bundle.getString("listaCartelera");
            Type type=new TypeToken<ArrayList<modelCartelera>>(){}.getType();
            ArrayList<modelCartelera> carteleras=gson.fromJson(datosCartelera, type);

            adaptadorCartelera=new recyclerViewAdaptador(carteleras, popupFecha, getApplicationContext());
            recyclerViewCartelera.setAdapter(adaptadorCartelera);
        }
        else {
            Toast.makeText(getApplicationContext(), "no pasa nada", Toast.LENGTH_SHORT).show();
            //adaptadorCartelera=new recyclerViewAdaptador(selectCarteleras(), popupFecha);
            //recyclerViewCartelera.setAdapter(adaptadorCartelera);
        }


    }


    @Override
    public void onBackPressed() {

    }
    //ESTA PETICION HTTP LA REALIZO EN EL MAIN ACTIVITY JUNTO A LA DEL USUARIO Y EN RESERVAASIENTO PARA NO REALIZAR MUCHAS PETICIONES EN SIMULTANEO

/*
    private List<modelCartelera> selectCarteleras() {
        final List<modelCartelera> carteleras=new ArrayList<>();

        RequestQueue rq= Volley.newRequestQueue(this);
        JsonRequest jrq;
        String url= "http://miusuariojf.000webhostapp.com/reservaAndroid/carteleras.php";
        //String url= "http://192.168.0.185/android/carteleras.php";

        jrq=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray= response.optJSONArray("datos");
                JSONObject jsonObject= null;
                try {
                    for(int i=0; i<jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String urlFilm=jsonObject.optString("imagen");
                        int imagen=R.drawable.avengers;
                        carteleras.add(new modelCartelera(jsonObject.optString("nombre"), jsonObject.optString("descripcion"), jsonObject.optInt("IDpelicula"), imagen) );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext (), "Error en la coneccion"+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        rq.add(jrq);
        return carteleras;
    }
*/

}
