package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.LigneFactureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LigneFacture and its DTO LigneFactureDTO.
 */
@Mapper(componentModel = "spring", uses = {FactureMapper.class})
public interface LigneFactureMapper extends EntityMapper<LigneFactureDTO, LigneFacture> {

    @Mapping(source = "facture.id", target = "factureId")
    LigneFactureDTO toDto(LigneFacture ligneFacture);

    @Mapping(source = "factureId", target = "facture")
    LigneFacture toEntity(LigneFactureDTO ligneFactureDTO);

    default LigneFacture fromId(Long id) {
        if (id == null) {
            return null;
        }
        LigneFacture ligneFacture = new LigneFacture();
        ligneFacture.setId(id);
        return ligneFacture;
    }
}
