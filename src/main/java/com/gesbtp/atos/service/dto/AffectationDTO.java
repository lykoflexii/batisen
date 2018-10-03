package com.gesbtp.atos.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gesbtp.atos.domain.enumeration.EtatAffectation;

/**
 * A DTO for the Affectation entity.
 */
public class AffectationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    private EtatAffectation etat;

    private Long travauxId;

    private String travauxNomTrav;

    private Long chantierId;

    private String chantierNomChantier;

    private Set<EmployeDTO> employes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public EtatAffectation getEtat() {
        return etat;
    }

    public void setEtat(EtatAffectation etat) {
        this.etat = etat;
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

    public Set<EmployeDTO> getEmployes() {
        return employes;
    }

    public void setEmployes(Set<EmployeDTO> employes) {
        this.employes = employes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AffectationDTO affectationDTO = (AffectationDTO) o;
        if(affectationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), affectationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AffectationDTO{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
