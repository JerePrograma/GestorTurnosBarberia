package ar.com.tubarberia.controlador;

import ar.com.tubarberia.entidades.Comercio;
import ar.com.tubarberia.excepciones.MiExcepcion;
import ar.com.tubarberia.servicios.ComercioServicio;
import ar.com.tubarberia.servicios.EmpleadoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;

@Controller
@RequestMapping("/empleados")
public class EmpleadoControlador {

    @Autowired
    private EmpleadoServicio empleadoServicio;

    @Autowired
    private ComercioServicio comercioServicio;

    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        // Obtener lista de comercios para el select
        List<Comercio> listaComercios = comercioServicio.listarComercios();
        model.addAttribute("listaComercios", listaComercios);
        return "empleado-form";
    }

    @PostMapping("/registrar")
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

    // Otros métodos, como listar empleados, etc.
}
