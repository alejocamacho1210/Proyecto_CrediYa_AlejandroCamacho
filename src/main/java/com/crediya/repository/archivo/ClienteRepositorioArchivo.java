package com.crediya.repository.archivo;

import com.crediya.model.Cliente;

import java.nio.file.Path;

public class ClienteRepositorioArchivo extends RepositorioArchivo<Cliente> {

    public ClienteRepositorioArchivo(Path ruta) {
        super(ruta, "Cliente");
    }

    @Override
    protected String serializar(Cliente c) {
        return String.join(SEPARADOR,
                String.valueOf(c.getId()),
                limpiar(c.getNombre()),
                limpiar(c.getDocumento()),
                limpiar(c.getCorreo()),
                limpiar(c.getTelefono()));
    }

    @Override
    protected Cliente deserializar(String[] c) {
        Cliente cliente = new Cliente(c[1], c[2], c[3], c[4]);
        cliente.setId(Integer.parseInt(c[0]));
        return cliente;
    }
}
