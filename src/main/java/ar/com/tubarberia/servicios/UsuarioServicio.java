package ar.com.tubarberia.servicios;

import ar.com.tubarberia.entidades.Empleado;
import ar.com.tubarberia.entidades.Usuario;
import ar.com.tubarberia.enumeraciones.Rol;
import ar.com.tubarberia.excepciones.MiExcepcion;
import ar.com.tubarberia.repositorios.EmpleadoRepositorio;
import ar.com.tubarberia.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Service
public class UsuarioServicio implements UserDetailsService {
    static {
        LoggerFactory.getLogger(UsuarioServicio.class);
    }

    private final UsuarioRepositorio usuarioRepositorio;
    //   private final ImagenServicio imagenServicio;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmpleadoRepositorio empleadoRepositorio;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio/*, ImagenServicio imagenServicio*/, BCryptPasswordEncoder passwordEncoder, EmpleadoRepositorio empleadoRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        //     this.imagenServicio = imagenServicio;
        this.passwordEncoder = passwordEncoder;
        this.empleadoRepositorio = empleadoRepositorio;
    }


//    @Autowired
//    private CalificacionRepositorio calificacionRepositorio;

    // Crear Usuarios//
    @Transactional
    public void crearUsuario(String nombre,
                             String direccion,
                             String telefono,
                             String email,
                             String password,
                             String password2
            /*, MultipartFile archivo*/) throws MiExcepcion {

        validarUsuario(nombre, direccion, telefono, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setDireccion(direccion);

        usuario.setPassword(passwordEncoder.encode(password));

        usuario.setRol(Rol.CLIENTE);

//       if (archivo != null && !archivo.isEmpty()) {
//            try {
//                Imagen imagen = imagenServicio.guardarImagen(archivo);
//                usuario.setImagen(imagen);
//            } catch (MiExcepcion e) {
//                throw new MiExcepcion("Error al guardar la imagen: " + e.getMessage());
//            }
//        } else {
//            throw new MiExcepcion("El archivo no puede estar nulo o vacío");
//        }


        usuario.setEstado(true);

        usuarioRepositorio.save(usuario);
    }

    // Crear Empleados//
    @Transactional
    public Empleado crearEmpleado(String nombre, String email, String password, String password2, String telefono,
                                  String direccion, String puesto, /*Long comercioId, */Date fechaContratacion) throws MiExcepcion {
        validarUsuario(nombre, direccion, telefono, email, password, password2);
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setEmail(email);
        empleado.setPassword(passwordEncoder.encode(password));
        empleado.setTelefono(telefono);
        empleado.setDireccion(direccion);
        empleado.setPuesto(puesto);
        empleado.setFechaContratacion(fechaContratacion);
        empleado.setRol(Rol.EMPLEADO);
        empleado.setEstado(true);

        return empleado;
    }

    @Transactional
    public void modificarUsuario(Long id,
                                 String nombre,
                                 String direccion,
                                 String telefono,
                                 String email,
                                 String password,
                                 String password2
            /*, MultipartFile archivo*/) throws MiExcepcion {

        validarUsuario(nombre, direccion, telefono, email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setTelefono(telefono);
            usuario.setEmail(email);
            usuario.setDireccion(direccion);

            usuario.setPassword(passwordEncoder.encode(password));

            // String idImagen = null;

            // if(usuario.getImagen() != null){
            // idImagen = usuario.getImagen().getId();

            // }

            // Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);

            // usuario.setImagen(imagen);

            usuarioRepositorio.save(usuario);
        }
    }

    @Transactional
    public void modificarEmpleado(Long id, String nombre, String email, String password, String password2, String telefono,
                                  String direccion, String puesto,/* Long comercioId,*/ Date fechaContratacion
            /*, MultipartFile archivo*/) throws MiExcepcion {
        modificarUsuario(id, nombre, direccion, telefono, email, password, password2);

        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Empleado empleado = respuesta.get();

            empleado.setTelefono(telefono);
            empleado.setDireccion(direccion);
            empleado.setPuesto(puesto);
            empleado.setFechaContratacion(fechaContratacion);
            empleado.setRol(Rol.EMPLEADO);
            empleado.setEstado(true);

            // String idImagen = null;

            // if(empleado.getImagen() != null){
            // idImagen = empleado.getImagen().getId();

            // }

            // Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);

            // empleado.setImagen(imagen);

            empleadoRepositorio.save(empleado);
        }
    }

//    @Transactional
//    public void actualizarImagenUsuario(String usuarioId, MultipartFile archivo) throws MiExcepcion {
//
//        Optional<Usuario> respuesta = usuarioRepositorio.findById(usuarioId);
//        if (respuesta.isPresent()) {
//            Usuario usuario = respuesta.get();
//            String idImagen = null;
//
//            if (usuario.getImagen() != null) {
//                idImagen = usuario.getImagen().getId();
//                Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);
//                usuario.setImagen(imagen);
//            } else {
//                Imagen imagen = imagenServicios.guardarImagen(archivo);
//                usuario.setImagen(imagen);
//            }
//
//            usuarioRepositorio.save(usuario);
//        }
//
//    }

    // Listar Usuarios//
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Empleado> listarEmpleados() {
        return empleadoRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarUsuario(Long id) throws MiExcepcion {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        if (usuario.isEmpty()) {
            throw new MiExcepcion("No existe el usuario");
        }
        return usuario.get();
    }

    // Activar o Desactivar Usuario//
    @Transactional
    public void cambiarEstadoUsuario(Long id) throws MiExcepcion {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isEmpty()) {
            throw new MiExcepcion("No existe el usuario");
        }
        Usuario usuario = respuesta.get();
        usuario.setEstado(!usuario.getEstado());
        usuarioRepositorio.save(usuario);
    }

    // Activar o Desactivar Empleado//
    @Transactional
    public void cambiarEstadoEmpleado(Long id) throws MiExcepcion {
        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
        if (respuesta.isEmpty()) {
            throw new MiExcepcion("No existe el empleado");
        }
        Empleado empleado = respuesta.get();
        empleado.setEstado(!empleado.getEstado());
        empleadoRepositorio.save(empleado);
    }

    //Buscar usuario en base de datos para validar por mail que no exista y crear usuario
    public boolean existeUsuarioPorEmail(String email) {
        return usuarioRepositorio.existsByEmail(email);
    }

    //Buscar usuario en base de datos para validar por dni que no exista y crear usuario
    public boolean existeUsuarioPorDni(Integer dni) {
        return usuarioRepositorio.existsByDni(dni);
    }

    public void validarUsuario(String nombre,
                               String direccion,
                               String telefono,
                               String email,
                               String password,
                               String password2)
            throws MiExcepcion {

        if (nombre.isEmpty()) {
            throw new MiExcepcion("El nombre no puede estar vacio");
        }

        if (direccion.isEmpty()) {
            throw new MiExcepcion("La direccion no puede estar vacia");
        }

        if (telefono.isEmpty()) {
            throw new MiExcepcion("El telefono no puede estar vacio");
        }

        if (email.isEmpty()) {
            throw new MiExcepcion("El email no puede estar vacio");
        }

        if (password.isEmpty()) {
            throw new MiExcepcion("La contraseña no puede estar vacia");
        }

        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas no coinciden");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String emailNormalizado = email.trim().toLowerCase();
        Usuario usuario = usuarioRepositorio.buscarPorEmail(emailNormalizado);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            // Si necesitas almacenar el usuario en la sesión
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }

}
