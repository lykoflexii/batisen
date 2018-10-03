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

import com.gesbtp.atos.domain.enumeration.EtatChantier;

/**
 * A Chantier.
 */
@Entity
@Table(name = "chantier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "chantier")
public class Chantier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nom_chantier", length = 50, nullable = false)
    private String nomChantier;

    @Size(max = 200)
    @Column(name = "description_chantier", length = 200)
    private String descriptionChantier;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @NotNull
    @Size(max = 100)
    @Column(name = "adresse", length = 100, nullable = false)
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_chantier")
    private EtatChantier etatChantier;

    @Column(name = "date_debut_reelle")
    private LocalDate dateDebutReelle;

    @Column(name = "date_fin_reelle")
    private LocalDate dateFinReelle;

    @NotNull
    @Column(name = "date_debut_prevu", nullable = false)
    private LocalDate dateDebutPrevu;

    @NotNull
    @Column(name = "date_fin_prevu", nullable = false)
    private LocalDate dateFinPrevu;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "chantier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Affectation> affectation3S = new HashSet<>();

    @OneToMany(mappedBy = "chantier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Devis> chantiers = new HashSet<>();

    @OneToMany(mappedBy = "chantier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Facture> facture2S = new HashSet<>();

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomChantier() {
        return nomChantier;
    }

    public Chantier nomChantier(String nomChantier) {
        this.nomChantier = nomChantier;
        return this;
    }

    public void setNomChantier(String nomChantier) {
        this.nomChantier = nomChantier;
    }

    public String getDescriptionChantier() {
        return descriptionChantier;
    }

    public Chantier descriptionChantier(String descriptionChantier) {
        this.descriptionChantier = descriptionChantier;
        return this;
    }

    public void setDescriptionChantier(String descriptionChantier) {
        this.descriptionChantier = descriptionChantier;
    }

    public String getVille() {
        return ville;
    }

    public Chantier ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public Chantier adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public EtatChantier getEtatChantier() {
        return etatChantier;
    }

    public Chantier etatChantier(EtatChantier etatChantier) {
        this.etatChantier = etatChantier;
        return this;
    }

    public void setEtatChantier(EtatChantier etatChantier) {
        this.etatChantier = etatChantier;
    }

    public LocalDate getDateDebutReelle() {
        return dateDebutReelle;
    }

    public Chantier dateDebutReelle(LocalDate dateDebutReelle) {
        this.dateDebutReelle = dateDebutReelle;
        return this;
    }

    public void setDateDebutReelle(LocalDate dateDebutReelle) {
        this.dateDebutReelle = dateDebutReelle;
    }

    public LocalDate getDateFinReelle() {
        return dateFinReelle;
    }

    public Chantier dateFinReelle(LocalDate dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
        return this;
    }

    public void setDateFinReelle(LocalDate dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }

    public LocalDate getDateDebutPrevu() {
        return dateDebutPrevu;
    }

    public Chantier dateDebutPrevu(LocalDate dateDebutPrevu) {
        this.dateDebutPrevu = dateDebutPrevu;
        return this;
    }

    public void setDateDebutPrevu(LocalDate dateDebutPrevu) {
        this.dateDebutPrevu = dateDebutPrevu;
    }

    public LocalDate getDateFinPrevu() {
        return dateFinPrevu;
    }

    public Chantier dateFinPrevu(LocalDate dateFinPrevu) {
        this.dateFinPrevu = dateFinPrevu;
        return this;
    }

    public void setDateFinPrevu(LocalDate dateFinPrevu) {
        this.dateFinPrevu = dateFinPrevu;
    }

    public Client getClient() {
        return client;
    }

    public Chantier client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Affectation> getAffectation3S() {
        return affectation3S;
    }

    public Chantier affectation3S(Set<Affectation> affectations) {
        this.affectation3S = affectations;
        return this;
    }

    public Chantier addAffectation3(Affectation affectation) {
        this.affectation3S.add(affectation);
        affectation.setChantier(this);
        return this;
    }

    public Chantier removeAffectation3(Affectation affectation) {
        this.affectation3S.remove(affectation);
        affectation.setChantier(null);
        return this;
    }

    public void setAffectation3S(Set<Affectation> affectations) {
        this.affectation3S = affectations;
    }

    public Set<Devis> getChantiers() {
        return chantiers;
    }

    public Chantier chantiers(Set<Devis> devis) {
        this.chantiers = devis;
        return this;
    }

    public Chantier addChantier(Devis devis) {
        this.chantiers.add(devis);
        devis.setChantier(this);
        return this;
    }

    public Chantier removeChantier(Devis devis) {
        this.chantiers.remove(devis);
        devis.setChantier(null);
        return this;
    }

    public void setChantiers(Set<Devis> devis) {
        this.chantiers = devis;
    }

    public Set<Facture> getFacture2S() {
        return facture2S;
    }

    public Chantier facture2S(Set<Facture> factures) {
        this.facture2S = factures;
        return this;
    }

    public Chantier addFacture2(Facture facture) {
        this.facture2S.add(facture);
        facture.setChantier(this);
        return this;
    }

    public Chantier removeFacture2(Facture facture) {
        this.facture2S.remove(facture);
        facture.setChantier(null);
        return this;
    }

    public void setFacture2S(Set<Facture> factures) {
        this.facture2S = factures;
    }

    public User getUser() {
        return user;
    }

    public Chantier user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        Chantier chantier = (Chantier) o;
        if (chantier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chantier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Chantier{" +
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
