package com.gesbtp.atos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.gesbtp.atos.domain.enumeration.Sexe;

/**
 * A Employe.
 */
@Entity
@Table(name = "employe")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employe")
public class Employe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nom_employe", length = 50, nullable = false)
    private String nomEmploye;

    @NotNull
    @Size(max = 50)
    @Column(name = "prenom_employe", length = 50, nullable = false)
    private String prenomEmploye;

    @NotNull
    @Column(name = "matricule", nullable = false)
    private String matricule;

    @NotNull
    @Column(name = "fonction", nullable = false)
    private String fonction;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "telephone_employe")
    private String telephoneEmploye;

    @Column(name = "salaire")
    private Double salaire;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private Sexe sexe;

    @ManyToOne
    private Entreprise entreprise;

    @ManyToMany(mappedBy = "employes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Affectation> affectation1S = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEmploye() {
        return nomEmploye;
    }

    public Employe nomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
        return this;
    }

    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }

    public String getPrenomEmploye() {
        return prenomEmploye;
    }

    public Employe prenomEmploye(String prenomEmploye) {
        this.prenomEmploye = prenomEmploye;
        return this;
    }

    public void setPrenomEmploye(String prenomEmploye) {
        this.prenomEmploye = prenomEmploye;
    }

    public String getMatricule() {
        return matricule;
    }

    public Employe matricule(String matricule) {
        this.matricule = matricule;
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getFonction() {
        return fonction;
    }

    public Employe fonction(String fonction) {
        this.fonction = fonction;
        return this;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public Employe dateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephoneEmploye() {
        return telephoneEmploye;
    }

    public Employe telephoneEmploye(String telephoneEmploye) {
        this.telephoneEmploye = telephoneEmploye;
        return this;
    }

    public void setTelephoneEmploye(String telephoneEmploye) {
        this.telephoneEmploye = telephoneEmploye;
    }

    public Double getSalaire() {
        return salaire;
    }

    public Employe salaire(Double salaire) {
        this.salaire = salaire;
        return this;
    }

    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public Employe sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public Employe entreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
        return this;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Set<Affectation> getAffectation1S() {
        return affectation1S;
    }

    public Employe affectation1S(Set<Affectation> affectations) {
        this.affectation1S = affectations;
        return this;
    }

    public Employe addAffectation1(Affectation affectation) {
        this.affectation1S.add(affectation);
        affectation.getEmployes().add(this);
        return this;
    }

    public Employe removeAffectation1(Affectation affectation) {
        this.affectation1S.remove(affectation);
        affectation.getEmployes().remove(this);
        return this;
    }

    public void setAffectation1S(Set<Affectation> affectations) {
        this.affectation1S = affectations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employe employe = (Employe) o;
        if (employe.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employe.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Employe{" +
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
