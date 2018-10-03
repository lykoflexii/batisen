package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.ClientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Client and its DTO ClientDTO.
 */
@Mapper(componentModel = "spring", uses = {EntrepriseMapper.class})
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {

    @Mapping(source = "entreprise.id", target = "entrepriseId")
    @Mapping(source = "entreprise.name", target = "entrepriseName")
    ClientDTO toDto(Client client);

    @Mapping(source = "entrepriseId", target = "entreprise")
    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "client1S", ignore = true)
    Client toEntity(ClientDTO clientDTO);

    default Client fromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
