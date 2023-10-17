import React from 'react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine-dark.css';
import { AgGridReact } from 'ag-grid-react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalHeader, Row, Col, Table, Input, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import Divider from 'app/shared/components/divider/divider';
import SelectScenario from 'app/shared/components/select/select';

import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';

import AllResults from 'app/modules/tools/WP4/T45/results/section/all/all-results';
import ConfigParams from 'app/modules/tools/WP4/T45/results/section/config-params';
import T45FlexResults from 'app/modules/tools/WP4/T45/results/section/flex-results';
import FlexActivations from 'app/modules/tools/WP4/T45/results/section/activation/flex-activations';
import { showResultsToSelect } from 'app/modules/tools/WP4/reducer/tool-results-select.reducer';
import { showCharts } from 'app/modules/tools/WP4/reducer/tools-results.reducer';

import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T45Results = (props: any) => {
  /* eslint-disable-next-line no-console */
  console.log('T45 Results:');
  const toolDescription = TOOLS_INFO.T45_AS_REAL_TIME_TX.description;

  const dispatch = useAppDispatch();
  const gridRef = React.useRef<AgGridReact>(null);
  const taskEntity = useAppSelector(state => state.task.entity);
  const t45ResultsSelect = useAppSelector(state => state.realTimeToolResultsSelect.entity);
  const loading = useAppSelector(state => state.realTimeToolResultsSelect.loading);

  /* eslint-disable-next-line no-console */
  console.log('T45taskEntity: ', taskEntity);

  /* eslint-disable-next-line no-console */
  console.log('T45ResultsSelect: ', t45ResultsSelect);

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
  const t45Pages = t45ResultsSelect?.pages;
  const configParameters = t45ResultsSelect?.toolConfigParameters?.parameters;
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
    setSheetName(t45Pages[index].sheetName);
    setPageTitle(t45Pages[index].title);
    setSection(t45Pages[index].section);
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
                <Input id="input-t45Pages" type="select" register={pageTitle} onChange={handleOnPageChange}>
                  <option key={0} value="" hidden>
                    Select Results...{' '}
                  </option>
                  {t45Pages?.map((page, index) => (
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
          <T45FlexResults location={location} data={flexCharts} title={sheetName} section={section} />
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

export default T45Results;
