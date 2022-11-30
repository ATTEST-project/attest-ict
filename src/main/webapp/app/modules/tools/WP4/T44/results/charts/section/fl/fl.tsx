import React from 'react';
import opfResponse from '../../../sample-data/response_CHART_OPF.json';
import Charts from 'app/shared/components/T41-44/results/charts/charts';
import sce1Con20 from '../../../sample-data/response_CONT_20_SCN_1_ntp5.json';

const FlSection = (props: any) => {
  return <Charts location={props.location} section="FL" type="bar" sampleResponse={sce1Con20} />;
};

export default FlSection;
