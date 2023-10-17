package com.attest.ict.service;

import com.attest.ict.service.dto.custom.CimRepoNetworkDTO;
import com.attest.ict.service.dto.custom.CimRepositoryNetworkDTO;
import com.attest.ict.service.dto.custom.CimRepositoryNetworkVersionDTO;
import java.io.IOException;
import java.util.List;

/**
 * Service Interface for managing CIM repository networks and data.
 */
public interface CimRepoService {
    /**
     * get networks stored in CIM repository
     *
     * @return the list of networks
     * @throws IOException
     */
    List<CimRepositoryNetworkDTO> getNetworks() throws IOException;

    /**
     * Get network versions stored in CIM repository
     *
     * @param networkId
     * @return the list of network versions
     * @throws IOException
     */
    List<CimRepositoryNetworkVersionDTO> getNetworkVersions(Long networkId) throws IOException;

    /**
     * Import network from CIM repository to ATTEST database
     *
     * @param cimRepoNetworkDTO the network to import
     * @return the list of ATTEST database ids of networks imported
     * @throws IOException
     */
    List<Integer> importNetwork(CimRepoNetworkDTO cimRepoNetworkDTO) throws IOException;
}
