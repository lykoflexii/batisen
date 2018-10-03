package com.gesbtp.atos.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gesbtp.atos.domain.enumeration.Sexe;

/**
 * A DTO for the Employe entity.
 */
public class EmployeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nomEmploye;

    @NotNull
    @Size(max = 50)
    private String prenomEmploye;

    @NotNull
    private String matricule;

    @NotNull
    private String fonction;

    private LocalDate dateNaissance;

    private String telephoneEmploye;

    private Double salaire;

    private Sexe sexe;

    private Long entrepriseId;

    private String entrepriseName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEmploye() {
        return nomEmploye;
    }

    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }

    public String getPrenomEmploye() {
        return prenomEmploye;
    }

    public void setPrenomEmploye(String prenomEmploye) {
        this.prenomEmploye = prenomEmploye;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephoneEmploye() {
        return telephoneEmploye;
    }

    public void setTelephoneEmploye(String telephoneEmploye) {
        this.telephoneEmploye = telephoneEmploye;
    }

    public Double getSalaire() {
        return salaire;
    }

    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
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

        EmployeDTO employeDTO = (EmployeDTO) o;
        if(employeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmployeDTO{" +
            "id=" + getId() +
            ", nomEmploye='" + getNomEmploye() + "'" +
            ", prenomEmploye='" + getPrenomEmploye() + "'" +
            ", matricule='" + getMatricule() + "'" +
            ", fonction='" + getFonction() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", telephoneEmploye='" + getTelephoneEmploye() + "'" +
            ", salaire=" + getSalaire() +
            ", sexe='" + getSexe() + "'" +
            "}";
    }
}
