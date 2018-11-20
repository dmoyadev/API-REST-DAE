/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dae.dae1819.service;

import com.dae.dae1819.DTOs.EventoDTO;
import com.dae.dae1819.DTOs.UsuarioDTO;
import com.dae.dae1819.Excepciones.ListaEventosVacia;
import com.dae.dae1819.pojos.Sistema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dml y jfaf
 */
@RestController
@RequestMapping("/")
public class RecursoSistema {
    
    Sistema sistema;
    
    @RequestMapping(method=GET, produces="application/json")
    public Sistema obtenerSistema() {
        return sistema;
    }
    
    @RequestMapping(value="/eventos", method=GET, produces="application/json")
    public List<EventoDTO> obtenerEvento(@RequestParam(defaultValue="") String nombre) throws ListaEventosVacia {
        List<EventoDTO> eventos = new ArrayList();
        try {
            eventos = sistema.buscarEventoPorNombre(nombre);
        } catch (ListaEventosVacia e) {
            throw e;
        }
        return eventos;
    }
    
    @RequestMapping(value="/eventos/{id}", method=GET, produces="application/json")
    public EventoDTO obtenerEvento(@PathVariable int id) {
        EventoDTO evento = sistema.buscarEventoPorId(id);
        return evento;
    }
    
    @RequestMapping(value="/eventos/{id}/asistentes", method=GET, produces="application/json")
    public List<UsuarioDTO> obtenerAsistentes(@PathVariable int id) {
        List<String> eventos = sistema.buscarEventoPorId(id).getAsistentes();
        List<UsuarioDTO> usuarios = new ArrayList();
        
        for(String u : eventos) {
            usuarios.add(sistema.buscarUsuario(u));
        }
        
        return usuarios;
    }
    
    @RequestMapping(value="/eventos/{id}/inscritos", method=GET, produces="application/json")
    public List<UsuarioDTO> obtenerInscritos(@PathVariable int id) {
        List<String> eventos = sistema.buscarEventoPorId(id).getInscritos();
        List<UsuarioDTO> usuarios = new ArrayList();
        
        for(String u : eventos) {
            usuarios.add(sistema.buscarUsuario(u));
        }
        
        return usuarios;
    }
    
    @RequestMapping(value="/usuario/{username}", method=GET, produces="application/json")
    public UsuarioDTO obtenerUsuario(@PathVariable String username) {
        UsuarioDTO usuario = sistema.buscarUsuario(username);
        return usuario;
    }
    
    @RequestMapping(value="/usuario/{username}/eventos", method=GET, produces="application/json")
    public List<EventoDTO> obtenerEventos(@PathVariable String username) {
        UsuarioDTO usuario = sistema.buscarUsuario(username);
        List<Integer> idEventos = usuario.getEventos();
        List<EventoDTO> eventos = new ArrayList();
        
        for(int u : idEventos) {
            eventos.add(sistema.buscarEventoPorId(u));
        }
        
        return eventos;
    }
    
    @RequestMapping(value="/usuario/{username}/listaEspera", method=GET, produces="application/json")
    public List<EventoDTO> obtenerListaEspera(@PathVariable String username) {
        UsuarioDTO usuario = sistema.buscarUsuario(username);
        List<Integer> idEventos = usuario.getListaEspera();
        List<EventoDTO> eventos = new ArrayList();
        
        for(int u : idEventos) {
            eventos.add(sistema.buscarEventoPorId(u));
        }
        
        return eventos;
    }
}
