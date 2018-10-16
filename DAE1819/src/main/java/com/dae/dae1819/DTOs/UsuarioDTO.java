/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dae.dae1819.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanf
 */
public class UsuarioDTO {

    private String username;
    private String email;
    private Integer token;

    private List<String> eventos;
    private List<String> organizados;
    private List<String> listaEspera;

    public UsuarioDTO() {
        eventos = new ArrayList();
        this. token = 0;
        organizados = new ArrayList();
        listaEspera = new ArrayList();
    }

    public UsuarioDTO(String username, String email) {
        this.username = username;
        this.email = email;
        this. token = 0;
        eventos = new ArrayList();
        organizados = new ArrayList();
        listaEspera = new ArrayList();
    }

    public UsuarioDTO(String username, String email, List<String> eventos, List<String> organizados) {
        this.username = username;
        this.email = email;
        this.token = 0;

        this.eventos.clear();
        eventos.forEach((evento) -> {
            this.eventos.add(evento);
        });

        this.organizados.clear();
        organizados.forEach((evento) -> {
            this.organizados.add(evento);
        });

        this.listaEspera.clear();
        listaEspera.forEach((evento) -> {
            this.listaEspera.add(evento);
        });
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
    public List<String> getEventos() {
        return eventos;
    }

    /**
     * @param eventos the eventos to set
     */
    public void setEventos(List<String> eventos) {
        this.eventos.clear();
        eventos.forEach((evento) -> {
            this.eventos.add(evento);
        });
    }

    /**
     * @return the organizados
     */
    public List<String> getOrganizados() {
        return organizados;
    }

    /**
     * @param organizados the organizados to set
     */
    public void setOrganizados(List<String> organizados) {
        this.organizados.clear();
        organizados.forEach((evento) -> {
            this.organizados.add(evento);
        });
    }

    /**
     * @return the listaEspera
     */
    public List<String> getListaEspera() {
        return listaEspera;
    }

    /**
     * @param listaEspera the listaEspera to set
     */
    public void setListaEspera(List<String> listaEspera) {
        this.listaEspera = listaEspera;
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
}
