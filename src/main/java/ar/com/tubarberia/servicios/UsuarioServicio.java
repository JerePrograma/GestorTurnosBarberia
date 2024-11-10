package ar.com.tubarberia.servicios;

import ar.com.tubarberia.entidades.Imagen;
import ar.com.tubarberia.entidades.Usuario;
import ar.com.tubarberia.enumeraciones.Rol;
import ar.com.tubarberia.excepciones.MiExcepcion;
import ar.com.tubarberia.repositorios.UsuarioRepositorio;
import jakarta.persistence.OneToOne;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio {
    private final UsuarioRepositorio usuarioRepositorio;
    private final ImagenServicio imagenServicio;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, ImagenServicio imagenServicio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.imagenServicio = imagenServicio;
    }

//    @Autowired
//    private CalificacionRepositorio calificacionRepositorio;

    @OneToOne
    private Imagen imagen;
    // Crear Usuarios//
    @Transactional
    public void crearUsuario(String nombre,
                              String direccion,
                              String telefono,
                              String email,
                              String password,
                              String password2
                              /*MultipartFile archivo*/) throws MiExcepcion {

        validarUsuario(nombre, direccion, telefono, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setDireccion(direccion);

        usuario.setPassword(new BCryptPasswordEncoder().encode(password));

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
}
