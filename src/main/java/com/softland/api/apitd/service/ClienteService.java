package com.softland.api.apitd.service;

import com.softland.api.apitd.domain.Cliente;
import com.softland.api.apitd.exception.ResourceNotFoundException;
import com.softland.api.apitd.repository.CLienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private CLienteRepository repository;
    public Cliente findByCedula(String cedula) throws ResourceNotFoundException {
        Cliente cliente = repository.findByCedula(cedula);
        if (cliente == null)
            throw new ResourceNotFoundException("No se encontró el cliente con la cédula: " + cedula);
        return cliente;
    }
    public List<Cliente> findAllClientes(int pageNumber, int rowPerPage){
        List<Cliente> clienteList = new ArrayList<>();
        repository.findAll(PageRequest.of(pageNumber-1, rowPerPage)).forEach(clienteList::add);
        return clienteList;
    }
    public Cliente findById(Long id){
        return repository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format("Cliente con id: %1$d no fue encontrado.", id)));
    }

    public Long getIdToUse(){
        Cliente cliente = repository.getLastClienteId();
        if (cliente == null)
            return 0L;
        return (cliente.getId() + 1L);
    }

    public Cliente saveCliente(Cliente cliente){
        return  repository.save(cliente);
    }
}
