package com.gesbtp.atos.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Devis entity.
 */
public class DevisDTO implements Serializable {

    private Long id;

    private String titre;

    @NotNull
    private LocalDate dateDeCreation;

    @NotNull
    private LocalDate validite;

    private Float tva;

    private Double coutMainDoeuvre;

    private Long clientId;

    private String clientNomClient;

    private Long chantierId;

    private String chantierNomChantier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getDateDeCreation() {
        return dateDeCreation;
    }

    public void setDateDeCreation(LocalDate dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
    }

    public LocalDate getValidite() {
        return validite;
    }

    public void setValidite(LocalDate validite) {
        this.validite = validite;
    }

    public Float getTva() {
        return tva;
    }

    public void setTva(Float tva) {
        this.tva = tva;
    }

    public Double getCoutMainDoeuvre() {
        return coutMainDoeuvre;
    }

    public void setCoutMainDoeuvre(Double coutMainDoeuvre) {
        this.coutMainDoeuvre = coutMainDoeuvre;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientNomClient() {
        return clientNomClient;
    }

    public void setClientNomClient(String clientNomClient) {
        this.clientNomClient = clientNomClient;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DevisDTO devisDTO = (DevisDTO) o;
        if(devisDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), devisDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DevisDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", dateDeCreation='" + getDateDeCreation() + "'" +
            ", validite='" + getValidite() + "'" +
            ", tva=" + getTva() +
            ", coutMainDoeuvre=" + getCoutMainDoeuvre() +
            "}";
    }
}
