package com.softland.api.apitd.repository;

import com.softland.api.apitd.domain.PredioRural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PredioRuralRepository extends JpaRepository<PredioRural, Long> {
    @Query(value = "select * from predio_rural as pre inner join poligono_rural as po on pre.id_poligono = po.id" +
            " inner join sector_rural as s on po.id_sector = s.id" +
            " inner join zona_rural as zr on s.id_zona = zr.id" +
            " inner join parroquia as pa on zr.id_parroquia = pa.id" +
            " inner join canton as c on pa.id_canton = c.id" +
            " inner join provincia as p on c.id_provincia = p.id" +
            " where (pre.predio = ?1" +
            " and pre.bloque = ?2" +
            " and pre.piso = ?3" +
            " and pre.\"propHorizontal\" = ?4)" +
            " and po.codigo = ?5" +
            " and s.codigo = ?6" +
            " and zr.codigo = ?7" +
            " and pa.codigo = ?8" +
            " and c.codigo = ?9" +
            " and p.codigo = ?10", nativeQuery = true)
    PredioRural findPredioRuralByClavePredial(String predio, String bloque, String piso, String propHorizontal,
                                              String idPoligono, String idSector, String idZona,
                                              String idParroquia, String idCanton, String idProvincia);
}
