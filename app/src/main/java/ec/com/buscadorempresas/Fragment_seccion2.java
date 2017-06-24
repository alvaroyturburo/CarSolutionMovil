package ec.com.buscadorempresas;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ec.com.buscadorempresas.model.Empresa;


public class Fragment_seccion2 extends Fragment {
    private static final String TAG = "Fragment_seccion2";
    static String NAMESPACE = "http://webservices.upse.edu.ec";
    static String URL = "http://192.168.1.63:8080/ServicesEmpresa/services/ServiciosEmpresa?wsdl";
    private String SOAP_ACTION1="http:///192.168.1.63:8080/ServicesEmpresa/services/ServiciosEmpresa/listaEmpresasTipo";
    private String METODO1="listaEmpresasTipo";

    private RecyclerView rvListaEmpresa;
    private ArrayList<Empresa> empresas;
    ArrayList<Empresa>  listaEmpresasFiltro;
    private RecyclerView rvListaRecurso;
    private RadioButton rbDistancia, rbNombre ;
    private ViewGroup layout;
    private ImageButton btDefault;
    Location loc;
    private LocationManager locManager;
    View vista;
    int contador = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        inicLocation();
        registerLocation();

        vista = inflater.inflate(R.layout.fragment_seccion2, container, false);

        rbDistancia = (RadioButton)vista.findViewById(R.id.rbDistancia);
        rbNombre = (RadioButton)vista.findViewById(R.id.rbNombre);
        layout = (ViewGroup) vista.findViewById(R.id.contentLayout);
        btDefault = (ImageButton) vista.findViewById(R.id.btDefault);

        rbDistancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                addDistancia();
                rbNombre.setChecked(false);
            }
        });
        rbNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                addNombre();
                rbDistancia.setChecked(false);
            }
        });
        btDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                rbDistancia.setChecked(false);
                rbNombre.setChecked(false);
                //inicializarDatos();
                inicializarAdaptador();
            }
        });

        rvListaRecurso = (RecyclerView) vista.findViewById(R.id.reciclador);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvListaRecurso.setLayoutManager(llm);

        inicializarDatos();
        contador=contador +1;
        if(contador<2){

            final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Cargando...");
            progressDialog.show();

            // TODO: Implement your own authentication logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 10000);}

        inicializarAdaptador();

        return vista;
    }

    public void inicializarDatos(){
        empresas = new ArrayList<>();
        SoapObject request = new SoapObject(NAMESPACE, METODO1);
        request.addProperty("request" ,"1");

        SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
        Envoltorio.setOutputSoapObject (request);

        HttpTransportSE TransporteHttp = new HttpTransportSE(URL);

        try {
            TransporteHttp.call(SOAP_ACTION1, Envoltorio);

            SoapObject result = (SoapObject) Envoltorio.bodyIn;

            if(result != null){
                String json = result.getProperty(0).toString();
                Gson gson = new Gson();
                Type founderListType = new TypeToken<ArrayList<Empresa>>(){}.getType();
                empresas = gson.fromJson(json, founderListType);
                Log.d("jasondatos", json);
                //Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this.getContext().getApplicationContext(), "No Response!", Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.getContext().getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();

        }

    }


    public ListaEmpresaAdapter adaptador;
    private void inicializarAdaptador(){
        adaptador = new ListaEmpresaAdapter(empresas);
        rvListaRecurso.setAdapter(adaptador);
    }

    private void inicializarAdaptadorFiltro(){
        adaptador = new ListaEmpresaAdapter(listaEmpresasFiltro);
        rvListaRecurso.setAdapter(adaptador);
    }

   public void inicializarDatos(String filtro, String parametro){
       listaEmpresasFiltro = new ArrayList<>();
       for(Empresa r : empresas) {
            if(filtro.equals("Distancia")){

                if (parametro.equals("Mostrar todos"))
                {
                    listaEmpresasFiltro.add(r);

                }

                if (parametro.equals("5 kilometros a la redonda"))
                {
                    LatLng puntoRecurso;
                    puntoRecurso = new LatLng(Double.parseDouble(r.getLatitud()), Double.parseDouble(r.getLongitud()));
                    if(distancia_loc_contenidos(loc,puntoRecurso,5)){
                        listaEmpresasFiltro.add(r);
                    }
                }


                if (parametro.equals("10 kilometros a la redonda"))
                {
                    LatLng puntoRecurso;
                    puntoRecurso = new LatLng(Double.parseDouble(r.getLatitud()), Double.parseDouble(r.getLongitud()));
                    if(distancia_loc_contenidos(loc,puntoRecurso,10)){
                        listaEmpresasFiltro.add(r);
                    }

                }
                if (parametro.equals("15 kilometros a la redonda"))
                {
                    LatLng puntoRecurso;
                    puntoRecurso = new LatLng(Double.parseDouble(r.getLatitud()), Double.parseDouble(r.getLongitud()));
                    if(distancia_loc_contenidos(loc,puntoRecurso,15)){
                        listaEmpresasFiltro.add(r);
                    }

                }
                if (parametro.equals("25 kilometros a la redonda"))
                {
                    LatLng puntoRecurso;
                    puntoRecurso = new LatLng(Double.parseDouble(r.getLatitud()), Double.parseDouble(r.getLongitud()));
                    if(distancia_loc_contenidos(loc,puntoRecurso,25)){
                        listaEmpresasFiltro.add(r);
                    }

                }
                if (parametro.equals("40 kilometros a la redonda"))
                {
                    LatLng puntoRecurso;
                    puntoRecurso = new LatLng(Double.parseDouble(r.getLatitud()), Double.parseDouble(r.getLongitud()));
                    if(distancia_loc_contenidos(loc,puntoRecurso,40)){
                        listaEmpresasFiltro.add(r);
                    }

                }


            }
            else{
                 if (r.getNombre().matches(""+parametro+".*")) {
                     listaEmpresasFiltro.add(r);
                 }
            }
        }
       inicializarAdaptadorFiltro();

    }

    public boolean distancia_loc_contenidos(Location miUbicacion, LatLng punto, double radio){
        Location instLoc = new Location("punto");
        boolean verificar_distancia = false;
        double distance;
        LatLng point = punto;
        instLoc.setLatitude(point.latitude);
        instLoc.setLongitude(point.longitude);


        distance = miUbicacion.distanceTo(instLoc);
        Log.v("ver cantidad ", ""+distance);
        if((distance/1000) < radio){
            verificar_distancia =true;
            Log.v("ver cantidad ", ""+distance);
        }
        return verificar_distancia;
    }

    private void inicLocation(){

        locManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    private void registerLocation(){
        locManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000,0,new Fragment_seccion2.MyLocationListener());
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        @Override
        public void onProviderEnabled(String provider) {
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        @Override
        public void onProviderDisabled(String provider) {
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }




    public void addNombre()
    {
        LayoutInflater inflater = LayoutInflater.from(vista.getContext());
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_por_nombre, null, false);

        final EditText ed = (EditText) relativeLayout.findViewById(R.id.editText);

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String item = ed.getText().toString();
                inicializarDatos("Nombre", item);
            }
        });
        layout.addView(relativeLayout);
    }


    public void addDistancia()
    {
        LayoutInflater inflater = LayoutInflater.from(vista.getContext());
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_canton__parroquia, null, false);
        Spinner spCP = (Spinner) relativeLayout.findViewById(R.id.spinnerCantonParroquia);
            //Creamos el adaptador
        ArrayAdapter adapter = ArrayAdapter.createFromResource(vista.getContext(),R.array.spinner_radio,android.R.layout.simple_spinner_item);
            //Añadimos el layout para el menú
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Le indicamos al spinner el adaptador a usar
            spCP.setAdapter(adapter);
            spCP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item=parent.getItemAtPosition(position).toString();
                    inicializarDatos("Distancia", item);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



        layout.addView(relativeLayout);
    }

    //limpiar el espacio se supone
    public void reset()
    {
        layout.removeAllViews();
    }


}
