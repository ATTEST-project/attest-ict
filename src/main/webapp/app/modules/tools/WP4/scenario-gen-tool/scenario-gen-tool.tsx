import React from 'react';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import Divider from 'app/shared/components/divider/divider';
import { Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FormProvider, useForm } from 'react-hook-form';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import ParametersSection from 'app/modules/tools/WP4/scenario-gen-tool/parameters/parameters';
import { runTool, reset as retry } from 'app/modules/tools/reducer/tools-execution.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import { downloadResults } from 'app/modules/tools/WP4/reducer/tools-results.reducer';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';

const ScenarioGenTool = (props: any) => {
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
    console.log('RUN!');
    setOpenModal(false);
    dispatch(
      runTool({
        networkId: form.networkId,
        toolName: form.toolName,
        filesDesc: form.filesDesc,
        files: form.files,
        parameterNames: form.parameterNames,
        parameterValues: form.parameterValues,
      })
    );
    /* .unwrap()
      .then(res => {
        setCompleted(true);
        setChartsResponse(res.data);
      })
      .catch(err => {
        /!* eslint-disable-next-line no-console *!/
        console.error(err);
        setCompleted(false);
      }); */
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
        <h4>{'Scenario Generation Tool'}</h4>
        <NetworkInfo network={network} />
        <Divider />
        <FormProvider {...methods}>
          <Form onSubmit={handleSubmit(submitForm)}>
            <div className="section-with-border">
              <h6>Choose the data scenario file</h6>
              <Row md="3">
                <Col>
                  <ValidatedField
                    register={register}
                    error={errors?.dataScenarioFile}
                    id={'data-scenario-file'}
                    label="Data Scenario File"
                    name="dataScenarioFile"
                    data-cy="dataScenarioFile"
                    type="file"
                    accept=".ods,.xls,.xlsx"
                    validate={{ required: true }}
                  />
                </Col>
              </Row>
            </div>
            <Divider />
            <ParametersSection />
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
                  <Button tag={Link} to={{ pathname: '/tools/sgt/results', state: { fromConfigPage: true } }} color="success">
                    <FontAwesomeIcon icon="poll" />
                    {' Show Results'}
                  </Button>{' '}
                  <Button color="success" type="button" onClick={download}>
                    <FontAwesomeIcon icon="file-download" />
                    {' Download Results'}
                  </Button>
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
        <Modal isOpen={openModal}>
          <ModalHeader toggle={() => setOpenModal(false)}>{'Configuration'}</ModalHeader>
          <ModalBody>
            {'Check the configuration...'}
            <pre>{JSON.stringify({ ...form, files: form.files.map((v: File) => v.name) }, null, 2)}</pre>
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

export default ScenarioGenTool;
