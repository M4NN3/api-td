package com.softland.api.apitd.controller;

import com.softland.api.apitd.domain.Cliente;
import com.softland.api.apitd.domain.CustomMessage;
import com.softland.api.apitd.domain.PredioRural;
import com.softland.api.apitd.domain.PredioUrbano;
import com.softland.api.apitd.service.PredioUrbanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api")
@RestController
public class PredioUrbanoController {
    public static final int CLAVE_LENGTH = 27;
    @Autowired
    private PredioUrbanoService service;

    @GetMapping(value = "/prediourbano/{clavePredial}")
    public ResponseEntity<PredioUrbano> getPredioUrbanoByClavePredial(@PathVariable String clavePredial) {
        try {
            if (clavePredial.length() != CLAVE_LENGTH)
                return ResponseEntity.badRequest().body(null);
            PredioUrbano predioUrbano = service.findByClavePredial(clavePredial);
            if (predioUrbano == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PredioUrbano());
            }
            return ResponseEntity.ok(predioUrbano);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/prediourbano")
    public ResponseEntity<PredioUrbano> findById(@RequestParam(value = "id", defaultValue = "1") Long id){
        try{
            PredioUrbano predioUrbano = service.findById(id);
            return ResponseEntity.ok(predioUrbano);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PredioUrbano());
        }
    }
//    @PutMapping("/prediourbano/{clavePredial}")
//    public ResponseEntity<CustomMessage> updatePredioUrbano(@Valid @RequestBody PredioUrbano predioUrbano,
//                                                            @PathVariable String clavePredial){
//        try {
//            if (clavePredial.length() < CLAVE_LENGTH)
//                return ResponseEntity.badRequest().body(new CustomMessage("La clave predial no tiene el formato correcto",
//                        HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
//            PredioUrbano pu = service.findByClavePredial(clavePredial);
//            if (pu == null){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                        new CustomMessage("No se encontró predio urbano con clave predial: " + clavePredial,
//                                HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase()));
//            }
//            predioUrbano.setId(pu.getId());
//            if (!service.update(predioUrbano)){
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                        new CustomMessage("Error: no se pudo actualizar los datos del predio",
//                                HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
//            }
//            return ResponseEntity.ok(new CustomMessage("Predio urbano con clave predial " + clavePredial + " actualizado correctamente",
//                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "/prediourbano/"+clavePredial));
//        }
//        catch (Exception ex){
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    new CustomMessage("Error: no se pudo actualizar los datos del predio",
//                            HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
//        }
//    }

}
