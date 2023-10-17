import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Button, Col, Input, Label, Row } from 'reactstrap';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import { reset as resetTable, showTable } from 'app/modules/tools/WP5/T53/reducer/tool-table.reducer';
import { reset as resetCharts, showCharts } from 'app/modules/tools/WP5/T53/reducer/tool-charts.reducer';

import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import Divider from 'app/shared/components/divider/divider';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T53Results = (props: any) => {
  const dispatch = useAppDispatch();
  const toolDescription = TOOLS_INFO.T53_MANAGEMENT.description;
  const divRef = React.useRef<HTMLDivElement>();
  const iframeRef = React.useRef<HTMLIFrameElement>();
  const taskEntity = useAppSelector(state => state.task.entity);
  const tableEntity = useAppSelector(state => state.t53ToolTable.entity);
  const pageEntity = useAppSelector(state => state.t53ToolCharts.entity);
  const loadingPage = useAppSelector(state => state.t53ToolCharts.loading);

  const [pageSelected, setPageSelected] = React.useState<string>(null);
  //  const response = useAppSelector(state => state.t53ToolExecution.entity) || {
  const response = {
    args: {
      networkId: taskEntity?.networkId,
      toolName: taskEntity?.tool?.name,
    },
    simulationId: taskEntity?.simulationUuid,
  };

  if (!response) {
    return <div>No results to display. First, run the tool!</div>;
  }

  React.useEffect(() => {
    dispatch(
      showTable({
        networkId: response.args?.networkId,
        toolName: response.args?.toolName,
        simulationId: response.simulationId,
      })
    );
    return () => {
      dispatch(resetTable());
      dispatch(resetCharts());
    };
  }, []);

  const changePageValue = React.useCallback((page: string) => {
    const pageFormatted = page.replace('.html', '').replace('_', ' ');
    return pageFormatted.charAt(0).toUpperCase() + pageFormatted.substring(1);
  }, []);

  const onButtonClick = () => {
    if (!pageSelected) {
      return;
    }
    dispatch(
      showCharts({
        networkId: response.args?.networkId,
        toolName: response.args?.toolName,
        simulationId: response.simulationId,
        fileName: pageSelected,
      })
    );
  };

  const disableAllUselessStuff = React.useCallback(() => {
    const iframe = iframeRef?.current;

    const mainClusteringLogo = iframe.contentWindow.document.querySelector<HTMLImageElement>('g.layer-above > g.imagelayer > image');
    if (mainClusteringLogo) {
      mainClusteringLogo.style.display = 'none';
    }

    const allLinks = iframe.contentWindow.document.querySelectorAll<HTMLLinkElement>('a');
    allLinks.forEach(x => {
      if (x.getAttribute('xlink:href')?.startsWith('./') || x.getAttribute('href')?.startsWith('./')) {
        x.style.cursor = 'default';
        x.style.pointerEvents = 'none';
        if (x.className === 'float') {
          x.style.display = 'none';
        }
      }
    });

    const imagesToRemove = ['logo.png', 'logo_white.png', 'return_icon.png'];
    const allImages = iframe.contentWindow.document.querySelectorAll<HTMLImageElement>('img');
    allImages.forEach(img => {
      imagesToRemove.forEach(x => {
        if (img.getAttribute('src')?.includes(x)) {
          img.style.display = 'none';
        }
      });
    });
  }, []);

  const backUrl = () => {
    return props.location?.state?.fromConfigPage ? '/tools/t53' : '/task';
  };

  return (
    <>
      <ToolTitle imageAlt={WP_IMAGE.WP5.alt} title={toolDescription} imageSrc={WP_IMAGE.WP5.src} />

      <Divider />
      <h4>Results: </h4>
      <Divider />
      <Row>
        <Col md="3">
          <Input id="input-pages" type="select" onChange={event => setPageSelected(event.target.value)}>
            <option key={0} value="" hidden>
              Page...
            </option>
            {tableEntity?.map((page, index) => (
              <option key={index} value={page}>
                {changePageValue(page)}
              </option>
            ))}
          </Input>
        </Col>
        <Col style={{ alignSelf: 'end' }}>
          <Button color="primary" onClick={onButtonClick}>
            View
          </Button>
        </Col>
      </Row>
      <Divider />
      <div ref={divRef}>
        {loadingPage && <LoadingOverlay ref={divRef} />}
        {pageEntity && (
          <iframe ref={iframeRef} style={{ width: '100%', height: 800 }} srcDoc={pageEntity} onLoad={disableAllUselessStuff} />
        )}
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

export default T53Results;
