package com.gesbtp.atos.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Entreprise entity.
 */
public class EntrepriseDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String commercialName;

    @NotNull
    private String sirenNumber;

    @NotNull
    private String entrepriseAdminEmail;

    private Integer telephone;

    private Integer pack;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getSirenNumber() {
        return sirenNumber;
    }

    public void setSirenNumber(String sirenNumber) {
        this.sirenNumber = sirenNumber;
    }

    public String getEntrepriseAdminEmail() {
        return entrepriseAdminEmail;
    }

    public void setEntrepriseAdminEmail(String entrepriseAdminEmail) {
        this.entrepriseAdminEmail = entrepriseAdminEmail;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public Integer getPack() {
        return pack;
    }

    public void setPack(Integer pack) {
        this.pack = pack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EntrepriseDTO entrepriseDTO = (EntrepriseDTO) o;
        if(entrepriseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entrepriseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntrepriseDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", commercialName='" + getCommercialName() + "'" +
            ", sirenNumber='" + getSirenNumber() + "'" +
            ", entrepriseAdminEmail='" + getEntrepriseAdminEmail() + "'" +
            ", telephone=" + getTelephone() +
            ", pack=" + getPack() +
            "}";
    }
}
