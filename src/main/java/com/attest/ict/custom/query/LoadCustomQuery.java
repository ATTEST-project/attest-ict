package com.attest.ict.custom.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadCustomQuery {

    private final Logger log = LoggerFactory.getLogger(LoadCustomQuery.class);

    public static final String LOAD_P_Q_GROUP_BY_SEASON =
        "	" +
        " SELECT lp.season AS season, lev.bus_id AS busId, lev.hour AS hh, lev.min AS mm, SUM(P) AS P, SUM(Q) AS Q " +
        " FROM   load_el_val  lev 													 " +
        " INNER JOIN load_profile lp ON lp.id= lev.load_profile_id 					 " +
        " WHERE lp.network_id= :networkId 										 	 " +
        " AND  bus_id= :busId					 							 	 " +
        " AND  lp.mode in (2,4)													     " + //2 = a representative business day for a season, 4 = a representative weekend for a season
        " GROUP BY lp.season, lev.bus_id, lev.hour, lev.min 						 " +
        " ORDER BY lp.season, lev.bus_id, lev.hour, lev.min                         ";

    public static final String LOAD_P_Q_GROUP_BY_SEASON_FOR_TYPICAL_DAY =
        "	" +
        " SELECT lp.season AS season, lev.bus_id AS busId, lev.hour AS hh, lev.min AS mm, SUM(P) AS P, SUM(Q) AS Q " +
        " FROM   load_el_val  lev 													 " +
        " INNER JOIN load_profile lp ON lp.id= lev.load_profile_id 					 " +
        " WHERE lp.network_id= :networkId 											 " +
        " AND  bus_id= :busId						 								 " +
        " AND  lp.mode in (2,4)														 " + //2 = a representative business day for a season, 4 = a representative weekend for a season
        " AND  lp.typical_day= :typicalDay                                           " +
        " GROUP BY lp.season, lev.bus_id, lev.hour, lev.min 						 " +
        " ORDER BY lp.season, lev.bus_id, lev.hour, lev.min                         ";

    public static final String LOAD_P_Q_GROUP_BY_TYPICAL_DAY =
        "	" +
        " SELECT lp.typical_day AS day, lev.bus_id AS busId, lev.hour AS hh, lev.min AS mm, SUM(P) AS P, SUM(Q) AS Q " +
        " FROM   load_el_val  lev 													 " +
        " INNER JOIN load_profile lp ON lp.id= lev.load_profile_id 					 " +
        " WHERE lp.network_id= :networkId 											 " +
        " AND  bus_id= :busId						 								 " +
        " AND  lp.mode in (2,4)													     " + //2 = a representative business day for a season, 4 = a representative weekend for a season
        " GROUP BY lp.typical_day, lev.bus_id, lev.hour, lev.min 					 " +
        " ORDER BY lp.typical_day, lev.bus_id, lev.hour, lev.min                    ";

    public static final String LOAD_P_Q_GROUP_BY_TYPICAL_DAY_FOR_SEASON =
        "	" +
        " SELECT lp.typical_day AS day, lev.bus_id AS busId, lev.hour AS hh, lev.min AS mm, SUM(P) AS P, SUM(Q) AS Q " +
        " FROM   load_el_val  lev 													 " +
        " INNER JOIN load_profile lp ON lp.id= lev.load_profile_id 					 " +
        " WHERE lp.network_id= :networkId 					 						 " +
        " AND  bus_id= :busId						 								 " +
        " AND  lp.mode in (2,4)														 " + //2 = a representative business day for a season, 4 = a representative weekend for a season
        " AND  lp.season= :season 													 " +
        " GROUP BY lp.typical_day, lev.bus_id, lev.hour, lev.min 					 " +
        " ORDER BY lp.typical_day, lev.bus_id, lev.hour, lev.min                    ";
}
