package com.softland.api.apitd.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@JsonIgnoreProperties(value = {"divisionBienes", "fechaActualizacion", "estadoCivil", "errorCode", "errorPhrase"})
public class Cliente {
    @Id
    private Long id;

    @Column(name = "cedula")
    @JsonProperty(value = "CedRuc")
    private String cedula;

    @JsonProperty(value = "Apellidos")
    @Column
    private String apellido;

    @JsonProperty(value = "Nombres")
    @Column
    private String nombre;

//    @Column
//    private String direccion;
//
//    @Column
//    private String telefono;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "\"fechaActualizacion\"")
    private LocalDate fechaActualizacion;

    @Column(name = "\"divisionBienes\"")
    private int divisionBienes;

    @Column(name = "\"estadoCivil\"")
    private String estadoCivil;

    public Cliente() {
    }
//    private int errorCode;
//    private String errorPhrase;

    public Cliente(String apellido, String nombre) {
        this.apellido = apellido;
        this.nombre = nombre;
    }
//    public Cliente(String errorPhrase, int errorCode) {
//        this.errorCode = errorCode;
//        this.errorPhrase = errorPhrase;
//    }
    //    @JsonBackReference
//    @OneToMany(mappedBy = "id_cliente", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PredioRural> predioRuralList;
}
