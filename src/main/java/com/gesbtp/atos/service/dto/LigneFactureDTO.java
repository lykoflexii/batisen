package com.gesbtp.atos.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the LigneFacture entity.
 */
public class LigneFactureDTO implements Serializable {

    private Long id;

    @NotNull
    private String designation;

    @NotNull
    private Double prixUnitaire;

    @NotNull
    private Integer quantite;

    private Integer quantiteRetenue;

    private Long factureId;

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

    public Integer getQuantiteRetenue() {
        return quantiteRetenue;
    }

    public void setQuantiteRetenue(Integer quantiteRetenue) {
        this.quantiteRetenue = quantiteRetenue;
    }

    public Long getFactureId() {
        return factureId;
    }

    public void setFactureId(Long factureId) {
        this.factureId = factureId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LigneFactureDTO ligneFactureDTO = (LigneFactureDTO) o;
        if(ligneFactureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ligneFactureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LigneFactureDTO{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", quantite=" + getQuantite() +
            ", quantiteRetenue=" + getQuantiteRetenue() +
            "}";
    }
}
