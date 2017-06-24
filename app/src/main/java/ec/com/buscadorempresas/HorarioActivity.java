package ec.com.buscadorempresas;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ec.com.buscadorempresas.model.Empresa;
import ec.com.buscadorempresas.model.EmpresaHorario;

public class HorarioActivity extends AppCompatActivity {

    private Empresa empresa;

    static String NAMESPACE = "http://webservices.upse.edu.ec";
    static String URL = "http://192.168.1.63:8080/ServicesEmpresa/services/ServiciosEmpresa?wsdl";
    private String SOAP_ACTION1="http:///192.168.1.63:8080/ServicesEmpresa/services/ServiciosEmpresa/empresa_por_idempresa";
    private String METODO1="empresa_por_idempresa";

    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empresa = new Empresa();
        json = getIntent().getExtras().getString("empresa");
        inicPuntosMarker();


        Tabla tabla = new Tabla(this, (TableLayout)findViewById(R.id.tabla));
        tabla.agregarCabecera(R.array.cabecera_tabla);

        for(EmpresaHorario eh : empresa.getEmpresahorarios())
        {

            ArrayList<String> elementos = new ArrayList<String>();
            elementos.add(eh.getDia());
            elementos.add(eh.getHora_inicio());
            elementos.add(eh.getHora_fin());
            tabla.agregarFilaTabla(elementos);
        }
    }

    public void inicPuntosMarker(){


        SoapObject request = new SoapObject(NAMESPACE, METODO1);
        request.addProperty("request" ,json);

        SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);
        Envoltorio.setOutputSoapObject (request);

        HttpTransportSE TransporteHttp = new HttpTransportSE(URL);

        try {
            TransporteHttp.call(SOAP_ACTION1, Envoltorio);

            SoapObject result = (SoapObject) Envoltorio.bodyIn;

            if(result != null){
                String json = result.getProperty(0).toString();
                Gson gson = new Gson();
                empresa = gson.fromJson(json, Empresa.class);
                Log.d("jasondatos", json);

            }else{
                Toast.makeText(this.getApplicationContext(), "No Response!", Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
