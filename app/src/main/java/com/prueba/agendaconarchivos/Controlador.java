package com.prueba.agendaconarchivos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Controlador {

    public boolean existeArchivos(String [] archivos, String buscado){
        for(int i=0; i<archivos.length;i++)
            if(archivos[i].compareToIgnoreCase(buscado)==0)return true;
        return false;
    }

    public void guardar(OutputStreamWriter arch, String datos){
            try {
                arch.write(datos+"\n");
                arch.flush();
                arch.close();
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    public void guardarModificaciones(OutputStreamWriter arch, ArrayList<String> datos){
        //con este metodo le agrego los elementos al archivo, recorro el vector y guardo cada informacion.
        try {
            for(int i=0; i<datos.size();i++)
                arch.write(datos.get(i)+"\n");
            arch.flush();
            arch.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList <String>[] buscarTodo(InputStreamReader aux, String buscado){
        //con este metodo busco aprovechar que los buscados con nombre
        //y los no buscados se los pueda rescatar, para evitar se eliminen
        ArrayList <String> retorno = new ArrayList<String>();
        ArrayList <String> rechazo = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(aux);
            String linea=br.readLine();
            while(linea!=null){
                if(linea.split(";")[0].compareToIgnoreCase(buscado)==0){
                    retorno.add(linea);
                }else
                    rechazo.add(linea);
                linea=br.readLine();
            }
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList [] {retorno,rechazo};
    }



}
