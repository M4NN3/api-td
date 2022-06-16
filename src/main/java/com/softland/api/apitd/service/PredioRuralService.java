package com.softland.api.apitd.service;

import com.softland.api.apitd.domain.PredioRural;
import com.softland.api.apitd.exception.ResourceNotFoundException;
import com.softland.api.apitd.repository.PredioRuralRepository;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PredioRuralService {
    @Autowired
    private PredioRuralRepository repository;

    public PredioRural findByClavePredial(String clavePredial) {
        //split clavePredial: Prov-Cant-Parro-Zona-Sect-Polg-Pred-Bloq-Piso-Unida
        Map<String, String> c = Utils.getClavePredial(clavePredial);
        PredioRural predioRural = repository.findPredioRuralByClavePredial(
                c.get("predio"),c.get("bloque"),c.get("piso"),c.get("prophorizontal"),c.get("poligono"), c.get("sector"),c.get("zona"),
                c.get("parroquia"),c.get("canton"), c.get("provincia"));
        return predioRural;
    }

    public void update(PredioRural predioRural) throws DataIntegrityViolationException{
        try {
            repository.save(predioRural);
        }
        catch (DataIntegrityViolationException ex){
            throw new DataIntegrityViolationException(ex.getRootCause().getMessage());
        }
    }

    public PredioRural findById(Long id) throws ResourceNotFoundException{
        PredioRural predioRural = repository.findById(id).orElse(null);
        if (predioRural == null){
            throw new ResourceNotFoundException("No se encontró predio rural con ID: " + id);
        }
        return predioRural;
    }
}
