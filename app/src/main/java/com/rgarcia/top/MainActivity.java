package com.rgarcia.top;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    @BindView(R.id.containerMain)
    CoordinatorLayout containerMain;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ArtistaAdapter adapter;
    public static final Artistas sArtista = new Artistas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        configToolbar();
        configAdpater();
        configRecyclerView();
        generarArtist();

    }

    private void configToolbar() {
        setSupportActionBar(toolbar);
    }

    private void configAdpater() {
        adapter = new ArtistaAdapter(new ArrayList<Artistas>(), this);
    }

    private void configRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void generarArtist() {
        String[] nombre = {"Gal", "Rachel", "Megan"};
        String[] apellidos = {"Gadot", "McAdams", "Fox"};
        long[] nacimientos = {483667200000L, 501033600000L, 485049600000L};
        String[] lugares = {"Rosh Ha'ayin, Israel", "London, Ontario, Canada", "Rockwood, Tennessee, USA"};
        short[] estaturas = {178, 163, 163};
        String[] notas = {
                "Gal Gadot is an Israeli actress, singer, martial artist, and model. She was born in Rosh Ha'ayin, Israel, to a Jewish family. Her parents are Irit, a teacher, and Michael, an engineer, who is a sixth-generation Israeli. She served in the IDF for two years, and won the Miss Israel title in 2004.",
                "Rachel Anne McAdams was born on November 17, 1978 in London, Ontario, Canada, to Sandra Kay (Gale), a nurse, and Lance Frederick McAdams, a truck driver and furniture mover. She is of English, Welsh, Irish, and Scottish descent.",
                "Megan Denise Fox was born in Rockwood, Tennessee, to Gloria Darlene (Cisson) and Franklin Thomas Fox, a parole officer. Megan began her training in drama and dance at age 5 and, at age 10, moved to St. Petersburg, Florida where she continued her training and finished school. Megan began acting and modeling at age 13 after winning several awards at the 1999 American Modeling and Talent Convention in Hilton Head, South Carolina. At age 17, she tested out of school using correspondence and eventually moved to Los Angeles, California. Megan made her film debut as Brianna Wallace in the Mary-Kate Olsen and Ashley Olsen film Holiday in the Sun (2001). Her best known roles are as Sam Witwicky's love interest Mikaela Banes in the first two installments of the Transformers series, Transformers (2007) and Transformers: Revenge of the Fallen (2009), and as April O'Neil in the film reboot of Teenage Mutant Ninja Turtles (2014)."
        };
        String[] fotos = {
                "https://m.media-amazon.com/images/M/MV5BMjUzZTJmZDItODRjYS00ZGRhLTg2NWQtOGE0YjJhNWVlMjNjXkEyXkFqcGdeQXVyMTg4NDI0NDM@._V1_UY317_CR51,0,214,317_AL_.jpg",
                "https://m.media-amazon.com/images/M/MV5BMTY5ODcxMDU4NV5BMl5BanBnXkFtZTcwMjAzNjQyNQ@@._V1_UY317_CR2,0,214,317_AL_.jpg",
                "https://m.media-amazon.com/images/M/MV5BMTc5MjgyMzk4NF5BMl5BanBnXkFtZTcwODk2OTM4Mg@@._V1_UY317_CR4,0,214,317_AL_.jpg"
        };

        for (int i = 0; i < 3; i++) {
            Artistas artista = new Artistas(i + 1, nombre[i], apellidos[i], nacimientos[i], lugares[i], estaturas[i], notas[i], i + 1, fotos[i]);
            adapter.add(artista);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*******
     * Metodos implementados por la interface OnItemClickListener
     ******/
    @Override
    public void onItemClick(Artistas artistas) {
        sArtista.setId(artistas.getId());
        sArtista.setNombre(artistas.getNombre());
        sArtista.setApellidos(artistas.getApellidos());
        sArtista.setFechaNacimiento(artistas.getFechaNacimiento());
        sArtista.setEstatura(artistas.getEstatura());
        sArtista.setLugarNacimientos(artistas.getLugarNacimientos());
        sArtista.setNotas(artistas.getNotas());
        sArtista.setOrden(artistas.getOrden());
        sArtista.setFotoUrl(artistas.getFotoUrl());

        Intent intent = new Intent(MainActivity.this, DetalleActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(Artistas artistas) {

    }

    @OnClick(R.id.fab)
    public void addArtist() {
        Intent intent = new Intent(MainActivity.this, AddArtistActivity.class);
        intent.putExtra(Artistas.ORDEN, adapter.getItemCount()+1);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1){
            adapter.add(sArtista);
        }
    }
}
