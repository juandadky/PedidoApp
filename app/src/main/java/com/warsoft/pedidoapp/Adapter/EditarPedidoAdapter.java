package com.warsoft.pedidoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.warsoft.pedidoapp.Local.Entities.DetallePedido;
import com.warsoft.pedidoapp.R;

import java.util.List;

public class EditarPedidoAdapter extends RecyclerView.Adapter<EditarPedidoAdapter.ViewHolder> {

    private List<DetallePedido> productoList;
    private OnItemClickListener listener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_registrar_pedido,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final DetallePedido detallePedido = productoList.get(position);
        viewHolder.txt_codigo_producto.setText(""+detallePedido.getIdProducto());
        viewHolder.txt_nombre_producto.setText(detallePedido.getNombreProducto());
        viewHolder.txt_precio_total.setText(String.format("S/.%.2f", detallePedido.getTotalPedido()==0?detallePedido.getImporte():detallePedido.getTotalPedido()));
        viewHolder.txt_precio_unidad.setText(String.format("S/.%.2f", detallePedido.getPrecioUnidad()));
        if(detallePedido.getTotalPedido() == 0) {
            int parteEntera = (int)detallePedido.getUnidades() / detallePedido.getFactor();
            int parteDecimal = (int)detallePedido.getUnidades() % detallePedido.getFactor();
            viewHolder.txt_cantidad_producto.setText("" + (parteDecimal != 0 ? parteEntera + "," + parteDecimal : parteEntera) +" " + detallePedido.getCantidadPorUnidad().toLowerCase());
        }else{
            viewHolder.txt_cantidad_producto.setText("" + (detallePedido.getUnidadesPedido() == 0 ? detallePedido.getCantidadPedido() + " " + detallePedido.getCantidadPorUnidad().toLowerCase() : detallePedido.getCantidadPedido() + "," + detallePedido.getUnidadesPedido()+ " " + detallePedido.getCantidadPorUnidad().toLowerCase()));
        }
        viewHolder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteItem(position);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChangeDataItem(detallePedido,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productoList!=null?productoList.size():0;
    }

    public void setProductoList(List<DetallePedido> productoList) {
        this.productoList = productoList;
        notifyDataSetChanged();
    }

    public  void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
        void onDeleteItem(int position);
        void onChangeDataItem(DetallePedido producto,int position);
    }
}
