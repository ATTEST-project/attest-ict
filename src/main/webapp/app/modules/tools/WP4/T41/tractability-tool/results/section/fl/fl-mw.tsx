import React from 'react';
import T41OutputData from '../../sample-data/t41_output_response_pt.json';
import Charts from 'app/shared/components/T41-44/results/charts/charts';

const FlMw = ({ location }) => {
  return <Charts location={location} section="FL_" type="scatter" sampleResponse={T41OutputData} />;
};

export default FlMw;
