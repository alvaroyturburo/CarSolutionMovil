package ec.com.buscadorempresas;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private LocationManager locManager;
    AlertDialog alert = null;
    AlertDialog alert2 = null;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    //Log
    private final String TAG = getClass().getSimpleName();


    private Adaptador_ViewPagerPrincipal Adaptador_ViewPagerPrincipal;
    //private ViewPager ViewPager;

    @Bind(R.id.ToolbarPrincipal)
    Toolbar toolbar;
    @Bind(R.id.AppbarPrincipal)
    AppBarLayout appbar;
    @Bind(R.id.TabLayoutPrincipal)
    TabLayout tabLayout;
    @Bind(R.id.ViewPagerPrincipal)
    android.support.v4.view.ViewPager ViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(checkAndRequestPermissions()) {

        }
        // Iniciamos la barra de herramientas.
        //Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarPrincipal);
        setSupportActionBar(toolbar);

        //final AppBarLayout appbar = (AppBarLayout) findViewById(R.id.AppbarPrincipal);

        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");


        // Iniciamos la barra de tabs
       // final TabLayout tabLayout = (TabLayout) findViewById(R.id.TabLayoutPrincipal);

        // Añadimos las 3 tabs de las secciones.
        // Le damos modo "fixed" para que todas las tabs tengan el mismo tamaño. Tambien le asignamos una gravedad centrada.

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());


        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertNoGps();
        }

        /*Bundle b = getIntent().getExtras();
        String _UsuarioMostrar = b.getString("_usuario");*/

        // Iniciamos el viewPager.
        //ViewPager = (ViewPager) findViewById(R.id.ViewPagerPrincipal);
        // Creamos el adaptador, al cual le pasamos por parámetro el gestor de Fragmentos y muy importante, el nº de tabs o secciones que hemos creado.
        System.out.println(""+tabLayout.getTabCount());
        Adaptador_ViewPagerPrincipal = new Adaptador_ViewPagerPrincipal(getSupportFragmentManager(), tabLayout.getTabCount(), this);
        // Y los vinculamos.
        ViewPager.setAdapter(Adaptador_ViewPagerPrincipal);

        // Y por último, vinculamos el viewpager con el control de tabs para sincronizar ambos.
        tabLayout.setupWithViewPager(ViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.maps);
        tabLayout.getTabAt(1).setIcon(R.drawable.worker);
        tabLayout.getTabAt(2).setIcon(R.drawable.gasstation);
        tabLayout.getTabAt(3).setIcon(R.drawable.store);

    }

    private  boolean checkAndRequestPermissions() {
        int permissionCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionCall != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void AlertNoGps(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta apagado, para el uso correcto de la aplicacion debe estar encendido. DESEA HABILITARLO?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy()
    {
        if(alert != null)
        {
            alert.dismiss ();
        }

        if(alert2 != null)
        {
            alert2.dismiss ();
        }
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            //aquí vendría las acciones que tengo que realizar
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Desea salir de SearchCompany?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alert2 = builder.create();
            alert2.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        Log.w(TAG, "App stopped");

        super.onStop();
    }




}
