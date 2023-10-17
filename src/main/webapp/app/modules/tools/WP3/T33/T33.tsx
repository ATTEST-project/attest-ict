import React from 'react';
import { Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FormProvider, useForm } from 'react-hook-form';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { toast } from 'react-toastify';

import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import Divider from 'app/shared/components/divider/divider';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';
import ToolTitle from 'app/shared/components/tool-title/tool-title';
import ParametersSection from 'app/modules/tools/WP3/T33/parameters/parameters';
import { runT33, reset as retry } from 'app/modules/tools/WP3/T33/reducer/tool-execution.reducer';
import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import { downloadResults } from 'app/modules/tools/WP4/reducer/tools-results.reducer';

const T33 = (props: any) => {
  /* eslint-disable-next-line no-console */
  console.log('T33 Config page - propos: ', props);
  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));
  const divRef = React.useRef<HTMLDivElement>();
  const dispatch = useAppDispatch();
  const toolDescription = TOOLS_INFO.T33_OPT_TOOL_PLAN_TSO_DSO.description;
  const methods = useForm();
  const {
    register,
    formState: { errors },
    reset,
    handleSubmit,
  } = methods;

  const isRunning = useAppSelector(state => state.t33ToolExecution.loading);
  const completed = useAppSelector(state => state.t33ToolExecution.updateSuccess);
  const response = useAppSelector(state => state.t33ToolExecution.entity);
  const [form, setForm] = React.useState(null);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);
  const parametersToSend = (parameters: any) => {
    const { ...rest } = parameters;
    const finalParameters = {
      case_dir: parameters.case_dir,
      specification_file: parameters.specification_file,
    };
    return { parameters: finalParameters };
  };

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('T33 submitMethod() - Form data: ', data);
    const parameters = { ...data.parameters };
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T33_OPT_TOOL_PLAN_TSO_DSO.name,
      files: [data.caseDirZipFile[0]],
      jsonConfig: JSON.stringify({ ...parametersToSend(parameters) }),
    };
    /* eslint-disable-next-line no-console */
    console.log('T33 submitMethod() - Final Form data: ', finalForm);
    setForm({ ...finalForm });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('T33 RUN!');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT33({
          ...form,
        })
      )
        .unwrap()
        .then(res => {
          if (res.data.status === 'ko') {
            toast.error('Tool execution failure, check log file for more details...');
          } else {
            toast.success('T33 is running!');
            setShowBtnGoToTask(true);
          }
        })
        .catch(err => {
          /* eslint-disable-next-line no-console */
          console.error(err);
        });
    }, 500);
  };

  return (
    <div ref={divRef}>
      {isRunning && <LoadingOverlay ref={divRef} />}
      <ToolTitle imageAlt={WP_IMAGE.WP3.alt} title={toolDescription} imageSrc={WP_IMAGE.WP3.src} />

      <div>
        <Divider />
        <NetworkInfo network={network} />
        <Divider />
        <FormProvider {...methods}>
          <Form onSubmit={handleSubmit(submitMethod)}>
            <div className="section-with-border">
              <h6>Choose the case dir zip file</h6>
              <Row md="3">
                <Col>
                  <ValidatedField
                    register={register}
                    error={errors?.caseDirZipFile}
                    id={'case-dir-zipfile'}
                    label="Case Dir Zip File"
                    name="caseDirZipFile"
                    data-cy="caseDirZipFile"
                    type="file"
                    accept=".zip"
                    validate={{ required: true }}
                  />
                </Col>
              </Row>
            </div>
            <Divider />
            <ParametersSection />
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

export default T33;
