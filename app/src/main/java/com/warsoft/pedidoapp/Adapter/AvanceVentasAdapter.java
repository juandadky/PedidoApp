package com.warsoft.pedidoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warsoft.pedidoapp.Local.Entities.ReporteAvanceVenta;
import com.warsoft.pedidoapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AvanceVentasAdapter extends RecyclerView.Adapter<AvanceVentasAdapter.ViewHolder> {

    private List<ReporteAvanceVenta> reporteAvanceVentas = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_avance_venta,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int posicion) {
        ReporteAvanceVenta reporteActual = reporteAvanceVentas.get(posicion);
        viewHolder.txt_tipo_busqueda.setText(reporteActual.getTitulo());
        if(!TextUtils.isEmpty(reporteActual.getCategoria())){
            viewHolder.txt_categoria.setVisibility(View.VISIBLE);
            viewHolder.txt_categoria.setText("Categoria: " + reporteActual.getCategoria());
        }else{
            viewHolder.txt_categoria.setVisibility(View.GONE);
        }
        viewHolder.txt_fecha_inicio.setText("Fecha Inicio: "+ agregarCeroAFecha(reporteActual.getFechaInicio().get(Calendar.DATE)) + "/" + agregarCeroAFecha((reporteActual.getFechaInicio().get(Calendar.MONTH)+1)) + "/" + reporteActual.getFechaInicio().get(Calendar.YEAR));
        viewHolder.txt_fecha_final.setText("Fecha Final: "+ agregarCeroAFecha(reporteActual.getFechaFinal().get(Calendar.DATE)) + "/" + agregarCeroAFecha((reporteActual.getFechaFinal().get(Calendar.MONTH)+1)) + "/" + reporteActual.getFechaFinal().get(Calendar.YEAR));
        viewHolder.txt_clientes_asignados.setText("Clientes asignados: " + reporteActual.getDetalle().getClientes());
        viewHolder.txt_clientes_atendidos.setText("Clientes atendidos: " + reporteActual.getDetalle().getCliAten());
        viewHolder.txt_ventas_contado.setText(String.format("Ventas totales al contado: S/.%.2f",reporteActual.getDetalle().getContado()));
        viewHolder.txt_ventas_credito.setText(String.format("Ventas totales al credito: S/.%.2f",reporteActual.getDetalle().getCredito()));

    }

    private String agregarCeroAFecha(int numeroFecha){
        return numeroFecha<10? "0"+numeroFecha: ""+numeroFecha;
    }

    @Override
    public int getItemCount() {
        return reporteAvanceVentas!=null?reporteAvanceVentas.size():0;
    }

    public void setReporteAvanceVentas(ReporteAvanceVenta reporteAvanceVentas) {
        this.reporteAvanceVentas.add(reporteAvanceVentas);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_tipo_busqueda,txt_fecha_inicio,txt_fecha_final,txt_categoria,txt_clientes_asignados,
                txt_clientes_atendidos,txt_ventas_contado,txt_ventas_credito;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tipo_busqueda = itemView.findViewById(R.id.txt_tipo_busqueda);
            txt_fecha_inicio = itemView.findViewById(R.id.txt_fecha_inicio);
            txt_fecha_final = itemView.findViewById(R.id.txt_fecha_final);
            txt_categoria = itemView.findViewById(R.id.txt_categoria);
            txt_clientes_asignados = itemView.findViewById(R.id.txt_clientes_asignados);
            txt_clientes_atendidos = itemView.findViewById(R.id.txt_clientes_atendidos);
            txt_ventas_contado = itemView.findViewById(R.id.txt_ventas_contado);
            txt_ventas_credito = itemView.findViewById(R.id.txt_ventas_credito);
        }
    }
}
