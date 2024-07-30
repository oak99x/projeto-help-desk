package mfreitas.msticket.resources.ticketMappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mfreitas.msticket.dtos.ProcedureCreateDTO;
import mfreitas.msticket.dtos.ProcedureDTO;
import mfreitas.msticket.models.Procedure;

@Mapper(componentModel = "spring")
public interface ProcedureMapper {

    ProcedureMapper INSTANCE = Mappers.getMapper(ProcedureMapper.class);

    // ProcedureDTO procedureToProcedureDTO(Procedure procedure);

    // Procedure procedureDTOToProcedure(ProcedureDTO procedureDTO);

    // Procedure procedureCreateDTOToProcedure(ProcedureCreateDTO procedureCreateDTO);

    List<ProcedureDTO> listProceduresToProcedureDTOs(List<Procedure> procedureList);

}
