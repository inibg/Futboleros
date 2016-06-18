/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.club;

/**
 *
 * @author inibg
 */
public class ClubDto {
    
    private Long id;
    private String nombre;

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
    
    public ClubDto(){}
    
    public ClubDto(Long id, String nombre){
        setId(id);
        setNombre(nombre);
    }
    
    @Override
    public String toString(){
        return "com.futboleros.dto.ClubDto[ id=" + id + " ]";
    }
}
