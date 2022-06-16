package com.softland.api.apitd.service;

import com.softland.api.apitd.domain.PredioUrbano;
import com.softland.api.apitd.exception.ResourceNotFoundException;
import com.softland.api.apitd.repository.PredioUrbanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PredioUrbanoService {
    @Autowired
    private PredioUrbanoRepository repository;

    public PredioUrbano findByClavePredial(String clavePredial) {
        Map<String, String> c = Utils.getClavePredial(clavePredial);
        PredioUrbano predioUrbano = repository.findPredioUrbanoByClavePredial(
                c.get("predio"), c.get("bloque"), c.get("piso"), c.get("prophorizontal"), c.get("poligono"), c.get("sector"), c.get("zona"),
                c.get("parroquia"), c.get("canton"), c.get("provincia"));
        return predioUrbano;
    }

    public void update(PredioUrbano predioUrbano) throws DataIntegrityViolationException {
        try {
            repository.save(predioUrbano);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getRootCause().getMessage());
        }
    }

    public PredioUrbano findById(Long id) throws ResourceNotFoundException {
        PredioUrbano predioUrbano = repository.findById(id).orElse(null);
        if (predioUrbano == null)
            throw new ResourceNotFoundException("No se encontró predio urbano con ID: " + id);
        return predioUrbano;
    }
}
