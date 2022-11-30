import React from 'react';
import Divider from 'app/shared/components/divider/divider';
import Plot from 'react-plotly.js';
import { useAppSelector } from 'app/config/store';
import Charts from 'app/shared/components/T41-44/results/charts/charts';

const Probabilities = ({ location }) => {
  return <Charts location={location} section="PROB" type="lines" />;
};

export default Probabilities;
