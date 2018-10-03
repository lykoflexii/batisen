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

/**
 * A Facture.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "facture")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @Column(name = "remise")
    private Double remise;

    @Column(name = "valider")
    private Boolean valider;

    @ManyToOne
    private Chantier chantier;

    @ManyToOne
    private Travaux travaux;

    @ManyToOne
    private Entreprise entreprise;

    @OneToMany(mappedBy = "facture")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LigneFacture> factures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public Facture dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getRemise() {
        return remise;
    }

    public Facture remise(Double remise) {
        this.remise = remise;
        return this;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public Boolean isValider() {
        return valider;
    }

    public Facture valider(Boolean valider) {
        this.valider = valider;
        return this;
    }

    public void setValider(Boolean valider) {
        this.valider = valider;
    }

    public Chantier getChantier() {
        return chantier;
    }

    public Facture chantier(Chantier chantier) {
        this.chantier = chantier;
        return this;
    }

    public void setChantier(Chantier chantier) {
        this.chantier = chantier;
    }

    public Travaux getTravaux() {
        return travaux;
    }

    public Facture travaux(Travaux travaux) {
        this.travaux = travaux;
        return this;
    }

    public void setTravaux(Travaux travaux) {
        this.travaux = travaux;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public Facture entreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
        return this;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Set<LigneFacture> getFactures() {
        return factures;
    }

    public Facture factures(Set<LigneFacture> ligneFactures) {
        this.factures = ligneFactures;
        return this;
    }

    public Facture addFacture(LigneFacture ligneFacture) {
        this.factures.add(ligneFacture);
        ligneFacture.setFacture(this);
        return this;
    }

    public Facture removeFacture(LigneFacture ligneFacture) {
        this.factures.remove(ligneFacture);
        ligneFacture.setFacture(null);
        return this;
    }

    public void setFactures(Set<LigneFacture> ligneFactures) {
        this.factures = ligneFactures;
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
        Facture facture = (Facture) o;
        if (facture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), facture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", remise=" + getRemise() +
            ", valider='" + isValider() + "'" +
            "}";
    }
}
