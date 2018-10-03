package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.AffectationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Affectation and its DTO AffectationDTO.
 */
@Mapper(componentModel = "spring", uses = {TravauxMapper.class, ChantierMapper.class, EmployeMapper.class})
public interface AffectationMapper extends EntityMapper<AffectationDTO, Affectation> {

    @Mapping(source = "travaux.id", target = "travauxId")
    @Mapping(source = "travaux.nomTrav", target = "travauxNomTrav")
    @Mapping(source = "chantier.id", target = "chantierId")
    @Mapping(source = "chantier.nomChantier", target = "chantierNomChantier")
    AffectationDTO toDto(Affectation affectation);

    @Mapping(source = "travauxId", target = "travaux")
    @Mapping(source = "chantierId", target = "chantier")
    Affectation toEntity(AffectationDTO affectationDTO);

    default Affectation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Affectation affectation = new Affectation();
        affectation.setId(id);
        return affectation;
    }
}
