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
import InputFileRow from 'app/modules/tools/T51/characterization-tool/input-row/input-file-row';

interface AlertProps {
  visible: boolean;
  color: string;
  message: string;
}

export const T51Characterization = () => {
  const dispatch = useAppDispatch();

  const [nRows, setNRows] = React.useState<number>(1);
  const [networkName, setNetworkName] = React.useState<string>('');
  const [assetsType, setAssetsType] = React.useState<string>('');
  const [finalJSON, setFinalJSON] = React.useState({});
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

  const callbackNRows = (value: number) => {
    if (value === -1) {
      jsonCallback(nRows - 1);
    }
    setNRows(prevState => prevState + value);
  };

  const jsonCallback = (index: number, value?: JsonToolConfig) => {
    const newJson = { ...finalJSON };
    if (value === undefined) {
      delete newJson[index];
    } else {
      newJson[index] = value;
    }
    setFinalJSON(newJson);
  };

  const clickSubmit = () => {
    const newJson = Object.values(finalJSON).filter(value => value !== '');
    /* eslint-disable-next-line no-console */
    console.log(newJson);
    setOpenModal(false);
    setIsToolRunning(true);

    const jsonConfig = JSON.stringify(newJson).replace(/"/g, '""');
    const launchUrl = 'api/tools/run/v1/t51';
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

  const viewModalSubmit = () => {
    setOpenModal(true);
  };

  const closeModal = () => {
    setOpenModal(false);
  };

  const clickRestore = () => {
    /* eslint-disable-next-line no-console */
    console.log('Restore!');
    setNRows(1);
    setAssetsType('');
    setFinalJSON({});
    clearStateRow();
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

  let clearStateRow = null;

  const assignClearRow = (clearRowFunc: () => void) => {
    clearStateRow = clearRowFunc;
  };

  return (
    <>
      <div>
        <h3>T5.1 Characterization Tool</h3>
      </div>
      <Row md="4">
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
      </Row>
      <Row md="4">
        <Col>
          <FormGroup>
            <Label for="assetsType">Assets Type</Label>
            <Input id="assetsTypeId" name="assetsType" placeholder="Type a name..." onChange={e => setAssetsType(e.target.value)} />
          </FormGroup>
        </Col>
      </Row>
      {[...Array(nRows).keys()].map((e, i) => (
        <div key={i} style={{ marginBottom: 20 }}>
          <InputFileRow
            networkName={networkName}
            assetsType={assetsType}
            index={i}
            nRows={nRows}
            callbackNRows={callbackNRows}
            jsonCallback={jsonCallback}
            passClearStateFunc={assignClearRow}
          />
        </div>
      ))}
      <Row md="4">
        <Col xs="4">
          <Button tag={Link} to="/tools/t51" replace color="info" data-cy="entityT51BackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>{' '}
          {!isToolRunning && (
            <Button
              id="jhi-confirm-t51"
              disabled={Object.keys(finalJSON).length === 0}
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
          <Button tag={Link} to={'/tools/t51/characterization/results'} color="success">
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
          <pre>
            {JSON.stringify(
              Object.values(finalJSON).filter(value => value !== ''),
              undefined,
              2
            )}
          </pre>
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

export default T51Characterization;
