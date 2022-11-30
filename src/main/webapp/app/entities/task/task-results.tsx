import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/task/task.reducer';
import { TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import T41TractabilityResults from 'app/modules/tools/WP4/T41/tractability-tool/results/results';
import T44Results from 'app/modules/tools/WP4/T44/results/results';
import T31Results from 'app/modules/tools/WP3/T31/results/results';
import axios from 'axios';
import ScenarioGenTool from 'app/modules/tools/WP4/scenario-gen-tool/scenario-gen-tool';
import SGTResults from 'app/modules/tools/WP4/scenario-gen-tool/results/results';
import T52Results from 'app/modules/tools/WP5/T52/results/results';
import T51CharacterizationResults from 'app/modules/tools/WP5/T51/characterization-tool/results/results';
import T51MonitoringResults from 'app/modules/tools/WP5/T51/monitoring-tool/results/results';
import T53Results from 'app/modules/tools/WP5/T53/results/results';
import T32Results from 'app/modules/tools/WP3/T32/results/results';

const TaskResults = (props: any) => {
  const { location, match, history, ...rest } = props;

  const dispatch = useAppDispatch();

  const taskEntity = useAppSelector(state => state.task.entity);

  React.useEffect(() => {
    dispatch(getEntity(match.params.id));
  }, []);

  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    console.log('Task entity: ', taskEntity);
  }, [taskEntity]);

  const resultsPages = {
    [TOOLS_NAMES.T41_TRACTABILITY]: <T41TractabilityResults />,
    [TOOLS_NAMES.T44_AS_DAY_HEAD_TX]: <T44Results location={location} match={match} history={history} />,
    [TOOLS_NAMES.T31_OPT_TOOL_DX]: <T31Results />,
    [TOOLS_NAMES.T32_OPT_TOOL_TX]: <T32Results />,
    [TOOLS_NAMES.T41_WIND_AND_PV]: <SGTResults location={location} match={match} />,
    [TOOLS_NAMES.T52_INDICATOR]: <T52Results />,
    [TOOLS_NAMES.T51_CHARACTERIZATION]: <T51CharacterizationResults />,
    [TOOLS_NAMES.T51_MONITORING]: <T51MonitoringResults />,
    [TOOLS_NAMES.T53_MANAGEMENT]: <T53Results />,
  };

  return <>{resultsPages[taskEntity?.tool?.name]}</>;
};

export default TaskResults;
