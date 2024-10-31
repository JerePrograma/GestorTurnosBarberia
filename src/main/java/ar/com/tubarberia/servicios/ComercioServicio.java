package ar.com.tubarberia.servicios;

import ar.com.tubarberia.entidades.Comercio;
import ar.com.tubarberia.excepciones.MiExcepcion;
import ar.com.tubarberia.repositorios.ComercioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ComercioServicio {

    private final ComercioRepositorio comercioRepositorio;

    public ComercioServicio(ComercioRepositorio comercioRepositorio) {
        this.comercioRepositorio = comercioRepositorio;
    }
/*
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private CalificacionRepositorio calificacionRepositorio;
*/

    // Crear comercios y Proveedores//
    @Transactional
    public void crearcomercio(String nombre,
                              String CUIT,
                              String localidad,
                              String direccion,
                              String barrio,
                              String telefono,
                              String email,
                              String password,
                              String password2/*,
                              MultipartFile archivo*/) throws MiExcepcion {

        validarcomercio(nombre, CUIT, localidad, direccion, barrio, telefono, email, password, password2);

        Comercio comercio = new Comercio();

        comercio.setNombre(nombre);

        comercio.setCUIT(CUIT);

        comercio.setLocalidad(localidad);

        comercio.setDireccion(direccion);

        comercio.setTelefono(telefono);

        comercio.setEmail(email);

/*        comercio.setPassword(new BCryptPasswordEncoder().encode(password));

        comercio.setRol(Rol.comercio);*/

/*        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen imagen = imagenServicios.guardarImagen(archivo);
                comercio.setImagen(imagen);
            } catch (MiExcepcion e) {
                throw new MiExcepcion("Error al guardar la imagen: " + e.getMessage());
            }
        } else {
            throw new MiExcepcion("El archivo no puede estar nulo o vacío");
        }*/


        comercio.setActivo(true);

        comercioRepositorio.save(comercio);
    }

    // Modificar comercio//

    @Transactional
    public void modificarcomercio(String nombre, String CUIT,
                                  String localidad, String direccion,
                                  String barrio, String telefono, String email,
                                  String password, String password2,
                                  /*MultipartFile archivo,*/
                                  Long id) throws MiExcepcion {

        validarcomercio(nombre, CUIT, localidad, direccion, barrio, telefono, email, password, password2);

        Optional<Comercio> respuesta = comercioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Comercio comercio = respuesta.get();

            comercio.setNombre(nombre);

            comercio.setCUIT(CUIT);

            comercio.setLocalidad(localidad);

            comercio.setDireccion(direccion);

            comercio.setTelefono(telefono);

            comercio.setEmail(email);
//            comercio.setPassword(new BCryptPasswordEncoder().encode(password));

            // String idImagen = null;

            // if(comercio.getImagen() != null){
            // idImagen = comercio.getImagen().getId();

            // }

            // Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);

            // comercio.setImagen(imagen);

            comercioRepositorio.save(comercio);
        }
    }

    /*// obtener promedio
    public String obtenerPromedioCalificaciones(Comercio proveedor) throws MiExcepcion {
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
    public int contarCalificaciones(Comercio proveedor) {
        Integer cantidadCalificaciones = calificacionRepositorio.buscarCalificacionesPorProveedor(proveedor).size();
        return cantidadCalificaciones;
    }

    @Transactional
    public void actualizarImagenComercio(String ComercioId, MultipartFile archivo) throws MiExcepcion {

        Optional<Comercio> respuesta = ComercioRepositorio.findById(ComercioId);
        if (respuesta.isPresent()) {
            Comercio Comercio = respuesta.get();
            String idImagen = null;

            if (Comercio.getImagen() != null) {
                idImagen = Comercio.getImagen().getId();
                Imagen imagen = imagenServicios.actualizarImagen(archivo, idImagen);
                Comercio.setImagen(imagen);
            } else {
                Imagen imagen = imagenServicios.guardarImagen(archivo);
                Comercio.setImagen(imagen);
            }

            ComercioRepositorio.save(Comercio);
        }

    }

    // Listar Comercios//
    @Transactional(readOnly = true)
    public List<Comercio> listarTodos() {
        List<Comercio> Comercios = ComercioRepositorio.findAll();
        return Comercios;
    }

    @Transactional(readOnly = true)
    public List<Comercio> listarcomercios() {
        List<Comercio> comercios = ComercioRepositorio.buscarPorRol(Rol.comercio);
        return comercios;
    }

    @Transactional(readOnly = true)
    public List<Comercio> listarProveedores() {
        List<Comercio> proveedores = ComercioRepositorio.buscarPorRol(Rol.PROVEEDOR);
        return proveedores;
    }

    @Transactional(readOnly = true)
    public List<Comercio> listarcomercioProveedores() {
        List<Comercio> comercioproveedores = ComercioRepositorio.buscarPorRol(Rol.comercioPROVEEDOR);
        return comercioproveedores;
    }

    @Transactional(readOnly = true)
    public List<Comercio> listarcomerciosProveedores() {
        List<Comercio> comerciosProveedores = ComercioRepositorio.buscarPorRol(Rol.comercioPROVEEDOR);
        return comerciosProveedores;
    }

    @Transactional(readOnly = true)
    public List<Comercio> listarPorServicio(String servicio) {

        List<Comercio> proveedores = ComercioRepositorio.buscarProveedoresPorIdServicio(servicio);

        return proveedores;

    }

    // Buscar Comercio//

    @Transactional(readOnly = true)
    public Comercio buscarComercio(String id) throws MiExcepcion {
        Optional<Comercio> Comercio = ComercioRepositorio.findById(id);
        if (Comercio.isEmpty()) {
            throw new MiExcepcion("No existe el Comercio");
        }
        return Comercio.get();
    }

    // Activar o Desactivar Comercio//
    @Transactional
    public void desactivarComercio(String id) throws MiExcepcion {
        Optional<Comercio> Comercio = ComercioRepositorio.findById(id);
        if (Comercio.isEmpty()) {
            throw new MiExcepcion("No existe el Comercio");
        }
        Comercio.get().setEstado(false);
        ComercioRepositorio.save(Comercio.get());
    }

    @Transactional
    public void activarComercio(String id) throws MiExcepcion {
        Optional<Comercio> Comercio = ComercioRepositorio.findById(id);
        if (Comercio.isEmpty()) {
            throw new MiExcepcion("No existe el Comercio");
        }
        Comercio.get().setEstado(true);
        ComercioRepositorio.save(Comercio.get());
    }
*/
    public void validarcomercio(String nombre,
                                String CUIT,
                                String localidad,
                                String direccion,
                                String barrio,
                                String telefono,
                                String email,
                                String password,
                                String password2)
            throws MiExcepcion {

        if (nombre.isEmpty()) {
            throw new MiExcepcion("El nombre no puede estar vacio");
        }

        if (CUIT == null) {
            throw new MiExcepcion("El CUIT no puede estar vacio");
        }

        if (localidad.isEmpty()) {
            throw new MiExcepcion("La localidad no puede estar vacia");
        }

        if (direccion.isEmpty()) {
            throw new MiExcepcion("La direccion no puede estar vacia");
        }

        if (barrio.isEmpty()) {
            throw new MiExcepcion("El barrio no puede estar vacio");
        }
    }

    /* @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Comercio comercio = ComercioRepositorio.buscarPorEmail(email);

        if (Comercio != null) {

            @SuppressWarnings({"rawtypes", "unchecked"})
            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + Comercio.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("Comerciosession", Comercio);

            return new User(Comercio.getEmail(), Comercio.getPassword(), permisos);

        } else {
            return null;
        }
    }*/

    // Metodos para manejar la logica de filtrado y ordenamiento:

    /*
     * Corregir todo esto porque no funciona
     * public List<Comercio> obtenerListaProveedoresPorIdServicios(String id) {
     * return ComercioRepositorio.buscarProveedorPorIdServicio(id);//
     * }
     *
     * @Autowired
     * private CalificacionRepositorio calificacionRepositorio;
     *
     * //Metodo para obtener el promedio de las calificaciones
     * public Double obtenerPromedioCalificacion(Comercio proveedor) {
     * List<Calificacion> calificaciones =
     * calificacionRepositorio.buscarCalificacionesPorProveedoredor(proveedor);
     * if (calificaciones.isEmpty()) {
     * return 0.0;
     * }
     * int sumaPuntaje = 0;
     * for (Calificacion calificacion : calificaciones) {
     * sumaPuntaje += calificacion.getPuntaje();
     * }
     * return (double) sumaPuntaje / calificaciones.size();}
     *
     * // ordenamiento:
     * public List<Comercio> obtenerProveedorPorFiltro(String id, String orden ){
     * List<Comercio> proveedores =
     * ComercioRepositorio.buscarProveedorPorIdServicio(id);
     * switch (orden.toLowerCase()) {
     *
     * case "nombre": //caso 1 filtrar por nombre de forma descendente y entregar
     * una lista
     * return
     * proveedores.stream().sorted(Comparator.comparing(Comercio::getNombre).reversed
     * ()).collect(Collectors.toList());
     *
     *
     * case "calificacion"://caso 2 filtrar por el promedio obtenido de un metodo
     * que dispara el resultado
     * // entre la suma de todas las calificaciones de cada provedor entre la
     * cantidad de calidicaciones,
     * //entrega una lista
     * return proveedores.stream().sorted(Comparator.comparingDouble(this::
     * obtenerPromedioCalificacion).reversed()).collect(Collectors.toList());
     *
     *
     *
     * case "Expeciencia": //caso 3 filtrar por los años de experiencia y devuelve
     * una lista de forma descendente
     * return proveedores.stream().sorted(Comparator.comparing(Comercio ::
     * getExperiencia).reversed()).collect(Collectors.toList());
     *
     *
     * default:
     * return proveedores;
     * }
     *
     * }
     */

/*    public Comercio buscarPorEmail(String email) {
        return comercioRepositorio.buscarPorEmail(email);
    }*/


/*    @Transactional
    public void convertircomercioAAdmin(String id) throws MiExcepcion {
        Optional<Comercio> ComercioOptional = ComercioRepositorio.findById(id);
        if (ComercioOptional.isPresent()) {
            Comercio Comercio = ComercioOptional.get();
            if (Comercio.getRol() == Rol.comercio) {
                Comercio.setRol(Rol.ADMIN);
                ComercioRepositorio.save(Comercio);
            } else {
                throw new MiExcepcion("El Comercio no es un comercio.");
            }
        } else {
            throw new MiExcepcion("No existe el Comercio");
        }
    }*/

    //Buscar comercio en base de datos para validar por mail que no exista y crear Comercio
/*    public boolean existecomercioPorEmail(String email) {
        return ComercioRepositorio.existsByEmail(email);
    }

    //Buscar comercio en base de datos para validar por CUIT que no exista y crear Comercio
    public boolean existecomercioPorCUIT(Integer CUIT) {
        return ComercioRepositorio.existsByCUIT(CUIT);
    }

    //Buscar proveedor en base de datos para validar por mail que no exista y crear Comercio
    public boolean existeProveedorPorEmail(String email) {
        return ComercioRepositorio.existsByEmail(email);
    }

    //Buscar comercio en base de datos para validar por CUIT que no exista y crear Comercio
    public boolean existeProveedorPorCUIT(Integer CUIT) {
        return ComercioRepositorio.existsByCUIT(CUIT);
    }


    @Transactional
    public void recuperarPass(String email, String password, String password2) throws MiExcepcion {

        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas no coinciden");
        }
        Optional<Comercio> ComercioOptional = Optional.ofNullable(ComercioRepositorio.buscarPorEmail(email));
        if (ComercioOptional.isPresent()) {
            Comercio Comercio = ComercioOptional.get();
            Comercio.setPassword(new BCryptPasswordEncoder().encode(password));
            ComercioRepositorio.saveAndFlush(Comercio);
        } else {
            throw new MiExcepcion("No existe el Comercio");
        }


    }*/
}
