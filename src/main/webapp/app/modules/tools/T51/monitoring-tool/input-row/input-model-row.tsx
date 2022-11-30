import { Alert, Button, Col, Input, Label, Row, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';
import axios from 'axios';

interface InputRowProps {
  networkName: string;
  name: string;
  labelName: string;
  accept: string;
  callback: (value: string) => void;
  passClearStateFunc: (clearStateFunc: () => void) => void;
}

interface AlertProps {
  visible: boolean;
  color: string;
  message: string;
}

export const InputModelRow = (props: InputRowProps) => {
  const { networkName, name, labelName, accept, callback, passClearStateFunc } = props;

  const [file, setFile] = React.useState<File>(null);
  const [loading, setLoading] = React.useState<boolean>(false);
  const [uploaded, setUploaded] = React.useState<boolean>(false);
  const [alert, setAlert] = React.useState<AlertProps>({
    visible: false,
    color: '',
    message: '',
  });
  const [inputKey, setInputKey] = React.useState(Date.now());

  const uploadFile = () => {
    /* eslint-disable-next-line no-console */
    console.log('Upload file button clicked with file: ', file);
    setLoading(true);
    const apiUploadUrl = 'api/tools/upload-inputs';
    const formData = new FormData();
    formData.append('networkName', networkName);
    formData.append('tool', 'T51_asset_monitoring');
    formData.append('files', file);
    axios
      .post(apiUploadUrl, formData, {
        headers: { 'content-type': 'multipart/form-data' },
      })
      .then(res => {
        setLoading(false);
        setUploaded(true);
        setAlert({
          color: 'success',
          message: 'Uploaded with success!',
          visible: true,
        });
        callback(file.name);
      })
      .catch(err => {
        setLoading(false);
        setUploaded(false);
        setAlert({
          color: 'danger',
          message: 'Error uploading',
          visible: true,
        });
      });
  };

  React.useEffect(() => {
    if (!alert.visible) {
      return;
    }
    setTimeout(() => {
      setAlert({
        visible: false,
        color: '',
        message: '',
      });
    }, 2000);
  }, [alert]);

  const setFileAndCallback = event => {
    const fileSelected = event.target.files[0];
    setFile(fileSelected);
  };

  const clearState = () => {
    setFile(null);
    setUploaded(false);
    setInputKey(Date.now());
  };

  passClearStateFunc(clearState);

  return (
    <>
      <Row md="2">
        <Col>
          <Label for={name}>{labelName}</Label>
        </Col>
      </Row>
      <Row md="2" style={{ marginBottom: 20 }}>
        <Col>
          <Input key={inputKey} id={name} name={name} type="file" accept={accept} onChange={setFileAndCallback} />
        </Col>
        <Col>
          <Button disabled={!file || uploaded} color="primary" onClick={uploadFile}>
            <FontAwesomeIcon icon="file-upload" />
            &nbsp; Upload
          </Button>
          <div style={{ display: 'inline-block', marginLeft: 10 }}>
            {loading && <Spinner color="primary" size="sm" />}
            <Alert color={alert.color} isOpen={alert.visible} fade>
              {alert.message}
            </Alert>
          </div>
        </Col>
      </Row>
    </>
  );
};

export default InputModelRow;
