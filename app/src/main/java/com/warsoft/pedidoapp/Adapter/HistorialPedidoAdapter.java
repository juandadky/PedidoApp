package com.warsoft.pedidoapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.warsoft.pedidoapp.Local.Entities.CabeceraPedido;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Utils.Util;
import com.warsoft.pedidoapp.View.DetalleHistorialPedidoActivity;

import java.util.List;

public class HistorialPedidoAdapter extends RecyclerView.Adapter<HistorialPedidoAdapter.ViewHolder> {

    private List<CabeceraPedido> pedidos;
    private Context context;

    public HistorialPedidoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HistorialPedidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_historial_pedido_card,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialPedidoAdapter.ViewHolder viewHolder, int position) {
        final CabeceraPedido cabeceraPedido = pedidos.get(position);
        viewHolder.txt_id_pedido.setText("ID: "+cabeceraPedido.getIdPedido());
        viewHolder.txt_fecha_pedido.setText("Fecha: " + Util.setearFecha(cabeceraPedido.getFechaPedido()));
        viewHolder.txt_cliente_pedido.setText("Nombre: " + cabeceraPedido.getNombreCliente());
        viewHolder.txt_tipoPago.setText("Tipo de Pago: " + (cabeceraPedido.getTipoVenta() == "1" ? "Contado" : "Crédito"));
        viewHolder.btn_ver_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetalleHistorialPedidoActivity.class);
                intent.putExtra(Util.CABECERA_PEDIDO,cabeceraPedido);
                context.startActivity(intent);
            }
        });
    }

    public void setPedidos(List<CabeceraPedido> pedidos) {
        this.pedidos = pedidos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return pedidos!=null?pedidos.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_id_pedido;
        private TextView txt_fecha_pedido;
        private TextView txt_cliente_pedido;
        private TextView txt_tipoPago;
        private Button btn_ver_mas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_id_pedido = itemView.findViewById(R.id.txt_id_pedido);
            txt_fecha_pedido = itemView.findViewById(R.id.txt_fecha_pedido);
            txt_cliente_pedido = itemView.findViewById(R.id.txt_cliente_pedido);
            txt_tipoPago = itemView.findViewById(R.id.txt_tipoPago);
            btn_ver_mas = itemView.findViewById(R.id.btn_ver_mas);
        }
    }
}
