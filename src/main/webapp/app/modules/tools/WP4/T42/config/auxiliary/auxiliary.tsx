import React from 'react';
import './auxiliary.scss';
import { Button, Col, FormGroup, Input, Label, Modal, ModalBody, ModalHeader, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ValidatedField } from 'react-jhipster';
import { useFieldArray, useFormContext } from 'react-hook-form';
import SpreadSheet from 'app/shared/components/spreadsheet/spreadsheet';
import { tractabilitySampleData } from 'app/shared/components/spreadsheet/sample-data/tractability-sample-data';

interface IT42AuxiliaryFileProp {
  enableUploadOtherToolResults: boolean;
}

const Auxiliary = (props: IT42AuxiliaryFileProp) => {
  const { enableUploadOtherToolResults } = props;

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

  // PV_production_profile_file (PV_production_diagram.xlsx)
  const [pvProductionProfileFile, setPvProductionProfileFile] = React.useState<File>(null);

  // state_estimation_csv_file (ES_Dx_03_2030_W_Bd_t1.csv)
  const [stateEstimationCsvFile, setStateEstimationCsvFile] = React.useState<File>(null);

  // flex_devices_tech_char_file ES_Dx_03_2030W.xlsx
  const [flexDevicesTechCharFile, setFlexDevicesTechCharFile] = React.useState<File>(null);

  // flexibity_devices_states_file  eg. ES_Dx_03_2030_W_Bd_WithoutFlex_output.xlsx -- FROM T41
  const [flexibityDevicesStatesFile, setFlexibityDevicesStatesFile] = React.useState<File>(null);

  //  trans_activation_file  eg. trans_decisions output FROM T45
  const [transActivationFile, setTransActivationFile] = React.useState<File>(null);

  const modalOpenedEvent = () => {
    setWindowOffset(window.scrollY);
    document.body.classList.add('modal-open-custom');
  };

  const modalClosedEvent = () => {
    document.body.classList.remove('modal-open-custom');
    window.scrollTo(0, windowOffset);
  };

  const getFileToViewEdit = type => {
    switch (type) {
      case 'PV_production_profile_file':
        return pvProductionProfileFile;
        break;
      case 'state_estimation_csv_file':
        return stateEstimationCsvFile;
        break;
      case 'flex_devices_tech_char_file':
        return flexDevicesTechCharFile;
        break;
      case 'flexibity_devices_states_file':
        return flexibityDevicesStatesFile;
        break;
      case 'trans_activation_file':
        return transActivationFile;
        break;
      default:
        return null;
        break;
    }
  };

  return (
    <div className="section-with-border">
      <span>{'Upload auxiliary data'}</span>
      <div style={{ marginTop: 10, marginBottom: 10 }} />
      <Row>
        <Col md="4">
          <ValidatedField
            register={register}
            error={errors?.auxiliary?.PV_production_profile_file}
            id={'PV_production_profile_file'}
            label="PV production profile [e.g PV_production_diagram.xlsx]"
            name="auxiliary[PV_production_profile_file]"
            data-cy="PV_production_profile_file"
            type="file"
            accept=".xlsx"
            validate={{ required: true }}
            onChange={event => setPvProductionProfileFile(event.target.files[0])}
          />
        </Col>
        <Col md="2" style={{ alignSelf: 'end' }}>
          <div className="mb-3">
            <Button
              id="open-grid-button"
              color="primary"
              style={{ width: '50%' }}
              type="button"
              onClick={() => toggleModal('PV_production_profile_file')}
            >
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
            error={errors?.auxiliary?.flex_devices_tech_char_file}
            id={'flex_devices_tech_char_file'}
            label="Flex Devices Tech Char File [e.g. ES_Dx_03_2030W.xlsx]"
            name="auxiliary[flex_devices_tech_char_file]"
            data-cy="flex_devices_tech_char_file"
            type="file"
            accept=".xlsx"
            validate={{ required: true }}
            onChange={event => setFlexDevicesTechCharFile(event.target.files[0])}
          />
        </Col>
        <Col md="2" style={{ alignSelf: 'end' }}>
          <div className="mb-3">
            <Button
              id="open-grid-button"
              color="primary"
              style={{ width: '50%' }}
              type="button"
              onClick={() => toggleModal('flex_devices_tech_char_file')}
            >
              <FontAwesomeIcon icon="file-excel" />
              {' View/Edit'}
            </Button>
          </div>
        </Col>
      </Row>

      <hr />
      {enableUploadOtherToolResults ? (
        <>
          <Row>
            <Col md="4">
              <ValidatedField
                register={register}
                error={errors?.auxiliary?.flexibity_devices_states_file}
                id={'flexibity_devices_states_file'}
                label="Flexibility Devices states file from T41 [e.g. ES_Dx_03_2030_W_Bd_WithoutFlex_output.xlsx]"
                name="auxiliary[flexibity_devices_states_file]"
                data-cy="flexibity_devices_states_file"
                type="file"
                accept=".xlsx"
                validate={{ required: true }}
                onChange={event => setFlexibityDevicesStatesFile(event.target.files[0])}
              />
            </Col>
            <Col md="2" style={{ alignSelf: 'end' }}>
              <div className="mb-3">
                <Button
                  id="open-grid-button"
                  color="primary"
                  style={{ width: '50%' }}
                  type="button"
                  onClick={() => toggleModal('flexibity_devices_states_file')}
                >
                  <FontAwesomeIcon icon="file-excel" />
                  {' View/Edit'}{' '}
                </Button>
              </div>
            </Col>
          </Row>
        </>
      ) : (
        <>{" T41 OutputFiles' results selected before! "}</>
      )}
      <hr />

      <Row>
        <Col md="4">
          <ValidatedField
            register={register}
            error={errors?.auxiliary?.trans_activation_file}
            id={'trans_activation_file'}
            label="Transmission activation from T4.5 [e.g. trans_decisions.xlsx]"
            name="auxiliary[trans_activation_file]"
            data-cy="trans_activation_file"
            type="file"
            accept=".xlsx"
            validate={{ required: true }}
            onChange={event => setTransActivationFile(event.target.files[0])}
          />
        </Col>

        <Col md="2" style={{ alignSelf: 'end' }}>
          <div className="mb-3">
            {/* <Label htmlFor="open-grid-button">{'Or fill the necessary fields from a spreadsheet'}</Label> */}
            <Button
              id="open-grid-button"
              color="primary"
              style={{ width: '50%' }}
              type="button"
              onClick={() => toggleModal('trans_activation_file')}
            >
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
            error={errors?.auxiliary?.state_estimation_csv_file}
            id={'state_estimation_csv_file'}
            label="State Estimation Csv File [e.g. ES_Dx_03_2030_W_Bd_t1.csv]"
            name="auxiliary[state_estimation_csv_file]"
            data-cy="state_estimation_csv_file"
            type="file"
            accept=".csv"
            validate={{ required: true }}
            onChange={event => setStateEstimationCsvFile(event.target.files[0])}
          />
        </Col>
        <Col md="2" style={{ alignSelf: 'end' }}>
          <div className="mb-3">
            <Button
              id="open-grid-button"
              color="primary"
              style={{ width: '50%' }}
              type="button"
              onClick={() => toggleModal('state_estimation_csv_file')}
            >
              <FontAwesomeIcon icon="file-excel" /> {' View/Edit'}{' '}
            </Button>
          </div>
        </Col>
      </Row>

      <Modal isOpen={openModal} toggle={toggleModal} fullscreen onOpened={modalOpenedEvent} onClosed={modalClosedEvent}>
        <ModalHeader toggle={toggleModal}>SpreadSheet</ModalHeader>
        <ModalBody>
          <SpreadSheet gridData={tractabilitySampleData} file={getFileToViewEdit(fileType)} />
        </ModalBody>
      </Modal>
    </div>
  );
};

export default Auxiliary;
