package com.warsoft.pedidoapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.warsoft.pedidoapp.Local.Entities.DetallePedido;
import com.warsoft.pedidoapp.R;

import java.util.List;

public class HistorialDetallePedidoAdapter extends RecyclerView.Adapter<HistorialDetallePedidoAdapter.ViewHolder> {

    private List<DetallePedido> detallePedidos;
    private Context context;

    public HistorialDetallePedidoAdapter(Context context,List<DetallePedido> detallePedidos) {
        this.context = context;
        this.detallePedidos = detallePedidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_detalle_pedido,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int posicion) {
        DetallePedido detalle = detallePedidos.get(posicion);
        viewHolder.txt_codigo_producto.setText(""+detalle.getIdProducto());
        viewHolder.txt_nombre_producto.setText(detalle.getNombreProducto());
        viewHolder.txt_precio_producto.setText(String.format("S/.%.2f", detalle.getImporte()-detalle.getDescuento()));
        int parteEntera = (int)detalle.getUnidades()/detalle.getFactor();
        int parteDecimal = (int)detalle.getUnidades()%detalle.getFactor();
        viewHolder.txt_cantidad_producto.setText("" + (parteDecimal!=0 ? parteEntera+","+parteDecimal : parteEntera ) + detalle.getCantidadPorUnidad().toLowerCase());
    }

    @Override
    public int getItemCount() {
        return detallePedidos!=null?detallePedidos.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_codigo_producto;
        private TextView txt_nombre_producto;
        private TextView txt_precio_producto;
        private TextView txt_cantidad_producto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_codigo_producto = itemView.findViewById(R.id.txt_codigo_producto);
            txt_nombre_producto = itemView.findViewById(R.id.txt_nombre_producto);
            txt_precio_producto = itemView.findViewById(R.id.txt_precio_producto);
            txt_cantidad_producto = itemView.findViewById(R.id.txt_cantidad_producto);
        }
    }
}
