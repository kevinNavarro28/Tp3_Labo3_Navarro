package com.example.tp3_labo3_navarro.ui.registro;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

        mv.getUsuarioM().observe(this, new Observer
                <Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.EtNombre.setText(usuario.getNombre());
                binding.EtApellido.setText(usuario.getApellido());
                binding.EtDni.setText(String.valueOf(usuario.getDni()));
                binding.EtMail.setText(usuario.getMail());
                binding.Etclave.setText(usuario.getClave());
                usuarioactual=usuario;
                //mv.leerFotoArchivo(usuarioactual.getFoto());
            }
        });



       binding.BtFoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   if (ActivityCompat.checkSelfPermission(RegistroActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                       if (intent.resolveActivity(getPackageManager()) != null) {
                           startActivityForResult(intent, 1);
                       }
                   } else {
                       ActivityCompat.requestPermissions(RegistroActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                   }
               } else {
                   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);if (intent.resolveActivity(getPackageManager()) != null) {startActivityForResult(intent, 1);}
               }
           }
       });







        mv.getFoto().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binding.IMGfoto.setImageBitmap(bitmap);
            }
        });


        mv.leerUsuario();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //mv.respuetaDeCamara(requestCode,resultCode,data,1,usuarioactual);
    }

}