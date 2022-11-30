import { Button, Col, Form, Row, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';
import { toast } from 'react-toastify';
import { useAppDispatch } from 'app/config/store';
import { uploadCSVProfile } from 'app/entities/network/network-csv-profile.reducer';
import { uploadExcelProfile } from 'app/entities/network/network-excel-profile.reducer';
import { ValidatedField } from 'react-jhipster';
import { useForm } from 'react-hook-form';
import { getEntitiesByNetworkId } from 'app/entities/input-file/input-file.reducer';

interface UploadFileRowProps {
  index?: number;
  acceptType: string;
  section?: string;
  nRows?: number;
  callbackNRows?: (value: number) => void;
  networkId: number;
  isTX?: boolean;
}

interface FormInfoProps {
  extension: string;
  type?: string;
  mode: number;
  season: string;
  typicalDay: string;
  file: File;
  networkId: number;
  isTX?: boolean;
}

export const UploadFileRow = (props: UploadFileRowProps) => {
  const { index, acceptType, section, nRows, callbackNRows, networkId, isTX } = props;

  const {
    handleSubmit,
    register,
    formState: { errors },
    reset,
  } = useForm();

  const dispatch = useAppDispatch();

  const [loading, setLoading] = React.useState(false);
  const [uploadCompleted, setUploadCompleted] = React.useState(false);

  const showUploadSuccess = () => {
    setUploadCompleted(true);
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

  const showUploadError = () => {
    setUploadCompleted(false);
    setLoading(false);
  };

  const createFinalForm = data => {
    const file = data.file[0];
    const extension = file.name.substring(file.name.lastIndexOf('.') + 1);
    const form: FormInfoProps = {
      file,
      extension,
      mode: data.mode,
      typicalDay: data.typicalDay,
      season: data.season,
      type: section,
      networkId,
      isTX,
    };
    return form;
  };

  const submitForm = data => {
    const form = createFinalForm(data);
    /* eslint-disable-next-line no-console */
    console.log('Form Data: ', form);
    setLoading(true);
    if (form.extension === 'csv') {
      dispatch(uploadCSVProfile(form))
        .unwrap()
        .then(res => {
          showUploadSuccess();
          dispatch(getEntitiesByNetworkId(networkId));
        })
        .catch(err => {
          showUploadError();
        });
    } else {
      dispatch(uploadExcelProfile(form))
        .unwrap()
        .then(res => {
          showUploadSuccess();
          dispatch(getEntitiesByNetworkId(networkId));
        })
        .catch(err => {
          showUploadError();
        });
    }
  };

  const resetRow = () => {
    reset();
  };

  return (
    <>
      <Form onSubmit={handleSubmit(submitForm)}>
        <Row md="7">
          <Col style={{ alignSelf: 'end' }}>
            <ValidatedField
              register={register}
              error={errors?.season}
              label={index === 0 || isTX ? 'Season' : null}
              id={'season-select_' + section + '_' + index}
              name="season"
              data-cy="season"
              type="select"
              validate={{ required: true }}
              disabled={uploadCompleted}
            >
              <option value="" hidden>
                Season...
              </option>
              <option value="A">Autumn</option>
              <option value="S">Spring</option>
              <option value="Su">Summer</option>
              <option value="W">Winter</option>
            </ValidatedField>
          </Col>
          <Col style={{ alignSelf: 'end' }}>
            <ValidatedField
              register={register}
              error={errors?.typicalDay}
              label={index === 0 || isTX ? 'Typical Day' : null}
              id={'typical-day-select_' + section + '_' + index}
              name="typicalDay"
              data-cy="typicalDay"
              type="select"
              validate={{ required: true }}
              disabled={uploadCompleted}
            >
              <option value="" hidden>
                Typical day...
              </option>
              <option value="Bd">Business day</option>
              <option value="Sa">Saturday</option>
              <option value="Su">Sunday</option>
            </ValidatedField>
          </Col>
          <Col style={{ alignSelf: 'end' }}>
            <ValidatedField
              register={register}
              error={errors?.mode}
              label={index === 0 || isTX ? 'Mode' : null}
              id={'mode-select_' + section + '_' + index}
              name="mode"
              data-cy="mode"
              type="select"
              validate={{ required: true }}
              disabled={uploadCompleted}
            >
              <option value="" hidden>
                Mode...
              </option>
              <option value="1">{'Year time-series'}</option>
              <option value="2">{'Business day for a season'}</option>
              <option value="3">{'Business day for a month'}</option>
              <option value="4">{'Weekend for a season'}</option>
              <option value="5">{'Weekend for a month'}</option>
            </ValidatedField>
          </Col>
          <Col xs="4" style={{ alignSelf: 'end' }}>
            <ValidatedField
              id={'file_' + section + '_' + index}
              register={register}
              error={errors?.file}
              name="file"
              data-cy="file"
              type="file"
              accept={acceptType}
              validate={{ required: true }}
              disabled={uploadCompleted}
            />
          </Col>
          <Col style={{ alignSelf: 'end', textAlign: 'right' }}>
            <div className="mb-3">
              <Button
                id={'upload-button_' + section + '_' + index}
                disabled={loading || uploadCompleted}
                color={uploadCompleted ? 'success' : 'primary'}
                type="submit"
              >
                {uploadCompleted ? (
                  ' Uploaded!'
                ) : loading ? (
                  <Spinner color="light" size="sm" />
                ) : (
                  <>
                    {' '}
                    <FontAwesomeIcon icon="file-upload" /> {' Upload'}{' '}
                  </>
                )}
              </Button>
            </div>
          </Col>
          <Col style={{ alignSelf: 'end', textAlign: 'right' }}>
            <div className="mb-3">
              {!uploadCompleted && (
                <div onClick={resetRow} style={{ cursor: 'pointer', padding: 10 }}>
                  <FontAwesomeIcon icon="times" />
                  {' Remove Filters'}
                </div>
              )}
            </div>
          </Col>
          {nRows && (
            <Col style={{ alignSelf: 'end', textAlign: 'right' }}>
              <div className="mb-3">
                <Button disabled={nRows > 11} onClick={() => callbackNRows(1)} color="secondary" style={{ marginRight: 10 }}>
                  +
                </Button>
                <Button disabled={nRows < 2} onClick={() => callbackNRows(-1)} color="secondary">
                  -
                </Button>
              </div>
            </Col>
          )}
        </Row>
      </Form>
    </>
  );
};

export default UploadFileRow;
