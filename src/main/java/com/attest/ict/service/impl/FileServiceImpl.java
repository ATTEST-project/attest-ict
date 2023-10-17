package com.attest.ict.service.impl;

import com.attest.ict.domain.Branch;
import com.attest.ict.domain.Bus;
import com.attest.ict.domain.Generator;
import com.attest.ict.helper.ExcelHelper;
import com.attest.ict.repository.BranchRepository;
import com.attest.ict.repository.BusRepository;
import com.attest.ict.repository.GeneratorRepository;
import com.attest.ict.service.FileService;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    BusRepository busRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    GeneratorRepository generatorRepository;

    @Override
    public ByteArrayInputStream load(Date startDate, Date endDate) {
        List<Bus> buses = busRepository.findBusByNetworkTime(startDate, endDate);
        List<Branch> branches = branchRepository.findBranchByNetworkTime(startDate, endDate);
        List<Generator> generators = generatorRepository.findGeneratorByNetworkTime(startDate, endDate);
        ByteArrayInputStream in = ExcelHelper.appToExcel(buses, branches, generators);
        return in;
    }
}
