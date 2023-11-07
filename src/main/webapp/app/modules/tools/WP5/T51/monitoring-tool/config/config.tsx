import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Button, Col, Label, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { getFileHeader } from 'app/modules/tools/WP5/T52/reducer/tool-file-header.reducer';
import { useAppDispatch } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import SectionHeader from 'app/shared/components/section-header/section-header';
import { showError } from 'app/modules/tools/custom-toast-error';

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

  const filterEmptyAndFirstColumn = (element, index, array) => {
    return index > 0 && element.length > 0;
  };

  const onFileChange = event => {
    setInputFile(event.target.files[0]);
    setVariables(null);
  };

  const onUploadButtonClick = () => {
    if (inputFile == null) {
      showError('Please select a file to upload');
      return;
    }
    setUploadLoading(true);
    dispatch(getFileHeader(inputFile))
      .unwrap()
      .then(res => {
        setVariables(res.data.filter(filterEmptyAndFirstColumn));
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
      <SectionHeader title="Upload Auxiliary Data" />

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
          <h5>Variables Selection</h5>
          <Row>
            <Col>
              <Label htmlFor="current">Current (A)</Label>
              <div id="current" className="variables-container">
                {variables?.map((variable, i) => (
                  <ValidatedField
                    key={'current-' + i}
                    register={register}
                    className="input-row-checkbox"
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
              <Label htmlFor="wind-temp">Winding Temperature (°C)</Label>
              <div id="wind-temp" className="variables-container">
                {variables?.map((variable, i) => (
                  <ValidatedField
                    key={'wind-temp-' + i}
                    className="input-row-checkbox"
                    register={register}
                    errors={errors?.config?.item3}
                    name={`config.item3`}
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
                    key={'oil-temp-' + i}
                    className="input-row-checkbox"
                    register={register}
                    errors={errors?.config?.item2}
                    name={`config.item2`}
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
