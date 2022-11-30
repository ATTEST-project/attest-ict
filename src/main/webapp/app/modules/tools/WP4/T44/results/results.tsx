import React from 'react';
import Contingencies from 'app/modules/tools/WP4/T44/results/contingencies/contingencies';
import { Button, Col, Form, FormGroup, Input, Label, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Divider from 'app/shared/components/divider/divider';
import T41TractabilityResults from 'app/modules/tools/WP4/T41/tractability-tool/results/results';
import { Link, Redirect } from 'react-router-dom';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import Charts from 'app/modules/tools/WP4/T44/results/charts/charts';
import { useAppSelector } from 'app/config/store';
import { sampleToolRunResponse } from 'app/modules/tools/WP4/T44/results/sample-data/response_tools_run';
import axios from 'axios';

const T44Results = ({ location, match, history, ...props }) => {
  // const { response } = location.state;

  const taskEntity = useAppSelector(state => state.task.entity);

  const response = useAppSelector(state => state.toolsExecution.entity) || {
    args: {
      networkId: taskEntity?.networkId,
      toolName: taskEntity?.tool?.name,
    },
    simulationId: taskEntity?.simulationUuid,
    configFile: taskEntity?.simulationConfigFile,
  };

  if (!response?.simulationId) {
    return <div>No results to display. First, run the tool!</div>;
  }

  const [isOpf, setIsOpf] = React.useState<boolean>(null);

  React.useEffect(() => {
    if (response.configFile) {
      const parsedJSON = JSON.parse(atob(response.configFile));
      setIsOpf(parsedJSON.parameters?.problem === 1);
    } else {
      const problemParamPos = response.args?.parameterNames.indexOf('problem');
      const problem = response.args?.parameterValues[problemParamPos];
      setIsOpf(problem === '1');
    }
  }, []);

  return (
    <>
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <h4>T4.4 Tool Results</h4>
      </div>
      <Divider />
      <Switch>
        <ErrorBoundaryRoute
          exact
          path={match.url}
          component={() => <Redirect to={{ pathname: match.url + '/table', state: location.state }} />}
        />
        <ErrorBoundaryRoute
          exact
          path={match.url + '/table'}
          component={() => (
            <Contingencies isOpf={isOpf} response={response} location={location} match={match} history={history} {...props} />
          )}
        />
        <ErrorBoundaryRoute exact path={match.url + '/charts'} component={Charts} />
      </Switch>
    </>
  );
};

export default T44Results;
