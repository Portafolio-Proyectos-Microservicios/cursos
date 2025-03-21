package com.microservice.cursos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document (collection = "Curso")
public class Curso {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private Set<String> estudiantesId = new HashSet<>();;

}
