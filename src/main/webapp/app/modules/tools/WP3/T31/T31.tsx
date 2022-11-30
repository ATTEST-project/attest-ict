import React from 'react';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import carouselImage1 from '../../../../../content/images/carousel_img_1.png';
import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import { FormProvider, useForm } from 'react-hook-form';
import Parameters from 'app/modules/tools/WP3/T31/parameters/parameters';
import { Link } from 'react-router-dom';
import { defaultParameters } from 'app/modules/tools/WP3/T31/parameters/default-parameters';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { runT31Tool, reset as retry } from 'app/modules/tools/WP3/T31/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP3/T31/reducer/tool-results.reducer';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { pathButton } from 'app/shared/reducers/back-button-path';

const T31 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();

  const methods = useForm({
    defaultValues: {
      parameters: { ...defaultParameters },
    },
  });
  const { handleSubmit, reset } = methods;

  const dispatch = useAppDispatch();

  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

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

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    const parameters = { ...data.parameters };
    Object.keys(parameters).forEach(key => (parameters[key] = convertStringToArray(key, parameters[key])));
    const finalForm = {
      networkId: network.id,
      toolName: TOOLS_INFO.T31_OPT_TOOL_DX.name,
      ...parameters,
    };
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
        runT31Tool({
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
    return completed && response.args?.toolName === TOOLS_INFO.T31_OPT_TOOL_DX.name;
  };

  return (
    <div ref={divRef}>
      {isRunning && <LoadingOverlay ref={divRef} />}
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Button color="dark">{<FontAwesomeIcon icon="bars" />}</Button>
        <img alt="wp3" src={carouselImage1} width={100} height={70} />
        <h4 style={{ marginLeft: 20 }}>{'T3.1 Optimisation Tool for distribution network planning'}</h4>
      </div>
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
                    <Button
                      tag={Link}
                      to={{ pathname: '/tools/t31/results', state: { fromConfigPage: true } }}
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
            <pre>{JSON.stringify({ ...form }, null, 2)}</pre>
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

export default T31;
