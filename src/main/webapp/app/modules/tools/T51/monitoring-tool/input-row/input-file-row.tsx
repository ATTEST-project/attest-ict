import axios from 'axios';
import React from 'react';
import { Alert, Button, Col, Input, Label, Row, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface InputRowProps {
  networkName: string;
  name: string;
  labelName: string;
  accept: string;
  callback: (filename: string, item1: string, item2: string, item3: string) => void;
  passClearStateFunc: (clearStateFunc: () => void) => void;
}

interface AlertProps {
  visible: boolean;
  color: string;
  message: string;
}

interface ItemsProps {
  item1: string;
  item2: string;
  item3: string;
}

export const InputFileRow = (props: InputRowProps) => {
  const { networkName, name, labelName, accept, callback, passClearStateFunc } = props;

  const [file, setFile] = React.useState<File>(null);
  const [items, setItems] = React.useState<ItemsProps>({ item1: '', item2: '', item3: '' });
  const [loading, setLoading] = React.useState<boolean>(false);
  const [uploaded, setUploaded] = React.useState<boolean>(false);
  const [variablesJson, setVariablesJson] = React.useState(null);
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
    formData.append('tool', 'T51_asset');
    formData.append('files', file);
    axios
      .post(apiUploadUrl, formData, {
        headers: { 'content-type': 'multipart/form-data' },
      })
      .then(res => {
        setLoading(false);
        const jsonRes = JSON.parse(JSON.stringify(res.data));
        setVariablesJson(jsonRes['variables']);
        setUploaded(true);
        setAlert({
          color: 'success',
          message: 'Uploaded with success!',
          visible: true,
        });
        callback(file.name, items.item1, items.item2, items.item3);
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

  const onItem1Change = event => {
    const item1 = event.target.value;
    setItems({ ...items, item1 });
    callback(file.name, item1, items.item2, items.item3);
  };

  const onItem2Change = event => {
    const item2 = event.target.value;
    setItems({ ...items, item2 });
    callback(file.name, items.item1, item2, items.item3);
  };

  const onItem3Change = event => {
    const item3 = event.target.value;
    setItems({ ...items, item3 });
    callback(file.name, items.item1, items.item2, item3);
  };

  const clearState = () => {
    setFile(null);
    setUploaded(false);
    setItems({ item1: '', item2: '', item3: '' });
    setVariablesJson(null);
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
          <Input key={inputKey} id={name} name={name} type="file" accept={accept} onChange={event => setFile(event.target.files[0])} />
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
      {uploaded && (
        <>
          <Row md="3">
            <Col>
              <div>Current</div>
            </Col>
            <Col>
              <div>Oil Temperature</div>
            </Col>
            <Col>
              <div>Winding Temperature</div>
            </Col>
          </Row>
          <Row md="3" style={{ marginBottom: 30 }}>
            <Col>
              <Input id="item1" name="item1" type="select" onChange={onItem1Change}>
                <option value="">Select a variable...</option>
                {variablesJson.map((variable, index) => (
                  <option key={'opt-one-' + index}>{variable.name}</option>
                ))}
              </Input>
            </Col>
            <Col>
              <Input id="item2" name="item2" type="select" onChange={onItem2Change}>
                <option value="">Select a variable...</option>
                {variablesJson.map((variable, index) => (
                  <option key={'opt-two-' + index}>{variable.name}</option>
                ))}
              </Input>
            </Col>
            <Col>
              <Input id="item3" name="item3" type="select" onChange={onItem3Change}>
                <option value="">Select a variable...</option>
                {variablesJson.map((variable, index) => (
                  <option key={'opt-three-' + index}>{variable.name}</option>
                ))}
              </Input>
            </Col>
          </Row>
        </>
      )}
    </>
  );
};

export default InputFileRow;
