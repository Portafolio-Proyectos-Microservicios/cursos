package com.microservice.cursos.service.impl;

import com.microservice.cursos.model.Curso;
import com.microservice.cursos.model.EstudianteDTO;
import com.microservice.cursos.repository.CursoRepository;
import com.microservice.cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Service
public class CursoImpl implements CursoService {

    private final WebClient.Builder webClientBuilder;

    public CursoImpl(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Autowired
    private CursoRepository cursoRepository;


    @Override
    public Flux<Curso> obtenerTodoslosCursos(){
        return cursoRepository.findAll();
    }

    @Override
    public Mono<Curso> obtenerCursosporId(String id){
        return cursoRepository.findById(id);
    }

    @Override
    public Mono<Curso> registrarCurso(Curso curso){
        curso.setEstudiantesId(curso.getEstudiantesId() != null ? curso.getEstudiantesId() : new HashSet<>());
        return cursoRepository.save(curso);
    }

    @Override
    public Mono<Curso> registrarEstudianteenCurso(String idCurso,String idEstudiante){
        Mono<Curso> cursoMono = cursoRepository.findById(idCurso);
        return cursoMono.flatMap(curso -> obtenerEstudianteporID(idEstudiante)
                .flatMap(estudiante ->{
                    Set<String> estudianteactualizado = new HashSet<>(curso.getEstudiantesId());
                    estudianteactualizado.add(idEstudiante);
                    curso.setEstudiantesId(estudianteactualizado);
                    return cursoRepository.save(curso);
                }).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Estudiante no encontrado")))).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Curso no encontrado")));

    }

    @Override
    public Mono<EstudianteDTO> obtenerEstudianteporID(String id){
        return webClientBuilder.build()
                .get()
                .uri("/api/estudiantes/{id}", id)
                .retrieve()
                .bodyToMono(EstudianteDTO.class);
    }

    @Override
    public Mono<Void> enviarMensajeAKafka(String topic, String key, Object mensaje) {
        return webClientBuilder.build()
                .post()
                .uri("/api/kafka/producer/{topic}/key/{key}", topic, key)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mensaje)
                .retrieve()
                .bodyToMono(Void.class);
    }

}
