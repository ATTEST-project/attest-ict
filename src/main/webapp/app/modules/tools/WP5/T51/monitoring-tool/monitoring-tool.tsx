import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader, Offcanvas, OffcanvasBody, OffcanvasHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import carouselImage1 from '../../../../../../content/images/carousel_img_1.png';
import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import { Link } from 'react-router-dom';
import Config from 'app/modules/tools/WP5/T51/monitoring-tool/config/config';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { runT512Tool, reset as retry } from 'app/modules/tools/WP5/T51/monitoring-tool/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP5/T51/monitoring-tool/reducer/tool-table.reducer';

const T51Monitoring = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();

  const dispatch = useAppDispatch();

  const methods = useForm();
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = methods;

  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

  if (!network) {
    props.history?.goBack();
    return;
  }

  const response = useAppSelector(state => state.t512ToolExecution.entity);
  const isRunning = useAppSelector(state => state.t512ToolExecution.loading);
  const completed = useAppSelector(state => state.t512ToolExecution.updateSuccess);

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);

  const cleanDataForm = (form: any) => {
    const { modelpath1, modelpath2, filepath2, ...rest } = form.config;
    const modelFileName = modelpath1?.[0].name;
    const errorModelFileName = modelpath2?.[0].name;
    const inputFileName = filepath2?.[0].name;
    return {
      modelpath1: modelFileName,
      modelpath2: errorModelFileName,
      filepath2: inputFileName,
      ...rest,
    };
  };

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T51_MONITORING.name,
      files: [data.config.modelpath1?.[0], data.config.modelpath2?.[0], data.config.filepath2?.[0]],
      jsonConfig: JSON.stringify({ ...cleanDataForm(data) }),
    };
    /* eslint-disable-next-line no-console */
    console.log('Final form data: ', finalForm);
    setForm({ ...finalForm });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('RUN!');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT512Tool({
          ...form,
        })
      );
    }, 500);
  };

  const checkCompleted = () => {
    return completed;
  };

  const checkCompletedWithError = () => {
    return completed && response?.status === 'ko';
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

  const retryToolRun = () => {
    dispatch(retry());
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
        <h4 style={{ marginLeft: 20 }}>{'T5.1 Monitoring Tool'}</h4>
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
                          to={{ pathname: '/tools/t51/monitoring/results', state: { response, fromConfigPage: true } }}
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
        <Button tag={Link} to="/tools/t51" color="info">
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

export default T51Monitoring;
