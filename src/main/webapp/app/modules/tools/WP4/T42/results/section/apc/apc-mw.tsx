import React from 'react';
import Charts from 'app/shared/components/T41-44/results/charts/charts';

const ApcMw = ({ location }) => {
  return <Charts location={location} section="APC" type="bar" />;
};

export default ApcMw;
