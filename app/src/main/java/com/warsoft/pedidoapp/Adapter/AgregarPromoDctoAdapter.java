package com.warsoft.pedidoapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.warsoft.pedidoapp.Local.Entities.CabeceraDescuento;
import com.warsoft.pedidoapp.R;

import java.util.List;

public class AgregarPromoDctoAdapter extends RecyclerView.Adapter<AgregarPromoDctoAdapter.ViewHolder> {

    private List<CabeceraDescuento> cabeceraDescuentoList;
    private static OnItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_promocion_descuento,viewGroup,false);
        return new ViewHolder(itemView);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        CabeceraDescuento actual = cabeceraDescuentoList.get(position);
        viewHolder.card_view.setCardBackgroundColor(0xFF5fdde5);
        viewHolder.txt_nombre_promo_dcto.setText(actual.getNombreDescuento());
        viewHolder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.eliminarPromoDscto(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cabeceraDescuentoList!=null?cabeceraDescuentoList.size():0;
    }

    public void setPromoDsctoList(List<CabeceraDescuento> cabeceraDescuentoList) {
        this.cabeceraDescuentoList = cabeceraDescuentoList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        TextView txt_nombre_promo_dcto;
        ImageButton btn_eliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            txt_nombre_promo_dcto = itemView.findViewById(R.id.txt_nombre_promo_dcto);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }

    public interface OnItemClickListener {
        void eliminarPromoDscto(int position);
    }
}
