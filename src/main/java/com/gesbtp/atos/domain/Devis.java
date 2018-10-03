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
 * A Devis.
 */
@Entity
@Table(name = "devis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "devis")
public class Devis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "titre")
    private String titre;

    @NotNull
    @Column(name = "date_de_creation", nullable = false)
    private LocalDate dateDeCreation;

    @NotNull
    @Column(name = "validite", nullable = false)
    private LocalDate validite;

    @Column(name = "tva")
    private Float tva;

    @Column(name = "cout_main_doeuvre")
    private Double coutMainDoeuvre;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Chantier chantier;

    @OneToMany(mappedBy = "devis")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LigneDevis> devis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public Devis titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getDateDeCreation() {
        return dateDeCreation;
    }

    public Devis dateDeCreation(LocalDate dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
        return this;
    }

    public void setDateDeCreation(LocalDate dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
    }

    public LocalDate getValidite() {
        return validite;
    }

    public Devis validite(LocalDate validite) {
        this.validite = validite;
        return this;
    }

    public void setValidite(LocalDate validite) {
        this.validite = validite;
    }

    public Float getTva() {
        return tva;
    }

    public Devis tva(Float tva) {
        this.tva = tva;
        return this;
    }

    public void setTva(Float tva) {
        this.tva = tva;
    }

    public Double getCoutMainDoeuvre() {
        return coutMainDoeuvre;
    }

    public Devis coutMainDoeuvre(Double coutMainDoeuvre) {
        this.coutMainDoeuvre = coutMainDoeuvre;
        return this;
    }

    public void setCoutMainDoeuvre(Double coutMainDoeuvre) {
        this.coutMainDoeuvre = coutMainDoeuvre;
    }

    public Client getClient() {
        return client;
    }

    public Devis client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Chantier getChantier() {
        return chantier;
    }

    public Devis chantier(Chantier chantier) {
        this.chantier = chantier;
        return this;
    }

    public void setChantier(Chantier chantier) {
        this.chantier = chantier;
    }

    public Set<LigneDevis> getDevis() {
        return devis;
    }

    public Devis devis(Set<LigneDevis> ligneDevis) {
        this.devis = ligneDevis;
        return this;
    }

    public Devis addDevis(LigneDevis ligneDevis) {
        this.devis.add(ligneDevis);
        ligneDevis.setDevis(this);
        return this;
    }

    public Devis removeDevis(LigneDevis ligneDevis) {
        this.devis.remove(ligneDevis);
        ligneDevis.setDevis(null);
        return this;
    }

    public void setDevis(Set<LigneDevis> ligneDevis) {
        this.devis = ligneDevis;
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
        Devis devis = (Devis) o;
        if (devis.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), devis.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Devis{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", dateDeCreation='" + getDateDeCreation() + "'" +
            ", validite='" + getValidite() + "'" +
            ", tva=" + getTva() +
            ", coutMainDoeuvre=" + getCoutMainDoeuvre() +
            "}";
    }
}
