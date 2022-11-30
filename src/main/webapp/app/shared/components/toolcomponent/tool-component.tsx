import React from 'react';
import './tool-component.css';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import CardActions from '@material-ui/core/CardActions';
import Button from '@material-ui/core/Button';
import makeStyles from '@material-ui/core/styles/makeStyles';
import { Link } from 'react-router-dom';

const useStyles = makeStyles({
  root: {
    minWidth: 275,
    minHeight: 160,
  },
  title: {
    fontSize: 14,
  },
  pos: {
    marginBottom: 12,
  },
  link: {
    textDecoration: 'none',
  },
});

interface Props {
  title: string;
  text: string;
  toPage: string;
  isReady: boolean;
}

const ToolComponent = ({ title, text, toPage, isReady }: Props) => {
  const classes = useStyles();

  return (
    <Card className={classes.root} style={isReady ? { backgroundColor: 'white' } : { backgroundColor: 'lightgrey' }}>
      <CardContent>
        <Typography variant="h5" component="h2">
          {isReady ? <div>{title}</div> : <div style={{ color: 'grey' }}>{title}</div>}
        </Typography>

        <Typography variant="body2" component="p">
          {text}
        </Typography>
      </CardContent>

      <CardActions>
        <Button
          disabled={!isReady}
          size="small"
          style={isReady ? { border: '1px solid black' } : { border: 'none' }}
          component={Link}
          to={toPage}
        >
          Run Tool
        </Button>
      </CardActions>
    </Card>
  );
};

export default ToolComponent;
