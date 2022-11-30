import React, { useState } from 'react';
import { Button, MuiThemeProvider } from '@material-ui/core';
import CircularProgress from '@material-ui/core/CircularProgress';
import makeStyles from '@material-ui/core/styles/makeStyles';
import Input from '@material-ui/core/Input';
import Alert, { Color } from '@material-ui/lab/Alert';
import { createTheme } from '@material-ui/core/styles';
import axios from 'axios';

const myTheme = createTheme({
  palette: {
    primary: {
      main: '#10272F',
    },
    secondary: {
      main: '#A8BEC9',
    },
  },
  typography: {
    fontFamily: 'Georgia Regular',
  },
});

const useStyles = makeStyles({
  root: {
    minWidth: 275,
  },
  bullet: {
    display: 'inline-block',
    margin: '0 2px',
    transform: 'scale(0.8)',
  },
  title: {
    fontSize: 14,
  },
  pos: {
    marginBottom: 12,
  },
});

function T25RealTime(props) {
  const [showAlert, setShowAlert] = React.useState(false);
  const [severity, setSeverity] = React.useState<Color>('info');
  const [message, setMessage] = React.useState('');

  const [networkName, setNetworkName] = useState('');
  const [files, setFiles] = useState([]);
  const [loading1, setLoading1] = useState(false);

  const handleFile = e => {
    const fileVar = e.target.files;
    /* eslint-disable-next-line no-console */
    console.log(e.target.files, '$$$$');
    const length = e.target.files.length;

    for (let i = 0; i < length; i++) {
      setFiles(oldFiles => [...oldFiles, fileVar[i]]);
    }
  };

  const handleUpload = e => {
    e.preventDefault();
    const formdata = new FormData();
    for (let i = 0; i < files.length; i++) {
      formdata.append('files', files[i]);
    }
    formdata.append('networkName', networkName);
    formdata.append('tool', 'T25_realtime');

    /* eslint-disable-next-line no-console */
    console.log('call uploadInput for T2.5 Real-time');
    axios({
      url: '/api/tools/upload-inputs',
      method: 'POST',
      data: formdata,
    })
      .then(res => {
        /* eslint-disable-next-line no-console */
        console.log(res);
      })
      .catch(err => {
        setShowAlert(true);
        setSeverity('error');
        setMessage('Files could not be uploaded ');
        /* eslint-disable-next-line no-console */
        console.error(err);
      });
  };

  const handleSubmit1 = event => {
    event.preventDefault();
    setLoading1(true);

    const formData = new FormData();
    // formData.append("files", files);
    formData.append('networkName', networkName);
    axios
      .post('/api/tools/run/T25-real-time', formData)
      .then(res => {
        /* eslint-disable-next-line no-console */
        console.log(res);
        setLoading1(false);
      })
      .catch(err => {
        /* eslint-disable-next-line no-console */
        console.log(err);
        setShowAlert(true);
        setSeverity('error');
        setMessage('Problem running Real time tool');
        setLoading1(false);
      });
  };

  return (
    <div>
      <div className="containerT41">
        <div className={'title'}>Real Time Optimization Tool</div>
        <div className="containert41_2">
          <p className="text">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
            enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor
            in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident,
            sunt in culpa qui officia deserunt mollit anim id est laborum.
          </p>
        </div>
        <div>{showAlert ? <Alert severity={severity}>{message}</Alert> : ''}</div>
        <MuiThemeProvider theme={myTheme}>
          <form>
            <label className="label1" style={{ padding: '30px' }} />
            <Input type="text" name="networkName" placeholder="Network Name" onChange={event => setNetworkName(event.target.value)} />
            <input style={{ display: 'hidden' }} type="file" name="file" multiple onChange={e => handleFile(e)} />
            <Button
              onClick={e => handleUpload(e)}
              variant="contained"
              style={{ backgroundColor: '#10272F', color: 'white' }}
              component="span"
            >
              Upload Files
            </Button>
          </form>
          <div className="title1">
            {' '}
            Real Time Optimization Tool
            {loading1 ? (
              <Button variant="contained" color={'primary'} size="small">
                {' '}
                <CircularProgress color="secondary" size="16px" />{' '}
              </Button>
            ) : (
              <Button onClick={handleSubmit1} disabled={!networkName} type="submit" variant="contained" color={'primary'} size="small">
                Run
              </Button>
            )}
          </div>
        </MuiThemeProvider>
      </div>
    </div>
  );
}

export default T25RealTime;
