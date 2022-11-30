import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import CircularProgress from '@material-ui/core/CircularProgress';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import IconButton from '@material-ui/core/IconButton';
import RefreshIcon from '@material-ui/icons/Refresh';
import Tooltip from '@material-ui/core/Tooltip';
import { useAppDispatch } from 'app/config/store';
import { getEntities } from 'app/entities/network/network.reducer';

const useStyles = makeStyles(theme => ({
  div_network: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  clearIndicator: {
    width: 30,
  },
  popupIndicator: {
    width: 30,
  },
  inputRoot: {
    color: '#10272F',
    '&.Mui-focused': {
      color: '#10272F',
    },
    '& .MuiOutlinedInput-notchedOutline': {
      borderColor: '#10272F',
    },
    '&:hover .MuiOutlinedInput-notchedOutline': {
      borderColor: '#10272F',
    },
    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
      borderColor: '#10272F',
    },
    '& .MuiOutlinedInput-notchedOutline > legend': {
      float: 'none',
    },
  },
  inputLabelRoot: {
    '&.MuiInputLabel-outlined': {
      color: '#10272F',
    },
    '&.Mui-focused': {
      color: '#10272F',
    },
  },
  buttonBranch: {
    border: '1px solid black',
  },
}));

export default function AutocompleteComponent(props) {
  const dispatch = useAppDispatch();
  const classes = useStyles();

  const [networkName, setNetworkName] = useState('');
  const [open, setOpen] = useState(false);
  const [options, setOptions] = useState([]);
  const loading = open && options.length === 0;
  const [showReload, setShowReload] = useState(false);

  React.useEffect(() => {
    let active = true;

    if (!loading) {
      return undefined;
    }

    (async () => {
      const response = await dispatch(getEntities({})).unwrap();
      const networkList = response.data;

      if (active) {
        const networkNames = [];
        networkList.forEach(network => {
          networkNames.push(network.name);
        });
        setOptions(networkNames);
      }
    })();

    return () => {
      active = false;
    };
  }, [loading]);

  const handleRefreshButtonClick = () => {
    setOptions([]);
  };

  const onTrigger = value => {
    props.parentCallback(value);
  };

  const sendData = value => {
    onTrigger(value);
    setNetworkName(value);
  };

  return (
    <Autocomplete
      id="networks-autocomplete"
      classes={{
        clearIndicator: classes.clearIndicator,
        popupIndicator: classes.popupIndicator,
        inputRoot: classes.inputRoot,
      }}
      style={{ width: 300, paddingRight: 10 }}
      open={open}
      onOpen={() => setOpen(true)}
      onClose={() => setOpen(false)}
      getOptionSelected={(option, value) => option === value}
      getOptionLabel={option => option}
      options={options}
      loading={loading}
      noOptionsText={'Network not found'}
      onChange={(event, newInputValue, reason) => {
        reason === 'clear' ? sendData('') : sendData(newInputValue);
      }}
      onMouseEnter={() => setShowReload(true)}
      onMouseLeave={() => setShowReload(false)}
      renderInput={params => (
        <TextField
          {...params}
          label="Network name"
          variant="outlined"
          // classes={classes.root}
          InputLabelProps={{
            classes: {
              root: classes.inputLabelRoot,
            },
          }}
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <React.Fragment>
                {loading ? (
                  <CircularProgress color="inherit" size={20} />
                ) : (
                  options.length > 0 &&
                  showReload && (
                    <Tooltip title="Refresh list" aria-label="refresh list">
                      <IconButton
                        aria-label="refresh list"
                        component="span"
                        style={{ padding: 5, color: '#0000008a' }}
                        onClick={handleRefreshButtonClick}
                      >
                        <RefreshIcon />
                      </IconButton>
                    </Tooltip>
                  )
                )}
                {params.InputProps.endAdornment}
              </React.Fragment>
            ),
          }}
        />
      )}
    />
  );
}
