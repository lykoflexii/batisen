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

import com.gesbtp.atos.domain.enumeration.Type;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nom_client", length = 50, nullable = false)
    private String nomClient;

    @NotNull
    @Size(max = 50)
    @Column(name = "ville_client", length = 50, nullable = false)
    private String villeClient;

    @NotNull
    @Size(max = 100)
    @Column(name = "adresse_client", length = 100, nullable = false)
    private String adresseClient;

    @Column(name = "telephone_client")
    private String telephoneClient;

    @Column(name = "fax")
    private String fax;

    @NotNull
    @Column(name = "email_client", nullable = false)
    private String emailClient;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_client", nullable = false)
    private Type typeClient;

    @Column(name = "prenom_client")
    private String prenomClient;

    @Column(name = "nom_commercial")
    private String nomCommercial;

    @ManyToOne
    private Entreprise entreprise;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Chantier> clients = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Devis> client1S = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public Client nomClient(String nomClient) {
        this.nomClient = nomClient;
        return this;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getVilleClient() {
        return villeClient;
    }

    public Client villeClient(String villeClient) {
        this.villeClient = villeClient;
        return this;
    }

    public void setVilleClient(String villeClient) {
        this.villeClient = villeClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public Client adresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
        return this;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public Client telephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
        return this;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    public String getFax() {
        return fax;
    }

    public Client fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public Client emailClient(String emailClient) {
        this.emailClient = emailClient;
        return this;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public Type getTypeClient() {
        return typeClient;
    }

    public Client typeClient(Type typeClient) {
        this.typeClient = typeClient;
        return this;
    }

    public void setTypeClient(Type typeClient) {
        this.typeClient = typeClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public Client prenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
        return this;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getNomCommercial() {
        return nomCommercial;
    }

    public Client nomCommercial(String nomCommercial) {
        this.nomCommercial = nomCommercial;
        return this;
    }

    public void setNomCommercial(String nomCommercial) {
        this.nomCommercial = nomCommercial;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public Client entreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
        return this;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Set<Chantier> getClients() {
        return clients;
    }

    public Client clients(Set<Chantier> chantiers) {
        this.clients = chantiers;
        return this;
    }

    public Client addClient(Chantier chantier) {
        this.clients.add(chantier);
        chantier.setClient(this);
        return this;
    }

    public Client removeClient(Chantier chantier) {
        this.clients.remove(chantier);
        chantier.setClient(null);
        return this;
    }

    public void setClients(Set<Chantier> chantiers) {
        this.clients = chantiers;
    }

    public Set<Devis> getClient1S() {
        return client1S;
    }

    public Client client1S(Set<Devis> devis) {
        this.client1S = devis;
        return this;
    }

    public Client addClient1(Devis devis) {
        this.client1S.add(devis);
        devis.setClient(this);
        return this;
    }

    public Client removeClient1(Devis devis) {
        this.client1S.remove(devis);
        devis.setClient(null);
        return this;
    }

    public void setClient1S(Set<Devis> devis) {
        this.client1S = devis;
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
        Client client = (Client) o;
        if (client.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", nomClient='" + getNomClient() + "'" +
            ", villeClient='" + getVilleClient() + "'" +
            ", adresseClient='" + getAdresseClient() + "'" +
            ", telephoneClient='" + getTelephoneClient() + "'" +
            ", fax='" + getFax() + "'" +
            ", emailClient='" + getEmailClient() + "'" +
            ", typeClient='" + getTypeClient() + "'" +
            ", prenomClient='" + getPrenomClient() + "'" +
            ", nomCommercial='" + getNomCommercial() + "'" +
            "}";
    }
}
