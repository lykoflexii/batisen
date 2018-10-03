package com.gesbtp.atos.service.mapper;

import com.gesbtp.atos.domain.*;
import com.gesbtp.atos.service.dto.EntrepriseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Entreprise and its DTO EntrepriseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EntrepriseMapper extends EntityMapper<EntrepriseDTO, Entreprise> {


    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "employes", ignore = true)
    @Mapping(target = "facture4S", ignore = true)
    Entreprise toEntity(EntrepriseDTO entrepriseDTO);

    default Entreprise fromId(Long id) {
        if (id == null) {
            return null;
        }
        Entreprise entreprise = new Entreprise();
        entreprise.setId(id);
        return entreprise;
    }
}
