import React from 'react';
import './T51.scss';
import { Button } from 'reactstrap';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Divider from 'app/shared/components/divider/divider';

const T51 = (props: any) => {
  const { location, match, history, ...rest } = props;

  return (
    <>
      <div className="t51-header">
        <h1>T5.1 Tools</h1>
      </div>
      <div className="section-with-border t51-container">
        <Button className="t51-button" tag={Link} to={match.url + '/characterization'} color="info">
          Characterization Tool
        </Button>
        <Button className="t51-button" tag={Link} to={match.url + '/monitoring'} color="info">
          Monitoring Tool
        </Button>
      </div>
      <Divider />
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <Button tag={Link} to="/tools" color="info">
          <FontAwesomeIcon icon="arrow-left" />
          {' Back'}
        </Button>
      </div>
    </>
  );
};

export default T51;
