package com.attest.ict.custom.query;

import com.attest.ict.constants.AttestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileCustomQuery {

    private final Logger log = LoggerFactory.getLogger(ProfileCustomQuery.class);

    public static final String INPUT_FILE_BY_NETWORK =
        "" +
        " SELECT inf.id, \"\" as season, \"\" as typical_day, null as mode ,null as time_interval , inf.file_name, inf.description " +
        " FROM input_file inf " +
        " WHERE inf.network_id = :networkId " +
        " AND inf.description = '" +
        AttestConstants.INPUT_FILE_NETWORK_DESCR +
        "' " +
        " UNION DISTINCT " +
        " SELECT inf.id, p.season, p.typical_day, p.mode, p.time_interval,  inf.file_name, inf.description " +
        " FROM input_file inf " +
        " INNER JOIN load_profile p ON p.input_file_id = inf.id " +
        " WHERE inf.network_id = :networkId " +
        " UNION DISTINCT " +
        " SELECT inf.id, p.season, p.typical_day, p.mode, p.time_interval,  inf.file_name, inf.description " +
        " FROM input_file inf " +
        " INNER JOIN gen_profile p ON p.input_file_id = inf.id " +
        " WHERE inf.network_id = :networkId " +
        " UNION DISTINCT " +
        " SELECT inf.id, p.season, p.typical_day, p.mode, p.time_interval,  inf.file_name, inf.description " +
        " FROM input_file inf " +
        " INNER JOIN flex_profile p ON p.input_file_id = inf.id " +
        " WHERE inf.network_id = :networkId " +
        " ORDER BY 6,1,2,3,4 ";
}
