import React from 'react';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine-dark.css';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalHeader, Row, Col, Table, Input, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import Divider from 'app/shared/components/divider/divider';
import SelectDay from 'app/shared/components/select/select-day';
import SelectNode from 'app/shared/components/select/select';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';

import T33Charts from 'app/modules/tools/WP3/T33/results/charts/allCharts';
import { showResultsToSelect } from 'app/modules/tools/WP3/T33/reducer/tool-results-select.reducer';
import { showResults } from 'app/modules/tools/WP3/T33/reducer/tool-results.reducer';

const T33Results = (props: any) => {
  /* eslint-disable-next-line no-console */
  console.log('T33 Results:');
  const { location, match, history } = props;
  const toolDescription = TOOLS_INFO.T33_OPT_TOOL_PLAN_TSO_DSO.description;
  const dispatch = useAppDispatch();
  const gridRef = React.useRef<AgGridReact>(null);
  const taskEntity = useAppSelector(state => state.task.entity);

  const params = {
    networkId: taskEntity?.networkId,
    toolName: taskEntity?.tool?.name,
    simulationId: taskEntity?.simulationUuid,
  };

  // -- get page list
  React.useEffect(() => {
    dispatch(
      showResultsToSelect({
        networkId: params?.networkId,
        toolName: params?.toolName,
        simulationId: params.simulationId,
      })
    );
    /* eslint-disable-next-line no-console */
    // console.log('useEffect: showResultsToSelect');
  }, []);

  const pagesToSelect = useAppSelector(state => state.t33ResultsSelect.entity);
  const loading = useAppSelector(state => state.t33ResultsSelect.loading);
  const pages = pagesToSelect?.pages;
  const initialDay = 'Summer';
  const [pageIndex, setPageIndex] = React.useState<number>(0);
  const [pageTitle, setPageTitle] = React.useState<string>(null);
  const [day, setDay] = React.useState(initialDay);
  const [showSelectDay, setShowSelectDay] = React.useState<boolean>(false);
  const [showSelectNode, setShowSelectNode] = React.useState<boolean>(false);
  const [showCharts, setShowCharts] = React.useState<boolean>(false);
  const [showAgGrid, setShowAgGrid] = React.useState<boolean>(false);
  const [rowData, setRowData] = React.useState(null);
  const [columnDefs, setColumnDefs] = React.useState(null);
  const [connectionNodesId, setConnectionNodesId] = React.useState(null);
  const [nodeId, setNodeId] = React.useState(null);
  const tableResponse = useAppSelector(state => state.t33ToolResults.entity);

  React.useEffect(() => {
    let isMounted = true;
    if (!pageTitle) {
      setShowCharts(false);
      // setShowAgGrid(false);
      return;
    }
    dispatch(
      showResults({
        networkId: params.networkId,
        toolName: params.toolName,
        simulationId: params.simulationId,
        title: pageTitle,
      })
    )
      .unwrap()
      .then(res => {
        if (isMounted) {
          setRowData(res.data.rowData);
          setColumnDefs(res.data.columnDefs);
          if (res.data.connectionNodesId != null) {
            setConnectionNodesId(res.data.connectionNodesId);
            setNodeId(res.data.connectionNodesId[0]);
          }
          setShowAgGrid(true);
          setShowCharts(true);
        }
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.error(err);
      });
    return () => {
      isMounted = false;
    };
  }, [pageTitle]);

  // -- Manage SelectDay  and SelectNode visualization
  {
    React.useLayoutEffect(() => {
      if (!pageIndex) {
        return;
      }
      setShowSelectDay(pages[pageIndex].filterDay);
      setShowSelectNode(pages[pageIndex].filterNode);
      // debugger; // eslint-disable-line no-debugger
    }, [rowData, pageIndex]);
  }

  const handleOnDayChange = e => {
    setDay(e.target.value);
  };

  const handleOnNodeChange = e => {
    setNodeId(Number(e.target.value));
  };

  const handleOnPageChange = e => {
    setPageIndex(e.target.value);
    setPageTitle(pages[e.target.value].title);
    setShowSelectDay(pages[e.target.value].filterDay);
    setShowSelectNode(pages[e.target.value].filterNode);
    setRowData(null);
    setColumnDefs(null);
    setShowCharts(false);
    setShowAgGrid(false);
    setDay(initialDay);
  };

  // DefaultColDef sets props common to all Columns
  const defaultColDef = React.useMemo(
    () => ({
      sortable: true,
    }),
    []
  );

  /* eslint-disable-next-line no-console */
  console.log('T33 Result - Table rowData: ', { rowData });
  /* eslint-disable-next-line no-console */
  console.log('T33 Result - PageTitle: ', { pageTitle });
  /* eslint-disable-next-line no-console */
  console.log('T33 Result - ShowSelectDay: ', { showSelectDay }, { day });
  /* eslint-disable-next-line no-console */
  console.log('T33 Result - ShowSelectNode: ', { showSelectNode }, { nodeId });

  const backUrl = () => {
    return '/task';
  };

  return (
    <>
      <div className="section-with-border">
        <ToolTitle imageAlt={WP_IMAGE.WP3.alt} title={toolDescription} imageSrc={WP_IMAGE.WP3.src} />
        <Divider />

        {loading ? (
          <Spinner className="m-5" color="primary">
            Loading...
          </Spinner>
        ) : (
          <>
            <Row>
              <Col md="3">
                <Input id="input-pages" type="select" register={pageTitle} onChange={handleOnPageChange}>
                  <option key={0} value="" hidden>
                    Select Planning Results...{' '}
                  </option>
                  {pages?.map((page, index) => (
                    <option key={index} value={index}>
                      {' '}
                      {page.title}{' '}
                    </option>
                  ))}
                </Input>
              </Col>
            </Row>
          </>
        )}
      </div>

      <Divider />

      {pageTitle && showAgGrid ? (
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
      ) : pageTitle ? (
        <div> Loading ..... </div>
      ) : (
        ''
      )}

      {showSelectDay && showAgGrid ? (
        <>
          <Divider />
          <SelectDay defaultDay={day} defaultShow={showSelectDay} handleOnChange={handleOnDayChange} />
        </>
      ) : (
        ''
      )}

      {showSelectNode && showAgGrid ? (
        <>
          <Divider />
          <SelectNode handleOnChange={handleOnNodeChange} options={connectionNodesId} defaultVal={nodeId} type={'Node'} />
        </>
      ) : (
        ''
      )}

      {showCharts ? (
        <>
          <Divider />
          <T33Charts rowData={rowData} day={day} nodeId={nodeId} pageTitle={pageTitle} />
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

export default T33Results;
