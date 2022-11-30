import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import SLDEntire from 'app/modules/network/diagram/view/entire/network-sld-entire';
import RadioGroup from '@material-ui/core/RadioGroup';
import Radio from '@material-ui/core/Radio';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import SLDGrid from 'app/modules/network/diagram/view/substation/network-sld-substations';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/network/network.reducer';
import { Button } from 'reactstrap';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { Link, Switch } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Divider from 'app/shared/components/divider/divider';

const useStyles = makeStyles(theme => ({
  mainDiv: {
    background: 'white',
    color: '#10272F',
  },
  divNetwork: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 10,
  },
  textFieldRoot: {
    '& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline': {
      borderColor: '#10272F',
    },
    '& .MuiOutlinedInput-input': {
      color: '#10272F',
    },
    '& .MuiInputLabel-outlined': {
      color: '#10272F',
    },
    '& .MuiOutlinedInput-notchedOutline > legend': {
      float: 'none',
    },
    marginRight: 20,
  },
  radioButtonRoot: {
    color: '#10272F',
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

function NetworkDiagram(props) {
  const dispatch = useAppDispatch();
  const classes = useStyles();

  const { match, history } = props;

  React.useEffect(() => {
    dispatch(getEntity(match.params.id));
    /* eslint-disable-next-line no-console */
    console.log('NetworkEntity from Network Diagram page: ', networkEntity);
  }, []);

  const networkEntity = useAppSelector(state => state.network.entity);

  const [diagramType, setDiagramType] = React.useState<string>('');

  const handleChangeType = event => {
    setDiagramType(event.target.value);
  };

  const handleGoBack = () => {
    history.push('/network');
  };

  return (
    <>
      <div className={classes.mainDiv}>
        <div id="div_network" className={classes.divNetwork}>
          <div style={{ width: 250, marginRight: 25, padding: 20, border: '1px solid #10272F', borderRadius: 5 }}>
            <span>{networkEntity.name}</span>
          </div>
          <RadioGroup row aria-label="diagram-type" name="diagram-type" value={diagramType} onChange={handleChangeType}>
            <FormControlLabel value="Network" control={<CustomRadio url={match.url + '/entire-view'} />} label="Network" />
            <FormControlLabel
              value="Substation View"
              control={<CustomRadio url={match.url + '/substation-view'} />}
              label="Substation View"
            />
          </RadioGroup>
        </div>
        <Divider />
        <Switch>
          <ErrorBoundaryRoute path={match.url + '/entire-view'} component={SLDEntire} />
          <ErrorBoundaryRoute path={match.url + '/substation-view'} component={SLDGrid} />
        </Switch>
      </div>
      <div style={{ marginTop: 10 }}>
        <Button onClick={handleGoBack} color="info" data-cy="entityUploadBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
      </div>
    </>
  );
}

export default NetworkDiagram;
