package com.warsoft.pedidoapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Local.Entities.Producto;
import com.warsoft.pedidoapp.R;

import java.util.List;

public class BuscarProductoAdapter extends RecyclerView.Adapter<BuscarProductoAdapter.ViewHolder> {
    private List<Producto> listaProductos;
    private Context context;
    private OnItemClickListener listener;

    public BuscarProductoAdapter(Context context,List<Producto> listaProductos,OnItemClickListener listener) {
        this.listaProductos = listaProductos;
        this.context = context;
        this.listener = listener;
    }

    public BuscarProductoAdapter(Context context,List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
        this.context = context;
    }

    @NonNull
    @Override
    public BuscarProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_buscar_producto,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuscarProductoAdapter.ViewHolder viewHolder, final int posicion) {
        try {
            final Producto producto = listaProductos.get(posicion);
            viewHolder.txt_stock_producto.setText("Stock \n" + (producto.getSaldoreal()/producto.getFactor())+ "." + producto.getSaldoreal()%producto.getFactor());
            viewHolder.txtNombreProducto.setText(producto.getNombreProducto());
            viewHolder.txtPrecioProducto.setText(String.format("Precio \nS/.%.2f", producto.getPreciocon()));
            viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(producto,posicion);
                }
            });

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardview;
        private TextView txt_stock_producto;
        private TextView txtNombreProducto;
        private TextView txtPrecioProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_stock_producto = itemView.findViewById(R.id.txt_stock_producto);
            txtNombreProducto = itemView.findViewById(R.id.txt_nombre_producto);
            txtPrecioProducto = itemView.findViewById(R.id.txt_precio_producto);
            cardview = itemView.findViewById(R.id.cardview);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Producto producto, int position);
    }
}
