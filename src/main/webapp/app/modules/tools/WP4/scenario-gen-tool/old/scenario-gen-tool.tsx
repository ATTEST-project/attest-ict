import React from 'react';
import NetworkInfo from 'app/shared/components/network-info/network-info';
import Divider from 'app/shared/components/divider/divider';
import { Button, Col, Form, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { useForm } from 'react-hook-form';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import axios from 'axios';

const ScenarioGenTool = (props: any) => {
  const network = props.location.network || JSON.parse(sessionStorage.getItem('network'));

  const {
    register,
    formState: { errors },
    reset,
    handleSubmit,
  } = useForm();

  const [file, setFile] = React.useState<File>(null);
  const [uploadCompleted, setUploadCompleted] = React.useState<boolean>(false);
  const [operationCompleted, setOperationCompleted] = React.useState<boolean>(false);
  const [loading, setLoading] = React.useState<boolean>(false);
  const [loadingRun, setLoadingRun] = React.useState<boolean>(false);

  const onInputChange = event => {
    if (!event.target.files) {
      return;
    }
    setFile(event.target.files[0]);
  };

  const uploadFile = () => {
    setLoading(true);

    const formData = new FormData();
    formData.append('files', file);
    formData.append('networkName', network.name);
    formData.append('tool', 'T41_windpv');

    axios({
      url: '/api/tools/upload-inputs',
      method: 'POST',
      data: formData,
    })
      .then(res => {
        setLoading(false);
        setUploadCompleted(true);
      })
      .catch(err => {
        setLoading(false);
        setUploadCompleted(false);
      });
  };

  const submitForm = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);

    const formData = new FormData();
    formData.append('networkName', network.name);

    setLoadingRun(true);
    axios
      .post('/api/tools/run/T41-windpv', formData)
      .then(res => {
        setLoadingRun(false);
        setOperationCompleted(true);
      })
      .catch(err => {
        setLoadingRun(false);
        setOperationCompleted(false);
      });
  };

  const resetUpload = () => {
    reset();
    setFile(null);
    setUploadCompleted(false);
    setOperationCompleted(false);
    setLoading(false);
  };

  return (
    <>
      <div>
        <h4>{'Scenario Generation Tool'}</h4>
        <NetworkInfo network={network} />
        <Divider />
        <div className="section-with-border">
          <h6>Choose the data scenario file</h6>
          <Form onSubmit={handleSubmit(submitForm)}>
            <Row md="3">
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.dataScenarioFile}
                  id={'data-scenario-file'}
                  label="Data Scenario File"
                  name="dataScenarioFile"
                  data-cy="dataScenarioFile"
                  type="file"
                  accept=".ods,.xls,.xlsx"
                  onChange={onInputChange}
                  validate={{ required: true }}
                />
              </Col>
              <Col style={{ alignSelf: 'end' }}>
                <div className="mb-3">
                  <Button color="primary" onClick={resetUpload}>
                    <FontAwesomeIcon icon="redo" />
                    {' Reset'}
                  </Button>{' '}
                  {uploadCompleted ? (
                    <Button disabled color="success">
                      {'Uploaded'}
                    </Button>
                  ) : (
                    <Button disabled={!file} color="primary" onClick={uploadFile}>
                      {loading ? (
                        <Spinner color="light" size="sm" />
                      ) : (
                        <>
                          <FontAwesomeIcon icon="file-upload" />
                          {' Upload'}
                        </>
                      )}
                    </Button>
                  )}{' '}
                  {uploadCompleted && (
                    <Button color="primary" type="submit">
                      {loadingRun ? (
                        <Spinner color="light" size="sm" />
                      ) : (
                        <>
                          <FontAwesomeIcon icon="play" />
                          {' Run'}
                        </>
                      )}
                    </Button>
                  )}
                </div>
              </Col>
              {operationCompleted && (
                <Col style={{ alignSelf: 'end', textAlign: 'right' }}>
                  <div className="mb-3">
                    <Button tag={Link} to="/wip" color="success">
                      <FontAwesomeIcon icon="poll" />
                      {' Results'}
                    </Button>
                  </div>
                </Col>
              )}
            </Row>
          </Form>
        </div>
        <Divider />
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <Button tag={Link} to="/tools" color="info">
            <FontAwesomeIcon icon="arrow-left" />
            {' Back'}
          </Button>
        </div>
      </div>
    </>
  );
};

export default ScenarioGenTool;
