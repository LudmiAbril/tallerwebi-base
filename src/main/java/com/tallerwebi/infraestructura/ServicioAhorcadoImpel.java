package com.tallerwebi.infraestructura;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tallerwebi.dominio.ServicioAhorcado;

@Service("ServicioAhorcado")
public class ServicioAhorcadoImpel implements ServicioAhorcado {


    @Override
    public Integer intentarLetra(Character letra,String palabra, Integer partesAhorcado) {
        if(!buscarLetraEnPalabra(letra, palabra)){
            partesAhorcado--;
        }
        return partesAhorcado;
    }

    @Override
    public Boolean buscarLetraEnPalabra(Character letra, String palabra) {
        return palabra.contains(String.valueOf(letra));
    }

    @Override
    public Boolean Perdio(Integer partesAhorcado) {

        if(partesAhorcado==0){
            return true;
        }
        return false;
    }



    @Override
    public String entregarPalabra() {

        return "supercalifragilisticoespialidoso";
    }




}