/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.usuario;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author inibg
 */
@NamedQueries({
    @NamedQuery(name = "ObtenerSesionPorToken", 
            query = "SELECT s FROM Sesion s WHERE s.token like :sesionToken"),
    @NamedQuery(name = "TerminarSesionPorId",
            query = "DELETE FROM Sesion s WHERE s.id = :sesionId"),
    @NamedQuery(name = "ObtenerSesionPorUsuario",
            query = "SELECT s FROM Sesion s WHERE s.usuario = :usuario")
})
@Entity
public class Sesion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="SesionId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="SesionToken", length=128, unique = true)
    @NotNull
    private String token;
    
    @JoinColumn(name="SesionUsuarioId", updatable = false)
    @OneToOne(targetEntity = Usuario.class,optional = false,fetch = EAGER)
    @NotNull
    private Usuario usuario;
    
    @Column(name="SesionFechaInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public Sesion(){}
    
    public Sesion(Long id, String token, Usuario usuario,Date fechaInicio){
        setId(id);
        setToken(token);
        setFechaInicio(fechaInicio);
        setUsuario(usuario);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sesion)) {
            return false;
        }
        Sesion other = (Sesion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.futboleros.usuario.Sesion[ id=" + id + " ]";
    }
    
}
