/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dae.dae1819.pojos;

import com.dae.dae1819.DAOs.EventoDAO;
import com.dae.dae1819.DAOs.UsuarioDAO;
import com.dae.dae1819.interfaces.SistemaInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.dae.dae1819.DTOs.EventoDTO;
import com.dae.dae1819.DTOs.UsuarioDTO;
import com.dae.dae1819.Excepciones.ListaEventosVacia;
import com.dae.dae1819.Excepciones.UsuarioExistente;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author dml y jfaf
 */
public class Sistema extends SistemaInterface {

    private String nombre;
    
    @Autowired
    private UsuarioDAO usuarios;
    
    @Autowired
    private EventoDAO eventos;
    
    private List<Integer> tokenConectados;

    public Sistema() {
        
    }

    public Sistema(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Comprueba si el token de sesión es válido
     *
     * @param token el token a comprobar
     * @return true si el token es válido, false si no
     */
    @Override
    public boolean isTokenValid(Integer token) {
        boolean ret = false;

        if (token != 0) {
            ret = tokenConectados.contains(token);
        }

        return ret;
    }

    /*
     ***************************************************************************
     ***************************************************************************
     ************ ACCIONES USUARIOS QUE NO HAN INICIADO SESIÓN *****************
     ***************************************************************************
     ***************************************************************************
     */
    /**
     * Registra a un usuario en el sistema
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     * @param password2 la contraseña para comprobar
     * @param email el email del usuario
     * @throws UsuarioExistente Excepcion si el usuario existe
     * @return true si los datos son correctos, false si no
     */
    @Override
    public boolean nuevoUsuario(String username, String password, String password2, String email) throws UsuarioExistente {
        boolean ret = false;

        if (password.equals(password2)) {
            Usuario usuario = new Usuario(username, password, email);
            try {
                usuarios.insertar(usuario);
                //TODO Controlar excepción
            } catch (Exception e){
                throw new UsuarioExistente("The user is already stored ", e);
            }
            ret = true;
        }

        return ret;
    }

    /**
     * Inicia la sesión de un usuario registrado en el sistema
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     * @return un UsuarioDTO válido si se ha iniciado sesión correctamente
     */
    @Override
    public UsuarioDTO login(String username, String password) {
        Usuario user = usuarios.buscar(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                Integer token = ThreadLocalRandom.current().nextInt(10000000, 100000000);
                tokenConectados.add(token);
                UsuarioDTO uDTO = this.usuarioToDTO(user);
                uDTO.setToken(token);
                return uDTO;
            }
        }
        return null;
    }
    
    /**
     * Inicia la sesión de un usuario registrado en el sistema
     *
     * @param uDTO el usuario que saldrá del sistema
     * @return null si se ha salido de la sesión correctamente
     */
    @Override
    public UsuarioDTO logout(UsuarioDTO uDTO) {
        if(this.isTokenValid(uDTO.getToken())) {
            tokenConectados.remove(uDTO.getToken());
            return null;
        } else {
            return uDTO;
        }
    }

    /**
     * Busca un evento por el nombre del mismo
     *
     * @param nombre el nombre del evento a buscar
     * @throws ListaEventosVacia Excepcion que se lanza si la lista de eventos esta vacia
     * @return un EventoDTO del evento encontrado, o uno vacío si no lo encuentra
     */
    @Override
    public EventoDTO buscarEventoPorNombre(String nombre) throws ListaEventosVacia {
        if(!eventos.isEmpty()) {
            try {
                return eventoToDTO(eventos.get(nombre));
            } catch (NullPointerException e) {
                throw new ListaEventosVacia("The list of events is empty ", e);
            }
        } else {
            return new EventoDTO();
        }
    }

    /**
     * Busca un evento por el tipo del mismo
     *
     * @param tipo el tipo del evento a buscar
     * @throws ListaEventosVacia Excepcion que se lanza si la lista de eventos esta vacia
     * @return una lista de EventoDTO encontrados, o una lista vacía si no
     * encuentra ninguno
     */
    @Override
    public List<EventoDTO> buscarEventosPorTipo(String tipo) throws ListaEventosVacia {
        List<EventoDTO> eventosPorTipo = new ArrayList();
        try {
            eventos.entrySet().forEach((entry) -> {
                if (entry.getValue().getTipo().equalsIgnoreCase(tipo)) {
                    EventoDTO e = eventoToDTO(entry.getValue());
                    eventosPorTipo.add(e);
                }
            });
        } catch (NullPointerException e) {
                throw new ListaEventosVacia("The list of events is empty ", e);
        }

        return eventosPorTipo;
    }

    ;
    
    /**
     * Busca un evento por la descripción del mismo
     * @param descripcion la descripción del evento a buscar
     * @throws ListaEventosVacia Excepcion que se lanza si la lista de eventos esta vacia
     * @return una lista de EventoDTO encontrados, o una lista vacía si no encuentra ninguno
     */
    @Override
    public List<EventoDTO> buscarEventosPorDescripcion(String descripcion) throws ListaEventosVacia {
        List<EventoDTO> eventosPorDescripcion = new ArrayList();
        try {
            eventos.entrySet().forEach((entry) -> {
                if (entry.getValue().getDescripcion().contains(descripcion)) {

                    EventoDTO e = eventoToDTO(entry.getValue());
                    eventosPorDescripcion.add(e);
                }
            });
        } catch (NullPointerException e) {
            throw new ListaEventosVacia("The list of events is empty ", e);
        }

        return eventosPorDescripcion;
    }

    /**
     * Lista todos los eventos del sistema
     * @throws ListaEventosVacia Excepcion que se lanza si la lista de eventos esta vacia
     * @return una lista con todos los eventos creados en forma DTO (vacía si no
     * encuentra ninguno)
     */
    @Override
    public List<EventoDTO> buscarEventos() throws ListaEventosVacia {
        List<EventoDTO> lista = new ArrayList();
        try {
            eventos.entrySet().forEach((entry) -> {
                EventoDTO e = eventoToDTO(entry.getValue());
                lista.add(e);
            });
        } catch (NullPointerException e) {
            throw new ListaEventosVacia("The list of events is empty ", e);
        }
        return lista;
    }

    /*
     ***************************************************************************
     ***************************************************************************
     *************** ACCIONES USUARIOS QUE HAN INICIADO SESIÓN *****************
     ***************************************************************************
     ***************************************************************************
     */
    /**
     * Crea un nuevo evento
     *
     * @param nombre el nombre del evento
     * @param fecha la fecha en la que se realiza el evento
     * @param tipo el tipo del evento
     * @param descripcion la descripción del evento
     * @param capacidad la capacidad de asistentes al evento
     * @param localizacion el lugar donde se realiza el evento
     * @param organizador el usuario que ha creado el evento
     * @return true si se ha creado bien, false si no
     */
    @Override
    public boolean nuevoEvento(String nombre, Date fecha, String tipo,
            String descripcion, Integer capacidad, String localizacion,
            String organizador) {

        Usuario u = usuarios.get(organizador);
        if (!this.isTokenValid(u.getToken())) {
            return false;
        } else {
            Evento evento = new Evento(nombre, fecha, tipo, descripcion, capacidad, localizacion, u);

            u.inscribirEnEvento(evento);

            eventos.put(nombre, evento);
        }
        return true;
    }
    

    /**
     * Cancela un evento, borrando en cascada
     *
     * @param eDTO el evento a cancelar
     * @param uDTO el usuario que cancela el evento
     * @return true si se cancela, false si no
     */
    @Override
    public boolean cancelarEvento(EventoDTO eDTO, UsuarioDTO uDTO) {
        //TODO notificacion
        if (!this.isTokenValid(uDTO.getToken())) {
            return false;
        } else if(!eDTO.getOrganizador().equals(uDTO.getUsername())){
            return false;
        } else {
            Evento e = eventos.get(eDTO.getNombre());
            e.setCancelado(true);
            eDTO.setCancelado(true);

            eventos.replace(e.getNombre(), e);
        }
        return true;
    }

    /**
     * Reactiva un evento
     *
     * @param eDTO el evento a reactivar
     * @param uDTO el usuario que reactiva el evento
     * @return true si se cancela, false si no
     */
    @Override
    public boolean reactivarEvento(EventoDTO eDTO, UsuarioDTO uDTO) {
        //TODO notificacion
        if (!this.isTokenValid(uDTO.getToken())) {
            return false;
        } else if(!eDTO.getOrganizador().equals(uDTO.getUsername())){
            return false;
        } else {
            Evento e = eventos.get(eDTO.getNombre());
            e.setCancelado(false);
            eDTO.setCancelado(false);

            eventos.replace(eDTO.getNombre(), e);
        }
        return true;
    }

    /**
     * Busca un usuario por su nombre de usuario
     *
     * @param username el nombre de usuario a buscar
     * @return un UsuarioDTO del usuario encontrado, o null si no lo encuentra
     */
    @Override
    public UsuarioDTO buscarUsuario(String username) {
        return usuarioToDTO(usuarios.get(username));
    }

    /**
     * Inscribe a un usuario en un evento
     *
     * @param uDTO el usuario al que se inscribirá en el evento
     * @param eDTO el evento en el que se inscribirá el usuario
     * @return true si se inscribe al usuario, false si entra en la lista de
     * espera
     */
    @Override
    public boolean inscribirse(UsuarioDTO uDTO, EventoDTO eDTO) {
        if (!this.isTokenValid(uDTO.getToken())) {
            return false;
        } else {
            boolean ret = false;

            Usuario u = usuarios.get(uDTO.getUsername());
            Evento e = eventos.get(eDTO.getNombre());
            if (!e.getAsistentes().contains(u)) { // Comprobamos que no esté el usuario ya inscrito previamente
                if (u.inscribirEnEvento(e)) {
                    ret = true;
                }
                // Si entra en la lista de espera, igualmente los incluímos en el mapa con las listas actualizadas
                usuarios.replace(uDTO.getUsername(), u);
                eventos.replace(eDTO.getNombre(), e);
            }

            return ret;
        }
    }

    /**
     * Desinscribe a un usuario de un evento
     *
     * @param uDTO el usuario al que se desinscribirá del evento
     * @param eDTO el evento del que se desinscribirá el usuario
     * @return true si se ha desinscrito correctamente, false si no
     */
    @Override
    public boolean desinscribirse(UsuarioDTO uDTO, EventoDTO eDTO) {
        if (!this.isTokenValid(uDTO.getToken())) {
            return false;
        } else {
            boolean ret = false;
            Usuario u = usuarios.get(uDTO.getUsername());
            Evento e = eventos.get(eDTO.getNombre());

            if (u.desinscribir(e) && e.desinscribir(u)) {
                ret = true;
            }

            return ret;
        }
    }

    /**
     * Busca los eventos en los que se ha inscrito el usuario
     *
     * @param uDTO usuario del que se comprobará el listado de eventos
     * @throws ListaEventosVacia Excepcion que se lanza si la lista de eventos esta vacia
     * @return una lista de EventoDTO con los eventos inscritos, o una vacía si
     * no encuentra ninguno
     */
    @Override
    public List<EventoDTO> buscarEventosInscritos(UsuarioDTO uDTO) throws ListaEventosVacia {
        if (!this.isTokenValid(uDTO.getToken())) {
            return new ArrayList();
        } else {
            List<EventoDTO> eventosInscritos = new ArrayList();
            try {
                for (Evento e : usuarios.get(uDTO.getUsername()).getEventos()) {
                    EventoDTO eDTO = this.buscarEventoPorNombre(e.getNombre());
                    eventosInscritos.add(eDTO);
                }
            } catch (NullPointerException e) {
                throw new ListaEventosVacia("The list of events is empty ", e);
            }
            return eventosInscritos;
        }
    }

    /**
     * Busca los eventos organizados por el usuario
     *
     * @param uDTO usuario del que se comprobará el listado de eventos
     * organizados
     * @throws ListaEventosVacia Excepcion que se lanza si la lista de eventos esta vacia
     * @return una lista de EventoDTO con los eventos organizados, o una vacía
     * si no encuentra ninguno
     */
    @Override
    public List<EventoDTO> buscarEventosOrganizados(UsuarioDTO uDTO) throws ListaEventosVacia {
        if (!this.isTokenValid(uDTO.getToken())) {
            return new ArrayList();
        } else {
            List<EventoDTO> eventosOrganizados = new ArrayList();
            try {
                for (Evento e : usuarios.get(uDTO.getUsername()).getOrganizados()) {
                    EventoDTO eDTO = this.buscarEventoPorNombre(e.getNombre());
                    eventosOrganizados.add(eDTO);
                }
            } catch (NullPointerException e) {
                throw new ListaEventosVacia("The list of events is empty ", e);
            }

            return eventosOrganizados;
        }
    }

    /**
     * Mapea un Usuario en un UsuarioDTO
     *
     * @param u el usuario a mapear
     * @return UsuarioDTO mapeado
     */
    public UsuarioDTO usuarioToDTO(Usuario u) {
        UsuarioDTO uDTO = new UsuarioDTO();
        uDTO.setUsername(u.getUsername());
        uDTO.setEmail(u.getEmail());

        List<String> e = new ArrayList();
        if (!u.getEventos().isEmpty()) {
            u.getEventos().forEach((evento) -> {
                e.add(evento.getNombre());
            });
            uDTO.setEventos(e);
        }

        if (!u.getOrganizados().isEmpty()) {
            List<String> o = new ArrayList();
            u.getOrganizados().forEach((organizado) -> {
                o.add(organizado.getNombre());
            });
            uDTO.setOrganizados(o);
        }

        if (!u.getListaEspera().isEmpty()) {
            List<String> l = new ArrayList();
            u.getListaEspera().forEach((listaEspera) -> {
                l.add(listaEspera.getNombre());
            });
            uDTO.setListaEspera(l);
        }

        return uDTO;
    }

    /**
     * Mapea un Evento en un EventoDTO
     *
     * @param e el evento a mapear
     * @return EventoDTO mapeado
     */
    public EventoDTO eventoToDTO(Evento e) {
        EventoDTO eDTO = new EventoDTO();
        eDTO.setNombre(e.getNombre());
        eDTO.setDescripcion(e.getDescripcion());
        eDTO.setFecha(e.getFecha());
        eDTO.setLocalizacion(e.getLocalizacion());
        eDTO.setCapacidad(e.getCapacidad());
        eDTO.setCancelado(e.isCancelado());

        if (!e.getAsistentes().isEmpty()) {
            List<String> asistentes = new ArrayList();
            e.getAsistentes().forEach((asistente) -> {
                asistentes.add(asistente.getUsername());
            });
            eDTO.setAsistentes(asistentes);
        } else {
            eDTO.setAsistentes(new ArrayList());
        }

        eDTO.setTipo(e.getTipo());
        eDTO.setOrganizador(e.getOrganizador().getUsername());

        return eDTO;
    }

    /*
     ***************************************************************************
     ***************************************************************************
     **************************** MODO DESARROLLADO ****************************
     ***************************************************************************
     ***************************************************************************
     */
    /**
     * Muestra los datos de todas las instancias en memoria con las que se
     * trabaja
     *
     * @param pass la contraseña necesaria para acceder al God Mode
     * @return true si el acceso es válido, false si no
     */
    @Override
    public boolean godMode(String pass) {
        boolean ret = true;

        if (!pass.equals("dae1819")) {
            ret = false;
        } else {
            for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
                if (entry != null) {
                    Usuario u = entry.getValue();
                    System.out.println("|-------------------------------------------------------------------------|");
                    System.out.println("[debug]- Username:\t" + u.getUsername());
                    System.out.println("[debug]- Email:\t\t" + u.getEmail());
                    System.out.println("[debug]- Contraseña:\t" + u.getPassword());
                    System.out.println("[debug]- Eventos Inscritos:\t" + u.getEventos().size());
                    List<Evento> leventos = u.getEventos();
                    for (Evento evento : leventos) {
                        System.out.println("\t|-----------------------------------------------------------------|");
                        System.out.println("\t[debug]- Nombre: \t\t" + evento.getNombre());
                        System.out.println("\t[debug]- Descripción: \t\t" + evento.getDescripcion());
                        System.out.println("\t[debug]- Fecha: \t\t" + evento.getFecha().toString());
                        System.out.println("\t[debug]- Tipo: \t\t\t" + evento.getTipo());
                        System.out.println("\t[debug]- Lugar: \t\t" + evento.getLocalizacion());
                        System.out.println("\t[debug]- Organizador:\t\t" + evento.getOrganizador().getUsername());
                        if ((evento.getCapacidad() - evento.getAsistentes().size()) > 0) {
                            System.out.println("\t[debug]- Plazas disponibles: \t" + (evento.getCapacidad() - evento.getAsistentes().size()) + "/" + evento.getCapacidad());
                        } else {
                            System.out.println("\t[debug]- Plazas disponibles:\t" + 0 + "/" + evento.getCapacidad());
                            System.out.println("\t[debug]- En lista de espera:\t" + (evento.getAsistentes().size() - evento.getCapacidad()));
                        }
                        System.out.println("\t[debug]- Usuarios inscritos:\t");
                        for (Usuario usuario : evento.getAsistentes()) {
                            System.out.println("\t\t[debug]- Username:\t " + usuario.getUsername());
                        }
                    }

                    System.out.println("[debug]- Eventos Organizados:\t" + u.getOrganizados().size());
                    List<Evento> eventosOrganizados = u.getOrganizados();
                    for (Evento evento : eventosOrganizados) {
                        System.out.println("\t|-----------------------------------------------------------------|");
                        System.out.println("\t[debug]- Nombre: \t\t" + evento.getNombre());
                        System.out.println("\t[debug]- Descripción: \t\t" + evento.getDescripcion());
                        System.out.println("\t[debug]- Fecha: \t\t" + evento.getFecha().toString());
                        System.out.println("\t[debug]- Tipo: \t\t\t" + evento.getTipo());
                        System.out.println("\t[debug]- Lugar: \t\t" + evento.getLocalizacion());
                        System.out.println("\t[debug]- Plazas máximas: \t" + evento.getCapacidad());
                        System.out.println("\t[debug]- Organizador:\t\t" + evento.getOrganizador().getUsername());
                        if ((evento.getCapacidad() - evento.getAsistentes().size()) > 0) {
                            System.out.println("\t[debug]- Plazas disponibles:\t" + (evento.getCapacidad() - evento.getAsistentes().size()));
                        } else {
                            System.out.println("\t[debug]- Plazas disponibles:\t" + 0);
                            System.out.println("\t[debug]- En lista de espera:\t" + (evento.getAsistentes().size() - evento.getCapacidad()));
                        }
                        System.out.println("\t[debug]- Usuarios inscritos:\t");
                        for (Usuario usuario : evento.getAsistentes()) {
                            System.out.println("\t\t[debug]- Username:\t " + usuario.getUsername());
                        }
                    }
                }
            }
        }

        return ret;
    }

}
