import React from 'react';
import { Button, Col, Form, Row, Table } from 'reactstrap';
import { contingenciesData } from 'app/modules/tools/WP4/T44/results/sample-data';
import Divider from 'app/shared/components/divider/divider';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { showCharts } from 'app/modules/tools/WP4/reducer/tools-results.reducer';
import { useForm, Validate } from 'react-hook-form';
import { ValidatedField } from 'react-jhipster';
import { sampleToolRunResponse } from '../sample-data/response_tools_run';
import { showTable } from 'app/modules/tools/WP4/reducer/tools-results-table.reducer';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { pathButton } from 'app/shared/reducers/back-button-path';

const Contingencies = (props: any) => {
  const { isOpf } = props;

  const dispatch = useAppDispatch();

  const taskEntity = useAppSelector(state => state.task.entity);

  const response = useAppSelector(state => state.toolsExecution.entity) || {
    args: {
      networkId: taskEntity?.networkId,
      toolName: taskEntity?.tool?.name,
    },
    simulationId: taskEntity?.simulationUuid,
  };

  const [contingencies, setContingencies] = React.useState<any>(null);

  const [scenario, setScenario] = React.useState<any>(null);
  const [contingency, setContingency] = React.useState<any>(null);

  React.useEffect(() => {
    if (!response) {
      props.history.push('/tools/t44');
      return;
    }
  }, []);

  React.useEffect(() => {
    let isMounted = true;
    if (isOpf || !response) {
      return;
    }
    dispatch(
      showTable({
        networkId: response.args?.networkId,
        toolName: response.args?.toolName,
        simulationId: response.simulationId,
      })
    )
      .unwrap()
      .then(res => {
        if (isMounted) {
          setContingencies(res.data);
        }
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.error(err);
      });

    return () => {
      isMounted = false;
    };
  }, [isOpf]);

  const displayContSelect = () => {
    const {
      register,
      handleSubmit,
      formState: { errors },
    } = useForm();

    const scenarios = [];
    scenarios.push(
      <option key={'scenario-0'} value="" hidden>
        {'Scenario...'}
      </option>
    );
    for (let i = 0; i < contingencies?.['number_of_scenarios']; ++i) {
      scenarios.push(
        <option key={'scenario-' + (i + 1)} value={i + 1}>
          {'Scenario ' + (i + 1)}
        </option>
      );
    }

    const cont = [];
    cont.push(
      <option key={'cont-0'} value="" hidden>
        {'Contingencies...'}
      </option>
    );
    for (let i = 0; i < contingencies?.['number_of_contingencies']; ++i) {
      cont.push(
        <option key={'cont-' + (i + 1)} value={i + 1}>
          {'Contingency ' + (i + 1)}
        </option>
      );
    }

    return (
      <Form onSubmit={handleSubmit(contSelected)}>
        <Row>
          <Col md="4">
            <ValidatedField
              register={register}
              error={errors?.scenario}
              name="scenario"
              label="Scenario"
              type="select"
              validate={{ required: true }}
              onChange={event => setScenario(event.target.value)}
            >
              {scenarios}
            </ValidatedField>
          </Col>
          <Col md="4">
            <ValidatedField
              register={register}
              error={errors?.contingencies}
              name="contingencies"
              label="Contingencies"
              type="select"
              validate={{ required: true }}
              onChange={event => setContingency(event.target.value)}
            >
              {cont}
            </ValidatedField>
          </Col>
          <Col md="4" style={{ alignSelf: 'end' }}>
            <div className="mb-3">
              <Button color="primary">View</Button>
            </div>
          </Col>
        </Row>
      </Form>
    );
  };

  const contSelected = () => {
    if (scenario && contingency) {
      /* eslint-disable-next-line no-console */
      console.log('Cont selected: ', { scenario, contingency });
      props.history.push({ pathname: props.match.url + '/charts', state: { response, result: { scenario, contingency } } });
    } else {
      props.history.push({ pathname: props.match.url + '/charts', state: { response, type: 'Normal' } });
    }
  };

  const opfButtonClicked = () => {
    props.history.push({ pathname: props.match.url + '/charts', state: { response, type: 'OPF' } });
  };

  const backUrl = () => {
    return props.location?.state?.fromConfigPage ? '/tools/t44' : '/task';
  };

  return (
    <>
      {isOpf ? (
        <Button color="primary" onClick={opfButtonClicked}>
          OPF
        </Button>
      ) : (
        <>
          <Button color="primary" onClick={contSelected}>
            Normal
          </Button>
          <Divider />
          {displayContSelect()}
        </>
      )}
      <Divider />
      <Row>
        <Col>
          <Button tag={Link} to={backUrl()} color="info">
            <FontAwesomeIcon icon="arrow-left" />
            {' Back'}
          </Button>
        </Col>
      </Row>
    </>
  );
};

export default Contingencies;
