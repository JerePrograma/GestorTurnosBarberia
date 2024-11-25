package ar.com.tubarberia.controlador;

import ar.com.tubarberia.excepciones.MiExcepcion;
import ar.com.tubarberia.servicios.ComercioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/comercios")
public class ComercioControlador {

    @Autowired
    private ComercioServicio comercioServicio;

    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("diasSemana", DayOfWeek.values());
        return "comercio-form";
    }

    @PostMapping("/registrar")
    public String registrarComercio(
            @RequestParam("CUIT") String CUIT,
            @RequestParam("direccion") String direccion,
            @RequestParam("telefono") String telefono,
            @RequestParam("email") String email,
            @RequestParam("nombre") String nombre,
            @RequestParam("localidad") String localidad,
            @RequestParam("barrio") String barrio,
            @RequestParam("horarioApertura") @DateTimeFormat(pattern = "HH:mm") LocalTime horarioApertura,
            @RequestParam("horarioCierre") @DateTimeFormat(pattern = "HH:mm") LocalTime horarioCierre,
            @RequestParam("diasAbiertos") List<DayOfWeek> diasAbiertos,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("rangoPrecios") String rangoPrecios,
            @RequestParam("especialidades") List<String> especialidades,
            @RequestParam("sitioWeb") String sitioWeb,
            @RequestParam Map<String, String> redesSociales, // Asume que los campos de redes sociales tienen nombres consistentes
            @RequestParam("politicasCancelacion") String politicasCancelacion,
            Model model) {

        try {
            // Procesar las redes sociales
            Map<String, String> redes = new HashMap<>();
            redes.put("facebook", redesSociales.get("facebook"));
            redes.put("instagram", redesSociales.get("instagram"));
            // Agrega más redes según tus necesidades

            Set<DayOfWeek> diasAbiertosSet = new HashSet<>(diasAbiertos);

            comercioServicio.crearComercio(CUIT, nombre, localidad, barrio, horarioApertura,
                    horarioCierre, diasAbiertosSet, descripcion,
                    rangoPrecios, especialidades, sitioWeb,
                    redesSociales, politicasCancelacion, email, telefono,
                    direccion);

            return "redirect:/comercios/lista"; // Redirige a la lista de comercios o a donde desees
        } catch (MiExcepcion e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("diasSemana", DayOfWeek.values());
            return "comercio-form";
        }
    }

    // Otros métodos, como listar comercios, etc.
}
