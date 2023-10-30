import React from 'react';
import NetworkInfo from 'app/shared/components/network-info/network-info';
import Divider from 'app/shared/components/divider/divider';
import { Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FormProvider, useForm } from 'react-hook-form';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import ParametersSection from 'app/modules/tools/WP4/scenario-gen-tool/parameters/parameters';
import { runTool, reset as retry } from 'app/modules/tools/reducer/tools-execution.reducer';
import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import toolsInfo from 'app/modules/tools/info/tools-info';
import { downloadResults } from 'app/modules/tools/WP4/reducer/tools-results.reducer';

import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';
import ToolTitle from 'app/shared/components/tool-title/tool-title';
import { RUN_TOOL_START, RUN_TOOL_FAILURE } from 'app/shared/util/toast-msg-constants';
import SectionHeader from 'app/shared/components/section-header/section-header';

const ScenarioGenTool = (props: any) => {
  const toolNum = toolsInfo.WP4[0].name;
  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));
  const divRef = React.useRef<HTMLDivElement>();
  const dispatch = useAppDispatch();
  const methods = useForm();

  const {
    register,
    formState: { errors },
    reset,
    handleSubmit,
  } = methods;

  const isRunning = useAppSelector(state => state.toolsExecution.loading);
  const completed = useAppSelector(state => state.toolsExecution.updateSuccess);
  const response = useAppSelector(state => state.toolsExecution.entity);

  const toolDescription = TOOLS_INFO.T41_WIND_AND_PV.description;
  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);

  const [form, setForm] = React.useState<any>('');

  const [openModal, setOpenModal] = React.useState<boolean>(false);

  const submitForm = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    const formData = {
      networkId: network.id,
      toolName: TOOLS_NAMES.T41_WIND_AND_PV,
      parameterNames: [...Object.keys(data.parameters)],
      parameterValues: [...Object.values(data.parameters)],
      filesDesc: ['scenario'],
      files: [data.dataScenarioFile[0]],
    };
    /* eslint-disable-next-line no-console */
    console.log('Form data to send: ', formData);
    setForm({ ...formData });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('TSG - checkAndRun() ');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runTool({
          networkId: form.networkId,
          toolName: form.toolName,
          filesDesc: form.filesDesc,
          files: form.files,
          parameterNames: form.parameterNames,
          parameterValues: form.parameterValues,
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
    return completed && response?.args?.toolName === TOOLS_INFO.T41_WIND_AND_PV.name;
  };

  return (
    <div ref={divRef}>
      {isRunning && <LoadingOverlay ref={divRef} />}
      <div>
        <ToolTitle imageAlt={WP_IMAGE.WP4.alt} title={toolDescription} imageSrc={WP_IMAGE.WP4.src} />
        <NetworkInfo network={network} />
        <Divider />
        <FormProvider {...methods}>
          <Form onSubmit={handleSubmit(submitForm)}>
            <ParametersSection />

            <Divider />
            <div className="section-with-border">
              <SectionHeader title="Upload Auxiliary Data" />

              <div style={{ marginBottom: 10 }} />
              <Row md="3">
                <Col>
                  <ValidatedField
                    register={register}
                    error={errors?.dataScenarioFile}
                    id={'data-scenario-file'}
                    label="Data Scenario File [e.g data_scenarios.ods]"
                    name="dataScenarioFile"
                    data-cy="dataScenarioFile"
                    type="file"
                    accept=".ods"
                    validate={{ required: true }}
                  />
                </Col>
              </Row>
            </div>

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
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <Button tag={Link} to="/tools" color="info">
            <FontAwesomeIcon icon="arrow-left" />
            {' Back'}
          </Button>
        </div>
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

export default ScenarioGenTool;
