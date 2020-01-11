package com.example.reproductomultimedia;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    VideoView video;
    Button btCargar;
    int REQUEST_READ_STORAGE =0;
    final int codigoSeleccion= 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializamos lso componentes y elementos
        video = findViewById(R.id.videoView);
        btCargar = findViewById(R.id.btCargar);
        btCargar.setOnClickListener(this);

    }

    //Cargamos el contenido del explorador de archivos
    public void cargarContenido(){
        //Indicamos al Intent que recoja un contenido
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        //Cualquier tipo de contenido
        i.setType("video/*");
        //Pero debe ser ejecutable
        i.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            //Iniciamos el explorador
            startActivityForResult(Intent.createChooser(i, "Seleccione un contenido"), codigoSeleccion);
        }catch(android.content.ActivityNotFoundException ex){
            //Si no disponemos de un explorador lanzamos un mensaje de excepcion
            Toast.makeText(this,"Por favor instala un explorador de archivos",Toast.LENGTH_SHORT).show();
        }
    }
    //Finalizamos el proceso ejecutado pro startActivityForResult
    protected void onActivityResult(int cod, int resultado, Intent datos){
        switch(cod){
            case codigoSeleccion:
                if(resultado == RESULT_OK){
                    //Obtenemos la uri del fichero
                    Uri uri = datos.getData();
                    //Establecemos la uri en el VideoView
                    video.setVideoURI(uri);
                }
                break;
        }
    }
    @Override
    //Indicamos a botón que ejecute la carga de contenido
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btCargar:
                cargarContenido();
                break;
        }
        //Tras cargar el contenido de VideoView le indicamos que introduzca un MediaController
        video.setMediaController(new MediaController(this));
        //Iniciamos el video.
        video.start();
        //Indicamos al Activity que verifique los permisos para reproducir el video
        boolean tienePermiso = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE))== REQUEST_READ_STORAGE;
        if(!tienePermiso){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_STORAGE);
        }
    }
}
