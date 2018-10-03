package com.gesbtp.atos.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the LigneDevis entity.
 */
public class LigneDevisDTO implements Serializable {

    private Long id;

    @NotNull
    private String designation;

    @NotNull
    private Double prixUnitaire;

    @NotNull
    private Integer quantite;

    private Long devisId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Long getDevisId() {
        return devisId;
    }

    public void setDevisId(Long devisId) {
        this.devisId = devisId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LigneDevisDTO ligneDevisDTO = (LigneDevisDTO) o;
        if(ligneDevisDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ligneDevisDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LigneDevisDTO{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", quantite=" + getQuantite() +
            "}";
    }
}
