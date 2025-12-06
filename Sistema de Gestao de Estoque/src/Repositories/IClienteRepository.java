package Repositories;

import java.util.List;

public interface IClienteRepository<T> {
    boolean adicionar(T cliente);

    T buscarPorId(int id);

    T buscarPorDocumento(String documento);

    List<T> getAll();

    boolean excluir(int id);

    String getUltimaMensagem();
}