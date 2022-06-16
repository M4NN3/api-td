package com.softland.api.apitd.controller;

import com.google.gson.Gson;
import com.softland.api.apitd.domain.*;
import com.softland.api.apitd.exception.ResourceNotFoundException;
import com.softland.api.apitd.repository.ApiLogsRepository;
import com.softland.api.apitd.service.ClienteService;
import com.softland.api.apitd.service.PredioRuralService;
import com.softland.api.apitd.service.PredioUrbanoService;
import com.softland.api.apitd.service.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PredioRuralController {
    private static final Logger LOG= LoggerFactory.getLogger(PredioRuralController.class);

    public static final int CLAVE_LENGTH = 27;
    @Autowired
    private PredioRuralService ruralService;
    @Autowired
    private PredioUrbanoService urbanoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ApiLogsRepository apiLogsRepository;
    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;
    private String ruralUrbano = "";
    @Autowired
    private HttpServletRequest request;

    private String idTransaccion = "";
    @GetMapping(value = "/prediorural/{clavePredial}")
    public ResponseEntity<PredioRural> getPredioRuralByClavePredial(@PathVariable String clavePredial) {
        try {

            if (clavePredial.length() != CLAVE_LENGTH)
                return ResponseEntity.badRequest().body(null);
            PredioRural predioRural = ruralService.findByClavePredial(clavePredial);
            if (predioRural == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PredioRural());
            }
            return ResponseEntity.ok(predioRural);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/prediorural")
    public ResponseEntity<PredioRural> findById(@RequestParam(value = "id",
            defaultValue = "1") Long id) {
        try {
            PredioRural predioRural = ruralService.findById(id);
            return ResponseEntity.ok(predioRural);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PredioRural());
        }
    }
    private ResponseEntity<CustomMessage> notFoundStatus(String message){
        String completeMessage = String.format("ERROR [%d]: %s", HttpStatus.NOT_FOUND.value(), message);
        Utils.logConsole(completeMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new CustomMessage(completeMessage, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase()));
    }
    private ResponseEntity<CustomMessage> internalErrorStatus(String message){
        String completeMessage = String.format("ERROR [%d]: %s", HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
        Utils.logConsole(completeMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new CustomMessage(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }
    private ResponseEntity<CustomMessage> badRequestStatus(String message){
        String completeMessage = String.format("ERROR [%d]: %s", HttpStatus.BAD_REQUEST.value(), message);
        Utils.logConsole(completeMessage);
        return ResponseEntity.badRequest().body(new CustomMessage(completeMessage,
                HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }
    private ResponseEntity<CustomMessage> oktStatus(String message){
        String completeMessage = String.format("OK [%d]: %s", HttpStatus.OK.value(), message);
        return ResponseEntity.ok(new
                CustomMessage(completeMessage,
                HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }


    @PostMapping("/predio/{clavePredial}")
    public ResponseEntity<CustomMessage> updatePredio(@Valid @RequestBody PredioAndCliente predioAndCliente,
                                                      @PathVariable String clavePredial,
                                                      @RequestParam(value = "tipoPredio", defaultValue = "1") int tipoPredio) {
        idTransaccion = UUID.randomUUID().toString().substring(0,7);
        //0. tipoPredio | 1 = rural 2 = urbano
        String predioObservaciones = "";
        Utils.logConsole(String.format("TRANSACCION INICIADA: [%s]", idTransaccion));
        Utils.logConsole("Actualización iniciada");
        //1. Validate ClavePredial format
        if (clavePredial.length() != CLAVE_LENGTH) {
            return badRequestStatus(String.format("Clave predial [%s] no tiene formato correcto", clavePredial));
        }
        switch (tipoPredio){
            case 2:{//rural
                //LOG.info(idTransaccion);
                //LOG.
                ruralUrbano = "RURAL";
                PredioRural predioRural = ruralService.findByClavePredial(clavePredial);
                if (predioRural==null){
                    Utils.logConsole(String.format("TRANSACCION TERMINADA: [%s]", idTransaccion));
                    return notFoundStatus(String.format("Predio rural [%s] no encontrado", clavePredial));
                }//2 predio rural exists | Find cliente by cedula
                try {
                    Cliente cliente = clienteService.findByCedula(predioAndCliente.cliente.getCedula());
                    //2.1 cliente exists - set  id
                    Utils.logConsole(String.format("Cliente encontrado: ID[%1$s] / Cédula[%2$s]", cliente.getId(), cliente.getCedula()));
                    predioAndCliente.predio.setIdcliente(cliente.getId());
                }catch (ResourceNotFoundException ex){
                    //2.2 cliente not found
                    Utils.logConsole(String.format("Cliente NO encontrado: Cédula[%1$s]", predioAndCliente.cliente.getCedula()));
                    Cliente clienteToBeSaved = saveCliente(predioAndCliente.cliente);
                    if (clienteToBeSaved == null) {
                        Utils.logConsole(String.format("TRANSACCION TERMINADA: [%s]", idTransaccion));
                        return internalErrorStatus("ERROR: no se pudo guardar los datos del cliente");
                    }
                    //2.3 cliente saved
                    Utils.logConsole(String.format("Cliente creado: ID[%1$s] / Cédula[%2$s]",
                            clienteToBeSaved.getId(), clienteToBeSaved.getCedula()));
                    predioObservaciones = String.format("Cliente creado: ID[%1$s] / Cédula[%2$s]",
                            clienteToBeSaved.getId(), clienteToBeSaved.getCedula());
                    predioAndCliente.predio.setIdcliente(clienteToBeSaved.getId());
                }
                // 3. all good - do update
                try {
                    predioAndCliente.predio.setId(predioRural.getId());
                    predioAndCliente.predio.setAreaEscritura(predioRural.getAreaEscritura());//areaEsc no se modifica
                    predioAndCliente.predio.setObservaciones(
                            String.format("%1$s\n%2$s", predioRural.getObservaciones(), predioAndCliente.predio.getObservaciones()));
                    ruralService.update(predioAndCliente.predio);
                    Utils.logConsole(String.format("Predio rural: [%s] actualizado, guardando log...", clavePredial));
                }catch (DataIntegrityViolationException ex){
                    ex.printStackTrace();
                    Utils.logConsole(String.format("TRANSACCION TERMINADA: [%s]", idTransaccion));
                    return internalErrorStatus("Actualización fallida: " + ex.getMessage());
                }
                //save api log
                try {
                    saveApiLog(predioRural.getId(), new Gson().toJson(predioAndCliente.predio), new Gson().toJson(predioRural),
                            predioAndCliente.predio.getIdcliente(), predioRural.getIdcliente(),predioObservaciones, ruralUrbano);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
            }
            case 1:{//urbano
                ruralUrbano = "URBANO";
                PredioUrbano predioUrbano = urbanoService.findByClavePredial(clavePredial);
                if (predioUrbano == null){
                    Utils.logConsole(String.format("TRANSACCION TERMINADA: [%s]", idTransaccion));
                    return notFoundStatus(String.format("Predio urbano [%s] no encontrado", clavePredial));
                }//2 predio urbano exists | Find cliente by cedula
                try {
                    Cliente cliente = clienteService.findByCedula(predioAndCliente.cliente.getCedula());
                    //2.1 cliente exists - set  id
                    Utils.logConsole(String.format("Cliente encontrado: ID[%1$s] / Cédula[%2$s]", cliente.getId(), cliente.getCedula()));
                    predioAndCliente.predio.setIdcliente(cliente.getId());
                }catch (ResourceNotFoundException ex){
                    //2.2 cliente not found
                    Utils.logConsole(String.format("Cliente NO encontrado: Cédula[%1$s]", predioAndCliente.cliente.getCedula()));
                    Cliente clienteToBeSaved = saveCliente(predioAndCliente.cliente);
                    if (clienteToBeSaved == null)
                        return internalErrorStatus("ERROR: no se pudo guardar los datos del cliente");
                    //2.3 cliente saved
                    Utils.logConsole(String.format("Cliente creado: ID[%1$s] / Cédula[%2$s]",
                            clienteToBeSaved.getId(), clienteToBeSaved.getCedula()));
                    predioObservaciones = String.format("Cliente creado: ID[%1$s] / Cédula[%2$s]",
                            clienteToBeSaved.getId(), clienteToBeSaved.getCedula());
                    predioAndCliente.predio.setIdcliente(clienteToBeSaved.getId());
                }
                //3. all good, do update
                PredioUrbano nuevoPredioUrbano = new PredioUrbano();
                try {
                    nuevoPredioUrbano.setId(predioUrbano.getId());
                    nuevoPredioUrbano.setIdcliente(predioAndCliente.predio.getIdcliente());
                    nuevoPredioUrbano.setAreaEscritura(predioUrbano.getAreaEscritura());//areaEsc se mantiene igual
                    nuevoPredioUrbano.setFechaNotaria(predioAndCliente.predio.getFechaNotaria());
                    nuevoPredioUrbano.setFechaRegistro(predioAndCliente.predio.getFechaRegistro());
                    nuevoPredioUrbano.setNotaria(predioAndCliente.predio.getNotaria());
                    nuevoPredioUrbano.setObservaciones(
                            String.format("%1$s\n%2$s", predioUrbano.getObservaciones(),
                                    predioAndCliente.predio.getObservaciones()));
                    nuevoPredioUrbano.setNroRegistro(predioAndCliente.predio.getNroRegistro());
                    //areaEscr no la modificamos
                    urbanoService.update(nuevoPredioUrbano);
                    Utils.logConsole(String.format("Predio urbano: [%s] actualizado, guardando log...", clavePredial));
                }catch (DataIntegrityViolationException ex){
                    ex.printStackTrace();
                    Utils.logConsole(String.format("TRANSACCION TERMINADA: [%s]", idTransaccion));
                    return internalErrorStatus("Actualización fallida: " + ex.getMessage());
                }
                //save api log
                try {
                    saveApiLog(predioUrbano.getId(), new Gson().toJson(nuevoPredioUrbano), new Gson().toJson(predioUrbano),
                            predioAndCliente.predio.getIdcliente(), predioUrbano.getIdcliente(),predioObservaciones, ruralUrbano);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
            }
        }
        // finish -
        Utils.logConsole(String.format("TRANSACCION TERMINADA: [%s]", idTransaccion));
        return oktStatus(String.format("[%s] Operación de actualización ejecutada correctamente", ruralUrbano));
    }

    private void saveApiLog(Long idPredio, String predioDatoActual, String predioDatoAnterior, long idClienteActual, long idClienteAnterior, String predioObservaciones,  String tipoPredio) {
        ApiLogs apiLogs = new ApiLogs();
        apiLogs.setIdPredio(idPredio);
        apiLogs.setIdClienteAnterior(idClienteAnterior);
        apiLogs.setIdClienteActual(idClienteActual);
        apiLogs.setPredioDatoAnterior(predioDatoAnterior);
        apiLogs.setPredioDatoActual(predioDatoActual);
        apiLogs.setTipoPredio(tipoPredio);
        apiLogs.setPredioObservaciones(predioObservaciones);
        apiLogs.setFechaActualizacion(Utils.getCurrentDateTime());
        apiLogsRepository.save(apiLogs);
    }

    public static class PredioAndCliente {
        public PredioRural predio;
        public Cliente cliente;
    }

    private Cliente saveCliente(Cliente cliente) {
        try {
            long id = clienteService.getIdToUse();
            if (id == 0)
                return null;
            cliente.setId(id);
            cliente.setEstadoCivil(Utils.ESTADO_CIVIL);
            cliente.setDivisionBienes(Utils.DIVISION_BIENES);
            cliente.setFechaActualizacion(Utils.getCurrentDate());
            return clienteService.saveCliente(cliente);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    private String getVisitorIp() {
        try {
            return request.getRemoteHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NO-IP";
    }
}
