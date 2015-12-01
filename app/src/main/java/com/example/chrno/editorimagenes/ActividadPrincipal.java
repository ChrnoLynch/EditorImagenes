package com.example.chrno.editorimagenes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActividadPrincipal extends AppCompatActivity {
    private ImageView iv,btAzul,btRojo,btVerde, btsepia, btNegro, btAmarillo, btMorado;
    private LinearLayout panelColores, panelFiltros, panelBrillo;
    private Button bt;
    private Bitmap bitmap, bitmapCopia;
    private Edicion editor;
    private int extNombre=0;
    public static final int REQUEST_IMAGE_GET = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        iv = (ImageView) findViewById(R.id.imagen);
        btAzul= (ImageView) findViewById(R.id.btFiltroAzul);
        btRojo= (ImageView) findViewById(R.id.btFiltroRojo);
        btVerde= (ImageView) findViewById(R.id.btFiltroVerde);
        btsepia= (ImageView) findViewById(R.id.btSepia);
        btNegro= (ImageView) findViewById(R.id.btNegro);
        btAmarillo= (ImageView) findViewById(R.id.btAmarillo);
        btMorado= (ImageView) findViewById(R.id.btMorado);
        panelColores= (LinearLayout) findViewById(R.id.panelColores);
        panelBrillo= (LinearLayout) findViewById(R.id.panelBrillo);
        panelFiltros= (LinearLayout) findViewById(R.id.panelFiltros);
        inicio();
    }

    @Override
    protected void onResume() {
        super.onResume();
        botones();
    }

    public void inicio(){
        Intent intent=getIntent();
        Uri uri=intent.getData();
        iv.setImageURI(uri);
        bitmapCopia=((BitmapDrawable) iv.getDrawable()).getBitmap();
        editor=new Edicion();
    }

    public void cargarFotoGaleria(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_GET) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            Uri uri = data.getData();
            if (uri != null) { //Obtiene la Uri de la imagen
                iv.setImageURI(uri);
                bitmapCopia=((BitmapDrawable) iv.getDrawable()).getBitmap();
            }
        }
    }


    //METODOS PARA LOS BOTONES
    /***********************************************************************/
    public void panelBrillo(View v){
        panelColores.setVisibility(View.GONE);
        panelFiltros.setVisibility(View.GONE);
        panelBrillo.setVisibility(View.VISIBLE);
    }

    public void panelColores(View v){
        panelFiltros.setVisibility(View.GONE);
        panelBrillo.setVisibility(View.GONE);
        panelColores.setVisibility(View.VISIBLE);
    }

    public void panelFiltros(View v){
        panelColores.setVisibility(View.GONE);
        panelBrillo.setVisibility(View.GONE);
        panelFiltros.setVisibility(View.VISIBLE);
    }

    public void restaurar(View v){
        iv.setImageBitmap(bitmapCopia);
    }

    public void masBrillo(View v){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.brillo(bitmap, 10);
        iv.setImageBitmap(bmp);
    }

    public void menosBrillo(View v){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.brillo(bitmap,-10);
        iv.setImageBitmap(bmp);
    }

    public  void filtroAzul(View v){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.BLUE);
        iv.setImageBitmap(bmp);
    }
    public  void filtroRojo(View v){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.RED);
        iv.setImageBitmap(bmp);
    }
    public  void filtroVerde(View v){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.GREEN);
        iv.setImageBitmap(bmp);
    }

    public  void filtroAmarillo(View v){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.YELLOW);
        iv.setImageBitmap(bmp);
    }

    public  void filtroMorado(View v){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.rgb(102,15,130));
        iv.setImageBitmap(bmp);
    }

    public void sepia(View v){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmpSepia = editor.toSephia(bitmap);//Se pasa a sepia
        iv.setImageBitmap(bmpSepia);

    }

    public void convertirGris(View v) {
        BitmapDrawable bmpDraw = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bmpDraw.getBitmap();
        Bitmap bmpGris = editor.toEscalaDeGris(bitmap);//Se pasa a gris
        iv.setImageBitmap(bmpGris);
    }

    public void girar(View v) {
        BitmapDrawable bmpDraw = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bmpDraw.getBitmap();
        iv.setImageBitmap(editor.rotarBitmap(bitmap, 90));
    }

    public  void girarEspejo(View v){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        iv.setImageBitmap(editor.espejo(bitmap));
    }

    public void guardar(View v) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("¿Desea guardar los cambios?");
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BitmapDrawable b=(BitmapDrawable)iv.getDrawable();
                Bitmap bm=(Bitmap) b.getBitmap();
                guardarCambio(bm);
                Toast.makeText(getApplicationContext(), "La foto ha sido guardada en myAppDir/imagen",
                        Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        alert.show();
    }
/***************************************************************************************************/

    //Metodos para las miniaturas

    public  Bitmap miniFiltroAzul(){
    bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
    Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.BLUE);
    return bmp;
   }

    public  Bitmap miniFiltroRojo(){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.RED);
        return bmp;
    }

    public  Bitmap miniFiltroVerde(){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.GREEN);
        return bmp;
    }

    public  Bitmap miniFiltroAmarillo(){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.YELLOW);
        return bmp;
    }

    public  Bitmap miniFiltroMorado(){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmp = editor.cambioBitmapColor(bitmap, Color.rgb(102,15,130));
        return bmp;
    }

    public Bitmap miniSepia(){
        bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        Bitmap bmpSepia = editor.toSephia(bitmap);
        return bmpSepia;
    }

    public Bitmap miniConvertirGris() {
        BitmapDrawable bmpDraw = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bmpDraw.getBitmap();
        Bitmap bmpGris = editor.toEscalaDeGris(bitmap);
        return bmpGris;
    }

    public void botones(){
        btAzul.setImageBitmap(miniFiltroAzul());
        btRojo.setImageBitmap(miniFiltroRojo());
        btVerde.setImageBitmap(miniFiltroVerde());
        btMorado.setImageBitmap(miniFiltroMorado());
        btAmarillo.setImageBitmap(miniFiltroAmarillo());
        btNegro.setImageBitmap(miniConvertirGris());
        btsepia.setImageBitmap(miniSepia());
    }

/**************************************************************************************************/
    //GUARDAR EN OTRA CLASE
    private boolean guardarCambio(Bitmap imageData) {
        extNombre++;
        //Obtener ruta de la memoria externa
        String ruta="/myAppDir/imagen/";
        String iconsStoragePath = Environment.getExternalStorageDirectory() + ruta;
        File sdIconStorageDir = new File(iconsStoragePath);
        String filename="Modificada"+extNombre+".JPEG";
        //Crea el directorio si este no existe
        sdIconStorageDir.mkdirs();

        try {
            String filePath = sdIconStorageDir.toString() + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {}

        return true;
    }

}
