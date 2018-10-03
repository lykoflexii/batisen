package com.gesbtp.atos.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gesbtp.atos.domain.enumeration.EtatChantier;

/**
 * A DTO for the Chantier entity.
 */
public class ChantierDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nomChantier;

    @Size(max = 200)
    private String descriptionChantier;

    @NotNull
    private String ville;

    @NotNull
    @Size(max = 100)
    private String adresse;

    private EtatChantier etatChantier;

    private LocalDate dateDebutReelle;

    private LocalDate dateFinReelle;

    @NotNull
    private LocalDate dateDebutPrevu;

    @NotNull
    private LocalDate dateFinPrevu;

    private Long clientId;

    private String clientNomClient;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomChantier() {
        return nomChantier;
    }

    public void setNomChantier(String nomChantier) {
        this.nomChantier = nomChantier;
    }

    public String getDescriptionChantier() {
        return descriptionChantier;
    }

    public void setDescriptionChantier(String descriptionChantier) {
        this.descriptionChantier = descriptionChantier;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public EtatChantier getEtatChantier() {
        return etatChantier;
    }

    public void setEtatChantier(EtatChantier etatChantier) {
        this.etatChantier = etatChantier;
    }

    public LocalDate getDateDebutReelle() {
        return dateDebutReelle;
    }

    public void setDateDebutReelle(LocalDate dateDebutReelle) {
        this.dateDebutReelle = dateDebutReelle;
    }

    public LocalDate getDateFinReelle() {
        return dateFinReelle;
    }

    public void setDateFinReelle(LocalDate dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }

    public LocalDate getDateDebutPrevu() {
        return dateDebutPrevu;
    }

    public void setDateDebutPrevu(LocalDate dateDebutPrevu) {
        this.dateDebutPrevu = dateDebutPrevu;
    }

    public LocalDate getDateFinPrevu() {
        return dateFinPrevu;
    }

    public void setDateFinPrevu(LocalDate dateFinPrevu) {
        this.dateFinPrevu = dateFinPrevu;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChantierDTO chantierDTO = (ChantierDTO) o;
        if(chantierDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chantierDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChantierDTO{" +
            "id=" + getId() +
            ", nomChantier='" + getNomChantier() + "'" +
            ", descriptionChantier='" + getDescriptionChantier() + "'" +
            ", ville='" + getVille() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", etatChantier='" + getEtatChantier() + "'" +
            ", dateDebutReelle='" + getDateDebutReelle() + "'" +
            ", dateFinReelle='" + getDateFinReelle() + "'" +
            ", dateDebutPrevu='" + getDateDebutPrevu() + "'" +
            ", dateFinPrevu='" + getDateFinPrevu() + "'" +
            "}";
    }
}
