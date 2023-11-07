import React from 'react';
import './auxiliary.scss';
import { useFormContext } from 'react-hook-form';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toast } from 'react-toastify';
import { Button, Col, Collapse, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { getFileHeader } from 'app/modules/tools/WP5/T52/reducer/tool-file-header.reducer';
import { useAppDispatch } from 'app/config/store';
import { showError } from 'app/modules/tools/custom-toast-error';

const Auxiliary = (props: any) => {
  const { index } = props;

  const {
    register,
    formState: { errors },
  } = useFormContext();

  const dispatch = useAppDispatch();

  const [showAuxSection, setShowAuxSection] = React.useState<boolean>(false);
  const [auxInputFile, setAuxInputFile] = React.useState<File>(null);
  const [uploadLoading, setUploadLoading] = React.useState<boolean>(false);
  const [auxVariables, setAuxVariables] = React.useState<string[]>([]);

  React.useEffect(() => {
    return () => {
      // unregister('mainConfig');
    };
  }, []);

  const onFileChange = event => {
    setAuxInputFile(event.target.files[0]);
    setAuxVariables([]);
  };

  const onUploadButtonClick = () => {
    // eslint-disable-next-line no-console
    // console.log ('T51 Characterization  onUploadButtonClick inputFile: ', auxInputFile );
    if (auxInputFile == null) {
      showError('Please select an auxiliary file to upload');
      return;
    }

    setUploadLoading(true);
    dispatch(getFileHeader(auxInputFile))
      .unwrap()
      .then(res => {
        setAuxVariables(res.data);
        setUploadLoading(false);
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.error(err);
        setUploadLoading(false);
      });
  };

  return (
    <div className="section-with-border auxiliary-section">
      <div
        style={{ display: 'flex', justifyContent: 'space-between', cursor: 'pointer' }}
        onClick={() => setShowAuxSection(!showAuxSection)}
      >
        <h6>{'Auxiliary data (optional)'}</h6>
        <div style={{ marginRight: 10 }}>
          <FontAwesomeIcon icon="angle-down" style={showAuxSection && { transform: 'rotate(180deg)' }} />
        </div>
      </div>
      <Collapse isOpen={showAuxSection}>
        <Row md="4">
          <Col>
            <ValidatedField
              register={register}
              error={errors?.config?.[index]?.inputAuxFile}
              name={`config[${index}].inputAuxFile`}
              label="Auxiliary File [e.g. HEP_extra_info_trafo.xlsx]"
              type="file"
              onChange={onFileChange}
            />
          </Col>
          {!auxVariables || auxVariables.length === 0 ? (
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
                  error={errors?.config?.[index]?.variables2}
                  name={`config[${index}].component2_field`}
                  label="Aux Variables"
                  type="select"
                  validate={{ required: true }}
                >
                  <option key="aux-var-0" value="" hidden>
                    Variable...
                  </option>
                  {auxVariables?.map((variable, index) => (
                    <option key={'aux-var-' + index}>{variable}</option>
                  ))}
                </ValidatedField>
              </Col>
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.config?.[index]?.variables2}
                  name={`config[${index}].variables2`}
                  label="Group-by Attribute"
                  type="select"
                  validate={{ required: true }}
                >
                  <option key="aux-var1-0" value="" hidden>
                    Variable...
                  </option>
                  {auxVariables?.map((variable, index) => (
                    <option key={'aux-var1-' + index}>{variable}</option>
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

export default Auxiliary;
