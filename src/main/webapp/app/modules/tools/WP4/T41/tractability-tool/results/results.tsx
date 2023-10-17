import React from 'react';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine-dark.css';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalHeader, Row, Col, Table, Input, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';

import { showResultsToSelect } from 'app/modules/tools/WP4/T41/tractability-tool/reducer/tool-results-select.reducer';
import { showCharts } from 'app/modules/tools/WP4/reducer/tools-results.reducer';
import AllResults from 'app/modules/tools/WP4/T41/tractability-tool/results/section/all/all-results';
import T41ResultsChart from 'app/modules/tools/WP4/T41/tractability-tool/results/charts/results-chart';
import T41FlexResults from 'app/modules/tools/WP4/T41/tractability-tool/results/section/flex-results';
import LogInfo from 'app/modules/tools/WP4/T41/tractability-tool/results/section/log-info';
import T41ConfigParams from 'app/modules/tools/WP4/T41/tractability-tool/results/section/config-params';

import Divider from 'app/shared/components/divider/divider';
import SelectScenario from 'app/shared/components/select/select';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T41Results = (props: any) => {
  const toolDescription = TOOLS_INFO.T41_TRACTABILITY.description;
  const gridRef = React.useRef<AgGridReact>(null);

  const dispatch = useAppDispatch();
  const taskEntity = useAppSelector(state => state.task.entity);
  /* eslint-disable-next-line no-console */
  console.log('T41Result results for task id: ', taskEntity.id);

  const t41ResultsSelect = useAppSelector(state => state.t41ResultsSelect.entity);
  const loading = useAppSelector(state => state.t41ResultsSelect.loading);

  const params = {
    networkId: taskEntity?.networkId,
    toolName: taskEntity?.tool?.name,
    simulationId: taskEntity?.simulationUuid,
  };

  // -- get sheets' list
  React.useEffect(() => {
    dispatch(
      showResultsToSelect({
        networkId: params?.networkId,
        toolName: params?.toolName,
        simulationId: params.simulationId,
      })
    );
    /* eslint-disable-next-line no-console */
    // console.log('--- useEffect: showResultsToSelect');
  }, [taskEntity]);

  const [showSelectScenario, setShowSelectScenario] = React.useState<boolean>(false);
  const [showNotFlexCharts, setShowNotFlexCharts] = React.useState<boolean>(false);
  const [showAgGrid, setShowAgGrid] = React.useState<boolean>(false);
  const [showFlexCharts, setShowFlexCharts] = React.useState<boolean>(false);

  const pages = t41ResultsSelect?.pages;
  const logInfos = t41ResultsSelect?.logInfos;

  const configParameters = t41ResultsSelect?.toolConfigParameters?.parameters;

  const [pageIndex, setPageIndex] = React.useState<number>(0);
  const [pageTitle, setPageTitle] = React.useState<string>(null);
  const [sheetName, setSheetName] = React.useState<string>(null);

  const [scenarioOptions, setScenarioOptions] = React.useState(null);
  const [scenario, setScenario] = React.useState(null);

  // used for Not Flexibility data
  const [rowData, setRowData] = React.useState(null);
  const [columnDefs, setColumnDefs] = React.useState(null);

  const [yAxisTitle, setYAxisTitle] = React.useState(null);
  const [titleLongDescr, setTitleLongDescr] = React.useState(null);

  // used for Flexibility data
  const [flexCost, setFlexCost] = React.useState(null);
  const [flexCharts, setFlexCharts] = React.useState(null);

  React.useEffect(() => {
    if (!sheetName) {
      setShowNotFlexCharts(false);
      setShowAgGrid(false);
      setShowFlexCharts(false);
      setShowSelectScenario(false);
      return;
    }

    let isMounted = true;
    dispatch(
      showCharts({
        networkId: params.networkId,
        toolName: params.toolName,
        simulationId: params.simulationId,
        title: sheetName,
      })
    )
      .unwrap()
      .then(res => {
        if (isMounted) {
          if (res.data.charts != null) {
            // -- response contains charts with flexible data
            setShowNotFlexCharts(false);
            setShowAgGrid(false);
            setShowFlexCharts(true);
            setFlexCost(res.data.cost);
            setFlexCharts(res.data.charts);
          } else {
            // -- response contains charts and data grid NO FLEX data
            setRowData(res.data.rowData);
            setColumnDefs(res.data.columnDefs);
            setTitleLongDescr(res.data.titleLongDescription);
            setYAxisTitle(res.data.yAxisTitle);

            if (res.data.scenarios != null) {
              setScenarioOptions(res.data.scenarios);
              setScenario(res.data.scenarios[0]);
            }
            setShowAgGrid(true);
            setShowNotFlexCharts(!pages[pageIndex].violation);
            setShowFlexCharts(false);
          }
        }
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.error(err);
        setShowNotFlexCharts(false);
        setShowAgGrid(false);
        setShowFlexCharts(false);
        setShowSelectScenario(false);
      });
    return () => {
      isMounted = false;
    };
  }, [pageTitle]);

  // -- Manage and SelectScenario visualization
  React.useLayoutEffect(() => {
    if (!pageIndex) {
      return;
    }
    setShowSelectScenario(!pages[pageIndex].violation);
    // debugger; // eslint-disable-line no-debugger
  }, [rowData, pageIndex]);

  const handleOnScenarioChange = e => {
    setScenario(Number(e.target.value));
  };

  const handleOnPageChange = e => {
    const index = e.target.value;
    setPageIndex(index);
    setSheetName(pages[index].sheetName);
    setPageTitle(pages[index].title);
    setShowSelectScenario(true);
    setRowData(null);
    setColumnDefs(null);
    setFlexCost(null);
    setFlexCharts(null);
    setShowNotFlexCharts(false);
    setShowAgGrid(false);
    setShowFlexCharts(false);
  };

  // DefaultColDef sets props common to all Columns in agGrid
  const defaultColDef = React.useMemo(
    () => ({
      sortable: true,
    }),
    []
  );

  const backUrl = () => {
    return '/task';
  };

  return (
    <>
      <ToolTitle imageAlt={WP_IMAGE.WP4.alt} title={toolDescription} imageSrc={WP_IMAGE.WP4.src} />
      <Divider />

      {loading ? (
        <Spinner className="m-5" color="primary">
          Loading...
        </Spinner>
      ) : (
        ''
      )}

      {!loading && configParameters ? (
        <>
          <T41ConfigParams parameters={configParameters} /> <Divider />
        </>
      ) : (
        ''
      )}
      <div className="section-with-border">
        <h5> Results: </h5>
        {logInfos && !loading ? (
          <>
            <Divider />
            <LogInfo logInfos={logInfos} />
            <Divider />
          </>
        ) : (
          ''
        )}

        <Row>
          {!loading && !pages?.length ? (
            <div>No data to display</div>
          ) : (
            <Col md="3">
              <span>Select results: </span>
              <Input id="input-pages" type="select" register={pageTitle} onChange={handleOnPageChange}>
                <option key={0} value="" hidden>
                  Select...{' '}
                </option>
                {pages?.map((page, index) => (
                  <option key={index} value={index}>
                    {' '}
                    {page.sheetName} {' - '} {page.title}{' '}
                  </option>
                ))}
              </Input>
            </Col>
          )}
        </Row>
      </div>

      <Divider />

      {sheetName && showAgGrid && !loading ? (
        <>
          <Divider />
          <h4> {pageTitle}</h4>
          <div className="ag-theme-alpine-dark" style={{ width: '100%', height: '100%' }}>
            <AgGridReact
              ref={gridRef}
              rowData={rowData}
              columnDefs={columnDefs}
              defaultColDef={defaultColDef}
              domLayout={'autoHeight'}
              pagination={true}
              paginationPageSize={15}
            />
          </div>
        </>
      ) : sheetName && !showFlexCharts && !loading ? (
        <div> Loading ..... </div>
      ) : (
        ''
      )}

      {showSelectScenario && showAgGrid && !loading ? (
        <>
          <Divider />
          <SelectScenario handleOnChange={handleOnScenarioChange} options={scenarioOptions} defaultVal={scenario} type={'Scenario'} />
        </>
      ) : (
        ''
      )}

      {showNotFlexCharts && !loading ? (
        <>
          <Divider />
          <T41ResultsChart
            rowData={rowData}
            scenario={scenario}
            title={sheetName}
            titleLongDescr={titleLongDescr}
            yAxisTitle={yAxisTitle}
          />
        </>
      ) : (
        ''
      )}

      {showFlexCharts && !loading ? (
        <>
          <Divider />
          <T41FlexResults location={location} cost={flexCost} data={flexCharts} title={sheetName} />
        </>
      ) : (
        ''
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

export default T41Results;
