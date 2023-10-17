package com.attest.ict.custom.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolResultsCustomQuery {

    public static final String TOOL_RESULTS =
        " " +
        " SELECT  outfl.network_id, " +
        "         outfl.tool_id, " +
        "         outfl.id as outfile_id, " +
        "         outfl.file_name, " +
        "         sim.id, " +
        "         sim.uuid, " +
        "         sim.description, " +
        "         tsk.id as taskId, " +
        "         tsk.date_time_start, " +
        "         tsk.date_time_end, " +
        "         tsk.user_id  ,   " +
        "         jhiuser.login     " +
        " FROM OUTPUT_FILE outfl                                                                                                               " +
        " JOIN SIMULATION sim on sim.id = outfl.simulation_id                                                                                  " +
        " JOIN TASK tsk on tsk.simulation_id = sim.id    " +
        " JOIN jhi_user jhiuser on  jhiuser.id = tsk.user_id                                                                                      " +
        " WHERE outfl.tool_id= :toolId                                                                                                " +
        " AND outfl.network_id = :networkId                                                                                                " +
        " AND tsk.task_status ='PASSED'                                                                                                        " +
        " AND (:dateTimeEnd IS NULL OR tsk.date_time_end >= :dateTimeEnd) " +
        " AND (outfl.file_name LIKE CONCAT('%', :fileName, '%') OR :fileName IS NULL)                                                                                  " +
        " ORDER by tsk.id, outfl.file_name desc                                                                                                ";
}
