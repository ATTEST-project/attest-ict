import React from 'react';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine-dark.css';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalHeader, Row, Col, Table, Input, Spinner } from 'reactstrap';

import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';

import AllResults from 'app/modules/tools/WP4/T42/results/section/all/all-results';
import ConfigParams from 'app/modules/tools/WP4/T42/results/section/config-params';
import T42FlexResults from 'app/modules/tools/WP4/T42/results/section/flex-results';
import FlexActivations from 'app/modules/tools/WP4/T42/results/section/activation/flex-activations';
import { showResultsToSelect } from 'app/modules/tools/WP4/reducer/tool-results-select.reducer';
import { showCharts } from 'app/modules/tools/WP4/reducer/tools-results.reducer';

import Divider from 'app/shared/components/divider/divider';
import SelectScenario from 'app/shared/components/select/select';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T42Results = (props: any) => {
  /* eslint-disable-next-line no-console */
  console.log('T42 Results:');

  const dispatch = useAppDispatch();
  const gridRef = React.useRef<AgGridReact>(null);

  const taskEntity = useAppSelector(state => state.task.entity);
  const t42ResultsSelect = useAppSelector(state => state.realTimeToolResultsSelect.entity);
  const loading = useAppSelector(state => state.realTimeToolResultsSelect.loading);
  const toolDescription = TOOLS_INFO.T42_AS_REAL_TIME_DX.description;
  const wp4Icon = 'content/images/carousel_img_2.png';

  /* eslint-disable-next-line no-console */
  console.log('T42taskEntity: ', taskEntity);

  /* eslint-disable-next-line no-console */
  console.log('T42ResultsSelect: ', t42ResultsSelect);

  /* eslint-disable-next-line no-console */
  console.log('loading: ', loading);

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
    console.log('useEffect: showResultsToSelect');
  }, [params.simulationId]);

  const [showFlexCharts, setShowFlexCharts] = React.useState<boolean>(false);
  const t42Pages = t42ResultsSelect?.pages;
  const configParameters = t42ResultsSelect?.toolConfigParameters?.parameters;
  const [pageIndex, setPageIndex] = React.useState<number>(0);
  const [pageTitle, setPageTitle] = React.useState<string>(null);
  const [sheetName, setSheetName] = React.useState<string>(null);
  const [section, setSection] = React.useState<string>(null);
  const tp = configParameters?.current_time_period;

  // used for Flexibility data
  const [flexCharts, setFlexCharts] = React.useState(null);
  const [flexActivations, setFlexActivations] = React.useState(null);

  React.useEffect(() => {
    if (!sheetName) {
      setShowFlexCharts(false);
      return;
    }

    let isMounted = true;
    dispatch(
      showCharts({
        networkId: params.networkId,
        toolName: params.toolName,
        simulationId: params.simulationId,
        title: sheetName,
        timePeriod: tp,
      })
    )
      .unwrap()
      .then(res => {
        if (isMounted) {
          if (res.data.charts != null) {
            // -- response contains charts with flexible data
            setShowFlexCharts(true);
            setFlexCharts(res.data.charts);
            setFlexActivations(res.data.activations);
          }
        }
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.error(err);
        setShowFlexCharts(false);
      });
    return () => {
      isMounted = false;
    };
  }, [pageTitle]);

  // -- Manage and SelectScenario visualization

  const handleOnPageChange = e => {
    const index = e.target.value;
    setPageIndex(index);
    setSheetName(t42Pages[index].sheetName);
    setPageTitle(t42Pages[index].title);
    setSection(t42Pages[index].section);
    setFlexCharts(null);
    setShowFlexCharts(false);
  };

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
          <ConfigParams parameters={configParameters} /> <Divider />
        </>
      ) : (
        ''
      )}
      <div className="section-with-border">
        <h5> Select Results: </h5>
        {!loading ? (
          <>
            <Row>
              <Col md="3">
                <Input id="input-t42Pages" type="select" register={pageTitle} onChange={handleOnPageChange}>
                  <option key={0} value="" hidden>
                    Select...{' '}
                  </option>
                  {t42Pages?.map((page, index) => (
                    <option key={index} value={index}>
                      {' '}
                      {page.title}{' '}
                    </option>
                  ))}
                </Input>
              </Col>
            </Row>
          </>
        ) : (
          ''
        )}
      </div>

      <Divider />

      {flexActivations && !loading ? (
        <>
          <Divider />
          <FlexActivations
            flexActivations={flexActivations}
            title={'Request for the DSO for flexibility service activation in real-time at the TSO-DSO interface'}
          />
        </>
      ) : (
        ''
      )}

      {showFlexCharts && !loading ? (
        <>
          <Divider />
          <T42FlexResults location={location} data={flexCharts} title={sheetName} section={section} />
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

export default T42Results;
