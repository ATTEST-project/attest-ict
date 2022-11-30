import React from 'react';
import Divider from 'app/shared/components/divider/divider';
import Plot from 'react-plotly.js';
import { useAppSelector } from 'app/config/store';
import Charts from 'app/shared/components/T41-44/results/charts/charts';

const PV = ({ location }) => {
  return <Charts location={location} section="PV" type="lines" />;
};

export default PV;
