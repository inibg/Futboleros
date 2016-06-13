
package com.futboleros.configuracion;

import com.futboleros.dto.ParametroDto;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author inibg
 */
@Stateless
@LocalBean
public class ParametroBean {
    @PersistenceContext
    private EntityManager em;
    
    private ParametroDto toDto(Parametro ent){
        ParametroDto nuevo = new ParametroDto(ent.getId(), ent.getNombre(),
                ent.getValor(),ent.getCifrado());
        return nuevo;
    }
    private Parametro toEntity(ParametroDto dto){
        Parametro nuevo = new Parametro(dto.getId(), dto.getNombre(),
                dto.getValor(),dto.getCifrado());
        return nuevo;
    }  
    
    public void agregarParametro(ParametroDto parametro){
        Parametro nuevoParametro = toEntity(parametro);
        em.persist(nuevoParametro);
    }
    
    
}
