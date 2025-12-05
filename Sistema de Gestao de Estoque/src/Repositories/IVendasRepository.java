package Repositories;

import Models.Venda;
import java.util.List;

public interface IVendasRepository {

    boolean adicionar(Venda venda);

    Venda buscarPorId(int id);

    List<Venda> getAll();

    boolean cancelar(int id);

    List<Venda> buscarPorCliente(int idCliente);

    List<Venda> buscarPorProduto(int idProduto);

    String getUltimaMensagem();
}
