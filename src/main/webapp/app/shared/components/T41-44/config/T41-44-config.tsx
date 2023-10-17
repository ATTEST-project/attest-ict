import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FormProvider, useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import { Button, Form, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { runTool, reset as retry } from 'app/modules/tools/reducer/tools-execution.reducer';
import { downloadResults } from 'app/modules/tools/WP4/reducer/tools-results.reducer';

import Divider from 'app/shared/components/divider/divider';
import NetworkInfo from 'app/shared/components/T41-44/config/network-info/network-info';
import ProfilesSection from 'app/shared/components/T41-44/config/profiles/profiles';
import Auxiliary from 'app/shared/components/T41-44/config/auxiliary/auxiliary';
import { Link } from 'react-router-dom';
import LoadingOverlay from 'app/shared/components/loading-overlay/loading-overlay';
import { TOOLS_INFO } from 'app/modules/tools/info/tools-names';
import { WP_IMAGE } from 'app/modules/tools/info/tools-info';
import { T41DefaultParameters } from 'app/modules/tools/WP4/T41/tractability-tool/parameters/default-parameters';
import { T44DefaultParameters } from 'app/modules/tools/WP4/T44/parameters/default-parameters';

import { pathButton } from 'app/shared/reducers/back-button-path';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import ModalConfirmToolExecution from 'app/shared/components/tool-confirm-execution/modal-tool-confirm-execution';
import ToolTitle from 'app/shared/components/tool-title/tool-title';

const T4144Config = (props: any) => {
  const { title, toolName, resultsPath, additionalNetwork, parameters } = props;

  const divRef = React.useRef<HTMLDivElement>();

  const dispatch = useAppDispatch();

  const methods =
    toolName === TOOLS_INFO.T41_TRACTABILITY.name
      ? useForm({ defaultValues: { parameters: { ...T41DefaultParameters } } })
      : useForm({ defaultValues: { parameters: { ...T44DefaultParameters } } });

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
  const [showBtnGoToTask, setBtnGoToTask] = React.useState<boolean>(false);

  const loading = useAppSelector(state => state.toolsExecution.loading);
  const response = useAppSelector(state => state.toolsExecution.entity);
  const completed = useAppSelector(state => state.toolsExecution.updateSuccess);

  /* eslint-disable-next-line no-console */
  // console.log('loading: ', loading);

  /* eslint-disable-next-line no-console */
  console.log('entity: ', response);

  /* eslint-disable-next-line no-console */
  // console.log('completed: ', completed);

  // debugger;
  // eslint-disable-line no-debugger

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
            toast.success('Tool is running!');
            setBtnGoToTask(true);
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

  return (
    <div ref={divRef}>
      {loading && <LoadingOverlay ref={divRef} />}

      <ToolTitle imageAlt={WP_IMAGE.WP4.alt} title={title} imageSrc={WP_IMAGE.WP4.src} />
      <Divider />

      {network && (
        <>
          <NetworkInfo network={network} />
          <FormProvider {...methods}>
            <Form onSubmit={handleSubmit(submitMethod)}>
              <Divider />
              {additionalNetwork}
              <Divider />
              {/*
              <ProfilesSection network={network} rowSelection={SELECTION_TYPE.MULTIPLE} />
              */}
              <Divider />
              {parameters}
              <Divider />
              <Auxiliary />
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
          toolDescription={title}
          form={form}
          openModal={openModal}
          checkAndRun={checkAndRun}
          setOpenModal={setOpenModal}
        />
      )}
    </div>
  );
};

export default T4144Config;
