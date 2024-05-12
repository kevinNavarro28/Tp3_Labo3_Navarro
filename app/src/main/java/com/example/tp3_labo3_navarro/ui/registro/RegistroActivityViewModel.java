package com.example.tp3_labo3_navarro.ui.registro;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp3_labo3_navarro.Modelo.Usuario;
import com.example.tp3_labo3_navarro.request.ApiClient;
import com.example.tp3_labo3_navarro.ui.login.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegistroActivityViewModel extends AndroidViewModel {

    private Context context;
    private ApiClient apiClient;

    private MutableLiveData<Usuario> usuarioM;
    private MutableLiveData<Bitmap> fotoM;
    public RegistroActivityViewModel(@NonNull Application application) {
        super(application);

        context = application.getApplicationContext();
        apiClient = new ApiClient();

    }
    public LiveData<Bitmap> getFoto(){
        if(fotoM==null){
            fotoM=new MutableLiveData<>();
        }
        return fotoM;
    }

    public LiveData<Usuario> getUsuarioM(){
        if(usuarioM==null){
            usuarioM = new MutableLiveData<>();
        }
        return usuarioM;
    }

    public void registrarUsuario(String nombre, String apellido, String dni,String mail, String clave) {
        Long dniLong = Long.parseLong(dni);
        Usuario usuario = new Usuario(nombre, apellido, dniLong, mail, clave);

        apiClient.guardarUsuario(usuario,context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void leerUsuario() {
        if (apiClient.leerUsuario(context) == null) {
            Toast.makeText(context, "esta vacio", Toast.LENGTH_LONG).show();
        } else {
            usuarioM.setValue(apiClient.leerUsuario(context));
            obtenerFoto();
        }

    }

        public void obtenerFoto() {
            Bitmap bitmap = ApiClient.leerFoto(context);
            if (bitmap != null) fotoM.setValue(bitmap);
        }





    /*public void respuetaDeCamara(int requestCode, int resultCode, @Nullable Intent data, int REQUEST_IMAGE_CAPTURE,Usuario usuarioActual){
        Log.d("salida",requestCode+"");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Recupero los datos provenientes de la camara.
            Bundle extras = data.getExtras();
            //Casteo a bitmap lo obtenido de la camara.
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //Rutina para optimizar la foto,
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            fotoM.setValue(imageBitmap);



            //Rutina para convertir a un arreglo de byte los datos de la imagen
            byte [] b=baos.toByteArray();


            //Aquí podría ir la rutina para llamar al servicio que recibe los bytes.
            File archivo =new File(context.getFilesDir(),usuarioActual.getDni()+".png");
            usuarioActual.setFoto(usuarioActual.getDni()+".png");
            if(archivo.exists()){
                archivo.delete();
            }
            try {
                FileOutputStream fo=new FileOutputStream(archivo);
                BufferedOutputStream bo=new BufferedOutputStream(fo);
                ObjectOutputStream ob = new ObjectOutputStream(bo);
                ob.writeObject(imageBitmap);
                bo.flush();
                bo.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public void respuestaDeCamara(int requestCode, int resultCode, @Nullable Intent data, int REQUEST_IMAGE_CAPTURE) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fotoM.setValue(ApiClient.guardarFoto(context, imageBitmap));
        }
    }


}

