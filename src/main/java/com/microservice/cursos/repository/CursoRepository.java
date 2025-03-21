package com.microservice.cursos.repository;

import com.microservice.cursos.model.Curso;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CursoRepository extends ReactiveMongoRepository<Curso, String> {

}
