package ar.com.tubarberia;

import ar.com.tubarberia.entidades.Usuario;
import ar.com.tubarberia.enumeraciones.Rol;
import ar.com.tubarberia.repositorios.UsuarioRepositorio;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EntityScan(basePackages = {"ar.com.tubarberia.entidades"})
class TubarberiaApplicationTests {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Test
    void testFindByEmail() {
        // Crear y guardar un usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan.perez@example.com");
        usuario.setTelefono("123456789");
        usuario.setDireccion("Calle Falsa 123");
        usuario.setPassword("password123");
        usuario.setRol(Rol.CLIENTE);
        usuario.setEstado(true);

        usuarioRepositorio.save(usuario);

        // Buscar el usuario por email
        Usuario usuarioEncontrado = usuarioRepositorio.buscarPorEmail("juan.perez@example.com");

        // Verificar que el usuario fue encontrado
        assertNotNull(usuarioEncontrado);
        assertEquals("Juan Pérez", usuarioEncontrado.getNombre());
        assertEquals("juan.perez@example.com", usuarioEncontrado.getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        // Intentar buscar un usuario que no existe
        Usuario usuarioNoEncontrado = usuarioRepositorio.buscarPorEmail("no.existe@example.com");

        // Verificar que no se encontró ningún usuario
        assertNull(usuarioNoEncontrado);
    }
}
