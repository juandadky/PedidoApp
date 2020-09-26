package com.warsoft.pedidoapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.warsoft.pedidoapp.Local.Entities.DeudaPendiente;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Utils.Util;

import java.util.List;

public class DeudaPendienteAdapter extends RecyclerView.Adapter<DeudaPendienteAdapter.ViewHolder> {

    private List<DeudaPendiente> deudasPendientes;
    private Context context;
    private OnItemClickListener listener;

    public DeudaPendienteAdapter(Context context, List<DeudaPendiente> deudasPendientes, OnItemClickListener listener) {
        this.deudasPendientes = deudasPendientes;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_deudas_pendientes,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int posicion) {
        final DeudaPendiente deuda = deudasPendientes.get(posicion);
        viewHolder.txt_id_orden.setText("" + deuda.getIdDocOrden().substring(0,4) +"-"+deuda.getIdDocOrden().substring(4));
        viewHolder.txt_fecha_emision.setText("Emisión: " + Util.setearFecha(deuda.getFecha()));
        viewHolder.txt_fecha_vence.setText("Vence: " + Util.setearFecha(deuda.getDiasCredito()));
        viewHolder.txt_importe.setText(String.format("Importe: S/.%.2f",deuda.getTotal()));
        viewHolder.txt_saldo_pagar.setText(String.format("Saldo: S/.%.2f",deuda.getSaldoSoles()));
        viewHolder.btn_proceder_pago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(deuda,posicion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deudasPendientes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_id_orden,txt_fecha_emision,txt_importe,txt_fecha_vence,txt_saldo_pagar;
        private Button btn_proceder_pago;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_id_orden = itemView.findViewById(R.id.txt_id_orden);
            txt_fecha_emision = itemView.findViewById(R.id.txt_fecha_emision);
            txt_importe = itemView.findViewById(R.id.txt_importe);
            txt_fecha_vence = itemView.findViewById(R.id.txt_fecha_vence);
            txt_saldo_pagar = itemView.findViewById(R.id.txt_saldo_pagar);
            btn_proceder_pago = itemView.findViewById(R.id.btn_proceder_pago);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DeudaPendiente deudaPendiente,int position);
    }
}
