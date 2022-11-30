package com.attest.ict.config;

import java.time.Duration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.attest.ict.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.attest.ict.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.attest.ict.domain.User.class.getName());
            createCache(cm, com.attest.ict.domain.Authority.class.getName());
            createCache(cm, com.attest.ict.domain.User.class.getName() + ".authorities");
            createCache(cm, com.attest.ict.domain.Network.class.getName());
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".buses");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".generators");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".branches");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".storages");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".transformers");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".capacitors");
            //createCache(cm, com.attest.ict.domain.Network.class.getName() + ".dbFiles");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".assetUgCables");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".assetTransformers");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".billingConsumptions");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".billingDers");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".lineCables");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".genProfiles");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".loadProfiles");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".flexProfiles");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".transfProfiles");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".topologyBuses");
            createCache(cm, com.attest.ict.domain.BaseMVA.class.getName());
            createCache(cm, com.attest.ict.domain.Bus.class.getName());
            createCache(cm, com.attest.ict.domain.BusName.class.getName());
            createCache(cm, com.attest.ict.domain.BusCoordinate.class.getName());
            createCache(cm, com.attest.ict.domain.BusExtension.class.getName());
            createCache(cm, com.attest.ict.domain.Branch.class.getName());
            createCache(cm, com.attest.ict.domain.LoadProfile.class.getName());
            createCache(cm, com.attest.ict.domain.LoadProfile.class.getName() + ".loadElVals");
            createCache(cm, com.attest.ict.domain.LoadElVal.class.getName());
            createCache(cm, com.attest.ict.domain.Generator.class.getName());
            createCache(cm, com.attest.ict.domain.GeneratorExtension.class.getName());
            createCache(cm, com.attest.ict.domain.GenTag.class.getName());
            createCache(cm, com.attest.ict.domain.GenProfile.class.getName());
            createCache(cm, com.attest.ict.domain.GenProfile.class.getName() + ".genElVals");
            createCache(cm, com.attest.ict.domain.GenElVal.class.getName());
            createCache(cm, com.attest.ict.domain.BillingDer.class.getName());
            createCache(cm, com.attest.ict.domain.Storage.class.getName());
            createCache(cm, com.attest.ict.domain.StorageCost.class.getName());
            createCache(cm, com.attest.ict.domain.ToolLogFile.class.getName());
            createCache(cm, com.attest.ict.domain.FlexProfile.class.getName());
            createCache(cm, com.attest.ict.domain.FlexProfile.class.getName() + ".flexElVals");
            createCache(cm, com.attest.ict.domain.FlexProfile.class.getName() + ".flexCosts");
            createCache(cm, com.attest.ict.domain.FlexElVal.class.getName());
            createCache(cm, com.attest.ict.domain.FlexCost.class.getName());
            createCache(cm, com.attest.ict.domain.TransfElVal.class.getName());
            createCache(cm, com.attest.ict.domain.GenCost.class.getName());
            createCache(cm, com.attest.ict.domain.ProtectionTool.class.getName());
            createCache(cm, com.attest.ict.domain.Transformer.class.getName());
            createCache(cm, com.attest.ict.domain.WeatherForecast.class.getName());
            createCache(cm, com.attest.ict.domain.CapacitorBankData.class.getName());
            createCache(cm, com.attest.ict.domain.LineCable.class.getName());
            createCache(cm, com.attest.ict.domain.AssetTransformer.class.getName());
            createCache(cm, com.attest.ict.domain.Node.class.getName());
            createCache(cm, com.attest.ict.domain.SolarData.class.getName());
            createCache(cm, com.attest.ict.domain.Price.class.getName());
            createCache(cm, com.attest.ict.domain.AssetUGCable.class.getName());
            createCache(cm, com.attest.ict.domain.Topology.class.getName());
            createCache(cm, com.attest.ict.domain.OutputFile.class.getName());
            createCache(cm, com.attest.ict.domain.TopologyBus.class.getName());
            createCache(cm, com.attest.ict.domain.VoltageLevel.class.getName());
            createCache(cm, com.attest.ict.domain.TransfProfile.class.getName());
            createCache(cm, com.attest.ict.domain.TransfProfile.class.getName() + ".transfElVals");
            createCache(cm, com.attest.ict.domain.WindData.class.getName());
            createCache(cm, com.attest.ict.domain.BillingConsumption.class.getName());
            createCache(cm, com.attest.ict.domain.BranchExtension.class.getName());
            createCache(cm, com.attest.ict.domain.Task.class.getName());
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".inputFiles");
            createCache(cm, com.attest.ict.domain.TopologyBus.class.getName() + ".topologies");
            createCache(cm, com.attest.ict.domain.InputFile.class.getName());
            createCache(cm, com.attest.ict.domain.DsoTsoConnection.class.getName());
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".dsoTsoConnections");
            createCache(cm, com.attest.ict.domain.Tool.class.getName());
            createCache(cm, com.attest.ict.domain.Tool.class.getName() + ".inputFiles");
            createCache(cm, com.attest.ict.domain.Tool.class.getName() + ".outputFiles");
            createCache(cm, com.attest.ict.domain.Tool.class.getName() + ".tasks");
            createCache(cm, com.attest.ict.domain.Generator.class.getName() + ".genElVals");
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".branchProfiles");
            createCache(cm, com.attest.ict.domain.Bus.class.getName() + ".loadELVals");
            createCache(cm, com.attest.ict.domain.Branch.class.getName() + ".transfElVals");
            createCache(cm, com.attest.ict.domain.Branch.class.getName() + ".branchElVals");
            createCache(cm, com.attest.ict.domain.BranchElVal.class.getName());
            createCache(cm, com.attest.ict.domain.BranchProfile.class.getName());
            createCache(cm, com.attest.ict.domain.BranchProfile.class.getName() + ".branchElVals");
            createCache(cm, com.attest.ict.domain.InputFile.class.getName() + ".simulations");
            createCache(cm, com.attest.ict.domain.Tool.class.getName() + ".parameters");
            createCache(cm, com.attest.ict.domain.Simulation.class.getName());
            createCache(cm, com.attest.ict.domain.Simulation.class.getName() + ".inputFiles");
            createCache(cm, com.attest.ict.domain.Simulation.class.getName() + ".outputFiles");
            createCache(cm, com.attest.ict.domain.ToolParameter.class.getName());
            createCache(cm, com.attest.ict.domain.Network.class.getName() + ".simulations");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
