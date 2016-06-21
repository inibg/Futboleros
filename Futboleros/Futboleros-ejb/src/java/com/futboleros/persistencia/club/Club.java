package com.futboleros.persistencia.club;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@NamedQueries({
        @NamedQuery(name = "obtenerClubPorNombre", 
            query = "SELECT c FROM Club c WHERE c.nombre = :nombreclub"),
        @NamedQuery(name = "obtenerTodosLosClubes", query = "SELECT c FROM Club c"
            + " order by c.nombre"),
        @NamedQuery(name = "eliminarClub", query = "DELETE FROM Club c WHERE c.id =:idclub"),
   
        @NamedQuery(name = "modificarNombreClub", query = "UPDATE Club c SET c.nombre=:nombreClub WHERE c.id =:idclub")
        })


@Entity
@XmlRootElement
public class Club implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ClubId")
    private Long id;
    
  @Column(name = "ClubNombre", unique = true)
  @NotNull
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
    
  public Club(){}
 
  public Club(Long id, String nombre) {
    setId(id);
    setNombre(nombre);
  }
    
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Club)) {
      return false;
    }
    Club other = (Club) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.futboleros.club.Club[ id=" + id + " ]";
  }
    
}
