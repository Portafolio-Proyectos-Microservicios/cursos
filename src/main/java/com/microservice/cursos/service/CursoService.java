package com.microservice.cursos.service;

import com.microservice.cursos.model.Curso;
import com.microservice.cursos.model.EstudianteDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CursoService {

    Flux<Curso> obtenerTodoslosCursos();
    Mono<Curso> obtenerCursosporId(String id);
    Mono<Curso> registrarCurso(Curso curso);
    Mono<Curso> registrarEstudianteenCurso(String idCurso,String idEstudiante);
    Mono<EstudianteDTO> obtenerEstudianteporID(String id);
    public Mono<Void> enviarMensajeAKafka(String topic, String key, Object mensaje);


}
