package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.DevisDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Devis and its DTO DevisDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, ChantierMapper.class})
public interface DevisMapper extends EntityMapper<DevisDTO, Devis> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.nomClient", target = "clientNomClient")
    @Mapping(source = "chantier.id", target = "chantierId")
    @Mapping(source = "chantier.nomChantier", target = "chantierNomChantier")
    DevisDTO toDto(Devis devis);

    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "chantierId", target = "chantier")
    @Mapping(target = "devis", ignore = true)
    Devis toEntity(DevisDTO devisDTO);

    default Devis fromId(Long id) {
        if (id == null) {
            return null;
        }
        Devis devis = new Devis();
        devis.setId(id);
        return devis;
    }
}
