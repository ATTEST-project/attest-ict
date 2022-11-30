import React from 'react';
import './input-row.scss';
import { useFormContext } from 'react-hook-form';
import { Button, Col, Input, Label, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch } from 'app/config/store';
import { getFileHeader } from 'app/modules/tools/WP5/T52/reducer/tool-file-header.reducer';

const InputRow = (props: any) => {
  const { index, nRows, callbackNRows } = props;

  const dispatch = useAppDispatch();

  const {
    register,
    formState: { errors },
    reset,
    resetField,
    unregister,
  } = useFormContext();

  const [assetsFile, setAssetsFile] = React.useState<File>(null);
  const [uploadLoading, setUploadLoading] = React.useState<boolean>(false);
  const [variables, setVariables] = React.useState<string[]>(null);

  React.useEffect(() => {
    return () => {
      unregister('mainConfig');
    };
  }, []);

  const onUploadButtonClick = () => {
    setUploadLoading(true);
    dispatch(getFileHeader(assetsFile))
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
        <Col>
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
            <Col>
              <ValidatedField
                register={register}
                error={errors?.mainConfig?.[index]?.index}
                name={`mainConfig[${index}].index`}
                label="Header"
                type="select"
                validate={{ required: true }}
              >
                <option key="select-index" value="" hidden>
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
                {variables.map((variable, i) => (
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