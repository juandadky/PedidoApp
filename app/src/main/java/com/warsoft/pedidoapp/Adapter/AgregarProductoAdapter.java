package com.warsoft.pedidoapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Local.Entities.Producto;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Utils.Util;
import com.warsoft.pedidoapp.View.BuscarProductoActivity;
import com.warsoft.pedidoapp.View.RegistrarPedidoActivity;

import java.util.List;

public class AgregarProductoAdapter extends RecyclerView.Adapter<AgregarProductoAdapter.ViewHolder> {

    private List<Producto> productoList;
    private int tipoPrecio = 0;
    private Context context;
    private static OnItemClickListener listener;

    public AgregarProductoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_registrar_pedido,viewGroup,false);
        return new ViewHolder(itemView);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final Producto producto = productoList.get(position);
        viewHolder.txt_codigo_producto.setText(""+producto.getIdProducto());
        viewHolder.txt_nombre_producto.setText(producto.getNombreProducto());
        if(!producto.isBonificacion()) {
            if (tipoPrecio == 0) {
                viewHolder.txt_precio_unidad.setText(String.format("S/.%.2f c/u", producto.getPreciocon() * (100 - producto.getDescuento()) / (double) 100));
                viewHolder.txt_precio_total.setText(String.format("S/.%.2f", (producto.getTotalunidadesPedido() / (double) producto.getFactor() * producto.getPreciocon()) * (100 - producto.getDescuento()) / (double) 100));
            } else if (tipoPrecio == 1) {
                viewHolder.txt_precio_unidad.setText(String.format("S/.%.2f c/u", producto.getPreciocre() * (100 - producto.getDescuento()) / (double) 100));
                viewHolder.txt_precio_total.setText(String.format("S/.%.2f", (producto.getTotalunidadesPedido() / (double) producto.getFactor() * producto.getPreciocre()) * (100 - producto.getDescuento()) / (double) 100));
            } else if (tipoPrecio == 2) {
                viewHolder.txt_precio_unidad.setText(String.format("S/.%.2f c/u", producto.getPreciomay2() * (100 - producto.getDescuento()) / (double) 100));
                viewHolder.txt_precio_total.setText(String.format("S/.%.2f", (producto.getTotalunidadesPedido() / (double) producto.getFactor() * producto.getPreciomay2()) * (100 - producto.getDescuento()) / (double) 100));
            } else if (tipoPrecio == 3) {
                viewHolder.txt_precio_unidad.setText(String.format("S/.%.2f c/u", producto.getPreciomay() * (100 - producto.getDescuento()) / (double) 100));
                viewHolder.txt_precio_total.setText(String.format("S/.%.2f", (producto.getTotalunidadesPedido() / (double) producto.getFactor() * producto.getPreciomay()) * (100 - producto.getDescuento()) / (double) 100));
            } else if (tipoPrecio == 4) {
                viewHolder.txt_precio_unidad.setText(String.format("S/.%.2f c/u", producto.getPreciovol() * (100 - producto.getDescuento()) / (double) 100));
                viewHolder.txt_precio_total.setText(String.format("S/.%.2f", (producto.getTotalunidadesPedido() / (double) producto.getFactor() * producto.getPreciovol()) * (100 - producto.getDescuento()) / (double) 100));
            }
        }else {
            viewHolder.txt_precio_unidad.setText("S/.00.00");
            viewHolder.txt_precio_total.setText("S/.00.00");
        }
        viewHolder.txt_cantidad_producto.setText(producto.getCantidadPedido() + (producto.getUnidadesPedido()==0 ?" " : "," + producto.getUnidadesPedido() + " ") + producto.getCantidadPorUnidad().toLowerCase());

        if(!producto.isBonificacion()) {
            viewHolder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onViewIntemClick(producto,position);
                }
            });
        }else{
            viewHolder.btn_eliminar.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return productoList!=null?productoList.size():0;
    }

    public void setProductoList(List<Producto> productoList,int tipoPrecio) {
        this.productoList = productoList;
        this.tipoPrecio = tipoPrecio;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_codigo_producto;
        private TextView txt_nombre_producto;
        private TextView txt_precio_total;
        private TextView txt_precio_unidad;
        private TextView txt_cantidad_producto;
        private ImageButton btn_eliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_codigo_producto = itemView.findViewById(R.id.txt_codigo_producto);
            txt_nombre_producto = itemView.findViewById(R.id.txt_nombre_producto);
            txt_precio_total = itemView.findViewById(R.id.txt_precio_total);
            txt_precio_unidad = itemView.findViewById(R.id.txt_precio_unidad);
            txt_cantidad_producto = itemView.findViewById(R.id.txt_cantidad_producto);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onViewIntemClick(Producto producto,int position);
    }
}
