package com.prueba.agendaconarchivos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTextName, editTextTexName2, editTextEmail, editTexPhone;
    String[] archivos;
    ArrayList<String> todos = new ArrayList<String>();
    ArrayList<String> rechazo = new ArrayList<String>();
    int contador =0;
    Controlador c = new Controlador();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //se une la parte logica con la parte grafica
        setContentView(R.layout.activity_main);
        editTextTextName=(EditText)findViewById(R.id.editTextTextName);
        editTextTexName2=(EditText)findViewById(R.id.editTextTexName2);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTexPhone=(EditText)findViewById(R.id.editTexPhone);
        String datos;
        //el vector sirve para poder acceder a mis archivos guardados
        archivos=fileList();
        //con el for voy a recorrer buscando la informacion
        for(int i=0;i< archivos.length;i++){
            Toast.makeText(this,archivos[i], Toast.LENGTH_LONG).show();
        }
        if(archivos.length==0){
            Toast.makeText(this,"No hay archivos aun", Toast.LENGTH_LONG).show();
        }

    }

    public void guardar(View view){
        //el que permite crear un archivo
        try{
            OutputStreamWriter archi=new OutputStreamWriter(openFileOutput("contactos.txt", Activity.MODE_APPEND));
            //con el write le mando a escribir sobre el archivo
            archi.write(editTextTextName.getText().toString()+";"+editTextTexName2.getText().toString()+";"+editTextEmail.getText().toString()+";"
                    +editTexPhone.getText().toString()+"\n");
            //limpia la entrada para no tener basura, en caso de utilizar nuevamente
            archi.flush();
            archi.close();
            //luego de que se guarde la informacion le envio a que se vacie los campos de escribir
            editTextTextName.setText("");
            editTextTexName2.setText("");
            editTextEmail.setText("");
            editTexPhone.setText("");
            //utilizo un toast con el mensaje de que se guarda la informacion
            Toast.makeText(this,"La informacion fue guardada", Toast.LENGTH_LONG).show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public void buscar(View view){
        //le limpio para que no me guarda datos innecesarios
        todos.clear();
        int cantidad=0;
        if(archivos.length!=0){
            try{
                InputStreamReader aux = new InputStreamReader(openFileInput("contactos.txt"));
                //le creo un vector y le paso desde el controlador el contenido buscado
                ArrayList<String>[] auxiliar = c.buscarTodo(aux,editTextTextName.getText().toString());
                //la posicion 0 son los encontrados que voy a modificar
                //los de posicion 1 son los que no pertenecian a la busqueda original pero no los quiero perder
                todos=auxiliar[0];
                rechazo=auxiliar[1];
            }catch (IOException e){
                e.printStackTrace();
            }
            if(todos.size()!=0){
                //aqui es donde muestro los datos extraidos del vector
                editTextTextName.setText(todos.get(0).split(";")[0]);
                editTextTexName2.setText(todos.get(0).split(";")[1]);
                editTextEmail.setText(todos.get(0).split(";")[2]);
                editTexPhone.setText(todos.get(0).split(";")[3]);
                cantidad+=todos.size();
                Toast.makeText(this,"Se encontro "+cantidad+" contactos con ese nombre", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"No existe contacto con ese nombre"+editTextTextName.getText().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void despliegue(View view){
        //este metodo es para poder ver los elementos que se ayan encontrado con la busqueda
        String tododelbuscado="";
        for(int i=0;i<todos.size();i++ ){
            tododelbuscado+=todos.get(i)+"\n";
        }
        Toast.makeText(this,tododelbuscado, Toast.LENGTH_LONG).show();
    }

    public void siguiente(View view){
        //este metodo es para recorrer los encontrados,
        if(contador<todos.size()-1){
            //el if sirve para que no siga dando clics degana ya que controla la existencia de datos
            contador++;
            editTextTextName.setText(todos.get(contador).split(";")[0]);
            editTextTexName2.setText(todos.get(contador).split(";")[1]);
            editTextEmail.setText(todos.get(contador).split(";")[2]);
            editTexPhone.setText(todos.get(contador).split(";")[3]);
        }else{
            //con el toast muestra si existen o no mas elementos para que me lance el mensaje
            Toast.makeText(this,"No existe contacto mas con el nombre: "+editTextTextName.getText().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void anterior(View view){
        if(contador>=1){
            contador--;
            editTextTextName.setText(todos.get(contador).split(";")[0]);
            editTextTexName2.setText(todos.get(contador).split(";")[1]);
            editTextEmail.setText(todos.get(contador).split(";")[2]);
            editTexPhone.setText(todos.get(contador).split(";")[3]);
        }else{
            Toast.makeText(this,"No existe contacto mas con el nombre: "+editTextTextName.getText().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void modificar(View view){
        //primero remuevo los datos del vector
        todos.remove(contador);
        //agrego los datos modificados
        todos.add(editTextTextName.getText().toString()+";"+editTextTexName2.getText().toString()+";"+editTextEmail.getText().toString()+";"+editTexPhone.getText().toString());
        //con este for les uno todos los datos, tanto los buscados como los encontrados fuera del nombre
        for(int j=0;j<rechazo.size();j++)
            todos.add(rechazo.get(j));
        try{
            OutputStreamWriter archi=new OutputStreamWriter(openFileOutput("contactos.txt", Activity.MODE_PRIVATE));
            c.guardarModificaciones(archi,todos);
            editTextTextName.setText("");
            editTextTexName2.setText("");
            editTextEmail.setText("");
            editTexPhone.setText("");
            Toast.makeText(this,"La informacion fue modificada", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}