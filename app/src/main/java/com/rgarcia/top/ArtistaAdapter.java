package com.rgarcia.top;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistaAdapter extends RecyclerView.Adapter<ArtistaAdapter.ViewHolder> {

    private List<Artistas> artistas;
    private Context context;
    private OnItemClickListener listener;

    public ArtistaAdapter(List<Artistas> artistas, OnItemClickListener listener) {
        this.artistas = artistas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Artistas artista = artistas.get(i);
        viewHolder.setListener(artista, listener);

        viewHolder.tvNombre.setText(artista.getNombreCompleto());
        viewHolder.tvOrden.setText(String.valueOf(artista.getOrden()));

        if(artista.getFotoUrl() != null){
            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            options.centerCrop();
            options.placeholder(R.drawable.ic_sentiment_satisfied);
            Glide.with(context).load(artista.getFotoUrl()).apply(options).into(viewHolder.imgFoto);
        }else{
            viewHolder.imgFoto.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_black));
        }
    }

    public void add(Artistas artista){
        if(!artistas.contains(artista)){
            artistas.add(artista);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return this.artistas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgFoto)
        AppCompatImageView imgFoto;
        @BindView(R.id.tvNombre)
        AppCompatTextView tvNombre;
        @BindView(R.id.tvOrden)
        AppCompatTextView tvOrden;
        @BindView(R.id.containerMain)
        RelativeLayout containerMain;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setListener(final Artistas artista, final OnItemClickListener listener){
            containerMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(artista);
                }
            });

            containerMain.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongItemClick(artista);
                    return true;
                }
            });
        }
    }
}
