package com.attest.ict.custom.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenCustomQuery {

    private final Logger log = LoggerFactory.getLogger(GenCustomQuery.class);

    public static final String GEN_P_Q_GROUP_BY_SEASON =
        "	" +
        " SELECT gp.season AS season, gev.generator_id AS genId, gev.hour AS hh, gev.min AS mm, SUM(P) AS P, SUM(Q) AS Q " +
        " FROM   gen_el_val  gev 													 " +
        " INNER JOIN gen_profile gp ON gp.id= gev.gen_profile_id 					 " +
        " WHERE gp.network_id= :networkId 										 	 " +
        " AND  generator_id= :genId						 							 " +
        " AND  gp.mode in :mode														 " +
        " GROUP BY gp.season, gev.generator_id, gev.hour, gev.min 						 " +
        " ORDER BY gp.season, gev.generator_id, gev.hour, gev.min                         ";

    public static final String GEN_P_Q_GROUP_BY_SEASON_FOR_TYPICAL_DAY =
        "	" +
        " SELECT gp.season AS season, gev.generator_id AS genId, gev.hour AS hh, gev.min AS mm, SUM(P) AS P, SUM(Q) AS Q " +
        " FROM   gen_el_val  gev 													 " +
        " INNER JOIN gen_profile gp ON gp.id= gev.gen_profile_id 					 " +
        " WHERE gp.network_id= :networkId 											 " +
        " AND  generator_id= :genId					 								 " +
        " AND  gp.typical_day= :typicalDay                                           " +
        " AND  gp.mode in :mode													 	 " +
        " GROUP BY gp.season, gev.generator_id, gev.hour, gev.min 						 " +
        " ORDER BY gp.season, gev.generator_id, gev.hour, gev.min                         ";

    public static final String GEN_P_Q_GROUP_BY_TYPICAL_DAY =
        "	" +
        " SELECT gp.typical_day AS day, gev.generator_id AS genId, gev.hour AS hh, gev.min AS mm, SUM(P) AS P, SUM(Q) AS Q " +
        " FROM   gen_el_val  gev 													 " +
        " INNER JOIN gen_profile gp ON gp.id= gev.gen_profile_id 					 " +
        " WHERE gp.network_id= :networkId 											 " +
        " AND  generator_id= :genId				 									 " +
        " AND  gp.mode in :mode														 " +
        " GROUP BY gp.typical_day, gev.generator_id, gev.hour, gev.min 					 " +
        " ORDER BY gp.typical_day, gev.generator_id, gev.hour, gev.min                    ";

    public static final String GEN_P_Q_GROUP_BY_TYPICAL_DAY_FOR_SEASON =
        "	" +
        " SELECT gp.typical_day AS day, gev.generator_id AS genId, gev.hour AS hh, gev.min AS mm, SUM(P) AS P, SUM(Q) AS Q " +
        " FROM   gen_el_val  gev 													 " +
        " INNER JOIN gen_profile gp ON gp.id= gev.gen_profile_id 					 " +
        " WHERE gp.network_id= :networkId 					 						 " +
        " AND  generator_id= :genId						 							 " +
        " AND  gp.season= :season 													 " +
        " AND  gp.mode in :mode														 " +
        " GROUP BY gp.typical_day, gev.generator_id, gev.hour, gev.min 					 " +
        " ORDER BY gp.typical_day, gev.generator_id, gev.hour, gev.min                    ";
}
