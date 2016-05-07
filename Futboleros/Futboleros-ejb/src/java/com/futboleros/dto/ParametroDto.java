/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.dto;

import java.util.Set;

/**
 *
 * @author inibg
 */
public class ParametroDto {
    private Long id;
    private String nombre;
    private String valor;
    private Boolean cifrado;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Boolean getCifrado() {
        return cifrado;
    }

    public void setCifrado(Boolean cifrado) {
        this.cifrado = cifrado;
    }
    
    public ParametroDto(){}
    
     public ParametroDto(Long id, String nombre, String valor, Boolean cifrado){
         setId(id);
         setNombre(nombre);
         setValor(valor);
         setCifrado(cifrado);
         
     }
     @Override
    public String toString() {
        return "com.futboleros.dto.ParametroDto[ id=" + id + " ]";
    }
}


