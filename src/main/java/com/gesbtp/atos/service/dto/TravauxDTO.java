package com.gesbtp.atos.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Travaux entity.
 */
public class TravauxDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomTrav;

    @Size(max = 200)
    private String descriptionTrav;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomTrav() {
        return nomTrav;
    }

    public void setNomTrav(String nomTrav) {
        this.nomTrav = nomTrav;
    }

    public String getDescriptionTrav() {
        return descriptionTrav;
    }

    public void setDescriptionTrav(String descriptionTrav) {
        this.descriptionTrav = descriptionTrav;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TravauxDTO travauxDTO = (TravauxDTO) o;
        if(travauxDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), travauxDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TravauxDTO{" +
            "id=" + getId() +
            ", nomTrav='" + getNomTrav() + "'" +
            ", descriptionTrav='" + getDescriptionTrav() + "'" +
            "}";
    }
}
