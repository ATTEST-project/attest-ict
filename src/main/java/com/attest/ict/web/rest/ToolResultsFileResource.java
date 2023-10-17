package com.attest.ict.web.rest;

import com.attest.ict.service.OutputFileService;
import com.attest.ict.service.dto.custom.ToolResultsOutputFileDTO;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing tool's result.
 */
@RestController
@RequestMapping("/api")
public class ToolResultsFileResource {

    private final Logger log = LoggerFactory.getLogger(ToolResultsFileResource.class);

    private final OutputFileService outputFileService;

    public ToolResultsFileResource(OutputFileService outputFileService) {
        this.outputFileService = outputFileService;
    }

    /**
     * {@code GET  /tool-results} : retrieves a list of tool results' output files based on the provided filters.
     *
     * @param networkId    The ID of the network associated with the tool results.
     * @param toolId       The ID of the tool associated with the tool results.
     * @param fileName     The name of the output file to filter by. Pass {@code null} to ignore this filter.
     * @param dateTimeEnd  The end date and time to filter by. Pass {@code null} to ignore this filter.
     * @return A list of {@link ToolResultsOutputFileDTO} objects containing the filtered tool results' output files.
     */
    @GetMapping("/tool-results")
    public ResponseEntity<List<ToolResultsOutputFileDTO>> findToolResults(
        @RequestParam("networkId") Long networkId,
        @RequestParam("toolId") Long toolId,
        @RequestParam(name = "fileName", required = false) String fileName,
        @RequestParam(name = "dateTimeEnd", required = false) Instant dateTimeEnd
    ) {
        List<ToolResultsOutputFileDTO> results = outputFileService.findToolResults(networkId, toolId, fileName, dateTimeEnd);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
