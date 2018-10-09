/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dae.dae1819.pojos;
import com.dae.dae1819.DTOs.UsuarioDTO;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author dml y jfaf
 */
public class Usuario{
    private String username;
    private String password;
    private String email;
    private Integer token;
    
    private List<Evento> eventos;
    private List<Evento> organizados;
    
    public Usuario() {
        eventos = new ArrayList();
        organizados = new ArrayList();
    }
    
     public Usuario(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    public Usuario(String username, String password, String email, List<Evento> eventos, List<Evento> organizados) {
        this.username = username;
        this.password = password;
        this.email = email;
        
        this.eventos.clear();
        for (Evento evento : eventos) {
            this.eventos.add(evento);
        }
        this.organizados.clear();
        for (Evento evento : organizados) {
            this.organizados.add(evento);
        }
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the eventos
     */
    public List<Evento> getEventos() {
        return eventos;
    }

    /**
     * @param eventos the eventos to set
     */
    public void setEventos(List<Evento> eventos) {
        this.eventos.clear();
        for (Evento evento : eventos) {
            this.eventos.add(evento);
        }
    }

    /**
     * @return the organizados
     */
    public List<Evento> getOrganizados() {
        return organizados;
    }

    /**
     * @param organizados the organizados to set
     */
    public void setOrganizados(List<Evento> organizados) {
        this.organizados.clear();
        for (Evento evento : organizados) {
            this.organizados.add(evento);
        }
    }

    /**
     * @return the token
     */
    public Integer getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(Integer token) {
        this.token = token;
    }
    
    public UsuarioDTO toDTO() {
        UsuarioDTO u = new UsuarioDTO();
        u.setUsername(this.getUsername());
        u.setEmail(this.getEmail());
        
        List<String> eventos = new ArrayList();
        this.getEventos().forEach((evento) -> {
            eventos.add(evento.getNombre());
        });
        
        List<String> organizados = new ArrayList();
        this.getOrganizados().forEach((organizado) -> {
            organizados.add(organizado.getNombre());
        });
      
        return u;
    }
    
}
