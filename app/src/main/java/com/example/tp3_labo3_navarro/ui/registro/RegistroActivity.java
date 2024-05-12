package com.example.tp3_labo3_navarro.ui.registro;


import static android.Manifest.permission.CAMERA;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.tp3_labo3_navarro.Modelo.Usuario;
import com.example.tp3_labo3_navarro.databinding.ActivityRegistroBinding;
import com.example.tp3_labo3_navarro.ui.login.MainActivityViewModel;

public class RegistroActivity extends AppCompatActivity {


    private static final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int TAKE_PICTURE =100;

    private static final int REQUEST_PERMISION_WRITE_STORAGE = 200;

    private ActivityRegistroBinding binding;

    private RegistroActivityViewModel mv;


    private MainActivityViewModel main;

    private static int REQUEST_IMAGE_CAPTURE=1;

    private  Usuario usuarioactual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mv = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegistroActivityViewModel.class);
        main = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainActivityViewModel.class);




        binding.BtRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mv.registrarUsuario(binding.EtNombre.getText().toString(),binding.EtApellido.getText().toString(),binding.EtDni.getText().toString(),binding.EtMail.getText().toString(),binding.Etclave.getText().toString());
            }
        });

        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        mv.getUsuarioM().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                boolean formularioVacio= getIntent().getBooleanExtra("formulario_vacio",false);
                if(formularioVacio){
                    binding.EtNombre.setText("");
                    binding.EtApellido.setText("");
                    binding.EtDni.setText("");
                    binding.EtMail.setText("");
                    binding.Etclave.setText("");

                }
                else{

                    if(usuario!=null){
                        binding.EtNombre.setText(usuario.getNombre());
                        binding.EtApellido.setText(usuario.getApellido());
                        binding.EtDni.setText(String.valueOf(usuario.getDni()));
                        binding.EtMail.setText(usuario.getMail());
                        binding.Etclave.setText(usuario.getClave());
                    }

                }

            }
        });

        validarPermisos();

        mv.getFoto().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binding.IMGfoto.setImageBitmap(bitmap);
            }
        });
        mv.leerUsuario();

        binding.BtFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturarFoto(v);

            }
        });

    }
    private boolean validarPermisos() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if(checkSelfPermission(Manifest.permission_group.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if(shouldShowRequestPermissionRationale(Manifest.permission_group.CAMERA)) {
            cargarRecomendacion();
        }else{
            requestPermissions(new String[]{Manifest.permission_group.CAMERA},100);
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    public void capturarFoto(View v){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("salida", "tomarFoto:  intent");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            validarPermisos();
        } else {
            Log.d("salida", "tomarFoto");
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        /*if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mv.respuestaDeCamara(requestCode, resultCode, data, REQUEST_IMAGE_CAPTURE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
            }else{
                solicitarPermisos();
            }
        }

    }
    private void solicitarPermisos() {
        final CharSequence[] opciones={"Si","No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RegistroActivity.this);
        alertOpciones.setTitle("¿Configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri= Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }
    private void cargarRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(RegistroActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{Manifest.permission_group.CAMERA},100);
            }
        });
        dialogo.show();
    }
}


