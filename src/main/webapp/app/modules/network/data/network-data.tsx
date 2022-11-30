import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Radio from '@material-ui/core/Radio';
import TextField from '@material-ui/core/TextField';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Bus from 'app/entities/bus';
import Branch from 'app/entities/branch';
import Generator from 'app/entities/generator';
import { Link, Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/network/network.reducer';
import { Button, Form, FormGroup, Input, Label } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const useStyles = makeStyles(theme => ({
  divNetwork: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
    background: '#303030',
    color: 'white',
  },
  textFieldRoot: {
    '& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline': {
      borderColor: 'white',
    },
    '& .MuiOutlinedInput-input': {
      color: 'white',
    },
    '& .MuiInputLabel-outlined': {
      color: 'white',
    },
    '& .MuiOutlinedInput-notchedOutline > legend': {
      float: 'none',
    },
    marginRight: 20,
  },
  radioButtonRoot: {
    color: 'white',
    '&:hover': {
      backgroundColor: '#b7bec1',
    },
  },
}));

function CustomRadio(props: { url: string }) {
  const classes = useStyles();

  const { url } = props;

  return (
    <Link to={url}>
      <Radio className={classes.radioButtonRoot} color="default" {...props} />
    </Link>
  );
}

export const NetworkData = props => {
  const dispatch = useAppDispatch();
  const classes = useStyles();

  const { match, history } = props;

  React.useEffect(() => {
    dispatch(getEntity(match.params.id));
  }, []);

  const [tableChosen, setTableChosen] = React.useState<string>('');
  const backVisible = useAppSelector(state => state.backButtonDisplay.display);

  const networkEntity = useAppSelector(state => state.network.entity);
  /* const [networkName, setNetworkName] = React.useState<string>(undefined);
  const networkRef = React.useRef<string>();

  React.useEffect(() => {
    /!* eslint-disable-next-line no-console *!/
    console.log("NetworkRef from Network Data: ", networkRef);
    if (networkEntity.name !== networkRef.current) {
      setNetworkName(networkEntity.name);
    }
    networkRef.current = networkEntity.name;
  }) */

  const handleChangeTable = event => {
    setTableChosen(event.target.value);
  };

  const handleGoBack = () => {
    history.push('/network');
  };

  return (
    <>
      <div>
        <div id="div_network" className={classes.divNetwork}>
          <div style={{ width: 250, marginRight: 25, padding: 20, border: '1px solid white', borderRadius: 5 }}>
            <span>{networkEntity.name}</span>
          </div>
          {/* <TextField
            id="outlined-read-only-input"
            disabled
            label="Network"
            className={classes.textFieldRoot}
            defaultValue={networkName}
            InputProps={{
              readOnly: true,
            }}
            variant="outlined"
          /> */}
          {/* <Form>
            <FormGroup>
              <Label for="network_input">
                Network
              </Label>
              <Input style={{ width: 250, marginRight: 25 }} id="network_input" type="text" defaultValue={networkName} readOnly />
            </FormGroup>
          </Form> */}
          <RadioGroup row aria-label="table-chosen" name="table-chosen" value={tableChosen} onChange={handleChangeTable}>
            <FormControlLabel value="bus" control={<CustomRadio url={match.url + '/bus'} />} label="Buses" />
            <FormControlLabel value="branch" control={<CustomRadio url={match.url + '/branch'} />} label="Branches" />
            <FormControlLabel value="generator" control={<CustomRadio url={match.url + '/generator'} />} label="Generators" />
          </RadioGroup>
        </div>
        <Switch>
          <ErrorBoundaryRoute path={match.url + '/bus'} component={Bus} />
          <ErrorBoundaryRoute path={match.url + '/branch'} component={Branch} />
          <ErrorBoundaryRoute path={match.url + '/generator'} component={Generator} />
        </Switch>
      </div>
      {backVisible && (
        <div style={{ marginTop: 10 }}>
          <Button onClick={handleGoBack} color="info" data-cy="entityUploadBackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">{'Back'}</span>
          </Button>
        </div>
      )}
    </>
  );
};

export default NetworkData;
