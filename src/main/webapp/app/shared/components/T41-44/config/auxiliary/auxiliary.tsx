import React from 'react';
import './auxiliary.scss';
import { Button, Col, FormGroup, Input, Label, Modal, ModalBody, ModalHeader, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ValidatedField } from 'react-jhipster';
import { useFieldArray, useFormContext } from 'react-hook-form';
import SpreadSheet from 'app/shared/components/spreadsheet/spreadsheet';
import { tractabilitySampleData } from 'app/shared/components/spreadsheet/sample-data/tractability-sample-data';

const Auxiliary = props => {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [fileType, setFileType] = React.useState<string>('');
  const toggleModal = type => {
    setFileType(type);
    setOpenModal(!openModal);
  };

  const [windowOffset, setWindowOffset] = React.useState(0);

  const [flexFile, setFlexFile] = React.useState<File>(null);
  const [scenarioGenFile, setScenarioGenFile] = React.useState<File>(null);

  const modalOpenedEvent = () => {
    setWindowOffset(window.scrollY);
    document.body.classList.add('modal-open-custom');
  };

  const modalClosedEvent = () => {
    document.body.classList.remove('modal-open-custom');
    window.scrollTo(0, windowOffset);
  };

  return (
    <div className="section-with-border">
      <span>{'Upload auxiliary data'}</span>
      <div style={{ marginTop: 10, marginBottom: 10 }} />
      <Row>
        <Col md="4">
          <ValidatedField
            register={register}
            error={errors?.auxiliary?.flex}
            id={'flex-data'}
            label="Additional Flexibility Data"
            name="auxiliary[flex]"
            data-cy="flexData"
            type="file"
            validate={{ required: true }}
            onChange={event => setFlexFile(event.target.files[0])}
          />
        </Col>
        <Col md="2" style={{ alignSelf: 'end' }}>
          <div className="mb-3">
            {/* <Label htmlFor="open-grid-button">{'Or fill the necessary fields from a spreadsheet'}</Label> */}
            <Button id="open-grid-button" color="primary" style={{ width: '50%' }} type="button" onClick={() => toggleModal('flex')}>
              <FontAwesomeIcon icon="file-excel" />
              {' View/Edit'}
            </Button>
          </div>
        </Col>
      </Row>
      <hr />
      <Row>
        <Col md="4">
          <ValidatedField
            register={register}
            error={errors?.auxiliary?.scenario}
            id={'scenario-gen-data'}
            label="Scenario Generation Data"
            name="auxiliary[scenario]"
            data-cy="scenarioGenData"
            type="file"
            validate={{ required: true }}
            onChange={event => setScenarioGenFile(event.target.files[0])}
          />
        </Col>
        <Col md="2" style={{ alignSelf: 'end' }}>
          <div className="mb-3">
            {/* <Label htmlFor="open-grid-button">{'Or fill the necessary fields from a spreadsheet'}</Label> */}
            <Button id="open-grid-button-sg" color="primary" style={{ width: '50%' }} type="button" onClick={() => toggleModal('sg')}>
              <FontAwesomeIcon icon="file-excel" />
              {' View/Edit'}
            </Button>
          </div>
        </Col>
      </Row>
      <Modal isOpen={openModal} toggle={toggleModal} fullscreen onOpened={modalOpenedEvent} onClosed={modalClosedEvent}>
        <ModalHeader toggle={toggleModal}>SpreadSheet</ModalHeader>
        <ModalBody>
          <SpreadSheet gridData={tractabilitySampleData} file={fileType === 'flex' ? flexFile : scenarioGenFile} />
        </ModalBody>
      </Modal>
    </div>
  );
};

export default Auxiliary;
