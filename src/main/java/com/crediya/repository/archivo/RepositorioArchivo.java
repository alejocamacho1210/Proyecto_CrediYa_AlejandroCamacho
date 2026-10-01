package com.crediya.repository.archivo;

import com.crediya.exception.EntidadNoEncontradaException;
import com.crediya.exception.PersistenciaException;
import com.crediya.model.Entidad;
import com.crediya.repository.Repositorio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class RepositorioArchivo<T extends Entidad> implements Repositorio<T> {

    protected static final String SEPARADOR = "|";

    private final Path ruta;
    private final String nombreEntidad;

    protected RepositorioArchivo(Path ruta, String nombreEntidad) {
        this.ruta = ruta;
        this.nombreEntidad = nombreEntidad;
        inicializar();
    }

    protected abstract String serializar(T entidad);

    protected abstract T deserializar(String[] campos);

    @Override
    public synchronized T guardar(T entidad) {
        List<T> todas = leer();
        int siguiente = todas.stream().mapToInt(Entidad::getId).max().orElse(0) + 1;
        entidad.setId(siguiente);
        todas.add(entidad);
        escribir(todas);
        return entidad;
    }

    @Override
    public synchronized void actualizar(T entidad) {
        List<T> todas = leer();
        for (int i = 0; i < todas.size(); i++) {
            if (todas.get(i).getId() == entidad.getId()) {
                todas.set(i, entidad);
                escribir(todas);
                return;
            }
        }
        throw new EntidadNoEncontradaException(nombreEntidad, entidad.getId());
    }

    @Override
    public synchronized Optional<T> buscarPorId(int id) {
        return leer().stream().filter(entidad -> entidad.getId() == id).findFirst();
    }

    @Override
    public synchronized List<T> listar() {
        return leer();
    }

    public synchronized void reemplazarTodo(List<T> entidades) {
        escribir(new ArrayList<>(entidades));
    }

    protected static String limpiar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace(SEPARADOR, " ").replace("\r", " ").replace("\n", " ");
    }

    private void inicializar() {
        try {
            Path padre = ruta.toAbsolutePath().getParent();
            if (padre != null) {
                Files.createDirectories(padre);
            }
            if (!Files.exists(ruta)) {
                Files.createFile(ruta);
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo preparar el archivo " + ruta, e);
        }
    }

    private List<T> leer() {
        try (Stream<String> lineas = Files.lines(ruta, StandardCharsets.UTF_8)) {
            return lineas
                    .filter(linea -> !linea.isBlank())
                    .map(linea -> deserializar(linea.split("\\|", -1)))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer el archivo " + ruta, e);
        } catch (RuntimeException e) {
            throw new PersistenciaException("El archivo " + ruta + " tiene registros con formato invalido", e);
        }
    }

    private void escribir(List<T> entidades) {
        List<String> lineas = entidades.stream().map(this::serializar).collect(Collectors.toList());
        try {
            Files.write(ruta, lineas, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir el archivo " + ruta, e);
        }
    }
}
