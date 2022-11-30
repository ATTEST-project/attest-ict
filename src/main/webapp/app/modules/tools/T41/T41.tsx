import React, { useState } from 'react';
import './T41.scss';
import { Button, MuiThemeProvider } from '@material-ui/core';
import CircularProgress from '@material-ui/core/CircularProgress';
import makeStyles from '@material-ui/core/styles/makeStyles';
import AutocompleteComponent from 'app/shared/components/autocomplete/autocomplete';
import { useHistory } from 'react-router-dom';
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

const useStyles = makeStyles(theme => ({
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
  divNetwork: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  titleRoot: {
    ...theme.typography.button,
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(1),
    flex: 1,
    margin: '0 1% 1% 0',
    textAlign: 'center',
    color: '#10272F',
  },
  inputButton: {
    marginLeft: 20,
  },
}));

const toolsName = {
  windPV: 'T41_windpv',
  stochasticMP: 'T41_stochasticmp',
};

function InputButton(props) {
  const classes = useStyles();

  const uploadInput = event => {
    const formData = new FormData();
    formData.append('files', event.target.files[0]);
    formData.append('networkName', props.networkName);
    formData.append('tool', props.toolName);

    /* eslint-disable-next-line no-console */
    console.log('upload input file button clicked!');

    axios({
      url: '/api/tools/upload-inputs',
      method: 'POST',
      data: formData,
    })
      .then(res => res)
      .catch(err => err);
  };

  return (
    <div className={'input_button'} style={{ marginRight: 30 }}>
      <input
        accept=".ods"
        style={{ display: 'none' }}
        id={'contained-button-file-' + props.toolName}
        type="file"
        multiple
        onChange={uploadInput}
      />
      <label htmlFor={'contained-button-file-' + props.toolName}>
        <Button disabled={!props.networkName} variant="contained" component="span" className={classes.inputButton}>
          Upload
        </Button>
      </label>
    </div>
  );
}

function T41() {
  const classes = useStyles();

  const history = useHistory();

  const [networkName, setNetworkName] = useState('');
  // const [file, setFile] = useState('');
  const [loading1, setLoading1] = useState(false);
  const [loading2, setLoading2] = useState(false);
  const [message, setMessage] = useState('You may run the first tool');
  const [isCompleted, setCompleted] = useState(false);

  const handleSubmit1 = event => {
    event.preventDefault();
    setLoading1(true);

    /* eslint-disable-next-line no-console */
    console.log(networkName);

    setMessage('Please hold on');
    const formData = new FormData();

    formData.append('networkName', networkName);

    /* eslint-disable-next-line no-console */
    console.log('Run Button 1 clicked!');

    axios
      .post('/api/tools/run/T41-windpv', formData)
      .then(res => {
        setLoading1(false);
        setMessage('Now you can proceed to 2nd tool');
      })
      .catch(err => {
        setLoading1(false);
        setMessage('Error running Wind PV Tool');
      });
  };

  const handleSubmit2 = event => {
    event.preventDefault();

    setLoading2(true);
    setMessage('Please hold on');

    const formData = new FormData();
    formData.append('networkName', networkName);

    /* eslint-disable-next-line no-console */
    console.log('Run Button 2 clicked!');

    axios
      .post('/api/tools/run/T41-stochasticmp', formData)
      .then(res => {
        setLoading2(false);
        setMessage('Now go check the results');
        setCompleted(true);
      })
      .catch(err => {
        setLoading2(false);
        setMessage('Error running Stochastic MP Tool');
      });
  };

  const handleCallback = childData => {
    setNetworkName(childData);
  };

  const handleResultsClick = () => {
    /* dispatch({
      type: 'SET_DEFAULTNETWORKNAME',
      defaultNetworkName: networkName,
    }) */
    history.push('t41/results');
  };

  return (
    <div>
      <MuiThemeProvider theme={myTheme}>
        <div className="containerT41">
          <div className={'title'}>Ancillary Services Procurement in DA Planning of the Distribution Network</div>

          <div className="containert41_2">
            <p className="text">
              This tool will support the DSO on the procurement of ancillary services (for voltage control and congestion management) to
              mitigate renewables uncertainty and ensure that the network capacity is never exceeded during the real-time operation stage.
              {/* <br/> <br/> The outputs of the TSO/DSO coordination mechanisms that will run in parallel with the market simulator will define constraints for this tool to avoid that TSOs and DSOs procure conflicting ancillary services in the markets. */}
            </p>
          </div>

          <div id="div_network" className={classes.divNetwork}>
            <AutocompleteComponent parentCallback={handleCallback} />
          </div>
          <div style={{ margin: 20 }}>{message}</div>
          <div id="run_tool_div">
            <div className={classes.titleRoot}>{'Wind and PV scenario generation module'}</div>
            <InputButton className={'item'} networkName={networkName} toolName={toolsName.windPV} />
            <div className={'item'}>
              {loading1 ? (
                <Button variant="contained" style={{ backgroundColor: '#10272F', color: 'white' }} size="small">
                  <CircularProgress color="secondary" size="16px" />
                </Button>
              ) : (
                <Button onClick={handleSubmit1} disabled={!networkName} type="submit" variant="contained" color={'primary'} size="small">
                  Run
                </Button>
              )}
            </div>
          </div>
          <div id="run_tool_div">
            <div className={classes.titleRoot}>{'Stochastic multi-period AC-OPF benchmark module'}</div>
            <InputButton className={'item'} networkName={networkName} toolName={toolsName.stochasticMP} />
            <div className={'item'}>
              {loading2 ? (
                <Button variant="contained" size="small" style={{ backgroundColor: '#10272F', color: 'white' }}>
                  <CircularProgress color="secondary" size="16px" />
                </Button>
              ) : (
                <Button onClick={handleSubmit2} disabled={!networkName} type="submit" variant="contained" color={'primary'} size="small">
                  Run
                </Button>
              )}
            </div>
          </div>
          <div style={{ margin: 20 }}>
            {isCompleted ? (
              <Button className="button3" disabled variant="contained" color={'primary'} size="small">
                {' '}
                results{' '}
              </Button>
            ) : (
              <Button variant="contained" style={{ backgroundColor: '#10272F', color: 'white' }} size="small" onClick={handleResultsClick}>
                {' '}
                results{' '}
              </Button>
            )}
          </div>
        </div>
      </MuiThemeProvider>
    </div>
  );
}

export default T41;
