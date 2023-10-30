import React from 'react';
import './auxiliary.scss';
import { Button, Col, FormGroup, Input, Label, Modal, ModalBody, ModalHeader, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ValidatedField } from 'react-jhipster';
import { useFieldArray, useFormContext } from 'react-hook-form';
import SpreadSheet from 'app/shared/components/spreadsheet/spreadsheet';
import { tractabilitySampleData } from 'app/shared/components/spreadsheet/sample-data/tractability-sample-data';
import { PrintResult } from 'app/shared/components/tool-results-search/print-results-selected';
import { IToolResult } from 'app/shared/model/tool-results.model';
import SectionHeader from 'app/shared/components/section-header/section-header';

interface IT45AuxiliaryFileProp {
  enableUploadT44Results: boolean;
  resultsSelected?: IToolResult[];
}

const Auxiliary = (props: IT45AuxiliaryFileProp) => {
  const { enableUploadT44Results, resultsSelected } = props;
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

  // state_estimation_csv_file (state_estimation.csv)
  const [stateEstimationCsvFile, setStateEstimationCsvFile] = React.useState<File>(null);

  // flex_devices_tech_char_file PT_Tx_2040.xlsx
  const [flexDevicesTechCharFile, setFlexDevicesTechCharFile] = React.useState<File>(null);

  // flexibity_devices_states_file  from T44 procured_flexibility_2040_SU_wf.xlsx -- FROM T44
  const [flexibityDevicesStatesFile, setFlexibityDevicesStatesFile] = React.useState<File>(null);

  // DA_curtailment_file: 2040_SU_wf_Normal.xlsx -- FROM T44
  const [daCurtailmentFile, setDaCurtailmentFile] = React.useState<File>(null);

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
      case 'DA_curtailment_file':
        return daCurtailmentFile;
        break;
      default:
        return null;
        break;
    }
  };

  return (
    <div className="section-with-border">
      <SectionHeader title="Upload Auxiliary Data" />

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
            label="Flex Devices Tech Char File [e.g. HR_Tx_01_2030.xlsx]"
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
      {enableUploadT44Results ? (
        <>
          <Row>
            <Col md="4">
              <ValidatedField
                register={register}
                error={errors?.auxiliary?.flexibity_devices_states_file}
                id={'flexibity_devices_states_file'}
                label="Flexibility Devices states file from T44 [e.g. procured_flexibility_HR_2030_wf_Su.xlsx]"
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
          <hr />

          <Row>
            <Col md="4">
              <ValidatedField
                register={register}
                error={errors?.auxiliary?.DA_curtailment_file}
                id={'da_curtailment_file'}
                label="DA curtailment file from T44 [e.g. HR_2030_wf_Normal_Su.xlsx]"
                name="auxiliary[DA_curtailment_file]"
                data-cy="da_curtailment_file"
                type="file"
                accept=".xlsx"
                validate={{ required: true }}
                onChange={event => setDaCurtailmentFile(event.target.files[0])}
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
                  onClick={() => toggleModal('da_curtailment_file')}
                >
                  <FontAwesomeIcon icon="file-excel" />
                  {' View/Edit'}
                </Button>
              </div>
            </Col>
          </Row>
        </>
      ) : (
        <>
          {' '}
          <PrintResult toolResults={resultsSelected} title={'Results from T44: '} />
        </>
      )}

      <hr />
      <Row>
        <Col md="4">
          <ValidatedField
            register={register}
            error={errors?.auxiliary?.state_estimation_csv_file}
            id={'state_estimation_csv_file'}
            label="State Estimation Csv File"
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
