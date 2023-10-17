import React from 'react';

import Charts from 'app/shared/components/T41-44/results/charts/charts';

const FlMw = ({ location }) => {
  return <Charts location={location} section="FL_" type="bar" />;
};

export default FlMw;
