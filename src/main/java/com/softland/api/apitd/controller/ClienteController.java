package com.softland.api.apitd.controller;

import com.softland.api.apitd.domain.Cliente;
import com.softland.api.apitd.exception.ResourceNotFoundException;
import com.softland.api.apitd.service.ClienteService;
import com.softland.api.apitd.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClienteController {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ClienteService service;
    @GetMapping("/cliente/{cedula}")
    private ResponseEntity<Cliente> getClienteByCedula(@PathVariable String cedula){
        try {
            Cliente cliente = service.findByCedula(cedula);
            return ResponseEntity.ok(cliente);
        }
        catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Cliente(HttpStatus.NOT_FOUND.getReasonPhrase(),""+HttpStatus.NOT_FOUND.value()));
        }
    }
    @GetMapping("/clienteid/{id}")
    @ResponseBody
    private Cliente getClienteById(@PathVariable Long id){
        return service.findById(id);
    }

    @GetMapping("/cliente")
    private ResponseEntity<List<Cliente>> findALl(@RequestParam(value = "page",
            defaultValue = "1") int pageNumber){
        return ResponseEntity.ok(service.findAllClientes(pageNumber, Utils.ROW_PER_PAGE));
    }

    @PostMapping("/cliente")
    private ResponseEntity<Cliente> saveCliente(@Valid @RequestBody Cliente cliente){
        try{
            long id = service.getIdToUse();
            if (id == 0)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Cliente("No se pudo generar el ID",""+HttpStatus.INTERNAL_SERVER_ERROR.value()));
            cliente.setId(id);
            cliente.setEstadoCivil(Utils.ESTADO_CIVIL);
            cliente.setDivisionBienes(Utils.DIVISION_BIENES);
            cliente.setFechaActualizacion(Utils.getCurrentDate());
            return ResponseEntity.ok(service.saveCliente(cliente));
        }
        catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Cliente(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),""+HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    @GetMapping("/shutdown")
    private void shutdownApi(){
        SpringApplication.exit(context);
    }
}