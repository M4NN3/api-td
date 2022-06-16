package com.softland.api.apitd.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "predio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
public class PredioUrbano{
    @Id
    private Long id;

    @Column(name = "id_cliente")
    private Long idcliente;

    @Column(name = "\"areaEscritura\"")
    private double areaEscritura;
    @Column()
    private String notaria;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "\"fechaNotaria\"")
    private LocalDate fechaNotaria;

    @Column(name = "\"nroRegistro\"")
    private String nroRegistro;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "\"fechaRegistro\"")
    private LocalDate fechaRegistro;

    @Column(name = "observaciones")
    private String observaciones;
}
