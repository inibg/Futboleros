package com.futboleros.club;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author inibg
 */
@Entity 
//@IdClass(Partido.class)
//@Table(uniqueConstraints= { 
//@UniqueConstraint(columnNames={"PartidoClubLocalId", "PartidoClubVisitanteId", "PartidoFecha"})})
public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "PartidoId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
   // @ManyToOne(optional = false, targetEntity = Club.class)
    //@JoinColumn(name ="PartidoClubLocalId", referencedColumnName = "ClubId")
  //  @Id
    @NotNull
    @Column(name = "PartidoClubLocalId")
    private Long clubLocal;
    
  //  @ManyToOne(optional = false, targetEntity = Club.class)
   // @JoinColumn(name ="PartidoClubVisitanteId", referencedColumnName = "ClubId")
   //@Id
    @Column(name = "PartidoClubVisitanteId")
    @NotNull
    private Long clubVisitante;
    
    //@Id
    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "PartidoFecha")
    private Date fechaPartido;
    
    @Column(name = "PartidoGolesLocal")
    private Integer golesLocal;
    
    @Column(name = "PartidoGolesVisitante")
    private Integer golesVisitante;
    
    @Transient
    private String Resultado;
    
    @Transient
    private Club ganador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public void setClubLocal(Long clubLocal) {
        this.clubLocal = clubLocal;
    }

    public Long getClubLocal() {
        return this.clubLocal;
    }

    
    public Long getClubVisitante() {
        return clubVisitante;
    }

    public void setClubVisitante(Long clubVisitante) {
        this.clubVisitante = clubVisitante;
    }

    public Date getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(Date fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public Integer getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(Integer golesLocal) {
        this.golesLocal = golesLocal;
        calcularResultado();
    }

    public Integer getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(Integer golesVisitante) {
        this.golesVisitante = golesVisitante;
        calcularResultado();
    }

    public String getResultado() {
        return Resultado;
    }

    private void setResultado(String Resultado) {
        this.Resultado = Resultado;
    }

    public Club getGanador() {
        return ganador;
    }

    private void setGanador(Club ganador) {
        this.ganador = ganador;
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
        if (!(object instanceof Partido)) {
            return false;
        }
        Partido other = (Partido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.futboleros.club.Partido[ id=" + id + " ]";
    }
    
    public Partido(){}
    
    public Partido(Long id, Long local, Long visitante, Date fechaPartido,
            Integer golesLocal, Integer golesVisitante){
        setId(id);
        setClubLocal(local);
        setClubVisitante(visitante);
        setGolesLocal(golesLocal);
        setGolesVisitante(golesVisitante);
        setFechaPartido(fechaPartido);
    }
    
    private void calcularResultado(){
      /*  if (getGolesLocal() != null && getGolesVisitante() != null){
            if (getGolesLocal() == getGolesVisitante()){
                setResultado("EMPATE");
                setGanador(null);
            } else {
                if (getGolesLocal() > getGolesVisitante()){
                    setResultado("GANA " + getClubLocal().getNombre());
                    setGanador(getClubLocal());
                } else {
                    setResultado("GANA " + getClubVisitante().getNombre());
                    setGanador(getClubVisitante());
                }
                   
            }
        }*/
    }
}
