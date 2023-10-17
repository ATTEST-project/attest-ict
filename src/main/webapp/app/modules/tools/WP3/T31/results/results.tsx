import React from 'react';
import { Button, Col, Input, List, Offcanvas, OffcanvasBody, OffcanvasHeader, Row, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import TotalCost from 'app/modules/tools/WP3/T31/results/section/total-cost/total-cost';
import BranchInvestment from 'app/modules/tools/WP3/T31/results/section/branch-investment/branch-investment';
import OperationCostPerYear from 'app/modules/tools/WP3/T31/results/section/cost-per-year/operation-cost-per-year';
import TotalInvestCostYear from 'app/modules/tools/WP3/T31/results/section/cost-per-year/total-investment-cost-per-year';
import AllResults from 'app/modules/tools/WP3/T31/results/section/all/all-results';
import FlexInvestment from 'app/modules/tools/WP3/T31/results/section/flex-investment/flex-investment';
import { showCharts } from 'app/modules/tools/WP3/T31/reducer/tool-results.reducer';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';

import Divider from 'app/shared/components/divider/divider';

import { pathButton } from 'app/shared/reducers/back-button-path';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T31Results = (props: any) => {
  const toolDescription = TOOLS_INFO.T31_OPT_TOOL_DX.description;
  const dispatch = useAppDispatch();
  const task = useAppSelector(state => state.task);
  const taskEntity = useAppSelector(state => state.task.entity);

  // const response = useAppSelector(state => state.t31ToolExecution.entity) || {
  const response = {
    args: {
      networkId: taskEntity?.networkId,
      toolName: taskEntity?.tool?.name,
    },
    simulationId: taskEntity?.simulationUuid,
  };

  /* eslint-disable-next-line no-console */
  console.log('T31 Response: ', response);

  const results = useAppSelector(state => state.t31ToolResults.entity);

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
  }, [taskEntity]);

  const changeSection = (newSection: string) => {
    setSection(newSection);
    setOpenOffCanvas(false);
  };

  const sections = {
    all: <AllResults />,
    total: <TotalCost />,
    operationCostYear: <OperationCostPerYear />,
    totalInvestCostYear: <TotalInvestCostYear />,
    brInv: <BranchInvestment />,
    flexInv: <FlexInvestment />,
  };

  const backUrl = () => {
    return props.location?.state?.fromConfigPage ? '/tools/t31' : '/task';
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
                  <Button onClick={() => changeSection('operationCostYear')}>OperationCost Per Year</Button>
                </li>
                <li>
                  <Button onClick={() => changeSection('totalInvestCostYear')}> Total Investment Cost Per Year</Button>
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

            <ToolTitle imageAlt={WP_IMAGE.WP3.alt} title={toolDescription} imageSrc={WP_IMAGE.WP3.src} />
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

export default T31Results;
