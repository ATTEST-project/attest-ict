import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FormProvider, useForm } from 'react-hook-form';
import { runTool, reset as retry } from 'app/modules/tools/reducer/tools-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP4/reducer/tools-results.reducer';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader, Offcanvas, OffcanvasBody, OffcanvasHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import carouselImage1 from '../../../../../content/images/carousel_img_1.png';
import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import ProfilesSection from 'app/shared/components/T41-44/config/profiles/profiles';
import Auxiliary from 'app/shared/components/T41-44/config/auxiliary/auxiliary';
import { Link } from 'react-router-dom';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { defaultParameters } from 'app/modules/tools/WP4/T41/tractability-tool/parameters/default-parameters';
import { toast } from 'react-toastify';
import { pathButton } from 'app/shared/reducers/back-button-path';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';

const T4144Config = (props: any) => {
  const { title, toolName, resultsPath, additionalNetwork, parameters } = props;

  const divRef = React.useRef<HTMLDivElement>();

  const dispatch = useAppDispatch();

  const methods =
    toolName === TOOLS_INFO.T41_TRACTABILITY.name
      ? useForm({ defaultValues: { parameters: { ...defaultParameters } } })
      : useForm({ defaultValues: { parameters: {} } });

  const { handleSubmit, reset } = methods;

  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

  if (!network) {
    props.history?.goBack();
    return;
  }

  React.useEffect(() => {
    const backPath = toolName === TOOLS_INFO.T41_TRACTABILITY.name ? '/tools/t41' : '/tools/t44';
    dispatch(pathButton(backPath));
  }, []);

  const [openOffCanvas, setOpenOffCanvas] = React.useState<boolean>(false);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [form, setForm] = React.useState(null);

  const isRunning = useAppSelector(state => state.toolsExecution.loading);
  const response = useAppSelector(state => state.toolsExecution.entity);
  const completed = useAppSelector(state => state.toolsExecution.updateSuccess);

  const convertBooleanToString = React.useCallback((value: boolean) => {
    return value ? '1' : '0';
  }, []);

  const submitMethod = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    const finalForm = {
      networkId: network.id,
      toolName,
      ...(data.parameters && { parameterNames: [...Object.keys(data.parameters)] }),
      ...(data.parameters && {
        parameterValues: [
          ...Object.values(data.parameters).map(param => (typeof param === 'boolean' ? convertBooleanToString(param) : param)),
        ],
      }),
      ...(data.profiles?.length > 0 ? { profileIds: [...data.profiles.map(profile => profile.id)] } : {}),
      filesDesc: [...(data.additionalNetwork?.length > 0 ? ['additionalNetwork'] : []), ...Object.keys(data.auxiliary)],
      files: [
        ...(data.additionalNetwork?.length > 0 ? data.additionalNetwork : []),
        ...Object.values(data.auxiliary).map(files => files[0]),
      ],
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
        runTool({
          networkId: form.networkId,
          toolName: form.toolName,
          filesDesc: form.filesDesc,
          files: form.files,
          parameterNames: form.parameterNames || [],
          parameterValues: form.parameterValues || [],
          ...(form.profileIds && { profileIds: form.profileIds }),
        })
      )
        .unwrap()
        .then(res => {
          if (res.data.status === 'ko') {
            toast.error('Tool execution failure, check log file for more details...');
          } else {
            toast.success('Tool ran successfully!');
          }
        })
        .catch(err => {
          /* eslint-disable-next-line no-console */
          console.error(err);
        });
    }, 500);
  };

  const checkCompleted = () => {
    return completed && response.args?.toolName === toolName;
  };

  const checkCompletedWithError = () => {
    return completed && response.args?.toolName === toolName && response.status === 'ko';
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

  const retryToolRun = () => {
    dispatch(retry());
  };

  return (
    <div ref={divRef}>
      {isRunning && <LoadingOverlay ref={divRef} />}
      <Offcanvas isOpen={openOffCanvas} toggle={() => setOpenOffCanvas(false)}>
        <OffcanvasHeader toggle={() => setOpenOffCanvas(false)}>{'WP4 Tools'}</OffcanvasHeader>
        <OffcanvasBody>{'Tool_x'}</OffcanvasBody>
      </Offcanvas>
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Button color="dark" onClick={() => setOpenOffCanvas(true)}>
          {<FontAwesomeIcon icon="bars" />}
        </Button>
        <img alt="wp4" src={carouselImage1} width={100} height={70} />
        <h4 style={{ marginLeft: 20 }}>{title}</h4>
      </div>
      <Divider />
      {network && (
        <>
          <NetworkInfo network={network} />
          <FormProvider {...methods}>
            <Form onSubmit={handleSubmit(submitMethod)}>
              <Divider />
              {additionalNetwork}
              <Divider />
              <ProfilesSection network={network} rowSelection={SELECTION_TYPE.MULTIPLE} />
              <Divider />
              {parameters}
              <Divider />
              <Auxiliary />
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
                          to={{ pathname: resultsPath, state: { response, fromConfigPage: true } }}
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

export default T4144Config;
