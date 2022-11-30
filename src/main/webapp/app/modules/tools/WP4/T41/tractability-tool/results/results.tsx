import React from 'react';
import { Button, Col, List, Offcanvas, OffcanvasBody, OffcanvasHeader, Row, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import ApcMw from 'app/modules/tools/WP4/T41/tractability-tool/results/section/apc/apc-mw';
import EesMw from 'app/modules/tools/WP4/T41/tractability-tool/results/section/ees/ees-mw';
import FlMw from 'app/modules/tools/WP4/T41/tractability-tool/results/section/fl/fl-mw';
import Divider from 'app/shared/components/divider/divider';
import { Redirect } from 'react-router-dom';
import T41OutputData from './sample-data/t41_output_response_pt.json';
import AllResults from 'app/modules/tools/WP4/T41/tractability-tool/results/section/all/all-results';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { showCharts } from 'app/modules/tools/WP4/reducer/tools-results.reducer';
import { pathButton } from 'app/shared/reducers/back-button-path';

const T41TractabilityResults = (props: any) => {
  const { location, match, history, ...rest } = props;

  const dispatch = useAppDispatch();

  const taskEntity = useAppSelector(state => state.task.entity);

  const response = useAppSelector(state => state.toolsExecution.entity) || {
    args: {
      networkId: taskEntity?.networkId,
      toolName: taskEntity?.tool?.name,
    },
    // simulationId: taskEntity?.simulation?.uuid,
    simulationId: taskEntity?.simulationUuid,
  };

  if (!response) {
    return <div>No results to display. First, run the tool!</div>;
  }

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);

  const chartsResponse = useAppSelector(state => state.toolsResults.entity);

  const [section, setSection] = React.useState<string>('all');

  React.useEffect(() => {
    dispatch(
      showCharts({
        networkId: response.args?.networkId,
        toolName: response.args?.toolName,
        simulationId: response.simulationId,
      })
    );
  }, []);

  const changeSection = (newSection: string) => {
    setSection(newSection);
    setOpenOffCanvas(false);
  };

  const backUrl = () => {
    return location?.state?.fromConfigPage ? '/tools/t41' : '/task';
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
              <Button onClick={() => changeSection('apc')}>APC</Button>
            </li>
            <li>
              <Button onClick={() => changeSection('ees')}>EES</Button>
            </li>
            <li>
              <Button onClick={() => changeSection('fl')}>FL</Button>
            </li>
          </List>
        </OffcanvasBody>
      </Offcanvas>
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Button color="dark" onClick={() => setOpenOffCanvas(true)}>
          {<FontAwesomeIcon icon="bars" />}
        </Button>
        <h4 style={{ marginLeft: 20 }}>T4.1 Tractability Tool Results</h4>
      </div>
      <Divider />
      <div>
        <div className="section-with-border">
          <Table>
            <thead>
              <tr>
                <th>Cost Type</th>
                <th>Value (€)</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Flexibility Procurement</td>
                <td>{parseFloat(chartsResponse?.cost).toFixed(3)}</td>
              </tr>
            </tbody>
          </Table>
        </div>
        <Divider />
        <>
          {(() => {
            if (!chartsResponse) {
              return null;
            }
            switch (section) {
              case 'all':
                return <AllResults location={location} />;
              case 'apc':
                return <ApcMw location={location} />;
              case 'ees':
                return <EesMw location={location} />;
              case 'fl':
                return <FlMw location={location} />;
              default:
                return null;
            }
          })()}
        </>
      </div>
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

export default T41TractabilityResults;
