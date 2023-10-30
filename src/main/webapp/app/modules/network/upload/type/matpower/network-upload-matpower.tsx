import React from 'react';
import './network-upload-matpower.scss';
import { Button, Col, Form, Input, Row, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { uploadNetworkFile, reset as resetReducer } from 'app/entities/network/network-matpower-upload.reducer';
import { toast } from 'react-toastify';
import { getEntity } from 'app/entities/network/network.reducer';
import { getEntitiesByNetworkId } from 'app/entities/input-file/input-file.reducer';
import { useForm } from 'react-hook-form';
import { ValidatedField } from 'react-jhipster';
import { SECTION } from 'app/shared/util/file-utils';

const NetworkUploadMatpower = props => {
  const dispatch = useAppDispatch();

  const {
    handleSubmit,
    register,
    formState: { errors },
    reset,
  } = useForm();

  const [loading, setLoading] = React.useState(false);
  const [uploadedFile, setUploadedFile] = React.useState(null);
  const networkEntity = useAppSelector(state => state.network.entity);
  const operationCompleted = useAppSelector(state => state.networkMatpowerUpload.updateSuccess);
  const inputFileEntities = useAppSelector(state => state.inputFile.entities) || props.files;
  const inputFileLoading = useAppSelector(state => state.inputFile.loading);

  const getMatpowerFile = () => {
    const matpowerFile = inputFileEntities.find(file => file.description === SECTION.NETWORK);
    if (matpowerFile) {
      setUploadedFile(matpowerFile);
      props.callback(matpowerFile);
    }
  };

  React.useEffect(() => {
    getMatpowerFile();
    return () => {
      // -- call  reset,  when unmount component (-> page exit)
      dispatch(resetReducer());
    };
  }, [inputFileEntities]);

  const showSuccess = () => {
    setLoading(false);
    toast.success('Uploaded successfully!', {
      position: 'top-right',
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
  };

  const showError = () => {
    setLoading(false);
  };

  React.useEffect(() => {
    if (!operationCompleted) {
      return;
    }
    dispatch(getEntity(networkEntity.id));
    dispatch(getEntitiesByNetworkId(networkEntity.id));
  }, [operationCompleted]);

  const submitUploadForm = (data: any) => {
    setLoading(true);
    dispatch(uploadNetworkFile({ networkName: networkEntity.name, file: data.matpowerFile[0] }))
      .unwrap()
      .then(res => showSuccess())
      .catch(err => showError());
  };

  const resetRow = () => {
    reset();
  };

  return (
    <>
      <div id="matpower-upload" className="matpower-upload-row">
        {!uploadedFile ? (
          <>
            <h5>Upload a Network file:</h5>
            <Form onSubmit={handleSubmit(submitUploadForm)}>
              <Row>
                <Col md="4">
                  <ValidatedField
                    register={register}
                    error={errors?.matpowerFile}
                    id="matpower-input-file"
                    name="matpowerFile"
                    type="file"
                    accept=".m,.ods"
                    validate={{ required: true }}
                  />
                </Col>
                <Col>
                  <Button id={'matpower-upload-button'} color="primary" type="submit">
                    {loading ? (
                      <Spinner color="light" size="sm" />
                    ) : (
                      <>
                        {' '}
                        <FontAwesomeIcon icon="file-upload" /> {' Upload'}{' '}
                      </>
                    )}
                  </Button>
                </Col>
                <Col>
                  {!uploadedFile && (
                    <div onClick={resetRow} style={{ cursor: 'pointer', padding: 10 }}>
                      <FontAwesomeIcon icon="times" />
                      {' Remove Filters'}
                    </div>
                  )}
                </Col>
              </Row>
            </Form>
          </>
        ) : (
          <>
            <Row>
              <Col md="4">
                <h5>Network file uploaded:</h5>
                <div>
                  <span style={{ fontSize: 16 }}>{uploadedFile.fileName}</span>
                </div>
              </Col>
            </Row>
          </>
        )}
      </div>
    </>
  );
};

export default NetworkUploadMatpower;
