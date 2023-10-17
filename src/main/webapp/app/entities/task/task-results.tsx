import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import axios from 'axios';

import { TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import T41TractabilityResults from 'app/modules/tools/WP4/T41/tractability-tool/results/results';
import T44Results from 'app/modules/tools/WP4/T44/results/results';
import T31Results from 'app/modules/tools/WP3/T31/results/results';
import ScenarioGenTool from 'app/modules/tools/WP4/scenario-gen-tool/scenario-gen-tool';
import SGTResults from 'app/modules/tools/WP4/scenario-gen-tool/results/results';
import T52Results from 'app/modules/tools/WP5/T52/results/results';
import T51CharacterizationResults from 'app/modules/tools/WP5/T51/characterization-tool/results/results';
import T51MonitoringResults from 'app/modules/tools/WP5/T51/monitoring-tool/results/results';
import T53Results from 'app/modules/tools/WP5/T53/results/results';
import T32Results from 'app/modules/tools/WP3/T32/results/results';
import T33Results from 'app/modules/tools/WP3/T33/results/results';
import T41Results from 'app/modules/tools/WP4/T41/tractability-tool/results/results';
import T45Results from 'app/modules/tools/WP4/T45/results/results';
import T42Results from 'app/modules/tools/WP4/T42/results/results';

import { getEntity } from 'app/entities/task/task.reducer';

const TaskResults = (props: any) => {
  const { location, match, history, ...rest } = props;

  const dispatch = useAppDispatch();
  const taskEntity = useAppSelector(state => state.task.entity);
  const taskId: number = taskEntity?.id;

  /* eslint-disable-next-line no-console */
  console.log('TaskResults for - task.id: ' + taskId);

  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    // console.log('--- useEffect getEntity( '+ match.params.id + '),  taskId: '+taskId);
    dispatch(getEntity(match.params.id));
  }, []);

  React.useLayoutEffect(() => {
    /* eslint-disable-next-line no-console */
    // console.log('--- useLayoutEffect taskEntityChange ');
  }, [taskEntity]);

  const redirectToResults: boolean = taskId !== undefined && match.params.id === taskId.toString();

  /* eslint-disable-next-line no-console */
  // console.log('--- (task.id === match.params.id) ? '+redirectToResults );

  const resultsPages = {
    [TOOLS_NAMES.T41_TRACTABILITY]: <T41Results />,
    [TOOLS_NAMES.T42_AS_REAL_TIME_DX]: <T42Results location={location} match={match} history={history} />,
    [TOOLS_NAMES.T44_AS_DAY_HEAD_TX]: <T44Results location={location} match={match} history={history} />,
    [TOOLS_NAMES.T45_AS_REAL_TIME_TX]: <T45Results location={location} match={match} history={history} />,
    [TOOLS_NAMES.T31_OPT_TOOL_DX]: <T31Results />,
    [TOOLS_NAMES.T32_OPT_TOOL_TX]: <T32Results />,
    [TOOLS_NAMES.T33_OPT_TOOL_PLAN_TSO_DSO]: <T33Results location={location} match={match} history={history} />,
    [TOOLS_NAMES.T41_WIND_AND_PV]: <SGTResults location={location} match={match} />,
    [TOOLS_NAMES.T52_INDICATOR]: <T52Results />,
    [TOOLS_NAMES.T51_CHARACTERIZATION]: <T51CharacterizationResults />,
    [TOOLS_NAMES.T51_MONITORING]: <T51MonitoringResults />,
    [TOOLS_NAMES.T53_MANAGEMENT]: <T53Results />,
  };

  return <>{redirectToResults ? resultsPages[taskEntity?.tool?.name] : <span> loading .. </span>}</>;
};

export default TaskResults;
