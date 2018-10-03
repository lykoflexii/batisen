package com.gesbtp.atos.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Facture entity.
 */
public class FactureDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateCreation;

    private Double remise;

    private Boolean valider;

    private Long chantierId;

    private String chantierNomChantier;

    private Long travauxId;

    private String travauxNomTrav;

    private Long entrepriseId;

    private String entrepriseName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getRemise() {
        return remise;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public Boolean isValider() {
        return valider;
    }

    public void setValider(Boolean valider) {
        this.valider = valider;
    }

    public Long getChantierId() {
        return chantierId;
    }

    public void setChantierId(Long chantierId) {
        this.chantierId = chantierId;
    }

    public String getChantierNomChantier() {
        return chantierNomChantier;
    }

    public void setChantierNomChantier(String chantierNomChantier) {
        this.chantierNomChantier = chantierNomChantier;
    }

    public Long getTravauxId() {
        return travauxId;
    }

    public void setTravauxId(Long travauxId) {
        this.travauxId = travauxId;
    }

    public String getTravauxNomTrav() {
        return travauxNomTrav;
    }

    public void setTravauxNomTrav(String travauxNomTrav) {
        this.travauxNomTrav = travauxNomTrav;
    }

    public Long getEntrepriseId() {
        return entrepriseId;
    }

    public void setEntrepriseId(Long entrepriseId) {
        this.entrepriseId = entrepriseId;
    }

    public String getEntrepriseName() {
        return entrepriseName;
    }

    public void setEntrepriseName(String entrepriseName) {
        this.entrepriseName = entrepriseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FactureDTO factureDTO = (FactureDTO) o;
        if(factureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), factureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FactureDTO{" +
            "id=" + getId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", remise=" + getRemise() +
            ", valider='" + isValider() + "'" +
            "}";
    }
}
