package com.futboleros.dto;

import com.futboleros.usuario.Rol;

/**
 *
 * @author inibg
 */
public class UsuarioDto {
    
    private Long id;
    private String nombreCompleto;
    private String nombreUsuario;
    private Rol rol;
    private String email;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public UsuarioDto(){}
    
    public UsuarioDto(Long id, String nombreCompleto, String nombreUsuario,
            Rol rol, String email){
        setId(id);
        setNombreCompleto(nombreCompleto);
        setNombreUsuario(nombreUsuario);
        setRol(rol);
        setEmail(email);
    }

    @Override
    public String toString() {
        return "com.futboleros.dto.UsuarioDto[ id=" + id + " ]";
    }

}
