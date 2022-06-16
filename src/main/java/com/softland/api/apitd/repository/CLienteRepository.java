package com.softland.api.apitd.repository;

import com.softland.api.apitd.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CLienteRepository extends JpaRepository<Cliente, Long> {
    @Query(value = "select * from cliente as c where cedula = :cedula limit 1", nativeQuery = true)
    Cliente findByCedula(String cedula);

    @Query(value = "select * from cliente order by id desc limit 1", nativeQuery = true)
    Cliente getLastClienteId();
}
