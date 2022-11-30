import { Alert, Badge, Button, Col, Input, Label, Row, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';
import axios from 'axios';
import { defaultValue, JsonToolConfig } from 'app/modules/tools/T51/model/json-tool-config';

interface T51FileRowProps {
  networkName: string;
  assetsType: string;
  index: number;
  nRows: number;
  callbackNRows: (value: number) => void;
  jsonCallback: (index: number, value?: JsonToolConfig) => void;
  passClearStateFunc: (clearStateFunc: () => void) => void;
}

interface AlertProps {
  visible: boolean;
  color: string;
  message: string;
}

export const InputFileRow = (props: T51FileRowProps) => {
  const { networkName, assetsType, index, nRows, callbackNRows, jsonCallback, passClearStateFunc } = props;

  const [file, setFile] = React.useState<File>(null);
  const [auxFile, setAuxFile] = React.useState<File>(null);
  const [variablesJson, setVariablesJson] = React.useState(null);
  const [loading, setLoading] = React.useState<boolean>(false);
  const [uploaded, setUploaded] = React.useState<boolean>(false);
  const [auxUploaded, setAuxUploaded] = React.useState<boolean>(false);
  const [auxFileChecked, setAuxFileChecked] = React.useState<boolean>(false);
  const [auxVariablesJson, setAuxVariablesJson] = React.useState(null);
  const [auxLoading, setAuxLoading] = React.useState<boolean>(false);
  const [rowSubmitted, setRowSubmitted] = React.useState<boolean>(false);
  const [alertVisible, setAlertVisible] = React.useState<AlertProps>({
    visible: false,
    color: '',
    message: '',
  });
  const [inputKey, setInputKey] = React.useState(Date.now());

  React.useEffect(() => {
    setUploaded(false);
  }, [file]);

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
        setAlertVisible({
          color: 'success',
          message: 'Uploaded with success!',
          visible: true,
        });
      })
      .catch(err => {
        setLoading(false);
        setUploaded(false);
        setAlertVisible({
          color: 'danger',
          message: 'Error uploading',
          visible: true,
        });
      });
  };

  const handleChangeInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (!event.target.files) {
      return;
    }
    /* eslint-disable-next-line no-console */
    console.log('Files: ', event.target.files);
    setFile(event.target.files[0]);
  };

  const uploadAuxFile = () => {
    /* eslint-disable-next-line no-console */
    console.log('Upload file button clicked with file: ', auxFile);
    setAuxLoading(true);
    const apiUploadUrl = 'api/tools/upload-inputs';
    const formData = new FormData();
    formData.append('networkName', networkName);
    formData.append('tool', 'T51_asset');
    formData.append('files', auxFile);
    axios
      .post(apiUploadUrl, formData, {
        headers: { 'content-type': 'multipart/form-data' },
      })
      .then(res => {
        setAuxLoading(false);
        const jsonRes = JSON.parse(JSON.stringify(res.data));
        setAuxVariablesJson(jsonRes['variables']);
        setAuxUploaded(true);
        setAlertVisible({
          color: 'success',
          message: 'Uploaded with success!',
          visible: true,
        });
      })
      .catch(err => {
        setAuxLoading(false);
        setAuxUploaded(false);
        setAlertVisible({
          color: 'danger',
          message: 'Error uploading',
          visible: true,
        });
      });
  };

  const handleChangeAuxInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (!event.target.files) {
      return;
    }
    /* eslint-disable-next-line no-console */
    console.log('Aux Files: ', event.target.files);
    setAuxFile(event.target.files[0]);
  };

  // const variables = ['age_years', 'H1_fail_prob', 'H2_criticality', 'H3_energy', 'asset_type'];
  const results = ['Asset Assessment', 'Number of Clusters assessment', 'Clustering Training Process'];

  const submitForm = event => {
    event.preventDefault();

    const allVariables = event.target.variables;
    const variablesChecked = [];
    for (let i = 0; i < allVariables.length; ++i) {
      if (allVariables[i].checked) {
        variablesChecked.push(allVariables[i].value);
      }
    }

    const allResults = event.target.results;
    const resultsChecked = [];
    for (let i = 0; i < allResults.length; ++i) {
      if (allResults[i].checked) {
        resultsChecked.push(allResults[i].value);
      }
    }

    const jsonRow: JsonToolConfig = {
      assestsType: assetsType,
      component1_field: event.target.selectedVariables.value,
      component2_field: event.target.variables2?.value || '',
      components: [],
      components2: [],
      config: event.target.dimension.value,
      method: event.target.method.value,
      path: '..\\..\\input_files\\' + file.name,
      path2: auxFile ? '..\\..\\input_files\\' + auxFile.name : '',
      results: resultsChecked,
      selectedVariables: event.target.selectedVariables.value,
      variables: variablesChecked,
      variables2: event.target.variables2?.value || '',
    };

    /* eslint-disable-next-line no-console */
    console.log(JSON.parse(JSON.stringify(jsonRow)));

    jsonCallback(index, jsonRow);
    setRowSubmitted(true);
  };

  const resetRow = () => {
    setFile(null);
    setVariablesJson(null);
    setLoading(false);
    setUploaded(false);
    setRowSubmitted(false);
    setAuxFileChecked(false);
    setAuxFile(null);
    setAuxVariablesJson(null);
    setAuxUploaded(false);
    setAlertVisible({
      visible: false,
      color: '',
      message: '',
    });
    setInputKey(Date.now());
    jsonCallback(index);
  };

  if (index === 0) {
    passClearStateFunc(resetRow);
  }

  React.useEffect(() => {
    if (!alertVisible.visible) {
      return;
    }
    setTimeout(() => {
      setAlertVisible({
        visible: false,
        color: '',
        message: '',
      });
    }, 2000);
  }, [alertVisible]);

  const auxFileCheckboxChange = () => {
    setAuxFileChecked(!auxFileChecked);
    setAuxUploaded(false);
    setAuxFile(null);
  };

  return (
    <div style={{ border: '1px solid white', borderRadius: 10, padding: 10 }}>
      <Alert color={alertVisible.color} isOpen={alertVisible.visible} fade>
        {alertVisible.message}
      </Alert>
      <Row md="10">
        <Col>
          <Label for="inputFile">Input File</Label>
        </Col>
        <Col />
        {uploaded && (
          <>
            <Col>
              <Label for="selectedVariables">Variable</Label>
            </Col>
            <Col>
              <Label for="variables">Variables</Label>
            </Col>
            <Col>
              <Label for="dimension">Dimension</Label>
            </Col>
            <Col>
              <Label for="method">Method</Label>
            </Col>
            <Col>
              <Label for="results">Results</Label>
            </Col>
            <Col />
            <Col />
            <Col />
            <Col />
          </>
        )}
      </Row>
      <form onSubmit={submitForm}>
        <Row md="10">
          <Col>
            <Input key={inputKey} id="inputFile" name="inputFile" type="file" accept=".csv,.xls,.xlsx" onChange={handleChangeInput} />
          </Col>
          <Col>
            <Button
              disabled={!file || uploaded}
              id="jhi-confirm-upload-file"
              data-cy="entityConfirmUploadButton"
              color="primary"
              onClick={uploadFile}
            >
              <FontAwesomeIcon icon="file-upload" />
              &nbsp; Upload
            </Button>
            <div style={{ display: 'inline-block', marginLeft: 10 }}>{loading && <Spinner color="primary" size="sm" />}</div>
          </Col>
          {uploaded && (
            <>
              <Col>
                <Input id="selectedVariables" name="selectedVariables" type="select">
                  {variablesJson.map((variable, index) => (
                    <option key={'opt-' + index}>{variable.name}</option>
                  ))}
                </Input>
              </Col>
              <Col>
                {variablesJson.map((variable, index) => (
                  <div key={'cbv-' + index}>
                    <Input disabled={!variable.enabled} type="checkbox" name="variables" value={variable.name} />
                    {variable.enabled ? (
                      <span>{' ' + variable.name}</span>
                    ) : (
                      <span style={{ color: 'lightgrey' }}>{' ' + variable.name}</span>
                    )}
                  </div>
                ))}
              </Col>
              <Col>
                <Input id="dimension" name="dimension" type="select">
                  <option>Life Assessment</option>
                  <option>Maintenance Strategy</option>
                  <option>Economic Impact</option>
                </Input>
              </Col>
              <Col>
                <Input id="method" name="method" type="select">
                  <option>Auto</option>
                  <option>Elbow Method</option>
                  <option>Gap Statistics</option>
                </Input>
              </Col>
              <Col>
                {results.map((result, index) => (
                  <div key={index}>
                    <Input type="checkbox" defaultChecked={true} name="results" value={result} />
                    <span>{' ' + result}</span>
                  </div>
                ))}
              </Col>
              <Col>
                <Input type="checkbox" name="auxFile" value="aux file?" onChange={auxFileCheckboxChange} />
                <span>{' aux file?'}</span>
              </Col>
              <Col>
                {!rowSubmitted ? (
                  <Button type="submit">Submit</Button>
                ) : (
                  <Button disabled style={{ background: 'green' }}>
                    Submitted!
                  </Button>
                )}
              </Col>
              <Col>
                <Button style={{ background: 'red' }} onClick={resetRow}>
                  Reset
                </Button>
              </Col>
            </>
          )}
          <Col style={{ textAlign: 'right' }}>
            {
              <Button disabled={nRows > 2} onClick={() => callbackNRows(1)} color="secondary" style={{ marginRight: 10 }}>
                +
              </Button>
            }
            {
              <Button disabled={nRows < 2} onClick={() => callbackNRows(-1)} color="secondary">
                -
              </Button>
            }
          </Col>
        </Row>
        <Row md="8" style={{ marginTop: 20 }}>
          {auxFileChecked && (
            <Col>
              <Label for="inputAuxFile">Auxiliary File</Label>
            </Col>
          )}
          <Col />
          {auxUploaded && (
            <Col>
              <Label for="variables2">Variable</Label>
            </Col>
          )}
          <Col />
          <Col />
          <Col />
          <Col />
        </Row>
        <Row md="8">
          {auxFileChecked && (
            <>
              <Col>
                <Input id="inputAuxFile" name="inputAuxFile" type="file" accept=".csv,.xls,.xlsx" onChange={handleChangeAuxInput} />
              </Col>
              <Col>
                <Button
                  disabled={!auxFile || auxUploaded}
                  id="jhi-confirm-upload-file"
                  data-cy="entityConfirmUploadButton"
                  color="primary"
                  onClick={uploadAuxFile}
                >
                  <FontAwesomeIcon icon="file-upload" />
                  &nbsp; Upload
                </Button>
                <div style={{ display: 'inline-block', marginLeft: 10 }}>{auxLoading && <Spinner color="primary" size="sm" />}</div>
              </Col>
            </>
          )}
          {auxUploaded && (
            <>
              <Col>
                <Input id="variables2" name="variables2" type="select">
                  {auxVariablesJson.map((variable, index) => (
                    <option key={'opt-aux-' + index}>{variable.name}</option>
                  ))}
                </Input>
              </Col>
            </>
          )}
          <Col />
          <Col />
          <Col />
          <Col />
        </Row>
      </form>
    </div>
  );
};

export default InputFileRow;
