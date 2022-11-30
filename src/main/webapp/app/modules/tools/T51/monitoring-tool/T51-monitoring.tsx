import React from 'react';
import {
  Alert,
  Badge,
  Button,
  Col,
  FormGroup,
  Input,
  Label,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Progress,
  Row,
  Spinner,
} from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { JsonToolConfig } from 'app/modules/tools/T51/model/json-tool-config';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/network/network.reducer';
import { defaultValue, JsonToolMonitoringConfig } from 'app/modules/tools/T51/model/json-tool-monitoring-config';
import InputModelRow from 'app/modules/tools/T51/monitoring-tool/input-row/input-model-row';
import InputFileRow from 'app/modules/tools/T51/monitoring-tool/input-row/input-file-row';

interface AlertProps {
  visible: boolean;
  color: string;
  message: string;
}

export const T51Monitoring = () => {
  const dispatch = useAppDispatch();

  const [networkName, setNetworkName] = React.useState<string>('');
  const [finalJSON, setFinalJSON] = React.useState<JsonToolMonitoringConfig>(defaultValue);
  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [isToolRunning, setIsToolRunning] = React.useState<boolean>(false);
  const [alert, setAlert] = React.useState<AlertProps>({
    visible: false,
    color: '',
    message: '',
  });

  const networkList = useAppSelector(state => state.network.entities);

  React.useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const clickSubmit = () => {
    /* eslint-disable-next-line no-console */
    console.log(finalJSON);
    setOpenModal(false);
    setIsToolRunning(true);

    const jsonConfig = JSON.stringify(finalJSON).replace(/"/g, '""');
    const launchUrl = 'api/tools/run/v1/t51-monitoring';
    const formData = new FormData();
    formData.append('networkName', networkName);
    formData.append('jsonConfig', jsonConfig);
    axios
      .post(launchUrl, formData)
      .then(res => {
        setIsToolRunning(false);
        setAlert({
          visible: true,
          color: 'success',
          message: 'Tool run successfully!',
        });
      })
      .catch(err => {
        setIsToolRunning(false);
        setAlert({
          visible: true,
          color: 'danger',
          message: 'Error running tool!',
        });
      });
  };

  const setModelFile = (filename: string) => {
    setFinalJSON({ ...finalJSON, modelpath1: '..\\..\\input_files\\' + filename });
  };

  const setModelErrorFile = (filename: string) => {
    setFinalJSON({ ...finalJSON, modelpath2: '..\\..\\input_files\\' + filename });
  };

  const setInputFileStuff = (filename: string, item1: string, item2: string, item3: string) => {
    setFinalJSON({ ...finalJSON, filepath2: '..\\..\\input_files\\' + filename, item1, item2, item3 });
  };

  const viewModalSubmit = () => {
    setOpenModal(true);
  };

  const closeModal = () => {
    setOpenModal(false);
  };

  const clickRestore = () => {
    /* eslint-disable-next-line no-console */
    console.log('Restore!');
    setFinalJSON(defaultValue);
    clearInputRow1();
    clearInputRow2();
    clearInputRow3();
  };

  React.useEffect(() => {
    if (!alert.visible) {
      return;
    }
    setTimeout(() => {
      setAlert({
        visible: false,
        color: '',
        message: '',
      });
    }, 2000);
  }, [alert]);

  const isFinalJsonInvalid = () => {
    return Object.values(finalJSON).some(value => value === '');
  };

  let clearInputRow1 = null;
  let clearInputRow2 = null;
  let clearInputRow3 = null;

  const assignClearInputRow1 = (childClearStateFunc: () => void) => {
    clearInputRow1 = childClearStateFunc;
  };

  const assignClearInputRow2 = (childClearStateFunc: () => void) => {
    clearInputRow2 = childClearStateFunc;
  };

  const assignClearInputRow3 = (childClearStateFunc: () => void) => {
    clearInputRow3 = childClearStateFunc;
  };

  return (
    <>
      <div>
        <h3>T5.1 Monitoring Tool</h3>
      </div>
      <Row md="6">
        <Col>
          <FormGroup>
            <Label for="network">Network</Label>
            <Input id="network" name="network" type="select" onChange={event => setNetworkName(event.target.value)}>
              {networkList.map((network, index) => (
                <option key={'network-opt-' + index}>{network.name}</option>
              ))}
            </Input>
          </FormGroup>
        </Col>
        <Col></Col>
      </Row>
      <InputModelRow
        networkName={networkName}
        name="modelFile"
        labelName="Model"
        accept=".h5"
        callback={setModelFile}
        passClearStateFunc={assignClearInputRow1}
      />
      <InputModelRow
        networkName={networkName}
        name="modelErrorFile"
        labelName="Error Model"
        accept=".json,.csv,.xls,.xlsx"
        callback={setModelErrorFile}
        passClearStateFunc={assignClearInputRow2}
      />
      <InputFileRow
        networkName={networkName}
        name="inputFile"
        labelName="Input File"
        accept=".xls,.xlsx"
        callback={setInputFileStuff}
        passClearStateFunc={assignClearInputRow3}
      />
      <Row md="4">
        <Col xs="4">
          <Button tag={Link} to="/tools/t51" replace color="info" data-cy="entityT51BackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>{' '}
          {!isToolRunning && (
            <Button
              id="jhi-confirm-t51"
              disabled={isFinalJsonInvalid()}
              data-cy="entityConfirmT51SubmitButton"
              color="primary"
              onClick={viewModalSubmit}
            >
              <FontAwesomeIcon icon="file-upload" />
              &nbsp; Submit
            </Button>
          )}{' '}
          <Button id="jhi-restore-t51" data-cy="entityRestoreT51Button" color="danger" onClick={clickRestore}>
            <FontAwesomeIcon icon="trash" />
            &nbsp; Restore
          </Button>{' '}
        </Col>
      </Row>
      <Row style={{ marginTop: 20 }}>
        <Col>
          <Button tag={Link} to={'/tools/t51/monitoring/results'} color="success">
            Results
          </Button>
        </Col>
      </Row>
      <Row>
        <Col>
          {isToolRunning && (
            <Progress animated value={100}>
              Tool is running...
            </Progress>
          )}
          {alert.visible && (
            <Alert color={alert.color} fade>
              {alert.message}
            </Alert>
          )}
        </Col>
      </Row>
      <Modal isOpen={openModal} toggle={closeModal}>
        <ModalHeader toggle={closeModal}>Launch Tool T5.1</ModalHeader>
        <ModalBody>
          Are you sure you want to run T5.1 tool with this configuration?
          <pre>{JSON.stringify(finalJSON, undefined, 2)}</pre>
        </ModalBody>
        <ModalFooter>
          <Button color="primary" onClick={clickSubmit}>
            Run
          </Button>{' '}
          <Button onClick={closeModal}>Cancel</Button>
        </ModalFooter>
      </Modal>
    </>
  );
};

export default T51Monitoring;
