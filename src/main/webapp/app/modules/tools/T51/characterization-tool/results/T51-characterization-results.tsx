import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button } from 'reactstrap';

export const T51CharacterizationResults = () => {
  return (
    <>
      <div style={{ background: 'white' }}>
        <iframe title="t51-results" src="http://localhost:8000" width="100%" height="800" />
      </div>
      <div style={{ marginTop: 20 }}>
        <Button tag={Link} to="/tools/t51/characterization" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
      </div>
    </>
  );
};

export default T51CharacterizationResults;
