import React from 'react';
import T41OutputData from '../../sample-data/t41_output_response_pt.json';
import Charts from 'app/shared/components/T41-44/results/charts/charts';

const EesMw = ({ location }) => {
  return <Charts location={location} section="EES_" type="bar" sampleResponse={T41OutputData} />;
};

export default EesMw;
