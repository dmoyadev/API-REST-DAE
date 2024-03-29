/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dae.dae1819.DAOs;

import com.dae.dae1819.pojos.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dml y jfaf
 */
@Repository
@Transactional
public class UsuarioDAO {

    @PersistenceContext
    EntityManager em;

    public List<Usuario> listar() {
        List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();

        return usuarios;
    }

//    @Cacheable(value="usuarios")
    public Usuario buscar(String username) {
        Usuario result = em.find(Usuario.class, username, LockModeType.OPTIMISTIC);
        return result;
    }

    public void insertar(Usuario u) {
        em.persist(u);
    }

    public Usuario actualizar(Usuario u) {
        Usuario usu = em.merge(u);
        return usu;
    }

}
