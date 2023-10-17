package com.attest.ict.cimrepo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class CimRepoClient {

    private static final String NETWORKS_URL = "/branches";
    private static final String NETWORK_VERSIONS_URL = "/commits";
    private static final String NETWORK_BUSES_URL = "/buses";
    private static final String NETWORK_LINES_URL = "/lines";
    private static final String NETWORK_TRANSFORMERS_URL = "/trafos";
    private static final String NETWORK_GENERATORS_URL = "/esources";
    private static final String NETWORK_INJECTIONS_URL = "/extinjs";
    private static final String NETWORK_TOPOISLANDS_URL = "/topoislands";

    private final Logger log = LoggerFactory.getLogger(CimRepoClient.class);
    private final String baseUrl;

    public CimRepoClient(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl);
    }

    private HttpEntity<?> getRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        return new HttpEntity<>(headers);
    }

    public String getNetworks() {
        log.debug("Getting networks from CIM repository {}", baseUrl);
        String url = baseUrl + NETWORKS_URL;
        HttpEntity<?> requestEntity = getRequestEntity();
        RestTemplate restTemplate = new RestTemplate();
        log.debug("Calling url {}", url);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, Collections.emptyMap());
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("Cannot get networks from CIM Repository {}: http status {}", baseUrl, response.getStatusCode());
            throw new HttpClientErrorException(response.getStatusCode(), "Cannot get networks from CIM Repository " + baseUrl);
        }
    }

    public String getNetworkVersion(Long networkId) {
        Objects.requireNonNull(networkId);
        log.debug("Getting versions of network {} from CIM repository {}", networkId, baseUrl);
        String url = baseUrl + NETWORK_VERSIONS_URL;
        HttpEntity<?> requestEntity = getRequestEntity();
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).queryParam("branchid", "{branchid}").encode().toUriString();
        Map<String, Object> params = new HashMap<String, Object>() {
            {
                put("branchid", networkId);
            }
        };
        RestTemplate restTemplate = new RestTemplate();
        log.debug("Calling url {}", urlTemplate);
        ResponseEntity<String> response = restTemplate.exchange(urlTemplate, HttpMethod.GET, requestEntity, String.class, params);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error(
                "Cannot get versions of network {} from CIM Repository {}: http status {}",
                networkId,
                baseUrl,
                response.getStatusCode()
            );
            throw new HttpClientErrorException(
                response.getStatusCode(),
                "Cannot get versions of network " + networkId + " from CIM Repository " + baseUrl
            );
        }
    }

    private String getEquipments(String equipmentType, String url, Long networkId, Long versionId) {
        Objects.requireNonNull(networkId);
        Objects.requireNonNull(versionId);
        log.debug("Getting {} of network {} version {} from CIM repository {}", equipmentType, networkId, versionId, baseUrl);
        HttpEntity<?> requestEntity = getRequestEntity();
        String urlTemplate = UriComponentsBuilder
            .fromHttpUrl(url)
            .queryParam("branchid", "{branchid}")
            .queryParam("commitid", "{commitid}")
            .encode()
            .toUriString();
        Map<String, Object> params = new HashMap<String, Object>() {
            {
                put("branchid", networkId);
                put("commitid", versionId);
            }
        };
        RestTemplate restTemplate = new RestTemplate();
        log.debug("Calling url {}", urlTemplate);
        ResponseEntity<String> response = restTemplate.exchange(urlTemplate, HttpMethod.GET, requestEntity, String.class, params);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error(
                "Cannot get {} of network {} version {} from CIM repository {}: http status {}",
                equipmentType,
                networkId,
                versionId,
                baseUrl,
                response.getStatusCode()
            );
            throw new HttpClientErrorException(
                response.getStatusCode(),
                "Cannot get " + equipmentType + " of network " + networkId + " version " + versionId + " from CIM Repository " + baseUrl
            );
        }
    }

    public String getBuses(Long networkId, Long versionId) {
        return getEquipments("buses", baseUrl + NETWORK_BUSES_URL, networkId, versionId);
    }

    public String getLines(Long networkId, Long versionId) {
        return getEquipments("lines", baseUrl + NETWORK_LINES_URL, networkId, versionId);
    }

    public String getTransformers(Long networkId, Long versionId) {
        return getEquipments("transformers", baseUrl + NETWORK_TRANSFORMERS_URL, networkId, versionId);
    }

    public String getGenerators(Long networkId, Long versionId) {
        return getEquipments("generators", baseUrl + NETWORK_GENERATORS_URL, networkId, versionId);
    }

    public String getInjections(Long networkId, Long versionId) {
        return getEquipments("injections", baseUrl + NETWORK_INJECTIONS_URL, networkId, versionId);
    }

    public String getTopoIslands(Long networkId, Long versionId) {
        return getEquipments("injections", baseUrl + NETWORK_TOPOISLANDS_URL, networkId, versionId);
    }
}
