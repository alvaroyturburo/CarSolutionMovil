package ec.com.buscadorempresas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import ec.com.buscadorempresas.model.Empresa;

/**
 * Created by Alvaro on 18/01/2017.
 */

public class ListaEmpresaAdapter extends RecyclerView.Adapter<ListaEmpresaAdapter.ListaRecursoViewHolder>{
    public List<Empresa> items;
    ListaRecursoViewHolder holder;

    public ListaEmpresaAdapter(List<Empresa> items) {
        this.items = items;
    }

    public static class ListaRecursoViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView direccion;
        public CardView cardViewEmpresa;

        public ListaRecursoViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombre);
            direccion = (TextView) v.findViewById(R.id.direccion);
            cardViewEmpresa = (CardView) v.findViewById(R.id.cardViewEmpresa);

        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ListaRecursoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_empresa, viewGroup, false);
        holder = new ListaRecursoViewHolder(v);
        return holder;
        //return new ListaRecursoViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ListaRecursoViewHolder viewHolder, final int i) {
        byte[] data = items.get(i).getFoto();
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, items.get(i).getFoto().length);

        viewHolder.imagen.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100,
               111, false));
        //viewHolder.imagen.setImageResource();
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.direccion.setText(items.get(i).getDireccion());
        final Empresa recursoEnviar = items.get(i);
        holder.cardViewEmpresa.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EmpresaDetalleActivity.class);
                Gson gson = new Gson();
                String jsonResultado = gson.toJson(seleccionEmpres(recursoEnviar.getNombre()));
                intent.putExtra("empresa", jsonResultado);
                //intent.putExtra("recurso", recursoEnviar.getNombre());
                view.getContext().startActivity(intent);
            }

        });
    }

    private Empresa seleccionEmpres(String Nombre){
        Empresa empresa =new Empresa();
        for(Empresa r : items) {
            if(r.getNombre().equals(Nombre)){
                empresa = r;
            }
        }
        return empresa;
    }
}