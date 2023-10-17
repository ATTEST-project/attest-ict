import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toast } from 'react-toastify';

import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import { Link } from 'react-router-dom';
import Config from 'app/modules/tools/WP5/T51/characterization-tool/config/config';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { runT511Tool, reset as retry } from 'app/modules/tools/WP5/T51/characterization-tool/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP5/T51/characterization-tool/reducer/tool-table.reducer';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';

import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T51Characterization = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();

  const dispatch = useAppDispatch();
  const toolDescription = TOOLS_INFO.T51_CHARACTERIZATION.description;
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

  const response = useAppSelector(state => state.t511ToolExecution.entity);
  const isRunning = useAppSelector(state => state.t511ToolExecution.loading);
  const completed = useAppSelector(state => state.t511ToolExecution.updateSuccess);

  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);

  const cleanDataForm = (form: any) => {
    const configs = [...form.config];
    const configCleaned = [];
    for (const config of configs) {
      const { inputFile, inputAuxFile, variables, variables2, results, selectedVariables, component2_field, ...rest } = config;
      const fileName = inputFile[0].name;
      const auxFileName = inputAuxFile[0]?.name || '';
      const variablesKeys = Object.keys(variables).filter(k => variables[k]);
      const resultsKeys = Object.keys(results).filter(k => results[k]);

      configCleaned.push({
        ...rest,
        path: fileName,
        path2: auxFileName,
        selectedVariables,
        variables: [...variablesKeys],
        results: [...resultsKeys],
        component1_field: selectedVariables,
        component2_field: component2_field || '',
        variables2: variables2 || '',
        components: [],
        components2: [],
      });
    }
    return { configs: [...configCleaned] };
  };

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T51_CHARACTERIZATION.name,
      files: [
        ...data.config.flatMap(element =>
          element.inputAuxFile.length > 0 ? [element.inputFile[0], element.inputAuxFile[0]] : [element.inputFile[0]]
        ),
      ],

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
        runT511Tool({
          ...form,
        })
      )
        .unwrap()
        .then(res => {
          if (res.data.status === 'ko') {
            toast.error('Characterization tool execution failure, check log file for more details...');
          } else {
            toast.success('Characterization tool is running!');
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

export default T51Characterization;
