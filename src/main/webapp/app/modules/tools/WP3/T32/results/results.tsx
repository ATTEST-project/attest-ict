import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { showCharts } from 'app/modules/tools/WP3/T32/reducer/tool-results.reducer';
import AllResults from 'app/modules/tools/WP3/T32/results/section/all/all-results';
import TotalCost from 'app/modules/tools/WP3/T32/results/section/total-cost/total-cost';
import CostPerYear from 'app/modules/tools/WP3/T32/results/section/cost-per-year/cost-per-year';
import BranchInvestment from 'app/modules/tools/WP3/T32/results/section/branch-investment/branch-investment';
import FlexInvestment from 'app/modules/tools/WP3/T32/results/section/flex-investment/flex-investment';
import { Button, Col, List, Offcanvas, OffcanvasBody, OffcanvasHeader, Row, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Divider from 'app/shared/components/divider/divider';
import { Link } from 'react-router-dom';

const T32Results = (props: any) => {
  /* eslint-disable-next-line no-console */
  console.log('T32 Results ');
  const dispatch = useAppDispatch();

  const taskEntity = useAppSelector(state => state.task.entity);

  /* eslint-disable-next-line no-console */
  console.log('T32 taskEntity ', taskEntity);

  const response = useAppSelector(state => state.t32ToolExecution.entity) || {
    args: {
      networkId: taskEntity?.networkId,
      toolName: taskEntity?.tool?.name,
    },
    simulationId: taskEntity?.simulationUuid,
  };
  const results = useAppSelector(state => state.t32ToolResults.entity);

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
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

  const sections = {
    all: <AllResults />,
    total: <TotalCost />,
    costY: <CostPerYear />,
    brInv: <BranchInvestment />,
    flexInv: <FlexInvestment />,
  };

  const backUrl = () => {
    return props.location?.state?.fromConfigPage ? '/tools/t32' : '/task';
  };

  return (
    <>
      {!response?.simulationId ? (
        <div>No results to display. First, run the tool!</div>
      ) : (
        <>
          <Offcanvas isOpen={openOffCanvas} toggle={() => setOpenOffCanvas(false)}>
            <OffcanvasHeader toggle={() => setOpenOffCanvas(false)}>{'Results section'}</OffcanvasHeader>
            <OffcanvasBody>
              <List type="unstyled">
                <li>
                  <Button onClick={() => changeSection('all')}>ALL</Button>
                </li>
                <li>
                  <Button onClick={() => changeSection('total')}>Total Cost</Button>
                </li>
                <li>
                  <Button onClick={() => changeSection('costY')}>Cost Per Year</Button>
                </li>
                <li>
                  <Button onClick={() => changeSection('brInv')}>Branch Investment</Button>
                </li>
                <li>
                  <Button onClick={() => changeSection('flexInv')}>Flex Investment</Button>
                </li>
              </List>
            </OffcanvasBody>
          </Offcanvas>
          <div style={{ display: 'flex', alignItems: 'center' }}>
            <Button color="dark" onClick={() => setOpenOffCanvas(true)}>
              <FontAwesomeIcon icon="bars" />
            </Button>
            <h4 style={{ marginLeft: 20 }}>T3.2 Results</h4>
          </div>
          <Table>
            <thead>
              <tr>
                <th>Country</th>
                <th>Case Name</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{results?.['Country']}</td>
                <td>{results?.['Case name']}</td>
              </tr>
            </tbody>
          </Table>
          <Divider />
          {results && sections[section]}
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

export default T32Results;
