import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import Config from 'app/modules/tools/WP5/T51/monitoring-tool/config/config';
import { runT512Tool, reset as retry } from 'app/modules/tools/WP5/T51/monitoring-tool/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP5/T51/monitoring-tool/reducer/tool-table.reducer';

import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T51Monitoring = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();
  const toolDescription = TOOLS_INFO.T51_MONITORING.description;

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

  // 2023 08 25 start
  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);
  // 2023 08 25 end

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
      )
        .unwrap()
        .then(res => {
          if (res.data.status === 'ko') {
            toast.error('Tool execution failure, check log file for more details...');
          } else {
            toast.success('Tool Monitoring is running!');
            setShowBtnGoToTask(true);
          }
        })
        .catch(err => {
          /* eslint-disable-next-line no-console */
          console.error(err);
        });
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

      <ToolTitle imageAlt={WP_IMAGE.WP5.alt} title={toolDescription} imageSrc={WP_IMAGE.WP5.src} />
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
        <Button tag={Link} to="/tools/t51" color="info">
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

export default T51Monitoring;
