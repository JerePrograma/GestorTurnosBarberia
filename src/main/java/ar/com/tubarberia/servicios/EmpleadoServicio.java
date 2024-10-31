package ar.com.tubarberia.servicios;

import ar.com.tubarberia.entidades.Empleado;
import ar.com.tubarberia.excepciones.MiExcepcion;
import ar.com.tubarberia.repositorios.EmpleadoRepositorio;
import ar.com.tubarberia.repositorios.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class EmpleadoServicio extends UsuarioServicio {

    private final EmpleadoRepositorio empleadoRepositorio;

    public EmpleadoServicio(EmpleadoRepositorio empleadoRepositorio, ImagenServicio imagenServicio, UsuarioRepositorio usuarioRepositorio) {
        super(usuarioRepositorio, imagenServicio);
        this.empleadoRepositorio = empleadoRepositorio;
    }
/*
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private CalificacionRepositorio calificacionRepositorio;
*/

    // Crear Empleados//
    public void crearEmpleado(String nombre,
                              String direccion,
                              String telefono,
                              String email,
                              String password,
                              String password2,
                              MultipartFile archivo,
                              String puesto) throws MiExcepcion {
        validarUsuario(nombre, direccion, telefono, email, password, password2);
        //        validarEmpleado(puesto);

        Empleado empleado = new Empleado();

        empleado.setPuesto(puesto);

        empleado.setFechaContratacion(new Date());

//      empleado.setSalario(salario);

//      empleado.setRol(Rol.Empleado);

        empleado.setActivo(true);

        empleadoRepositorio.save(empleado);
    }

    // Modificar Empleado//

//    @Transactional
//    public void modificarEmpleado(String nombre,
//                                  String apellido,
//                                  String localidad,
//                                  String direccion,
//                                  String barrio,
//                                  String telefono,
//                                  String email,
//                                  String puesto,
//                                  String password,
//                                  String password2,
//            /*MultipartFile archivo,*/
//                                  int id) throws MiExcepcion {
//
//        validarEmpleado(nombre, apellido, localidad, direccion, barrio, telefono, email, password, password2);
//
//        Optional<Empleado> respuesta = empleadoRepositorio.findById(id);
//
//        if (respuesta.isPresent()) {
//
//            Empleado empleado = respuesta.get();
//
//            empleado.setNombre(nombre);
//            empleado.setApellido(apellido);
//            empleado.setTelefono(telefono);
//            empleado.setEmail(email);
//            empleado.setPuesto(puesto);
//            empleado.setDireccion(direccion);
////        empleado.setSalario(salario);
//
//            empleado.setFechaContratacion(new Date());
////            empleado.setPassword(new BCryptPasswordEncoder().encode(password));
//
//            // String idImagen = null;
//
//            // if(empleado.getImagen() != null){
//            // idImagen = empleado.getImagen().getId();
//
//            // }
//
//            // Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);
//
//            // empleado.setImagen(imagen);
//
//            empleadoRepositorio.save(empleado);
//        }
//    }

    /*// obtener promedio
    public String obtenerPromedioCalificaciones(Empleado proveedor) throws MiExcepcion {
        List<Calificacion> calificaciones = calificacionRepositorio.buscarCalificacionesPorProveedor(proveedor);
        String mostrarPromedio;
        if (calificaciones.isEmpty()) {
            mostrarPromedio = "El profesional aun no ha recibido calificaciones";
        } else {
            double sumaPuntajes = calificaciones.stream()
                    .mapToInt(Calificacion::getPuntaje)
                    .sum();

            double apromediar = sumaPuntajes / calificaciones.size();

            Integer promedio = (int) Math.round(apromediar);
            mostrarPromedio = promedio.toString();
        }
        return mostrarPromedio;

    }

    // Contar calificaciones
    public int contarCalificaciones(Empleado proveedor) {
        Integer cantidadCalificaciones = calificacionRepositorio.buscarCalificacionesPorProveedor(proveedor).size();
        return cantidadCalificaciones;
    }

    @Transactional
    public void actualizarImagenEmpleado(String EmpleadoId, MultipartFile archivo) throws MiExcepcion {

        Optional<Empleado> respuesta = EmpleadoRepositorio.findById(EmpleadoId);
        if (respuesta.isPresent()) {
            Empleado Empleado = respuesta.get();
            String idImagen = null;

            if (Empleado.getImagen() != null) {
                idImagen = Empleado.getImagen().getId();
                Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);
                Empleado.setImagen(imagen);
            } else {
                Imagen imagen = imagenServicios.guardarImagen(archivo);
                Empleado.setImagen(imagen);
            }

            EmpleadoRepositorio.save(Empleado);
        }

    }

    // Listar Empleados//
    @Transactional(readOnly = true)
    public List<Empleado> listarTodos() {
        List<Empleado> Empleados = EmpleadoRepositorio.findAll();
        return Empleados;
    }

    @Transactional(readOnly = true)
    public List<Empleado> listarEmpleados() {
        List<Empleado> Empleados = EmpleadoRepositorio.buscarPorRol(Rol.Empleado);
        return Empleados;
    }

    @Transactional(readOnly = true)
    public List<Empleado> listarProveedores() {
        List<Empleado> proveedores = EmpleadoRepositorio.buscarPorRol(Rol.PROVEEDOR);
        return proveedores;
    }

    @Transactional(readOnly = true)
    public List<Empleado> listarEmpleadoProveedores() {
        List<Empleado> Empleadoproveedores = EmpleadoRepositorio.buscarPorRol(Rol.EmpleadoPROVEEDOR);
        return Empleadoproveedores;
    }

    @Transactional(readOnly = true)
    public List<Empleado> listarEmpleadosProveedores() {
        List<Empleado> EmpleadosProveedores = EmpleadoRepositorio.buscarPorRol(Rol.EmpleadoPROVEEDOR);
        return EmpleadosProveedores;
    }

    @Transactional(readOnly = true)
    public List<Empleado> listarPorServicio(String servicio) {

        List<Empleado> proveedores = EmpleadoRepositorio.buscarProveedoresPorIdServicio(servicio);

        return proveedores;

    }

    // Buscar Empleado//

    @Transactional(readOnly = true)
    public Empleado buscarEmpleado(String id) throws MiExcepcion {
        Optional<Empleado> Empleado = EmpleadoRepositorio.findById(id);
        if (Empleado.isEmpty()) {
            throw new MiExcepcion("No existe el Empleado");
        }
        return Empleado.get();
    }

    // Activar o Desactivar Empleado//
    @Transactional
    public void desactivarEmpleado(String id) throws MiExcepcion {
        Optional<Empleado> Empleado = EmpleadoRepositorio.findById(id);
        if (Empleado.isEmpty()) {
            throw new MiExcepcion("No existe el Empleado");
        }
        Empleado.get().setEstado(false);
        EmpleadoRepositorio.save(Empleado.get());
    }

    @Transactional
    public void activarEmpleado(String id) throws MiExcepcion {
        Optional<Empleado> Empleado = EmpleadoRepositorio.findById(id);
        if (Empleado.isEmpty()) {
            throw new MiExcepcion("No existe el Empleado");
        }
        Empleado.get().setEstado(true);
        EmpleadoRepositorio.save(Empleado.get());
    }
*/
    public void validarEmpleado(

    )
            throws MiExcepcion {

    }
}
