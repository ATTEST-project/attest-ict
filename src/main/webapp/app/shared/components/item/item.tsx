import React from 'react';
import { makeStyles } from '@material-ui/core';

const useStyles = makeStyles(() => ({
  divStyle: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '250px',
    width: '100%',
    backgroundColor: '#10272F',
    color: '#fff',
    margin: '0 15px',
    fontSize: '4em',
  },
}));

const Item = ({ children }) => {
  const classes = useStyles();

  return <div className={classes.divStyle}>{children}</div>;
};

export default Item;
