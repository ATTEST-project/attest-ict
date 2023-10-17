import React from 'react';

import Charts from 'app/shared/components/T41-44/results/charts/charts';

const EesPMw = ({ location }) => {
  return <Charts location={location} section="EES_P_" type="bar" />;
};

export default EesPMw;
