package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.EmployeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Employe and its DTO EmployeDTO.
 */
@Mapper(componentModel = "spring", uses = {EntrepriseMapper.class})
public interface EmployeMapper extends EntityMapper<EmployeDTO, Employe> {

    @Mapping(source = "entreprise.id", target = "entrepriseId")
    @Mapping(source = "entreprise.name", target = "entrepriseName")
    EmployeDTO toDto(Employe employe);

    @Mapping(source = "entrepriseId", target = "entreprise")
    @Mapping(target = "affectation1S", ignore = true)
    Employe toEntity(EmployeDTO employeDTO);

    default Employe fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employe employe = new Employe();
        employe.setId(id);
        return employe;
    }
}
