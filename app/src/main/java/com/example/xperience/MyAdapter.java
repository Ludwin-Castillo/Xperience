package com.example.xperience;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;
    private List<DataClass> originalList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.originalList = new ArrayList<>(dataList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImage()).into(holder.recImage);
        holder.recTitulo.setText(dataList.get(position).getTitulo());
        holder.recCategoria.setText(dataList.get(position).getCategoria());
        holder.recSinopsis.setText(dataList.get(position).getSinopsis());
        holder.recHora.setText(dataList.get(position).getHora());
        holder.recDuracion.setText(dataList.get(position).getDuracion());


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetallesActivity.class);
                intent.putExtra("Imagen", dataList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("Titulo", dataList.get(holder.getAdapterPosition()).getTitulo());
                intent.putExtra("Categoria", dataList.get(holder.getAdapterPosition()).getCategoria());
                intent.putExtra("Sinopsis", dataList.get(holder.getAdapterPosition()).getSinopsis());
                intent.putExtra("Hora", dataList.get(holder.getAdapterPosition()).getHora());
                intent.putExtra("Duracion", dataList.get(holder.getAdapterPosition()).getDuracion());
                intent.putExtra("key", dataList.get(holder.getAdapterPosition()).getKey());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList) {
        dataList = new ArrayList<>(searchList);
        notifyDataSetChanged();
    }

    public void restoreOriginalList() {
        dataList = new ArrayList<>(originalList);
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recTitulo, recCategoria, recSinopsis, recHora, recDuracion;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recTitulo = itemView.findViewById(R.id.recTitulo);
        recCategoria = itemView.findViewById(R.id.recCategoria);
        recSinopsis = itemView.findViewById(R.id.recSinopsis);
        recHora = itemView.findViewById(R.id.recHora);
        recDuracion = itemView.findViewById(R.id.recDuracion);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
