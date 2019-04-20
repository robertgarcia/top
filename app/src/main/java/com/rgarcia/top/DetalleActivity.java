package com.rgarcia.top;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetalleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.imgFoto)
    AppCompatImageView imgFoto;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.etNombre)
    TextInputEditText etNombre;
    @BindView(R.id.etApellido)
    TextInputEditText etApellido;
    @BindView(R.id.etFechaNacimiento)
    TextInputEditText etFechaNacimiento;
    @BindView(R.id.etEdad)
    TextInputEditText etEdad;
    @BindView(R.id.etEstatura)
    TextInputEditText etEstatura;
    @BindView(R.id.etOrden)
    TextInputEditText etOrden;
    @BindView(R.id.etLugarNacimiento)
    TextInputEditText etLugarNacimiento;
    @BindView(R.id.etNotas)
    TextInputEditText etNotas;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.containerMain)
    NestedScrollView containerMain;

    private Artistas mArtista;
    private boolean mIsEdit;
    private Calendar mCalendar;
    private MenuItem mMenuItem;
    private static final int RC_PHOTO_PICKER = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        ButterKnife.bind(this);

        configArtista(getIntent());
        configActionBar();
        configImageView(mArtista.getFotoUrl());
        configCalendar();
    }

    private void configActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        configTitle();
    }

    private void configTitle() {
        toolbarLayout.setTitle(mArtista.getNombreCompleto());
    }

    private void configArtista(Intent intent) {
        getArtist(intent.getLongExtra(Artistas.ID, 0));
        etNombre.setText(mArtista.getNombre());
        etApellido.setText(mArtista.getApellidos());
        etFechaNacimiento.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(mArtista.getFechaNacimiento()));
        etEdad.setText(getEdad(mArtista.getFechaNacimiento()));
        etEstatura.setText(String.valueOf(mArtista.getEstatura()));
        etOrden.setText(String.valueOf(mArtista.getOrden()));
        etLugarNacimiento.setText(mArtista.getLugarNacimientos());
        etNotas.setText(mArtista.getNotas());
    }

    private void getArtist(long id) {
        mArtista = SQLite.select().from(Artistas.class).where(Artistas_Table.id.is(id)).querySingle();
    }

    private String getEdad(long fechaNacimiento) {
        Long time = Calendar.getInstance().getTimeInMillis() / 1000 - fechaNacimiento / 1000;
        final int years = Math.round(time) / 31536000;
        return String.valueOf(years);
    }

    private void configImageView(String fotoUrl) {
        if (fotoUrl != null) {
            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop();
            Glide.with(this).load(fotoUrl).apply(options).into(imgFoto);
        } else {
            imgFoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_photo));
        }

        mArtista.setFotoUrl(fotoUrl);
    }

    private void configCalendar() {
        mCalendar = Calendar.getInstance(Locale.ROOT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        mMenuItem = menu.findItem(R.id.action_save);
        mMenuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveOrEdit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RC_PHOTO_PICKER :
                    savePhotoUrlArtist(data.getDataString());
                    break;
            }
        }
    }

    private void savePhotoUrlArtist(String fotoUrl) {
        try {
            mArtista.setFotoUrl(fotoUrl);
            mArtista.update();
            configImageView(fotoUrl);
            showMessage(R.string.detalle_message_update_success);
        } catch (Exception e) {
            showMessage(R.string.detalle_message_update_fail);
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fab)
    public void saveOrEdit() {
        if (mIsEdit) {
            if (validateFields()) {
                mArtista.setNombre(etNombre.getText().toString().trim());
                mArtista.setApellidos(etApellido.getText().toString().trim());
                mArtista.setEstatura(Short.valueOf(etEstatura.getText().toString().trim()));
                mArtista.setLugarNacimientos(etLugarNacimiento.getText().toString().trim());
                mArtista.setNotas(etNotas.getText().toString().trim());
                try {
                    mArtista.update();
                    configTitle();
                    showMessage(R.string.detalle_message_update_success);
                    Log.i("DBFlow", "Insercion correcta de datos");
                } catch (Exception e) {
                    Log.i("DBFlow", "Error insertar datos");
                    showMessage(R.string.detalle_message_update_fail);
                    e.printStackTrace();
                }
                //setResult(RESULT_OK);
                //finish();
            }
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_edit));
            enableUIElements(false);
            mIsEdit = false;
        } else {
            mIsEdit = true;
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_account_check));
            enableUIElements(mIsEdit);
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (etEstatura.getText().toString().trim().isEmpty() || Integer.valueOf(etEstatura.getText().toString().trim()) < getResources().getInteger(R.integer.estatura_min)) {
            etEstatura.setError(getString(R.string.addArtist_error_estaturaMin));
            etEstatura.requestFocus();
            isValid = false;
        }


        if (etApellido.getText().toString().trim().isEmpty()) {
            etApellido.setError(getString(R.string.addArtist_error_required));
            etApellido.requestFocus();
            isValid = false;
        }

        if (etNombre.getText().toString().trim().isEmpty()) {
            etNombre.setError(getString(R.string.addArtist_error_required));
            etNombre.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    private void enableUIElements(boolean enable) {
        etNombre.setEnabled(enable);
        etApellido.setEnabled(enable);
        etFechaNacimiento.setEnabled(enable);
        etEstatura.setEnabled(enable);
        etLugarNacimiento.setEnabled(enable);
        etNotas.setEnabled(enable);
        mMenuItem.setVisible(enable);
        appBar.setExpanded(!enable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            containerMain.setNestedScrollingEnabled(!enable);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);

        etFechaNacimiento.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(mCalendar.getTimeInMillis()));
        mArtista.setFechaNacimiento(mCalendar.getTimeInMillis());
        etEdad.setText(getEdad(mCalendar.getTimeInMillis()));
    }

    @OnClick(R.id.etFechaNacimiento)
    public void onSetFecha() {
        DialogSelectorFecha selectorFecha = new DialogSelectorFecha();
        selectorFecha.setListener(DetalleActivity.this);
        Bundle args = new Bundle();
        args.putLong(DialogSelectorFecha.FECHA, mArtista.getFechaNacimiento());
        selectorFecha.setArguments(args);
        selectorFecha.show(getSupportFragmentManager(), DialogSelectorFecha.SELECTED_DATE);
    }

    private void showMessage(int resource) {
        Snackbar.make(containerMain, resource, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick({R.id.imgDeleteFoto, R.id.imgFromGallery, R.id.imgFromUrl})
    public void photoHandler(View view) {
        switch (view.getId()) {
            case R.id.imgDeleteFoto:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.detalle_dialogDelete_title)
                        .setMessage(String.format(Locale.ROOT, getString(R.string.detalle_dialogDelete_message), mArtista.getNombreCompleto()))
                        .setPositiveButton(R.string.detalle_dialogDelete_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                savePhotoUrlArtist(null);
                            }
                        })
                        .setNegativeButton(R.string.detalle_dialogDelete_cancel, null);
                builder.show();
                break;
            case R.id.imgFromGallery:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                //intent.setType("image/png");
                //intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.detalle_chooser_title)), RC_PHOTO_PICKER);
                break;
            case R.id.imgFromUrl:
                showAddPhotoDialog();
                break;
        }
    }

    private void showAddPhotoDialog() {
        final EditText etFotoUrl = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.addArtist_dialogUrl_title)
                .setPositiveButton(R.string.label_dialog_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        savePhotoUrlArtist(etFotoUrl.getText().toString().trim());
                    }

                }).setNegativeButton(R.string.label_dialog_cancel, null);

        builder.setView(etFotoUrl);
        builder.show();
    }

}
