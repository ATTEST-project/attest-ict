import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalHeader, Row, Col, Table, Input, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Divider from 'app/shared/components/divider/divider';
import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import T44ConfigParams from 'app/modules/tools/WP4/T44/results/config-params';
import { showResultsToSelect } from 'app/modules/tools/WP4/T44/reducer/tool-results-select.reducer';
import { showCharts } from 'app/modules/tools/WP4/reducer/tools-results.reducer';
import T44Charts from 'app/modules/tools/WP4/T44/results/charts/charts';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T44Results = (props: any) => {
  const toolDescription = TOOLS_INFO.T44_AS_DAY_HEAD_TX.description;
  const dispatch = useAppDispatch();
  const taskEntity = useAppSelector(state => state.task.entity);
  const loadingTaskEntity = useAppSelector(state => state.task.loading);
  const [params, setParams] = useState({
    networkId: taskEntity?.networkId,
    toolName: taskEntity?.tool?.name,
    uuid: taskEntity?.simulationUuid,
  });

  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    // console.log('useEffect setParams ----  params:' +JSON.stringify(params) );
    setParams({ ...params });
  }, []);

  // loading response used for charts visualization
  const toolsResults = useAppSelector(state => state.toolsResults.entity);
  const loadingChartResult = useAppSelector(state => state.toolsResults.loading);

  // -- get Page List: Normal & PostContingencies
  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    // console.log('useEffect showResultsToSelect ----  params:' +JSON.stringify(params) );
    dispatch(
      showResultsToSelect({
        networkId: params?.networkId,
        toolName: params?.toolName,
        uuid: params?.uuid,
      })
    );
  }, []);

  // loading pages to select
  const loadingResultSelect = useAppSelector(state => state.t44ResultsSelect.loading);
  const pages = useAppSelector(state => state.t44ResultsSelect.pages);
  const scenariosOption = useAppSelector(state => state.t44ResultsSelect.scenariosOption);
  const contingenciesOption = useAppSelector(state => state.t44ResultsSelect.contingenciesOption);
  const configParameters = useAppSelector(state => state.t44ResultsSelect.parameters);
  const [nSc, setNSc] = React.useState<number>(0);
  const [nConting, setNConting] = React.useState<number>(0);
  const [type, setType] = React.useState<string>(null);
  const [pageIndex, setPageIndex] = React.useState<number>(0);
  const [pageTitle, setPageTitle] = React.useState<string>(null);
  const [flagShowCharts, setFlagShowCharts] = React.useState<boolean>(false);
  const [showSelectScenario, setShowSelectScenario] = React.useState<boolean>(false);
  const [costs, setCosts] = React.useState(null);
  const [charts, setCharts] = React.useState(null);

  /* eslint-disable-next-line no-console */
  // console.log('---  pages: ' + pages?.length);

  // get Charts
  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    // console.log("useEffect showCharts. --- pageTitle: "+ pageTitle +", nSc: " +nSc +", nConting:"+ nConting );

    if (!pageTitle) {
      setFlagShowCharts(false);
      setShowSelectScenario(false);
      setCosts(null);
      setCharts(null);
      return;
    }

    if (pageTitle !== 'Normal') {
      setShowSelectScenario(true);
    } else {
      setShowSelectScenario(false);
    }

    let isMounted = true;
    dispatch(
      showCharts({
        networkId: params?.networkId,
        toolName: params?.toolName,
        simulationId: params?.uuid,
        type,
        nSc,
        nConting,
      })
    )
      .unwrap()
      .then(res => {
        if (isMounted) {
          if (res.data.charts != null) {
            setFlagShowCharts(true);
            setCosts(res.data.flexCosts);
            setCharts(res.data.charts);
          }
        }
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.error('useEffect ShowChart Error: ' + err);
        setFlagShowCharts(false);
        setCosts(null);
        setCharts(null);
      });
    return () => {
      isMounted = false;
    };
  }, [pageTitle, nSc, nConting]);

  const backUrl = () => {
    return '/task';
  };

  const handleOnPageChange = e => {
    const index = e.target.value;
    const title = pages[index].title;
    const numContingencies = pages[index].numContingencies;
    const numScenarios = pages[index].numScenarios;

    if (title && title !== 'Normal') {
      setFlagShowCharts(true);
      setType(null);
      setNConting(1);
      setNSc(1);
    }

    if (title && title === 'Normal') {
      setType('Normal');
      setFlagShowCharts(true);
      setNConting(null);
      setNSc(null);
    }
    setPageIndex(index);
    setPageTitle(pages[index].title);
  };
  const handleOnScenarioChange = e => {
    setCosts(null);
    setCharts(null);
    setType(null);
    setNSc(Number(e.target.value));
  };

  const handleOnContingencyChange = e => {
    setCosts(null);
    setCharts(null);
    setType(null);
    setNConting(Number(e.target.value));
  };

  return (
    <>
      <ToolTitle imageAlt={WP_IMAGE.WP4.alt} title={toolDescription} imageSrc={WP_IMAGE.WP4.src} />
      <Divider />

      {loadingResultSelect || loadingChartResult ? (
        <Spinner className="m-5" color="primary">
          loading...
        </Spinner>
      ) : (
        ''
      )}

      {!loadingResultSelect && configParameters ? (
        <>
          <T44ConfigParams parameters={configParameters} /> <Divider />
        </>
      ) : (
        ''
      )}
      <div className="section-with-border">
        <h5>Select Results: </h5>
        <Divider />
        <Row>
          {!loadingResultSelect && !pages?.length ? (
            <span>No data to display</span>
          ) : (
            <Col md="3">
              <span>Normal or Post Contingencies: </span>
              <Input id="input-pages" type="select" register={pageTitle} onChange={handleOnPageChange}>
                <option key={0} value="" hidden>
                  {' '}
                  Select...{' '}
                </option>
                {pages?.map((page, index) => (
                  <option key={index} value={index}>
                    {' '}
                    {page.title}{' '}
                  </option>
                ))}
              </Input>
            </Col>
          )}

          {showSelectScenario ? (
            <>
              <Col md="3">
                <span>Contingency Number: </span>
                <Input id="selectConting" type="select" register={nConting} value={nConting} onChange={handleOnContingencyChange}>
                  {contingenciesOption?.map((contingNumber, index) => (
                    <option key={index} value={contingNumber}>
                      {' '}
                      {contingNumber}{' '}
                    </option>
                  ))}
                </Input>
              </Col>
              <Col md="3">
                <span>Scenario Number: </span>
                <Input id="selectScen" type="select" register={nSc} value={nSc} onChange={handleOnScenarioChange}>
                  {scenariosOption?.map((scenarioNum, index) => (
                    <option key={index} value={scenarioNum}>
                      {' '}
                      {scenarioNum}{' '}
                    </option>
                  ))}
                </Input>
              </Col>
            </>
          ) : (
            ''
          )}
        </Row>
      </div>

      <Divider />

      {flagShowCharts && !loadingChartResult ? (
        <>
          <Divider />
          <T44Charts costsData={costs} />
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

export default T44Results;
