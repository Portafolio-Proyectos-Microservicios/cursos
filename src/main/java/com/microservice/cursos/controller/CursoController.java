package com.microservice.cursos.controller;

import com.microservice.cursos.model.Curso;
import com.microservice.cursos.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public Flux<Curso> obtenerTodoslosCursos(){
        return cursoService.obtenerTodoslosCursos();
    }

    @GetMapping("/{id}")
    public Mono<Curso> obtenerCursosporId(@PathVariable String id){
        return cursoService.obtenerCursosporId(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Curso no encontrado")));
    }

    @PostMapping("/registrar-alumno/{cursoId}/estudiante/{estudianteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Curso>> registrarEstudianteenCurso(@PathVariable String cursoId, @PathVariable String estudianteId){
        return cursoService.registrarEstudianteenCurso(cursoId, estudianteId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/registrar-curso")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Curso> registrarCurso(@Valid @RequestBody Curso curso){
        return cursoService.registrarCurso(curso)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Datos invalidos")))
                .flatMap(cursoRegistrado ->
                cursoService.enviarMensajeAKafka("Registro-Curso", cursoRegistrado.getId(), cursoRegistrado)
                .thenReturn(cursoRegistrado));
    }

}
