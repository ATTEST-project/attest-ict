import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { defaultParameters } from 'app/modules/tools/WP3/T31/parameters/default-parameters';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { pathButton } from 'app/shared/reducers/back-button-path';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import carouselImage1 from '../../../../../content/images/carousel_img_1.png';
import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import Parameters from 'app/modules/tools/WP3/T32/parameters/parameters';
import { Link } from 'react-router-dom';
import ProfilesSection from 'app/shared/components/T41-44/config/profiles/profiles';
import { runT32Tool } from 'app/modules/tools/WP3/T32/reducer/tool-execution.reducer';
import { downloadResults, reset as retry } from 'app/modules/tools/WP3/T32/reducer/tool-results.reducer';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';

const T32 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();

  const methods = useForm();
  const { handleSubmit, reset } = methods;

  const dispatch = useAppDispatch();

  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

  if (!network) {
    props.history?.goBack();
    return;
  }

  const isRunning = useAppSelector(state => state.t32ToolExecution.loading);
  const response = useAppSelector(state => state.t32ToolExecution.entity);
  const completed = useAppSelector(state => state.t32ToolExecution.updateSuccess);

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);

  const parametersToSend = (parameters: any) => {
    const { scenario_gen, ...rest } = parameters;
    const xlsxFileName = rest.xlsx_file_name?.[0]?.name;
    const odsFileName = rest.ods_file_name?.[0]?.name;
    const finalParameters = {
      ...rest,
      test_case: network.name,
      xlsx_file_name: xlsxFileName?.substring(0, xlsxFileName?.indexOf('.')) || '',
      ods_file_name: odsFileName?.substring(0, odsFileName?.indexOf('.')),
      country: network.country,
      peak_hour: Number(rest.peak_hour),
      no_year: Number(rest.no_year),
    };
    return { parameters: finalParameters };
  };

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    const parameters = { ...data.parameters };
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T32_OPT_TOOL_TX.name,
      ...(data.profiles[0] && { profileId: data.profiles[0].id }),
      files: [parameters.ods_file_name?.[0], parameters.scenario_gen?.[0]],
      jsonConfig: JSON.stringify({ ...parametersToSend(parameters) }),
    };
    parameters.xlsx_file_name?.[0] && finalForm.files.unshift(parameters.xlsx_file_name?.[0]);
    /* eslint-disable-next-line no-console */
    console.log('Final Form data: ', finalForm);
    setForm({ ...finalForm });
    setOpenModal(true);
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('RUN!');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT32Tool({
          ...form,
        })
      );
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
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Button color="dark">{<FontAwesomeIcon icon="bars" />}</Button>
        <img alt="wp3" src={carouselImage1} width={100} height={70} />
        <h4 style={{ marginLeft: 20 }}>{'T3.2 Optimisation Tool for transmission network planning'}</h4>
      </div>
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
                    {checkCompletedWithError() ? (
                      <Button disabled className="rounded-circle" color="danger" type="button">
                        <FontAwesomeIcon icon="exclamation" />
                      </Button>
                    ) : (
                      <>
                        <Button
                          tag={Link}
                          to={{ pathname: '/tools/t32/results', state: { response, fromConfigPage: true } }}
                          color="success"
                          type="button"
                        >
                          <FontAwesomeIcon icon="poll" />
                          {' Show Results'}
                        </Button>{' '}
                        <Button color="success" type="button" onClick={download}>
                          <FontAwesomeIcon icon="file-download" />
                          {' Download Results'}
                        </Button>
                      </>
                    )}
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
        <Modal isOpen={openModal}>
          <ModalHeader toggle={() => setOpenModal(false)}>{'Configuration'}</ModalHeader>
          <ModalBody>
            {'Check the configuration...'}
            <pre>{JSON.stringify({ ...form, files: form.files.map((file: File) => file.name) }, null, 2)}</pre>
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

export default T32;
