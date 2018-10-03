package com.gesbtp.atos.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LigneFacture.
 */
@Entity
@Table(name = "ligne_facture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lignefacture")
public class LigneFacture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @NotNull
    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "quantite_retenue")
    private Integer quantiteRetenue;

    @ManyToOne
    private Facture facture;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public LigneFacture designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public LigneFacture prixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        return this;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public LigneFacture quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getQuantiteRetenue() {
        return quantiteRetenue;
    }

    public LigneFacture quantiteRetenue(Integer quantiteRetenue) {
        this.quantiteRetenue = quantiteRetenue;
        return this;
    }

    public void setQuantiteRetenue(Integer quantiteRetenue) {
        this.quantiteRetenue = quantiteRetenue;
    }

    public Facture getFacture() {
        return facture;
    }

    public LigneFacture facture(Facture facture) {
        this.facture = facture;
        return this;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
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
        LigneFacture ligneFacture = (LigneFacture) o;
        if (ligneFacture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ligneFacture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LigneFacture{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", quantite=" + getQuantite() +
            ", quantiteRetenue=" + getQuantiteRetenue() +
            "}";
    }
}
