import React from 'react';
import './input-row.scss';
import { useFormContext } from 'react-hook-form';
import { Button, Col, Input, Label, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch } from 'app/config/store';
import { getFileHeader } from 'app/modules/tools/WP5/T52/reducer/tool-file-header.reducer';
import { isStringEmptyOrNullOrUndefined } from 'app/shared/util/string-utils';
import { showError } from 'app/modules/tools/custom-toast-error';

const InputRow = (props: any) => {
  const { index, nRows, callbackNRows } = props;

  const dispatch = useAppDispatch();

  const {
    register,
    formState: { errors },
    reset,
    resetField,
    unregister,
    setValue,
  } = useFormContext();

  const [assetsFile, setAssetsFile] = React.useState<File>(null);
  const [uploadLoading, setUploadLoading] = React.useState<boolean>(false);
  const [variables, setVariables] = React.useState<string[]>(null);

  const [defaultHeader, setDefaultHeader] = React.useState('');
  const [checkboxVariables, setCheckboxVariables] = React.useState<string[]>(null);

  const filterAssetTypeAndFirstColumn = (element, index, array) => {
    return index > 0 && !element.includes('asset_type');
  };

  const isFirstColumn = (element, index, array) => {
    return index === 0;
  };

  React.useEffect(() => {
    return () => {
      unregister('mainConfig');
    };
  }, []);

  React.useEffect(() => {
    // eslint-disable-next-line no-console
    // console.log('T52  useEffect() enter - defaultHeader :  ', defaultHeader);
    if (isStringEmptyOrNullOrUndefined(defaultHeader)) {
      return;
    }

    setValue(`mainConfig[${index}].index`, defaultHeader);
  }, [defaultHeader]);

  const onUploadButtonClick = () => {
    if (assetsFile == null) {
      showError('Please select a file to upload');
      return;
    }
    setUploadLoading(true);
    dispatch(getFileHeader(assetsFile))
      .unwrap()
      .then(res => {
        setCheckboxVariables(res.data.filter(filterAssetTypeAndFirstColumn));
        setVariables(res.data);
        setDefaultHeader(res.data[0]);
        setUploadLoading(false);
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.error(err);
        setUploadLoading(false);
      });
  };

  const onFileChanged = event => {
    setAssetsFile(event.target.files[0]);
    setVariables(null);
    resetField(`mainConfig[${index}].index`);
    resetField(`mainConfig[${index}].variables`);
    resetField(`mainConfig[${index}].config`);
    resetField(`mainConfig[${index}].weights`);
  };

  return (
    <>
      <Row>
        <Col md="3">
          <ValidatedField
            register={register}
            error={errors?.mainConfig?.[index]?.assetsFile}
            name={`mainConfig[${index}].assetsFile`}
            label="Assets File"
            type="file"
            accept=".csv,.xlsx,.xls"
            onChange={onFileChanged}
            validate={{ required: true }}
          />
        </Col>
        {!variables ? (
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
            <Row md="6">
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.mainConfig?.[index]?.index}
                  name={`mainConfig[${index}].index`}
                  label="Header"
                  type="select"
                  value={defaultHeader}
                  validate={{ required: true }}
                >
                  <option key="select-index-0" value="" hidden>
                    Header...
                  </option>
                  {variables.map((variable, index) => (
                    <option key={'select-index-' + index}>{variable}</option>
                  ))}
                </ValidatedField>
              </Col>
              <Col>
                <Label htmlFor="variables">Variables</Label>
                <div id="variables" className="variables-container">
                  {checkboxVariables.map((variable, i) => (
                    <ValidatedField
                      key={'variable-' + i}
                      className="input-row-checkbox"
                      register={register}
                      errors={errors?.mainConfig?.[index]?.variables?.[variable]}
                      name={`mainConfig[${index}].variables[${variable}]`}
                      label={variable}
                      type="checkbox"
                    />
                  ))}
                </div>
              </Col>
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.mainConfig?.[index]?.config}
                  name={`mainConfig[${index}].config`}
                  label="Dimension"
                  type="select"
                  validate={{ required: true }}
                >
                  <option value="" hidden>
                    Dimension...
                  </option>
                  <option value="Life Assessment">Life Assessment</option>
                  <option value="Maintenance Stratgy">Maintenance Strategy</option>
                  <option value="Economic Impact">Economic Impact</option>
                </ValidatedField>
              </Col>
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.mainConfig?.[index]?.weights}
                  name={`mainConfig[${index}].weights`}
                  label="Weights"
                  type="text"
                  validate={{ required: true }}
                />
              </Col>
            </Row>
          </>
        )}
        <Col style={{ alignSelf: 'center', textAlign: 'right' }}>
          <div className="mb-3">
            <Button disabled={nRows > 3} onClick={() => callbackNRows(1)} color="secondary" style={{ marginRight: 10 }}>
              +
            </Button>
            <Button disabled={nRows < 2} onClick={() => callbackNRows(-1)} color="secondary">
              -
            </Button>
          </div>
        </Col>
      </Row>
    </>
  );
};

export default InputRow;
