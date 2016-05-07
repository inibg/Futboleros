/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.club;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author inibg
 */
@Stateless
public class ClubBean implements ClubBeanLocal {

    @PersistenceContext
    private EntityManager em;
    
}
