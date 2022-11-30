import React from 'react';
import { Button, Col, List, Offcanvas, OffcanvasBody, OffcanvasHeader, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import AllResults from 'app/modules/tools/WP4/scenario-gen-tool/results/section/all/all-results';
import Wind from 'app/modules/tools/WP4/scenario-gen-tool/results/section/wind/wind';
import PV from 'app/modules/tools/WP4/scenario-gen-tool/results/section/pv/pv';
import Probabilities from 'app/modules/tools/WP4/scenario-gen-tool/results/section/probabilities/probabilities';
import Divider from 'app/shared/components/divider/divider';
import { Redirect } from 'react-router-dom';
import SGTOutputData from '../../scenario-gen-tool/results/sample-data/TSG_v3.json';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { showCharts } from 'app/modules/tools/WP4/reducer/tools-results.reducer';

const SGTResults = ({ match, location }) => {
  // const chartsResponse = location.state?.chartsResponse || SGTOutputData;

  const dispatch = useAppDispatch();

  const taskEntity = useAppSelector(state => state.task.entity);

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);

  const response = useAppSelector(state => state.toolsExecution.entity) || {
    args: {
      networkId: taskEntity?.networkId,
      toolName: taskEntity?.tool?.name,
    },
    simulationId: taskEntity?.simulationUuid,
  };

  // const chartsResponse = useAppSelector(state => state.toolsResults.entity);

  if (!response?.simulationId) {
    return <div>No results to display. First, run the tool!</div>;
  }

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
    return location?.state?.fromConfigPage ? '/tools/sgt' : '/task';
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
              <Button onClick={() => changeSection('wind')}>Wind</Button>
            </li>
            <li>
              <Button onClick={() => changeSection('pv')}>PV</Button>
            </li>
            <li>
              <Button onClick={() => changeSection('prob')}>Probabilities</Button>
            </li>
          </List>
        </OffcanvasBody>
      </Offcanvas>
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Button color="dark" onClick={() => setOpenOffCanvas(true)}>
          {<FontAwesomeIcon icon="bars" />}
        </Button>
        <h4 style={{ marginLeft: 20 }}>Scenario Gen Tool Results</h4>
      </div>
      <Divider />
      <>
        {(() => {
          switch (section) {
            case 'all':
              return <AllResults location={location} />;
            case 'wind':
              return <Wind location={location} />;
            case 'pv':
              return <PV location={location} />;
            case 'prob':
              return <Probabilities location={location} />;
            default:
              return null;
          }
        })()}
      </>
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

export default SGTResults;
