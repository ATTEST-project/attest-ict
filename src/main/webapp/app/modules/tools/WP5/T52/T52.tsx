import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader, Offcanvas, OffcanvasBody, OffcanvasHeader } from 'reactstrap';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import carouselImage1 from '../../../../../content/images/carousel_img_1.png';
import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import { Link } from 'react-router-dom';
import Config from 'app/modules/tools/WP5/T52/config/config';
import Coordinates from 'app/modules/tools/WP5/T52/coordinates/coordinates';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { runT52Tool, reset as retry } from 'app/modules/tools/WP5/T52/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP5/T52/reducer/tool-table.reducer';

const T52 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();

  const dispatch = useAppDispatch();

  const methods = useForm();
  const { handleSubmit, reset } = methods;

  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

  if (!network) {
    props.history?.goBack();
    return;
  }

  const response = useAppSelector(state => state.t52ToolExecution.entity);
  const isRunning = useAppSelector(state => state.t52ToolExecution.loading);
  const completed = useAppSelector(state => state.t52ToolExecution.updateSuccess);

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);

  const cleanDataForm = (form: any) => {
    const mainConfig = [...form.mainConfig];
    const mainConfigCleaned = [];
    for (const config of mainConfig) {
      const { assetsFile, variables, weights, ...rest } = config;
      const fileName = assetsFile[0].name;
      const variablesKeys = Object.keys(variables).filter(k => variables[k]);
      const finalWeights = weights.split(',');
      mainConfigCleaned.push({ path: fileName, variables: [...variablesKeys], weights: [...finalWeights], ...rest });
    }

    const coordinatesConfig = { ...form.coordinatesConfig };
    const { coordsFile, coordinatesIdentifier, ...rest } = coordinatesConfig;

    if (coordinatesIdentifier) {
      const coordsFileName = coordsFile[0].name;
      return {
        mainConfig: [...mainConfigCleaned],
        coordinatesConfig: { coordinatesFilePath: coordsFileName, coordinatesIdentifier, ...rest },
      };
    }

    return { mainConfig: [...mainConfigCleaned] };
  };

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T52_INDICATOR.name,
      files: [...data.mainConfig.map(element => element.assetsFile[0])],
      jsonConfig: JSON.stringify({ ...cleanDataForm(data) }),
    };
    data.coordinatesConfig?.coordsFile?.[0] && finalForm.files.push(data.coordinatesConfig?.coordsFile?.[0]);
    /* eslint-disable-next-line no-console */
    console.log('Final form: ', finalForm);
    setForm({ ...finalForm });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('RUN!');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT52Tool({
          ...form,
        })
      );
    }, 500);
  };

  const checkCompleted = () => {
    return completed && response.args?.toolName === TOOLS_INFO.T52_INDICATOR.name;
  };

  const checkCompletedWithError = () => {
    return completed && response.args?.toolName === TOOLS_INFO.T52_INDICATOR.name && response.status === 'ko';
  };

  const retryToolRun = () => {
    dispatch(retry());
  };

  const download = () => {
    dispatch(
      downloadResults({
        networkId: response.args?.networkId,
        toolName: response.args?.toolName,
        simulationId: response.simulationId,
      })
    );
  };

  return (
    <div ref={divRef}>
      {isRunning && <LoadingOverlay ref={divRef} />}
      <Offcanvas isOpen={openOffCanvas} toggle={() => setOpenOffCanvas(false)}>
        <OffcanvasHeader toggle={() => setOpenOffCanvas(false)}>{'WP5 Tools'}</OffcanvasHeader>
        <OffcanvasBody>{'Tool_x'}</OffcanvasBody>
      </Offcanvas>
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Button color="dark" onClick={() => setOpenOffCanvas(true)}>
          {<FontAwesomeIcon icon="bars" />}
        </Button>
        <img alt="wp5" src={carouselImage1} width={100} height={70} />
        <h4 style={{ marginLeft: 20 }}>{'T5.2 Definition of condition indicators'}</h4>
      </div>
      <Divider />
      {network && (
        <>
          <NetworkInfo network={network} />
          <Divider />
          <FormProvider {...methods}>
            <Form onSubmit={handleSubmit(submitMethod)}>
              <Config />
              <Divider />
              <Coordinates />
              <Divider />
              <div style={{ float: 'right' }}>
                {!checkCompleted() ? (
                  <>
                    <Button color="primary" onClick={() => reset()}>
                      <FontAwesomeIcon icon="redo" />
                      {' Reset'}
                    </Button>{' '}
                    <Button color="primary" type="submit">
                      <FontAwesomeIcon icon="play" />
                      {' Run '}
                    </Button>
                  </>
                ) : (
                  <>
                    <Button color="primary" onClick={retryToolRun}>
                      <FontAwesomeIcon icon="redo" />
                      {' Retry'}
                    </Button>{' '}
                    {checkCompletedWithError() ? (
                      <Button disabled className="rounded-circle" color="danger" type="button">
                        <FontAwesomeIcon icon="exclamation" />
                      </Button>
                    ) : (
                      <>
                        <Button
                          tag={Link}
                          to={{ pathname: '/tools/t52/results', state: { response, fromConfigPage: true } }}
                          color="success"
                          type="button"
                        >
                          <FontAwesomeIcon icon="poll" />
                          {' Show Results'}
                        </Button>{' '}
                        <Button color="success" type="button" onClick={download}>
                          <FontAwesomeIcon icon="file-download" />
                          {' Download Results'}
                        </Button>
                      </>
                    )}
                  </>
                )}
              </div>
            </Form>
          </FormProvider>
        </>
      )}
      <Divider />
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <Button tag={Link} to="/tools" color="info">
          <FontAwesomeIcon icon="arrow-left" />
          {' Back'}
        </Button>
      </div>
      {form && (
        <Modal isOpen={openModal}>
          <ModalHeader toggle={() => setOpenModal(false)}>{'Configuration'}</ModalHeader>
          <ModalBody>
            {'Check the configuration...'}
            <pre>{JSON.stringify({ ...form, files: form.files.map((v: File) => v.name) }, null, 2)}</pre>
          </ModalBody>
          <ModalFooter>
            <Button onClick={() => setOpenModal(false)}>Cancel</Button>
            <Button color="primary" onClick={checkAndRun}>
              Run
            </Button>{' '}
          </ModalFooter>
        </Modal>
      )}
    </div>
  );
};

export default T52;
