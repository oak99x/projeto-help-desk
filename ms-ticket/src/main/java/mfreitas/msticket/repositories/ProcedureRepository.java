package mfreitas.msticket.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import mfreitas.msticket.models.Procedure;

public interface ProcedureRepository extends MongoRepository<Procedure, String>{
    
}
