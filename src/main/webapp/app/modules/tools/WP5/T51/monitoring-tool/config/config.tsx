import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Button, Col, Label, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { getFileHeader } from 'app/modules/tools/WP5/T52/reducer/tool-file-header.reducer';
import { useAppDispatch } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const Config = (props: any) => {
  const {
    register,
    formState: { errors },
    reset,
  } = useFormContext();

  const dispatch = useAppDispatch();

  const [inputFile, setInputFile] = React.useState<File>(null);
  const [variables, setVariables] = React.useState<string[]>(null);
  const [uploadLoading, setUploadLoading] = React.useState<boolean>(false);

  const onFileChange = event => {
    setInputFile(event.target.files[0]);
    setVariables(null);
  };

  const onUploadButtonClick = () => {
    setUploadLoading(true);
    dispatch(getFileHeader(inputFile))
      .unwrap()
      .then(res => {
        setVariables(res.data);
        setUploadLoading(false);
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.error(err);
        setUploadLoading(false);
      });
  };

  return (
    <div className="section-with-border">
      <Row>
        <Col md="4">
          <ValidatedField
            register={register}
            error={errors?.config?.modelpath1}
            name="config.modelpath1"
            label="Model"
            type="file"
            accept=".h5"
            validate={{ required: true }}
          />
        </Col>
      </Row>
      <Row>
        <Col md="4">
          <ValidatedField
            register={register}
            error={errors?.config?.modelpath2}
            name="config.modelpath2"
            label="Error Model"
            type="file"
            accept=".json,.csv"
            validate={{ required: true }}
          />
        </Col>
      </Row>
      <Row>
        <Col md="4">
          <ValidatedField
            register={register}
            error={errors?.config?.filepath2}
            name="config.filepath2"
            label="Input File"
            type="file"
            accept=".xlsx,.xls"
            onChange={onFileChange}
            validate={{ required: true }}
          />
        </Col>
        {!variables && (
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
        )}
      </Row>
      {variables && (
        <>
          <h6>Variables Selection</h6>
          <Row>
            <Col>
              <Label htmlFor="current">Current (A)</Label>
              <div id="current" className="variables-container">
                {variables?.map((variable, i) => (
                  <ValidatedField
                    key={'variable-' + i}
                    className="input-row-checkbox"
                    register={register}
                    errors={errors?.config?.item1}
                    name={`config.item1`}
                    label={variable}
                    type="radio"
                    value={variable}
                  />
                ))}
              </div>
            </Col>
            <Col>
              <Label htmlFor="oil-temp">Oil Temperature (°C)</Label>
              <div id="oil-temp" className="variables-container">
                {variables?.map((variable, i) => (
                  <ValidatedField
                    key={'variable-' + i}
                    className="input-row-checkbox"
                    register={register}
                    errors={errors?.config?.item1}
                    name={`config.item2`}
                    label={variable}
                    type="radio"
                    value={variable}
                  />
                ))}
              </div>
            </Col>
            <Col>
              <Label htmlFor="wind-temp">Wind Temperature (°C)</Label>
              <div id="wind-temp" className="variables-container">
                {variables?.map((variable, i) => (
                  <ValidatedField
                    key={'variable-' + i}
                    className="input-row-checkbox"
                    register={register}
                    errors={errors?.config?.item1}
                    name={`config.item3`}
                    label={variable}
                    type="radio"
                    value={variable}
                  />
                ))}
              </div>
            </Col>
          </Row>
        </>
      )}
    </div>
  );
};

export default Config;
