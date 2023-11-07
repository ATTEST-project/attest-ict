import React from 'react';
import { Button, Col, Collapse, Row, Spinner } from 'reactstrap';
import { showError } from 'app/modules/tools/custom-toast-error';
import { useFormContext } from 'react-hook-form';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch } from 'app/config/store';
import { getFileHeader } from 'app/modules/tools/WP5/T52/reducer/tool-file-header.reducer';

const Coordinates = () => {
  const dispatch = useAppDispatch();

  const {
    register,
    formState: { errors },
    reset,
    resetField,
  } = useFormContext();

  const [coordsFile, setCoordsFile] = React.useState<File>(null);
  const [uploadLoading, setUploadLoading] = React.useState<boolean>(false);
  const [fields, setFields] = React.useState<string[]>(null);

  const [showCoordsSection, setShowCoordsSection] = React.useState<boolean>(false);

  const onUploadButtonClick = () => {
    if (coordsFile == null) {
      showError('Please select a coordinates file to upload');
      return;
    }
    setUploadLoading(true);
    dispatch(getFileHeader(coordsFile))
      .unwrap()
      .then(res => {
        setFields(res.data);
        setUploadLoading(false);
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.error(err);
        setUploadLoading(false);
      });
  };

  const onFileChanged = event => {
    setCoordsFile(event.target.files[0]);
    setFields(null);
    resetField('coordinatesConfig.regionCB');
    resetField('coordinatesConfig.coordinatesIdentifier');
    resetField('coordinatesConfig.latColumn');
    resetField('coordinatesConfig.longColumn');
  };

  return (
    <div className="section-with-border">
      <div
        style={{ display: 'flex', justifyContent: 'space-between', cursor: 'pointer' }}
        onClick={() => setShowCoordsSection(!showCoordsSection)}
      >
        <h6>{'Coordinates (optional)'}</h6>
        <div style={{ marginRight: 10 }}>
          <FontAwesomeIcon icon="angle-down" style={showCoordsSection && { transform: 'rotate(180deg)' }} />
        </div>
      </div>
      <Collapse isOpen={showCoordsSection}>
        <Row>
          <Col md="4">
            <ValidatedField
              register={register}
              error={errors?.coordinatesConfig?.coordsFile}
              name="coordinatesConfig.coordsFile"
              label="Coordinates File"
              type="file"
              accept=".csv,.xlsx,.xls"
              onChange={onFileChanged}
            />
          </Col>
          {!fields ? (
            <Col style={{ alignSelf: 'end' }}>
              <div className="mb-3">
                <Button color="primary" onClick={onUploadButtonClick}>
                  {uploadLoading ? (
                    <>
                      <Spinner color="light" size="sm" />
                    </>
                  ) : (
                    <>
                      <FontAwesomeIcon icon="file-upload" />
                      {' Upload '}
                    </>
                  )}
                </Button>
              </div>
            </Col>
          ) : (
            <>
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.coordinatesConfig?.regionCB}
                  name="coordinatesConfig.regionCB"
                  label="Region"
                  type="select"
                  validate={{ required: true }}
                >
                  <option value="" hidden>
                    Region...
                  </option>
                  <option value="asia">Asia</option>
                  <option value="europe">Europe</option>
                  <option value="africa">Africa</option>
                  <option value="usa">USA</option>
                </ValidatedField>
              </Col>
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.coordinatesConfig?.coordinatesIdentifier}
                  name="coordinatesConfig.coordinatesIdentifier"
                  label="Coordinates Identifier"
                  type="select"
                  validate={{ required: true }}
                >
                  <option key="coords-index" value="" hidden>
                    Identifier...
                  </option>
                  {fields.map((field, index) => (
                    <option key={'coords-index-' + index}>{field}</option>
                  ))}
                </ValidatedField>
              </Col>
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.coordinatesConfig?.latColumn}
                  name="coordinatesConfig.latColumn"
                  label="Latitude Column"
                  type="select"
                  validate={{ required: true }}
                >
                  <option key="lat-col-index" value="" hidden>
                    Lat column...
                  </option>
                  {fields.map((field, index) => (
                    <option key={'lat-col-index-' + index}>{field}</option>
                  ))}
                </ValidatedField>
              </Col>
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.coordinatesConfig?.longColumn}
                  name="coordinatesConfig.longColumn"
                  label="Longitude Column"
                  type="select"
                  validate={{ required: true }}
                >
                  <option key="long-col-index" value="" hidden>
                    Long column...
                  </option>
                  {fields.map((field, index) => (
                    <option key={'long-col-index-' + index}>{field}</option>
                  ))}
                </ValidatedField>
              </Col>
            </>
          )}
        </Row>
      </Collapse>
    </div>
  );
};

export default Coordinates;
