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
import Config from 'app/modules/tools/WP2/T251/config/config';
import { runT251Tool, reset as retry } from 'app/modules/tools/WP2/T251/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP2/T251/reducer/tool-results.reducer';

const T251 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();

  const methods = useForm();
  const { handleSubmit, reset } = methods;

  const dispatch = useAppDispatch();

  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

  if (!network) {
    props.history?.goBack();
    return;
  }

  const isRunning = useAppSelector(state => state.t251ToolExecution.loading);
  const response = useAppSelector(state => state.t251ToolExecution.entity);
  const completed = useAppSelector(state => state.t251ToolExecution.updateSuccess);

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);

  const parametersToSend = (parameters: any) => {
    const { input_network_path, input_resources_path, input_other_path } = parameters;
    return {
      input_network_path: input_network_path?.[0].name,
      input_resources_path: input_resources_path?.[0].name,
      input_other_path: input_other_path?.[0].name,
    };
  };

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    const parameters = { ...data.parameters };
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T25_DAY_AHEAD_TOOL.name,
      files: [parameters.input_network_path?.[0], parameters.input_resources_path?.[0], parameters.input_other_path?.[0]],
      jsonConfig: JSON.stringify({ ...parametersToSend(parameters) }),
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
        runT251Tool({
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
        <h4 style={{ marginLeft: 20 }}>{'T2.5 Day Ahead Aggregator tool'}</h4>
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

export default T251;
