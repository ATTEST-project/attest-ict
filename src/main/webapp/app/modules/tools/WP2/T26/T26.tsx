import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import carouselImage1 from '../../../../../content/images/carousel_img_1.png';
import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import ProfilesSection from 'app/shared/components/T41-44/config/profiles/profiles';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import { Link } from 'react-router-dom';
import Config from 'app/modules/tools/WP2/T26/config/config';
import { runT26Tool, reset as retry } from 'app/modules/tools/WP2/T26/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP2/T26/reducer/tool-results.reducer';

const T26 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();

  const methods = useForm();
  const { handleSubmit, reset } = methods;

  const dispatch = useAppDispatch();

  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

  if (!network) {
    props.history?.goBack();
    return;
  }

  const isRunning = useAppSelector(state => state.t26ToolExecution.loading);
  const response = useAppSelector(state => state.t26ToolExecution.entity);
  const completed = useAppSelector(state => state.t26ToolExecution.updateSuccess);

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);

  const filesToSend = (data: any) => {
    const networkFiles = Object.entries(data.network).map(([k, v]) => v[0]);
    const energyFiles = Object.entries(data.energy).map(([k, v]) => v[0]);
    // const secondaryFiles = Object.entries(data.secondary).map(([k,v]) => (v[0]));
    // const tertiaryFiles = Object.entries(data.tertiary).map(([k,v]) => (v[0]));
    return [
      ...networkFiles,
      ...energyFiles,
      // ...(secondaryFiles && [...secondaryFiles]),
      // ...(tertiaryFiles && [...tertiaryFiles])
    ];
  };

  const configToSend = (data: any) => {
    const { network, energy, secondary, tertiary, ...rest } = data;
    const networkFileNames = Object.entries(network).map(([k, v]) => ({ [k]: v[0].name }));
    const energyFileNames = Object.entries(energy).map(([k, v]) => ({ [k]: v[0].name }));
    // const secondaryFileNames = Object.entries(secondary).map(([k,v]) => ({[k]: v[0].name}));
    // const tertiaryFileNames = Object.entries(tertiary).map(([k,v]) => ({[k]: v[0].name}));
    return {
      ...rest,
      networkFileNames,
      energyFileNames,
      // ...(secondaryFileNames && [...secondaryFileNames]),
      // ...(tertiaryFileNames && [...tertiaryFileNames])
    };
  };

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T26_MARKET_SIMUL.name,
      files: filesToSend(data),
      jsonConfig: JSON.stringify({ ...configToSend(data) }),
    };
    /* eslint-disable-next-line no-console */
    console.log('Final Form data: ', finalForm);
    setForm({ ...finalForm });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('RUN!');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT26Tool({
          ...form,
        })
      );
    }, 500);
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

  const checkCompleted = () => {
    return completed;
  };

  const checkCompletedWithError = () => {
    return completed && response.status === 'ko';
  };

  return (
    <div ref={divRef}>
      {isRunning && <LoadingOverlay ref={divRef} />}
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Button color="dark">{<FontAwesomeIcon icon="bars" />}</Button>
        <img alt="wp2" src={carouselImage1} width={100} height={70} />
        <h4 style={{ marginLeft: 20 }}>{'T2.5 Real Time Optimisation Tool'}</h4>
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
                        {/* <Button
                          tag={Link}
                          to={{ pathname: '/tools/t32/results', state: { response, fromConfigPage: true } }}
                          color="success"
                          type="button"
                        >
                          <FontAwesomeIcon icon="poll" />
                          {' Show Results'}
                        </Button> */}{' '}
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
            <pre>{JSON.stringify({ ...form, files: form.files.map((file: File) => file.name) }, null, 2)}</pre>
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

export default T26;
