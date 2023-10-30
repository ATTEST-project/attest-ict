import React from 'react';
import { Button, Col, Input, Label, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { useFormContext } from 'react-hook-form';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getFileHeader } from 'app/modules/tools/WP5/T52/reducer/tool-file-header.reducer';
import { useAppDispatch } from 'app/config/store';
import Auxiliary from 'app/modules/tools/WP5/T51/characterization-tool/auxiliary/auxiliary';
import Divider from 'app/shared/components/divider/divider';

const InputRow = (props: any) => {
  const { index, nRows, callbackNRows } = props;

  const {
    register,
    formState: { errors },
    unregister,
    reset,
    setValue,
  } = useFormContext();

  const dispatch = useAppDispatch();
  const [inputFile, setInputFile] = React.useState<File>(null);
  const [uploadLoading, setUploadLoading] = React.useState<boolean>(false);
  const [variables, setVariables] = React.useState<string[]>(null);
  const results = React.useMemo(() => ['Asset Assessment', 'Number of Clusters assessment', 'Clustering Training Process'], []);

  const [defaultHeader, setDefaultHeader] = React.useState(null);
  const [checkboxVariables, setCheckboxVariables] = React.useState<string[]>(null);

  const filterAssetTypeAndFirstColumn = (element, index, array) => {
    return index > 0 && !element.includes('asset_type');
  };

  const isFirstColumn = (element, index, array) => {
    return index === 0;
  };

  React.useEffect(() => {
    return () => {
      unregister('config');
    };
  }, []);

  React.useEffect(() => {
    // eslint-disable-next-line no-console
    // console.log('T51  useEffect() enter - defaultHeader :  ', defaultHeader);
    if (!defaultHeader) {
      return;
    }
    setValue(`config[${index}].selectedVariables`, defaultHeader);
  }, [defaultHeader]);

  const onFileChange = event => {
    setInputFile(event.target.files[0]);
    setVariables(null);
    setCheckboxVariables(null);
    setDefaultHeader(null);
  };

  const onUploadButtonClick = () => {
    setUploadLoading(true);
    dispatch(getFileHeader(inputFile))
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

  return (
    <div className="section-with-border">
      <Row>
        <Col md="3">
          <ValidatedField
            register={register}
            error={errors?.config?.[index]?.assestsType}
            name={`config[${index}].assestsType`}
            label="Assets Type"
            type="text"
            placeholder="Assets type..."
          />
        </Col>
      </Row>
      <Row>
        <Col md="3">
          <ValidatedField
            register={register}
            error={errors?.config?.[index]?.inputFile}
            name={`config[${index}].inputFile`}
            label="Input File"
            type="file"
            onChange={onFileChange}
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
                  error={errors?.config?.[index]?.selectedVariables}
                  name={`config[${index}].selectedVariables`}
                  label="Header"
                  type="select"
                  value={defaultHeader}
                  validate={{ required: true }}
                >
                  <option key="opt-0" value="" hidden>
                    Header...
                  </option>
                  {variables?.map((variable, index) => (
                    <option key={'opt-' + index}>{variable}</option>
                  ))}
                </ValidatedField>
              </Col>
              <Col>
                <Label htmlFor="variables">Variables</Label>
                <div id="variables" className="variables-container">
                  {checkboxVariables?.map((variable, i) => (
                    <ValidatedField
                      key={'variable-' + i}
                      className="input-row-checkbox"
                      register={register}
                      errors={errors?.config?.[index]?.checkboxVariables?.[variable]}
                      name={`config[${index}].variables[${variable}]`}
                      label={variable}
                      type="checkbox"
                    />
                  ))}
                </div>
              </Col>
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.config?.[index]?.config}
                  name={`config[${index}].config`}
                  label="Dimension"
                  type="select"
                  validate={{ required: true }}
                >
                  <option value="" hidden>
                    Dimension...
                  </option>
                  <option value="Life Assessment">Life Assessment</option>
                  <option value="Maintenance Strategy">Maintenance Strategy</option>
                  <option value="Economic Impact">Economic Impact</option>
                </ValidatedField>
              </Col>
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.config?.[index]?.method}
                  name={`config[${index}].method`}
                  label="Method"
                  type="select"
                  validate={{ required: true }}
                  value="Auto"
                >
                  <option value="" hidden>
                    Method...
                  </option>
                  <option value="Auto">Auto</option>
                  <option value="Elbow Method">Elbow Method</option>
                  <option value="Gap Statistics">Gap Statistics</option>
                </ValidatedField>
              </Col>
              <Col>
                <Label htmlFor="results">Results</Label>
                <div id="results" className="variables-container">
                  {results.map((result, i) => (
                    <ValidatedField
                      key={'results-' + i}
                      className="input-row-checkbox"
                      register={register}
                      errors={errors?.config?.[index]?.results?.[result]}
                      name={`config[${index}].results[${result}]`}
                      label={result}
                      type="checkbox"
                      checked
                    />
                  ))}
                </div>
              </Col>
            </Row>
            <Divider />
          </>
        )}
        <Auxiliary index={index} />
        <Divider />
        <Row>
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
      </Row>
    </div>
  );
};

export default InputRow;
