import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Button, Col, Input, Row } from 'reactstrap';

import Divider from 'app/shared/components/divider/divider';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { showTable, reset as resetTable } from 'app/modules/tools/WP5/T51/characterization-tool/reducer/tool-table.reducer';
import { showCharts, reset as resetCharts } from 'app/modules/tools/WP5/T51/characterization-tool/reducer/tool-charts.reducer';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T51CharacterizationResults = (props: any) => {
  const dispatch = useAppDispatch();
  const toolDescription = TOOLS_INFO.T51_CHARACTERIZATION.description;
  const divRef = React.useRef<HTMLDivElement>();
  const iframeRef = React.useRef<HTMLIFrameElement>();
  const taskEntity = useAppSelector(state => state.task.entity);
  const tableEntity = useAppSelector(state => state.t511ToolTable.entity);
  const pageListLoading = useAppSelector(state => state.t511ToolTable.loading);
  const pageEntity = useAppSelector(state => state.t511ToolCharts.entity);
  const loadingPage = useAppSelector(state => state.t511ToolCharts.loading);

  const [pageSelected, setPageSelected] = React.useState<string>(null);

  //  const response = useAppSelector(state => state.t511ToolExecution.entity) || {
  const response = {
    args: {
      networkId: taskEntity?.networkId,
      toolName: taskEntity?.tool?.name,
    },
    simulationId: taskEntity?.simulationUuid,
  };

  React.useEffect(() => {
    dispatch(
      showTable({
        networkId: response.args?.networkId,
        toolName: response.args?.toolName,
        simulationId: response.simulationId,
      })
    );
    return () => {
      // -- call  reset,  when unmount component (-> page exit)
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

    const iframeBody = window.getComputedStyle(iframe.contentDocument.body);
    if (iframeBody.background.includes('rgba(0, 0, 0, 0)')) {
      iframe.contentDocument.body.style.background = 'white';
    }

    const mainClusteringLogo = iframe.contentWindow.document.querySelector<HTMLImageElement>('g.layer-above > g.imagelayer > image');
    if (mainClusteringLogo) {
      mainClusteringLogo.style.display = 'none';
    }

    const allLinks = iframe.contentWindow.document.querySelectorAll<HTMLLinkElement>('a');
    allLinks.forEach(x => {
      if (
        x.getAttribute('xlink:href')?.startsWith('./') ||
        x.getAttribute('href')?.startsWith('./') ||
        x.getAttribute('xlink:href')?.startsWith('/') ||
        x.getAttribute('href')?.startsWith('/')
      ) {
        x.style.cursor = 'default';
        x.style.pointerEvents = 'none';
        if (x.className === 'float') {
          x.style.display = 'none';
        }
      }
    });

    const imagesToRemove = ['logo.png', 'logo_white.png', 'return_icon.png'];
    // const imagesToRemove = ['logo.png', 'logo_white.png'];
    const allImages = iframe.contentWindow.document.querySelectorAll<HTMLImageElement>('img');
    allImages.forEach(img => {
      imagesToRemove.forEach(x => {
        if (img.getAttribute('src')?.includes(x)) {
          img.style.display = 'none';
        }
      });
    });

    const mfbMenu = iframe.contentWindow.document.querySelector<HTMLUListElement>('#menu');
    if (mfbMenu) {
      mfbMenu.style.display = 'none';
    }
  }, []);

  const observerCallback = React.useCallback((mutationList, observer) => {
    disableAllUselessStuff();
  }, []);

  const onIframeLoad = () => {
    disableAllUselessStuff();
    const iframe = iframeRef?.current;
    const menuText = iframe.contentDocument.querySelector('.updatemenu-item-text');
    if (menuText) {
      const observer = new MutationObserver(observerCallback);
      observer.observe(menuText, { attributes: true, subtree: true, childList: true });
    }
  };

  const backUrl = () => {
    return props.location?.state?.fromConfigPage ? '/tools/t51/characterization' : '/task';
  };

  return (
    <>
      {!response?.simulationId ? (
        <div>No results to display. First, run the tool!</div>
      ) : (
        <>
          <ToolTitle imageAlt={WP_IMAGE.WP5.alt} title={toolDescription} imageSrc={WP_IMAGE.WP5.src} />
          <Divider />
          <h4>Results: </h4>

          {pageListLoading && <LoadingOverlay ref={divRef} />}
          {!pageListLoading && (
            <>
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
            </>
          )}
          <Divider />
          <div ref={divRef}>
            {loadingPage && <LoadingOverlay ref={divRef} />}
            {pageEntity && <iframe ref={iframeRef} style={{ width: '100%', height: 800 }} srcDoc={pageEntity} onLoad={onIframeLoad} />}
          </div>
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

export default T51CharacterizationResults;
