package com.softland.api.apitd.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "apilogs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
public class ApiLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long idPredio;
    @Column
    private long idClienteAnterior;
    @Column
    private long idClienteActual;
    @Column(length = 10000)
    private String predioDatoAnterior;
    @Column(length = 10000)
    private String predioDatoActual;
    @Column
    private String tipoPredio; //
    @Column
    private String predioObservaciones;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column
    private LocalDateTime fechaActualizacion;

}
