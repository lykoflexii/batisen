package com.gesbtp.atos.domain;

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

import com.gesbtp.atos.domain.enumeration.EtatAffectation;

/**
 * A Affectation.
 */
@Entity
@Table(name = "affectation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "affectation")
public class Affectation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatAffectation etat;

    @ManyToOne
    private Travaux travaux;

    @ManyToOne
    private Chantier chantier;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "affectation_employe",
               joinColumns = @JoinColumn(name="affectations_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="employes_id", referencedColumnName="id"))
    private Set<Employe> employes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public Affectation dateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Affectation dateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public EtatAffectation getEtat() {
        return etat;
    }

    public Affectation etat(EtatAffectation etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(EtatAffectation etat) {
        this.etat = etat;
    }

    public Travaux getTravaux() {
        return travaux;
    }

    public Affectation travaux(Travaux travaux) {
        this.travaux = travaux;
        return this;
    }

    public void setTravaux(Travaux travaux) {
        this.travaux = travaux;
    }

    public Chantier getChantier() {
        return chantier;
    }

    public Affectation chantier(Chantier chantier) {
        this.chantier = chantier;
        return this;
    }

    public void setChantier(Chantier chantier) {
        this.chantier = chantier;
    }

    public Set<Employe> getEmployes() {
        return employes;
    }

    public Affectation employes(Set<Employe> employes) {
        this.employes = employes;
        return this;
    }

    public Affectation addEmploye(Employe employe) {
        this.employes.add(employe);
        employe.getAffectation1S().add(this);
        return this;
    }

    public Affectation removeEmploye(Employe employe) {
        this.employes.remove(employe);
        employe.getAffectation1S().remove(this);
        return this;
    }

    public void setEmployes(Set<Employe> employes) {
        this.employes = employes;
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
        Affectation affectation = (Affectation) o;
        if (affectation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), affectation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Affectation{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
