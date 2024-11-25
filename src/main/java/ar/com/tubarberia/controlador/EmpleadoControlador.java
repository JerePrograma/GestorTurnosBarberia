package ar.com.tubarberia.controlador;

import ar.com.tubarberia.entidades.Comercio;
import ar.com.tubarberia.entidades.Usuario;
import ar.com.tubarberia.excepciones.MiExcepcion;
import ar.com.tubarberia.servicios.ComercioServicio;
import ar.com.tubarberia.servicios.EmpleadoServicio;
import ar.com.tubarberia.servicios.UsuarioServicio;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Date;

@Controller
@RequestMapping("/empleados")
public class EmpleadoControlador {

    private final UsuarioServicio usuarioServicio;

    private final EmpleadoServicio empleadoServicio;

    private final ComercioServicio comercioServicio;

    public EmpleadoControlador(UsuarioServicio usuarioServicio, EmpleadoServicio empleadoServicio, ComercioServicio comercioServicio) {
        this.usuarioServicio = usuarioServicio;
        this.empleadoServicio = empleadoServicio;
        this.comercioServicio = comercioServicio;
    }

    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        // Obtener lista de comercios para el select
        List<Comercio> listaComercios = comercioServicio.listarComercios();
        model.addAttribute("listaComercios", listaComercios);
        return "empleado-form";
    }

    @PostMapping("/registro")
    public String registrarEmpleado(
            @RequestParam("nombre") String nombre,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("password2") String password2,
            @RequestParam("telefono") String telefono,
            @RequestParam("direccion") String direccion,
            @RequestParam("puesto") String puesto,
            @RequestParam("comercioId") Long comercioId,
            @RequestParam("fechaContratacion") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaContratacion,
            Model model) {

        try {
            empleadoServicio.crearEmpleado(nombre, email, password, password2, telefono, direccion, puesto, comercioId, fechaContratacion);
            return "redirect:/empleados/lista"; // Redirige a la lista de empleados después del registro exitoso
        } catch (MiExcepcion e) {
            model.addAttribute("error", e.getMessage());
            // Volver a cargar la lista de comercios en caso de error
            List<Comercio> listaComercios = comercioServicio.listarComercios();
            model.addAttribute("listaComercios", listaComercios);
            return "empleado-form";
        }
    }

    // Método para manejar la modificación del usuario (registro o actualización)
    @PostMapping("/modificar")
    public String modificarUsuario(@ModelAttribute Usuario usuario, /*@RequestParam("archivo") MultipartFile archivo,*/ Model model) {
        try {
            // Lógica para guardar o actualizar el usuario (ej. guardar foto, validar datos)
     //       usuarioServicio.modificarEmpleado(usuario, archivo);
            model.addAttribute("exito", "Usuario registrado correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Hubo un error al registrar el usuario: " + e.getMessage());
        }
        return "modificar"; // Redirigir a la misma página con el resultado
    }

    // Método para manejar la acción de activar o desactivar un empleado
    @GetMapping("/estado")
    public String cambiarEstado(@RequestParam("id") Long id, Model model) {
        try {
            usuarioServicio.cambiarEstadoEmpleado(id);  // Llamada a la lógica para cambiar el estado
            model.addAttribute("exito", "Estado del empleado actualizado correctamente");
        } catch (MiExcepcion e) {
            model.addAttribute("error", "Error al cambiar el estado del usuario: " + e.getMessage());
        }
        return "redirect:/empleados/registrar";  // Redirigir al formulario de registro con el estado actualizado
    }

}
