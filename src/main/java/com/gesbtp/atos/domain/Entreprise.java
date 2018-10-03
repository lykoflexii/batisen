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
 * A Entreprise.
 */
@Entity
@Table(name = "entreprise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "entreprise")
public class Entreprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "commercial_name", nullable = false)
    private String commercialName;

    @NotNull
    @Column(name = "siren_number", nullable = false)
    private String sirenNumber;

    @NotNull
    @Column(name = "entreprise_admin_email", nullable = false)
    private String entrepriseAdminEmail;

    @Column(name = "telephone")
    private Integer telephone;

    @Column(name = "pack")
    private Integer pack;

    @OneToMany(mappedBy = "entreprise")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "entreprise")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Employe> employes = new HashSet<>();

    @OneToMany(mappedBy = "entreprise")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Facture> facture4S = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Entreprise name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public Entreprise commercialName(String commercialName) {
        this.commercialName = commercialName;
        return this;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getSirenNumber() {
        return sirenNumber;
    }

    public Entreprise sirenNumber(String sirenNumber) {
        this.sirenNumber = sirenNumber;
        return this;
    }

    public void setSirenNumber(String sirenNumber) {
        this.sirenNumber = sirenNumber;
    }

    public String getEntrepriseAdminEmail() {
        return entrepriseAdminEmail;
    }

    public Entreprise entrepriseAdminEmail(String entrepriseAdminEmail) {
        this.entrepriseAdminEmail = entrepriseAdminEmail;
        return this;
    }

    public void setEntrepriseAdminEmail(String entrepriseAdminEmail) {
        this.entrepriseAdminEmail = entrepriseAdminEmail;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public Entreprise telephone(Integer telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public Integer getPack() {
        return pack;
    }

    public Entreprise pack(Integer pack) {
        this.pack = pack;
        return this;
    }

    public void setPack(Integer pack) {
        this.pack = pack;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public Entreprise clients(Set<Client> clients) {
        this.clients = clients;
        return this;
    }

    public Entreprise addClient(Client client) {
        this.clients.add(client);
        client.setEntreprise(this);
        return this;
    }

    public Entreprise removeClient(Client client) {
        this.clients.remove(client);
        client.setEntreprise(null);
        return this;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<Employe> getEmployes() {
        return employes;
    }

    public Entreprise employes(Set<Employe> employes) {
        this.employes = employes;
        return this;
    }

    public Entreprise addEmploye(Employe employe) {
        this.employes.add(employe);
        employe.setEntreprise(this);
        return this;
    }

    public Entreprise removeEmploye(Employe employe) {
        this.employes.remove(employe);
        employe.setEntreprise(null);
        return this;
    }

    public void setEmployes(Set<Employe> employes) {
        this.employes = employes;
    }

    public Set<Facture> getFacture4S() {
        return facture4S;
    }

    public Entreprise facture4S(Set<Facture> factures) {
        this.facture4S = factures;
        return this;
    }

    public Entreprise addFacture4(Facture facture) {
        this.facture4S.add(facture);
        facture.setEntreprise(this);
        return this;
    }

    public Entreprise removeFacture4(Facture facture) {
        this.facture4S.remove(facture);
        facture.setEntreprise(null);
        return this;
    }

    public void setFacture4S(Set<Facture> factures) {
        this.facture4S = factures;
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
        Entreprise entreprise = (Entreprise) o;
        if (entreprise.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entreprise.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Entreprise{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", commercialName='" + getCommercialName() + "'" +
            ", sirenNumber='" + getSirenNumber() + "'" +
            ", entrepriseAdminEmail='" + getEntrepriseAdminEmail() + "'" +
            ", telephone=" + getTelephone() +
            ", pack=" + getPack() +
            "}";
    }
}
