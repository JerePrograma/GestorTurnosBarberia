package ar.com.tubarberia.servicios;

import ar.com.tubarberia.entidades.Imagen;
import ar.com.tubarberia.excepciones.MiExcepcion;
import ar.com.tubarberia.repositorios.ImagenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImagenServicio {
    @Autowired
    private ImagenRepositorio imagenRepositorio;

    @Transactional
    public Imagen guardarImagen(MultipartFile archivo) throws MiExcepcion {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getOriginalFilename());

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (Exception e) {

                throw new MiExcepcion("Error al guardar la imagen: " + e.getMessage());
            }
        } else {
            throw new MiExcepcion("El archivo no puede estar nulo o vacío");
        }


    }

    @Transactional
    public Imagen actualizarImagen(MultipartFile archivo, String idImagen) throws MiExcepcion {

        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen imagen = new Imagen();

                if (idImagen != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    } else {
                        throw new MiExcepcion("La imagen no existe");
                    }
                } else {
                    throw new MiExcepcion("La imagen no puede estar vacía");
                }
                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getOriginalFilename());

                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (Exception e) {

                throw new MiExcepcion("Error al guardar la imagen: " + e.getMessage());
            }
        } else {
            throw new MiExcepcion("El archivo no puede estar nulo o vacío");
        }


    }
}

