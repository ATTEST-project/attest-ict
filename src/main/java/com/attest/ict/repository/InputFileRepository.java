package com.attest.ict.repository;

import com.attest.ict.custom.query.ProfileCustomQuery;
import com.attest.ict.domain.InputFile;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InputFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InputFileRepository extends JpaRepository<InputFile, Long>, JpaSpecificationExecutor<InputFile> {
    //  @Query(value = "SELECT * FROM networks n where n.id=:networkId", nativeQuery = true)
    List<InputFile> findByNetworkId(Long networkId);

    // vecchio progetto input_files
    @Query(value = "SELECT file_name FROM input_file", nativeQuery = true)
    List<String> getAllFileNames();

    @Query(value = "SELECT f.id FROM input_file f WHERE f.file_name=:fileName", nativeQuery = true)
    Long getIdFileByName(String fileName);

    @Query(value = "SELECT f.file_name FROM input_file f WHERE f.network_id=:networkId", nativeQuery = true)
    List<String> getFileNamesByNetworkId(Long networkId);

    @Query(value = "SELECT * FROM input_file f WHERE f.network_id=:networkId", nativeQuery = true)
    List<InputFile> getFileList(Long networkId);

    Optional<InputFile> findTopByNetworkIdAndFileNameAndToolNameOrderByUploadTimeDesc(Long networId, String fileName, String toolName);

    Optional<InputFile> findTopByNetworkIdAndFileNameOrderByUploadTimeDesc(Long networId, String fileName);

    Optional<InputFile> findByNetworkIdAndFileName(Long networkId, String fileName);

    List<InputFile> findByNetworkIdAndToolIdIsNull(Long networkId);

    // Custom query
    @Query(value = ProfileCustomQuery.INPUT_FILE_BY_NETWORK, nativeQuery = true)
    List<Tuple> findAllProfileByNetworkId(@Param("networkId") Long networkId);

    // description can assume this values: 'all', 'generator', 'load',  'flexibility', 'dso_tso_connection', 'network'
    List<InputFile> findByNetworkIdAndDescription(Long networkId, String descr);

    // description can assume this values: 'all', 'generator', 'load',  'flexibility', 'dso_tso_connection', 'network'
    List<InputFile> findByNetworkIdAndFileNameAndDescription(Long networkId, String fileName, String description);
}
