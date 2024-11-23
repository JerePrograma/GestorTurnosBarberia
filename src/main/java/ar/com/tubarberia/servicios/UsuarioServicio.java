package ar.com.tubarberia.servicios;

import ar.com.tubarberia.entidades.Comercio;
import ar.com.tubarberia.entidades.Empleado;
import ar.com.tubarberia.entidades.Usuario;
import ar.com.tubarberia.enumeraciones.Rol;
import ar.com.tubarberia.excepciones.MiExcepcion;
import ar.com.tubarberia.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
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

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Service
public class UsuarioServicio implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);

    private final UsuarioRepositorio usuarioRepositorio;
    private final ImagenServicio imagenServicio;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, ImagenServicio imagenServicio, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.imagenServicio = imagenServicio;
        this.passwordEncoder = passwordEncoder;
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


        usuario.setActivo(true);

        usuarioRepositorio.save(usuario);
    }
    // Crear Empleados//
    @Transactional
    public Empleado crearEmpleado(String nombre, String email, String password, String password2, String telefono,
                              String direccion, String puesto, Long comercioId, Date fechaContratacion) throws MiExcepcion {

        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setEmail(email);
        empleado.setPassword(passwordEncoder.encode(password));
        empleado.setTelefono(telefono);
        empleado.setDireccion(direccion);
        empleado.setPuesto(puesto);
        empleado.setFechaContratacion(fechaContratacion);
        empleado.setRol(Rol.EMPLEADO);
        empleado.setActivo(true);

        return empleado;
    }

    @Transactional
    public Comercio crearComercio(String CUIT, String nombre, String localidad, String barrio, LocalTime horarioApertura,
                                  LocalTime horarioCierre, Set<DayOfWeek> diasAbiertos, String descripcion,
                                  String rangoPrecios, List<String> especialidades, String sitioWeb,
                                  Map<String, String> redesSociales, String politicasCancelacion, String email, String password, String telefono,
                                  String direccion) throws MiExcepcion {

        Comercio comercio = new Comercio();
        comercio.setCUIT(CUIT);
        comercio.setNombre(nombre);
        comercio.setEmail(email);
        comercio.setPassword(passwordEncoder.encode(password));
        comercio.setRol(Rol.COMERCIO);
        comercio.setTelefono(telefono);
        comercio.setDireccion(direccion);
        comercio.setLocalidad(localidad);
        comercio.setBarrio(barrio);
        comercio.setHorarioApertura(horarioApertura);
        comercio.setHorarioCierre(horarioCierre);
        comercio.setDiasAbiertos(diasAbiertos);
        comercio.setDescripcion(descripcion);
        comercio.setRangoPrecios(rangoPrecios);
        comercio.setEspecialidades(especialidades);
        comercio.setSitioWeb(sitioWeb);
        comercio.setRedesSociales(redesSociales);
        comercio.setPoliticasCancelacion(politicasCancelacion);
        comercio.setCalificacionPromedio(0.0); // Inicialmente en 0
        return comercio;
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
