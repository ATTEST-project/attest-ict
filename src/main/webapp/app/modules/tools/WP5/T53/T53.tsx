import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FormProvider, useForm } from 'react-hook-form';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader, Offcanvas, OffcanvasBody, OffcanvasHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import toolsInfo from 'app/modules/tools/info/tools-info';

import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';

import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/network-info/network-info';
import Config from 'app/modules/tools/WP5/T53/config/config';
import { runT53Tool, reset as retry } from 'app/modules/tools/WP5/T53/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP5/T53/reducer/tool-table.reducer';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';
import ToolTitle from 'app/shared/components/tool-title/tool-title';
import { RUN_TOOL_START, RUN_TOOL_FAILURE } from 'app/shared/util/toast-msg-constants';
import { showError } from 'app/modules/tools/custom-toast-error';

const T53 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();
  const toolDescription = TOOLS_INFO.T53_MANAGEMENT.description;
  const toolNum = toolsInfo.WP5[2].name;
  const dispatch = useAppDispatch();
  const methods = useForm();

  const { handleSubmit, reset } = methods;

  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

  if (!network) {
    props.history?.goBack();
    return;
  }

  const response = useAppSelector(state => state.t53ToolExecution.entity);
  const isRunning = useAppSelector(state => state.t53ToolExecution.loading);
  const completed = useAppSelector(state => state.t53ToolExecution.updateSuccess);
  // 2023 08 25 start
  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);
  // 2023 08 25 end

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);

  const cleanDataForm = (form: any) => {
    const dataForm = { ...form };
    const mainConfigCleaned = [];
    for (const config of dataForm.mainConfig) {
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
    const dataFormCleaned = {
      mainConfig: [...mainConfigCleaned],
      nScenarios: Number(dataForm.nScenarios),
      assetsName: dataForm.assetsName,
    };
    return { ...dataFormCleaned };
  };

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('T53 Form data: ', data);
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T53_MANAGEMENT.name,
      files: [...data.mainConfig.map(config => config.assetsFile[0])],
      jsonConfig: JSON.stringify({ ...cleanDataForm(data) }),
    };

    const returnData = JSON.parse(finalForm.jsonConfig);

    if (returnData.mainConfig.length === 0) {
      showError('Please select one ore more variables from each set');
      return;
    }
    /* eslint-disable-next-line no-console */
    console.log('T53 Final form: ', finalForm);
    setForm({ ...finalForm });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('T53 - checkAndRun()');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT53Tool({
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
    return completed;
  };

  const checkCompletedWithError = () => {
    return completed && response.status === 'ko';
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

export default T53;
