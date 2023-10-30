import React from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import toolsInfo from 'app/modules/tools/info/tools-info';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/network-info/network-info';
import ProfilesSection from 'app/shared/components/T41-44/config/profiles/profiles';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import { Link } from 'react-router-dom';
import Config from 'app/modules/tools/WP2/T26/config/config';
import { runT26Tool, reset as retry } from 'app/modules/tools/WP2/T26/reducer/tool-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP2/T26/reducer/tool-results.reducer';
import { toast } from 'react-toastify';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';
import ToolTitle from 'app/shared/components/tool-title/tool-title';
import { RUN_TOOL_START, RUN_TOOL_FAILURE } from 'app/shared/util/toast-msg-constants';

const T26 = (props: any) => {
  const divRef = React.useRef<HTMLDivElement>();

  const methods = useForm({ reValidateMode: 'onChange' });

  const { handleSubmit, reset } = methods;

  const dispatch = useAppDispatch();

  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

  const toolDescription = TOOLS_INFO.T26_MARKET_SIMUL.description;
  const toolNum = toolsInfo.WP2[2].name;

  if (!network) {
    props.history?.goBack();
    return;
  }

  const isRunning = useAppSelector(state => state.t26ToolExecution.loading);
  const response = useAppSelector(state => state.t26ToolExecution.entity);
  const completed = useAppSelector(state => state.t26ToolExecution.updateSuccess);

  const [openModal, setOpenModal] = React.useState<boolean>(false);

  const [form, setForm] = React.useState(null);
  const [showBtnGoToTask, setShowBtnGoToTask] = React.useState<boolean>(false);

  const renameFile = (f: any, prefix: string) => {
    return new File([f], prefix + f.name, { type: f.type });
  };

  const filesToSend = (data: any) => {
    const networkFiles = Object.entries(data.network)
      .map(([k, v]) => v[0])
      .map(f => renameFile(f, 'network_'));
    const energyFiles = Object.entries(data.energy)
      .filter(a => getLength(a[1]) > 0)
      .map(([k, v]) => v[0])
      .map(f => renameFile(f, 'energy_'));
    const secondaryFiles = Object.entries(data.secondary)
      .filter(a => getLength(a[1]) > 0)
      .map(([k, v]) => v[0])
      .map(f => renameFile(f, 'secondary_'));
    const tertiaryFiles = Object.entries(data.tertiary)
      .filter(a => getLength(a[1]) > 0)
      .map(([k, v]) => v[0])
      .map(f => renameFile(f, 'tertiary_'));
    return [...networkFiles, ...energyFiles, ...(secondaryFiles && [...secondaryFiles]), ...(tertiaryFiles && [...tertiaryFiles])];
  };

  const getLength = (data: any) => {
    /* eslint-disable-next-line no-console */
    // console.log(data.length);
    return data.length;
  };

  const configToSend = (data: any) => {
    const { network, energy, secondary, tertiary, ...rest } = data;
    const networkFileNames = Object.entries(network).map(([k, v]) => ({ [k]: v[0].name }));
    const energyFileNames = Object.entries(energy)
      .filter(a => getLength(a[1]) > 0)
      .map(([k, v]) => ({ [k]: v[0].name }));
    // debugger; // eslint-disable-line no-debugger
    const secondaryFileNames = secondary
      ? Object.entries(secondary)
          .filter(a => getLength(a[1]) > 0)
          .map(([k, v]) => ({ [k]: v[0].name }))
      : [];
    const tertiaryFileNames = tertiary
      ? Object.entries(tertiary)
          .filter(a => getLength(a[1]) > 0)
          .map(([k, v]) => ({ [k]: v[0].name }))
      : [];

    const retT26 = {
      ...rest,
      networkFileNames,
      energyFileNames,
      secondaryFileNames,
      tertiaryFileNames,
    };
    return retT26;
  };

  const [marketNotSelected, setMarketNotSelected] = React.useState<boolean>(false);

  const isMarketSelected = (data: any) => {
    const { parameters } = data;
    setMarketNotSelected(!parameters.run_energy && !parameters.run_secondary && !parameters.run_tertiary);
    // debugger; // eslint-disable-line no-debugger
    /* eslint-disable-next-line no-console */
    console.log('T26:  ' + marketNotSelected);
  };

  const submitMethod = data => {
    isMarketSelected(data);
    const { parameters } = data;
    if (parameters.run_energy || parameters.run_secondary || parameters.run_tertiary) {
      // debugger; // eslint-disable-line no-debugger
      /* eslint-disable-next-line no-console */
      console.log('submit:  ' + marketNotSelected);

      const finalForm = {
        networkId: network.id,
        toolName: TOOLS_INFO.T26_MARKET_SIMUL.name,
        files: filesToSend(data),
        jsonConfig: JSON.stringify({ ...configToSend(data) }),
      };
      /* eslint-disable-next-line no-console */
      console.log('Final Form data: ', finalForm);
      setForm({ ...finalForm });
      setOpenModal(true);
    }
  };

  const checkAndRun = () => {
    /* eslint-disable-next-line no-console */
    console.log('RUN!');
    setOpenModal(false);
    setTimeout(() => {
      dispatch(
        runT26Tool({
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

      <ToolTitle imageAlt={WP_IMAGE.WP2.alt} title={toolDescription} imageSrc={WP_IMAGE.WP2.src} />
      <Divider />

      {network && (
        <>
          <NetworkInfo network={network} />
          <Divider />
          <FormProvider {...methods}>
            <Form onSubmit={handleSubmit(submitMethod)}>
              <Config marketNotSelected={marketNotSelected} />
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

export default T26;
