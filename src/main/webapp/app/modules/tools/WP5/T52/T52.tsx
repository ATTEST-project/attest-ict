import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toast } from 'react-toastify';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/network-info/network-info';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';

import Config from 'app/modules/tools/WP5/T52/config/config';
import Coordinates from 'app/modules/tools/WP5/T52/coordinates/coordinates';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import toolsInfo from 'app/modules/tools/info/tools-info';

import { runT52Tool, reset as retry } from 'app/modules/tools/WP5/T52/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP5/T52/reducer/tool-table.reducer';
import { RUN_TOOL_START, RUN_TOOL_FAILURE } from 'app/shared/util/toast-msg-constants';
import ToolTitle from 'app/shared/components/tool-title/tool-title';
import { showError } from 'app/modules/tools/custom-toast-error';

const T52 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();
  const dispatch = useAppDispatch();
  const toolDescription = TOOLS_INFO.T52_INDICATOR.description;
  const toolNum = toolsInfo.WP5[1].name;

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
  // 2023 08 25 start
  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);
  // 2023 08 25 end

  const cleanDataForm = (form: any) => {
    const mainConfig = [...form.mainConfig];
    const mainConfigCleaned = [];
    for (const config of mainConfig) {
      const { assetsFile, variables, weights, ...rest } = config;
      const fileName = assetsFile[0].name;
      const variablesKeys = Object.keys(variables).filter(k => variables[k]);

      // --if no variables have been selected, show an error message.
      if (variablesKeys.length === 0) {
        return { mainConfig: [] };
      }

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

    const returnData = JSON.parse(finalForm.jsonConfig);
    if (returnData.mainConfig.length === 0) {
      showError('Please select one ore more variables from each set');
      return;
    }
    /* eslint-disable-next-line no-console */
    console.log('Final form: ', finalForm);
    setForm({ ...finalForm });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('T52 RUN!');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT52Tool({
          ...form,
        })
      )
        .unwrap()
        .then(res => {
          if (res.data.status === 'ko') {
            toast.error(toolNum + ': ' + RUN_TOOL_FAILURE);
          } else {
            toast.success(toolNum + ': ' + RUN_TOOL_START);
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
              <Coordinates />
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

export default T52;
