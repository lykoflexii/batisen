package com.gesbtp.atos.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.gesbtp.atos.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Travaux.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Travaux.class.getName() + ".affectation2S", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Employe.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Employe.class.getName() + ".affectation1S", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Affectation.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Affectation.class.getName() + ".employes", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Chantier.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Chantier.class.getName() + ".affectation3S", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Client.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Client.class.getName() + ".clients", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Chantier.class.getName() + ".chantiers", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Devis.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Devis.class.getName() + ".devis", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.LigneDevis.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Client.class.getName() + ".client1S", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Entreprise.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Entreprise.class.getName() + ".users", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Entreprise.class.getName() + ".client2S", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Entreprise.class.getName() + ".clients", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Entreprise.class.getName() + ".employes", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Travaux.class.getName() + ".facture3S", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Chantier.class.getName() + ".facture2S", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Entreprise.class.getName() + ".facture4S", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Facture.class.getName(), jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.Facture.class.getName() + ".factures", jcacheConfiguration);
            cm.createCache(com.gesbtp.atos.domain.LigneFacture.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
