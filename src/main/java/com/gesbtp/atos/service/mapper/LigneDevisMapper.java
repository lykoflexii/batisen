package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.LigneDevisDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LigneDevis and its DTO LigneDevisDTO.
 */
@Mapper(componentModel = "spring", uses = {DevisMapper.class})
public interface LigneDevisMapper extends EntityMapper<LigneDevisDTO, LigneDevis> {

    @Mapping(source = "devis.id", target = "devisId")
    LigneDevisDTO toDto(LigneDevis ligneDevis);

    @Mapping(source = "devisId", target = "devis")
    LigneDevis toEntity(LigneDevisDTO ligneDevisDTO);

    default LigneDevis fromId(Long id) {
        if (id == null) {
            return null;
        }
        LigneDevis ligneDevis = new LigneDevis();
        ligneDevis.setId(id);
        return ligneDevis;
    }
}
