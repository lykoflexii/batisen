package com.gesbtp.atos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Travaux.
 */
@Entity
@Table(name = "travaux")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "travaux")
public class Travaux implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nom_trav", nullable = false)
    private String nomTrav;

    @Size(max = 200)
    @Column(name = "description_trav", length = 200)
    private String descriptionTrav;

    @OneToMany(mappedBy = "travaux")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Affectation> affectation2S = new HashSet<>();

    @OneToMany(mappedBy = "travaux")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Facture> facture3S = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomTrav() {
        return nomTrav;
    }

    public Travaux nomTrav(String nomTrav) {
        this.nomTrav = nomTrav;
        return this;
    }

    public void setNomTrav(String nomTrav) {
        this.nomTrav = nomTrav;
    }

    public String getDescriptionTrav() {
        return descriptionTrav;
    }

    public Travaux descriptionTrav(String descriptionTrav) {
        this.descriptionTrav = descriptionTrav;
        return this;
    }

    public void setDescriptionTrav(String descriptionTrav) {
        this.descriptionTrav = descriptionTrav;
    }

    public Set<Affectation> getAffectation2S() {
        return affectation2S;
    }

    public Travaux affectation2S(Set<Affectation> affectations) {
        this.affectation2S = affectations;
        return this;
    }

    public Travaux addAffectation2(Affectation affectation) {
        this.affectation2S.add(affectation);
        affectation.setTravaux(this);
        return this;
    }

    public Travaux removeAffectation2(Affectation affectation) {
        this.affectation2S.remove(affectation);
        affectation.setTravaux(null);
        return this;
    }

    public void setAffectation2S(Set<Affectation> affectations) {
        this.affectation2S = affectations;
    }

    public Set<Facture> getFacture3S() {
        return facture3S;
    }

    public Travaux facture3S(Set<Facture> factures) {
        this.facture3S = factures;
        return this;
    }

    public Travaux addFacture3(Facture facture) {
        this.facture3S.add(facture);
        facture.setTravaux(this);
        return this;
    }

    public Travaux removeFacture3(Facture facture) {
        this.facture3S.remove(facture);
        facture.setTravaux(null);
        return this;
    }

    public void setFacture3S(Set<Facture> factures) {
        this.facture3S = factures;
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
        Travaux travaux = (Travaux) o;
        if (travaux.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), travaux.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Travaux{" +
            "id=" + getId() +
            ", nomTrav='" + getNomTrav() + "'" +
            ", descriptionTrav='" + getDescriptionTrav() + "'" +
            "}";
    }
}
