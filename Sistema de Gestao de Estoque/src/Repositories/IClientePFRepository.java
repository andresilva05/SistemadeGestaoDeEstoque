package Repositories;

import Models.ClientePF;

import java.util.List;

public interface IClientePFRepository<T extends ClientePF> {
    boolean adicionar(T cliente);

    T buscarPorId(int id);

    T buscarPorDocumento(String documento);

    List<T> getAll();

    boolean excluir(int id);

    String getUltimaMensagem(); // Adicione este método
}