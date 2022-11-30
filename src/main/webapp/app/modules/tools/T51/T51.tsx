import React from 'react';
import { Button } from 'reactstrap';
import { Link } from 'react-router-dom';
import './T51.scss';

export const T51 = () => {
  return (
    <>
      <div className="t51-header">
        <h1>T5.1 Tools</h1>
      </div>
      <div className="t51-container">
        <div className="t51-child">
          <Button className="t51-button" tag={Link} to="t51/characterization" color="info">
            Characterization Tool
          </Button>
        </div>
        <div className="t51-child">
          <Button className="t51-button" tag={Link} to="t51/monitoring" color="info">
            Monitoring Tool
          </Button>
        </div>
      </div>
    </>
  );
};

export default T51;
