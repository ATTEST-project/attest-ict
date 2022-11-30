import React from 'react';
import { Button, Col, Input, List, Offcanvas, OffcanvasBody, OffcanvasHeader, Row, Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import AllResults from 'app/modules/tools/WP4/T44/results/charts/section/all/all-results';
import opfResponse from '../sample-data/response_CHART_OPF.json';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, Redirect, Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import ArpSection from 'app/modules/tools/WP4/T44/results/charts/section/arp/arp';
import FlSection from 'app/modules/tools/WP4/T44/results/charts/section/fl/fl';
import StrSection from 'app/modules/tools/WP4/T44/results/charts/section/str/str';
import CurtSection from 'app/modules/tools/WP4/T44/results/charts/section/curt/curt';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { showCharts } from 'app/modules/tools/WP4/reducer/tools-results.reducer';
import { sampleToolRunResponse } from 'app/modules/tools/WP4/T44/results/sample-data/response_tools_run';

const Charts = (props: any) => {
  const { location, match } = props;

  // const { result, type } = location.state;
  const result = location.state?.result;
  const type = location.state?.type;

  const dispatch = useAppDispatch();

  const taskEntity = useAppSelector(state => state.task.entity);

  const response = useAppSelector(state => state.toolsExecution.entity) || {
    args: {
      networkId: taskEntity?.networkId,
      toolName: taskEntity?.tool?.name,
    },
    simulationId: taskEntity?.simulationUuid,
  };

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);

  const [section, setSection] = React.useState<string>('all');

  const chartsResponse = useAppSelector(state => state.toolsResults.entity);

  React.useEffect(() => {
    if (!response) {
      props.history.push('/tools/t44');
      return;
    }
  }, []);

  React.useEffect(() => {
    if (type) {
      dispatch(
        showCharts({
          networkId: response.args?.networkId,
          toolName: response.args?.toolName,
          simulationId: response.simulationId,
          type,
        })
      );
    } else {
      dispatch(
        showCharts({
          networkId: response.args?.networkId,
          toolName: response.args?.toolName,
          simulationId: response.simulationId,
          nSc: result.scenario,
          nConting: result.contingency,
        })
      );
    }
  }, []);

  const changeSection = (newSection: string) => {
    setSection(newSection);
    setOpenOffCanvas(false);
  };

  if (!chartsResponse || chartsResponse?.flexCosts?.length === 0 || chartsResponse?.charts?.length === 0) {
    return <div>No results to display!</div>;
  }

  const onBackClicked = () => {
    props.history.goBack();
  };

  return (
    <>
      <Offcanvas isOpen={openOffCanvas} toggle={() => setOpenOffCanvas(false)}>
        <OffcanvasHeader toggle={() => setOpenOffCanvas(false)}>{'Results section'}</OffcanvasHeader>
        <OffcanvasBody>
          <List type="unstyled">
            <li>
              <Button onClick={() => changeSection('all')}>ALL</Button>
            </li>
            <li>
              <Button onClick={() => changeSection('arp')}>ARP</Button>
            </li>
            <li>
              <Button onClick={() => changeSection('fl')}>FL</Button>
            </li>
            <li>
              <Button onClick={() => changeSection('str')}>STR</Button>
            </li>
            <li>
              <Button onClick={() => changeSection('curt')}>CURT</Button>
            </li>
          </List>
        </OffcanvasBody>
      </Offcanvas>
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Button color="dark" onClick={() => setOpenOffCanvas(true)}>
          <FontAwesomeIcon icon="bars" />
        </Button>
        <h4 style={{ marginLeft: 20 }}>Charts</h4>
      </div>
      <Divider />
      <Divider />
      {result && (
        <Table>
          <thead>
            <tr>
              <th>Scenario</th>
              <th>Contingency</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{'Scenario ' + result.scenario}</td>
              <td>{'Contingency ' + result.contingency}</td>
            </tr>
          </tbody>
        </Table>
      )}
      <Divider />
      <div className="section-with-border">
        <Table>
          <thead>
            <tr>
              <th>Cost Type</th>
              <th>Value</th>
              <th>Description</th>
            </tr>
          </thead>
          <tbody>
            {chartsResponse?.flexCosts?.map((cost, index) => (
              <tr key={index}>
                <td>{cost.costType}</td>
                <td>{cost.value}</td>
                <td>{cost.description}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
      <Divider />
      <>
        {(() => {
          switch (section) {
            case 'all':
              return <AllResults location={location} />;
            case 'arp':
              return <ArpSection location={location} />;
            case 'fl':
              return <FlSection location={location} />;
            case 'str':
              return <StrSection location={location} />;
            case 'curt':
              return <CurtSection location={location} />;
            default:
              return null;
          }
        })()}
      </>
      <Divider />
      <Row>
        <Col>
          <Button color="info" onClick={onBackClicked}>
            <FontAwesomeIcon icon="arrow-left" />
            {' Back'}
          </Button>
        </Col>
      </Row>
    </>
  );
};

export default Charts;
