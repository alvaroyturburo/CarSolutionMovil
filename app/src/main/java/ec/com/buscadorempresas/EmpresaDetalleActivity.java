package ec.com.buscadorempresas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import ec.com.buscadorempresas.model.Empresa;

public class EmpresaDetalleActivity extends AppCompatActivity {

    private TextView direccion,descripcion, telefono;
    private ImageView imagen;
    private Button rutaBtnn, senderoBtnn, galeriaBtnn;
    private ImageButton rutaBtn, horarioBtn, contactoBtn, servicioBtn;
    Location loc;

    private LocationManager locManager;

    String json;
    Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        json = getIntent().getExtras().getString("empresa");
        Gson gson = new Gson();
        empresa = gson.fromJson(json, Empresa.class);
        toolbar.setTitle(empresa.getNombre());

        imagen = (ImageView) findViewById(R.id.imagen);
        direccion = (TextView) findViewById(R.id.txt_direccionE);
        descripcion = (TextView) findViewById(R.id.txt_descripcionE);
        telefono = (TextView) findViewById(R.id.txt_telefonoE);

        rutaBtn = (ImageButton) findViewById(R.id.btn_rutaE);
        horarioBtn = (ImageButton) findViewById(R.id.btn_horarioE);
        contactoBtn = (ImageButton) findViewById(R.id.btn_contactoE);
        servicioBtn = (ImageButton) findViewById(R.id.btn_servicioE);

        byte[] data = empresa.getFoto();
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, empresa.getFoto().length);
        imagen.setImageBitmap(Bitmap.createScaledBitmap(bmp, 400, 400, false));
        direccion.setText("Direccion: "+empresa.getDireccion());
        descripcion.setText("Descripcion: "+empresa.getDescripcion_empresa());
        telefono.setText("Telefono: "+empresa.getTelefono());

        rutaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicLocation();
                registerLocation();
                String origen = loc.getLatitude()+","+ loc.getLongitude();
                String destino = empresa.getLatitud()+","+empresa.getLongitud();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+origen+"&daddr="+destino));
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

        contactoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                //intent.setPackage("com.android.phone");
                intent.setData(Uri.parse("tel:"+empresa.getTelefono()));
                startActivity(intent);
            }
        });
        horarioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HorarioActivity.class);
                intent.putExtra("empresa", ""+empresa.getId_empresa());
                startActivity(intent);
            }
        });
        servicioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServicioActivity.class);
                intent.putExtra("empresa", ""+empresa.getId_empresa());
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void inicLocation(){

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    private void registerLocation(){
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, new MyLocationListener());
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
}
