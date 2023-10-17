import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import ProfilesSection from 'app/shared/components/T41-44/config/profiles/profiles';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import { Link } from 'react-router-dom';
import Config from 'app/modules/tools/WP2/T251/config/config';
import { runT251Tool, reset as retry } from 'app/modules/tools/WP2/T251/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP2/T251/reducer/tool-results.reducer';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

import { toast } from 'react-toastify';

const T251 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();
  const toolDescription = TOOLS_INFO.T25_DAY_AHEAD_TOOL.description;

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

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);
  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);

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
    console.log('RUN T251!');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT251Tool({
          ...form,
        })
      )
        .unwrap()
        .then(res => {
          if (res.data.status === 'ko') {
            toast.error('Tool execution failure, check log file for more details...');
          } else {
            toast.success('T251 is running!');
            setShowBtnGoToTask(true);
          }
        })
        .catch(err => {
          /* eslint-disable-next-line no-console */
          console.error(err);
        });
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

      <ToolTitle imageAlt={WP_IMAGE.WP2.alt} title={toolDescription} imageSrc={WP_IMAGE.WP2.src} />
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
                {!showBtnGoToTask ? (
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
                    <Button tag={Link} to={'/task'} color="success">
                      <FontAwesomeIcon icon="poll" />
                      {' Go to Tasks '}
                    </Button>{' '}
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
        <ModalConfirmToolExecution
          toolDescription={toolDescription}
          form={form}
          openModal={openModal}
          checkAndRun={checkAndRun}
          setOpenModal={setOpenModal}
        />
      )}
    </div>
  );
};

export default T251;
