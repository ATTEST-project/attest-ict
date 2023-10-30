import React from 'react';
import { Link } from 'react-router-dom';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row, Col, Offcanvas, OffcanvasBody, OffcanvasHeader } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { FormProvider, useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/network-info/network-info';
import Parameters from 'app/modules/tools/WP3/T31/parameters/parameters';
import { defaultParameters } from 'app/modules/tools/WP3/T31/parameters/default-parameters';
import { defaultParametersES } from 'app/modules/tools/WP3/T31/parameters/default-parameters_ES';
import { defaultParametersHR } from 'app/modules/tools/WP3/T31/parameters/default-parameters_HR';
import { defaultParametersPT } from 'app/modules/tools/WP3/T31/parameters/default-parameters_PT';
import { defaultParametersUK } from 'app/modules/tools/WP3/T31/parameters/default-parameters_UK';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import toolsInfo from 'app/modules/tools/info/tools-info';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { runT31Tool, reset as retry } from 'app/modules/tools/WP3/T31/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP3/T31/reducer/tool-results.reducer';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { pathButton } from 'app/shared/reducers/back-button-path';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';
import ToolTitle from 'app/shared/components/tool-title/tool-title';
import { RUN_TOOL_START, RUN_TOOL_FAILURE } from 'app/shared/util/toast-msg-constants';

const T31 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();
  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));
  const country = network.country;
  const toolDescription = TOOLS_INFO.T31_OPT_TOOL_DX.description;
  const toolNum = toolsInfo.WP3[0].name;
  const defaultParamsValues = () => {
    switch (country) {
      case 'UK':
        return { parameters: { ...defaultParametersUK } };
      case 'HR':
        return { parameters: { ...defaultParametersHR } };
      case 'PT':
        return { parameters: { ...defaultParametersPT } };
      case 'ES':
        return { parameters: { ...defaultParametersES } };
      default:
        return { parameters: { ...defaultParameters } };
    }
  };

  const methods = useForm({ defaultValues: defaultParamsValues() });
  const { handleSubmit, reset } = methods;
  const dispatch = useAppDispatch();

  if (!network) {
    props.history?.goBack();
    return;
  }

  React.useEffect(() => {
    dispatch(pathButton('/tools/t31'));
  }, []);

  const isRunning = useAppSelector(state => state.t31ToolExecution.loading);
  const response = useAppSelector(state => state.t31ToolExecution.entity);
  const completed = useAppSelector(state => state.t31ToolExecution.updateSuccess);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);
  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);

  const convertStringToArray = React.useCallback((parameter, value) => {
    const listKeys = ['line_capacities', 'TRS_capacities', 'line_costs', 'TRS_costs', 'cont_list', 'line_length', 'scenarios'];
    if (listKeys.includes(parameter)) {
      if (typeof value === 'string' && value !== '') {
        return value.split(',').map(v => parseFloat(v));
      } else if (value === '') {
        return defaultParameters[parameter];
      }
      return value;
    }
    return value;
  }, []);

  const convertBooleanToNumber = React.useCallback((parameter, value) => {
    const listKeys = ['use_load_data_update'];
    if (listKeys.includes(parameter) && typeof value === 'boolean') {
      return value ? 1 : 0;
    } else {
      return value;
    }
  }, []);

  const parametersToSend = (parameters: any) => {
    const { ...rest } = parameters;
    const evDataFileName = rest.EV_data_file_path?.[0]?.name;
    const finalParameters = {
      ...rest,
      EV_data_file_path: evDataFileName,
    };
    return { parameters: finalParameters };
  };

  const submitMethod = data => {
    const parameters = { ...data.parameters };
    /* eslint-disable-next-line no-console */
    // console.info('T31 submitMethod() - parameters! ', parameters);

    Object.keys(parameters).forEach(key => (parameters[key] = convertStringToArray(key, parameters[key])));
    Object.keys(parameters).forEach(key => (parameters[key] = convertBooleanToNumber(key, parameters[key])));
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T31_OPT_TOOL_DX.name,
      files: [parameters.EV_data_file_path?.[0]],
      jsonConfig: JSON.stringify({ ...parametersToSend(parameters) }),
    };
    /* eslint-disable-next-line no-console */
    console.log('T31 submitMethod() T31 Final Form data: ', finalForm);
    setForm({ ...finalForm });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('T31 checkAndRun()');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT31Tool({
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
          console.error('Error running T31! ' + err);
        });
    }, 500);
  };

  const checkCompleted = () => {
    return !isRunning;
  };

  return (
    <div ref={divRef}>
      {isRunning && <LoadingOverlay ref={divRef} />}

      <ToolTitle imageAlt={WP_IMAGE.WP3.alt} title={toolDescription} imageSrc={WP_IMAGE.WP3.src} />
      <Divider />

      {network && (
        <>
          <NetworkInfo network={network} />
          <Divider />
          <FormProvider {...methods}>
            <Form onSubmit={handleSubmit(submitMethod)}>
              <Parameters network={network} />

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

export default T31;
