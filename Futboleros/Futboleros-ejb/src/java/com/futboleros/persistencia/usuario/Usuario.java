package com.futboleros.persistencia.usuario;

import com.futboleros.persistencia.club.Club;
import com.futboleros.logica.usuario.Rol;

import org.hibernate.validator.constraints.Email;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NamedQueries({
    @NamedQuery(name = "obtenerUsuarioPorNombre", 
            query = "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario"),
    @NamedQuery(name = "obtenerTodosLosUsuarios", 
            query = "SELECT u FROM Usuario u order by u.nombreUsuario"),
    @NamedQuery(name = "obtenerUsuariosPorClubSeguido", 
            query = "SELECT u FROM Usuario u join u.clubesSeguidos cb where cb = :club"),
    @NamedQuery(name = "hacerUsuarioAdmin",
            query = "UPDATE Usuario u SET u.rol = :rol where u.nombreUsuario = :nombreUsuario")
    })

@Entity
public class Usuario implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "UsuarioId")
  private Long id;
    
  @Column(name = "UsuarioNombreCompleto", length = 60)
  @Size(min = 5, max = 60)
  @NotNull
  private String nombreCompleto;

  @Column(name = "UsuarioNombreUsuario", length = 40, unique = true)
  @Size(min = 2)
  @NotNull
  private String nombreUsuario;

  @Column(name = "UsuarioRol")
  @NotNull
  @Enumerated(EnumType.STRING)
  private Rol rol;
    
  @Column(name = "UsuarioEmail")
  @NotNull
  @Email(message = "Dirección de email inválida")
  private String email;

  @ManyToMany(targetEntity = Club.class)
  @JoinTable(name = "usuarioclubes")
  private List<Club> clubesSeguidos;

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

  public List<Club> getClubesSeguidos() {
    return clubesSeguidos;
  }

  public void setClubesSeguidos(List<Club> clubesSeguidos) {
    this.clubesSeguidos = clubesSeguidos;
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
    if (!(object instanceof Usuario)) {
      return false;
    }
    Usuario other = (Usuario) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.futboleros.usuario.Usuario[ id=" + id + " ]";
  }

  public Usuario() {}
    
  public Usuario(Long id, String nombreCompleto, String nombreUsuario,
         Rol rol, String email) {
    setId(id);
    setNombreCompleto(nombreCompleto);
    setNombreUsuario(nombreUsuario);
    setRol(rol);
    setEmail(email);
  }
    
}