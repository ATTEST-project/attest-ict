import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button } from 'reactstrap';

export const T51MonitoringResults = () => {
  return (
    <>
      <div style={{ background: 'white' }}>
        <iframe
          title="t51-results"
          src="http://127.0.0.1:8000/static/results_condition_monitoring/condition_monitoring.html"
          width="100%"
          height="800"
        />
      </div>
      <div style={{ marginTop: 20 }}>
        <Button tag={Link} to="/tools/t51/monitoring" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
      </div>
    </>
  );
};

export default T51MonitoringResults;
