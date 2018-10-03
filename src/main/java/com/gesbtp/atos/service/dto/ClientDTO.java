package com.gesbtp.atos.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gesbtp.atos.domain.enumeration.Type;

/**
 * A DTO for the Client entity.
 */
public class ClientDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String nomClient;

    @NotNull
    @Size(max = 50)
    private String villeClient;

    @NotNull
    @Size(max = 100)
    private String adresseClient;

    private String telephoneClient;

    private String fax;

    @NotNull
    private String emailClient;

    @NotNull
    private Type typeClient;

    private String prenomClient;

    private String nomCommercial;

    private Long entrepriseId;

    private String entrepriseName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getVilleClient() {
        return villeClient;
    }

    public void setVilleClient(String villeClient) {
        this.villeClient = villeClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public Type getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(Type typeClient) {
        this.typeClient = typeClient;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getNomCommercial() {
        return nomCommercial;
    }

    public void setNomCommercial(String nomCommercial) {
        this.nomCommercial = nomCommercial;
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

        ClientDTO clientDTO = (ClientDTO) o;
        if(clientDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clientDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
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
