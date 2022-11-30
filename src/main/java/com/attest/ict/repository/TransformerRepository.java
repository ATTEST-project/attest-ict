package com.attest.ict.repository;

import com.attest.ict.domain.Transformer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transformer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransformerRepository extends JpaRepository<Transformer, Long>, JpaSpecificationExecutor<Transformer> {
    List<Transformer> findByNetworkId(Long networkId);
}
