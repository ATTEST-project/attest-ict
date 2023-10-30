import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { Link } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader, Offcanvas, OffcanvasBody, OffcanvasHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toast } from 'react-toastify';

import { pathButton } from 'app/shared/reducers/back-button-path';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import toolsInfo from 'app/modules/tools/info/tools-info';
import { defaultParameters } from 'app/modules/tools/WP3/T31/parameters/default-parameters';

import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/network-info/network-info';
import Parameters from 'app/modules/tools/WP3/T32/parameters/parameters';

import ProfilesSection from 'app/shared/components/T41-44/config/profiles/profiles';
import { runT32Tool } from 'app/modules/tools/WP3/T32/reducer/tool-execution.reducer';
import { downloadResults, reset as retry } from 'app/modules/tools/WP3/T32/reducer/tool-results.reducer';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';

import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';
import ToolTitle from 'app/shared/components/tool-title/tool-title';
import { RUN_TOOL_START, RUN_TOOL_FAILURE } from 'app/shared/util/toast-msg-constants';

const T32 = (props: any) => {
  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
  const divRef = React.useRef<HTMLDivElement>();
  const dispatch = useAppDispatch();
  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));
  const country = network.country;
  const toolDescription = TOOLS_INFO.T32_OPT_TOOL_TX.description;
  const toolNum = toolsInfo.WP3[1].name;

  const defaultParamsValues = () => {
    switch (country) {
      case 'UK':
        return { parameters: { use_load_data_update: false, add_load_data_case_name: 'UK_Tx_' } };
      case 'HR':
        return { parameters: { use_load_data_update: false, add_load_data_case_name: 'HR_Tx_01_' } };
      case 'PT':
        return { parameters: { use_load_data_update: false, add_load_data_case_name: 'PT_Tx_01_' } };
      default:
        return { parameters: { use_load_data_update: false } };
    }
  };

  const methods = useForm({ defaultValues: defaultParamsValues() });
  const { handleSubmit, reset } = methods;

  if (!network) {
    props.history?.goBack();
    return;
  }

  const convertBooleanToNumber = React.useCallback((parameter, value) => {
    const listKeys = ['use_load_data_update'];
    if (listKeys.includes(parameter) && typeof value === 'boolean') {
      return value ? 1 : 0;
    } else {
      return value;
    }
  }, []);

  const isRunning = useAppSelector(state => state.t32ToolExecution.loading);
  const response = useAppSelector(state => state.t32ToolExecution.entity);
  const completed = useAppSelector(state => state.t32ToolExecution.updateSuccess);

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);
  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);

  const parametersToSend = (parameters: any) => {
    const { scenario_gen, ...rest } = parameters;
    const xlsxFileName = rest.xlsx_file_name?.[0]?.name;
    const odsFileName = rest.ods_file_name?.[0]?.name;
    const evDataFileName = rest.EV_data_file_name?.[0]?.name;
    const finalParameters = {
      ...rest,
      test_case: network.name,
      xlsx_file_name: xlsxFileName?.substring(0, xlsxFileName?.indexOf('.')) || '',
      ods_file_name: odsFileName?.substring(0, odsFileName?.indexOf('.')),
      EV_data_file_name: evDataFileName,
      country: network.country,
      peak_hour: Number(rest.peak_hour),
      no_year: Number(rest.no_year),
    };
    return { parameters: finalParameters };
  };

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    // console.log('T32 Form data: ', data);
    const parameters = { ...data.parameters };
    Object.keys(parameters).forEach(key => (parameters[key] = convertBooleanToNumber(key, parameters[key])));
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T32_OPT_TOOL_TX.name,
      ...(data.profiles[0] && { profileId: data.profiles[0].id }),
      files: [parameters.ods_file_name?.[0], parameters.scenario_gen?.[0]],
      jsonConfig: JSON.stringify({ ...parametersToSend(parameters) }),
    };
    parameters.xlsx_file_name?.[0] && finalForm.files.unshift(parameters.xlsx_file_name?.[0]);
    parameters.EV_data_file_name?.[0] && finalForm.files.push(parameters.EV_data_file_name?.[0]);
    setForm({ ...finalForm });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log(' T32 RUN!');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT32Tool({
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

      <ToolTitle imageAlt={WP_IMAGE.WP3.alt} title={toolDescription} imageSrc={WP_IMAGE.WP3.src} />

      <Divider />
      {network && (
        <>
          <NetworkInfo network={network} />
          <Divider />
          <FormProvider {...methods}>
            <Form onSubmit={handleSubmit(submitMethod)}>
              <ProfilesSection network={network} rowSelection={SELECTION_TYPE.SINGLE} />
              <Divider />
              <Parameters />
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

export default T32;
