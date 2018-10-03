package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.ChantierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Chantier and its DTO ChantierDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, UserMapper.class})
public interface ChantierMapper extends EntityMapper<ChantierDTO, Chantier> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.nomClient", target = "clientNomClient")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ChantierDTO toDto(Chantier chantier);

    @Mapping(source = "clientId", target = "client")
    @Mapping(target = "affectation3S", ignore = true)
    @Mapping(target = "chantiers", ignore = true)
    @Mapping(target = "facture2S", ignore = true)
    @Mapping(source = "userId", target = "user")
    Chantier toEntity(ChantierDTO chantierDTO);

    default Chantier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chantier chantier = new Chantier();
        chantier.setId(id);
        return chantier;
    }
}
