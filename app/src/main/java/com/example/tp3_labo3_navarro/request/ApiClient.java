package com.example.tp3_labo3_navarro.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.tp3_labo3_navarro.Modelo.Usuario;

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

public class ApiClient {

    private static File foto;

    private static SharedPreferences sp;

    private static SharedPreferences conectar(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("datos", 0);
        }
        return sp;
    }

    private static File Foto (File dir) {
        if (foto == null) {
            foto = new File(dir, "foto_perfil.jpg");
        }
        return foto;
    }

    public static void registrar(Context context, Usuario usuario) {
        SharedPreferences sp = conectar(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("dni", usuario.getDni());
        editor.putString("apellido", usuario.getApellido());
        editor.putString("nombre", usuario.getNombre());
        editor.putString("mail", usuario.getMail());
        editor.putString("clave", usuario.getClave());
        editor.commit();
    }

    public static Usuario leer(Context context) {
        SharedPreferences sp = conectar(context);
        Long dni = sp.getLong("dni", -1);
        String apellido = sp.getString("apellido", "-1");
        String nombre = sp.getString("nombre", "-1");
        String mail = sp.getString("mail", "-1");
        String clave = sp.getString("clave", "-1");

        Usuario usuario = new Usuario(nombre, apellido, dni, mail, clave);

        return usuario;
    }

    public static Usuario login(Context context, String mail, String password) {
        Usuario usuarioLogin = null;
        SharedPreferences sp = conectar(context);
        Long dniLogin = sp.getLong("dni", -1);
        String apellidoLogin = sp.getString("apellido", "-1");
        String nombreLogin = sp.getString("nombre", "-1");
        String mailLogin = sp.getString("mail", "-1");
        String claveLogin = sp.getString("clave", "-1");

        if (mail.equals(mail) && password.equals(claveLogin)) {
            usuarioLogin = new Usuario(nombreLogin, apellidoLogin, dniLogin, mailLogin, claveLogin);
        }
        return usuarioLogin;
    }

    public void guardarUsuario(Usuario usuario, Context context) {
        File carpeta = context.getFilesDir();
        File archivo = new File(carpeta, "usuario.dat");

        try {
            FileOutputStream fs = new FileOutputStream(archivo);
            BufferedOutputStream bos = new BufferedOutputStream(fs);
            ObjectOutputStream ous = new ObjectOutputStream(bos);
            ous.writeObject(usuario);
            bos.flush();
            fs.close();


        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error al guardar", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error al E/S", Toast.LENGTH_LONG).show();
        }

    }

    public Usuario leerUsuario(Context context) {
        File carpeta = context.getFilesDir();
        File archivo = new File(carpeta, "usuario.dat");
        Usuario usuario = null;
        if (!archivo.exists()) {
            // Manejar el caso en el que el archivo no existe
            Toast.makeText(context, "El archivo de usuario no existe", Toast.LENGTH_LONG).show();
            return null;
        }

        try {
            FileInputStream fs = new FileInputStream(archivo);
            BufferedInputStream bus = new BufferedInputStream(fs);
            ObjectInputStream os = new ObjectInputStream(bus);
            usuario = (Usuario) os.readObject();

            fs.close();


        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Error al leer", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error al E/S", Toast.LENGTH_LONG).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return usuario;
    }

    public Usuario loginUsuario(Context context, String mail, String clave) {
        File carpeta = context.getFilesDir();
        File archivo = new File(carpeta, "usuario.dat");
        Usuario usuarioLogin = null;

        FileInputStream fs = null;

            try {
                fs = new FileInputStream(archivo);
                BufferedInputStream bus = new BufferedInputStream(fs);
                ObjectInputStream os = new ObjectInputStream(bus);
                Usuario usuarioGuardado = (Usuario) os.readObject();

                if (mail.equals(usuarioGuardado.getMail()) && clave.equals(usuarioGuardado.getClave())) {
                    usuarioLogin = usuarioGuardado;}


            } catch (FileNotFoundException e) {
                Toast.makeText(context, "Error al leer", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(context, "Error E/S", Toast.LENGTH_LONG).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
                return  usuarioLogin;
        }


    public static Bitmap guardarFoto(Context context, Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte [] b = baos.toByteArray();
        File archivo = Foto(context.getFilesDir());
        try{
            FileOutputStream fos = new FileOutputStream(archivo);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(b);
            bos.flush();
            bos.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap leerFoto(Context context){
        File archivo = Foto(context.getFilesDir());
        return BitmapFactory.decodeFile(archivo.getAbsolutePath());
    }

}

