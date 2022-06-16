package com.softland.api.apitd.repository;

import com.softland.api.apitd.domain.PredioUrbano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PredioUrbanoRepository extends JpaRepository<PredioUrbano, Long> {
    @Query(value = "select * from predio as pre inner join poligono as po on pre.id_poligono = po.id" +
            " inner join sector as s on po.id_sector = s.id" +
            " inner join zona as z on s.id_zona = z.id" +
            " inner join parroquia as pa on z.id_parroquia = pa.id" +
            " inner join canton as c on pa.id_canton = c.id" +
            " inner join provincia as p on c.id_provincia = p.id" +
            " where (pre.predio = ?1" +
            " and pre.bloque = ?2" +
            " and pre.piso = ?3" +
            " and pre.\"propHorizontal\" = ?4)" +
            " and po.codigo = ?5" +
            " and s.codigo = ?6" +
            " and z.codigo = ?7" +
            " and pa.codigo = ?8" +
            " and c.codigo = ?9" +
            " and p.codigo = ?10", nativeQuery = true)
    PredioUrbano findPredioUrbanoByClavePredial(String predio, String bloque, String piso, String propHorizontal,
                                                String idPoligono, String idSector, String idZona,
                                                String idParroquia, String idCanton, String idProvincia);
}
