package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.TravauxDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Travaux and its DTO TravauxDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TravauxMapper extends EntityMapper<TravauxDTO, Travaux> {


    @Mapping(target = "affectation2S", ignore = true)
    @Mapping(target = "facture3S", ignore = true)
    Travaux toEntity(TravauxDTO travauxDTO);

    default Travaux fromId(Long id) {
        if (id == null) {
            return null;
        }
        Travaux travaux = new Travaux();
        travaux.setId(id);
        return travaux;
    }
}
