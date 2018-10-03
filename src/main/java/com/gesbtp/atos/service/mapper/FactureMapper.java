package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.FactureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Facture and its DTO FactureDTO.
 */
@Mapper(componentModel = "spring", uses = {ChantierMapper.class, TravauxMapper.class, EntrepriseMapper.class})
public interface FactureMapper extends EntityMapper<FactureDTO, Facture> {

    @Mapping(source = "chantier.id", target = "chantierId")
    @Mapping(source = "chantier.nomChantier", target = "chantierNomChantier")
    @Mapping(source = "travaux.id", target = "travauxId")
    @Mapping(source = "travaux.nomTrav", target = "travauxNomTrav")
    @Mapping(source = "entreprise.id", target = "entrepriseId")
    @Mapping(source = "entreprise.name", target = "entrepriseName")
    FactureDTO toDto(Facture facture);

    @Mapping(source = "chantierId", target = "chantier")
    @Mapping(source = "travauxId", target = "travaux")
    @Mapping(source = "entrepriseId", target = "entreprise")
    @Mapping(target = "factures", ignore = true)
    Facture toEntity(FactureDTO factureDTO);

    default Facture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Facture facture = new Facture();
        facture.setId(id);
        return facture;
    }
}
