import React from 'react';
import opfResponse from '../../../sample-data/response_CHART_OPF.json';
import sce1Con20 from '../../../sample-data/response_CONT_20_SCN_1_ntp5.json';
import Charts from 'app/shared/components/T41-44/results/charts/charts';

const CurtSection = (props: any) => {
  return <Charts location={props.location} section="CURT" type="bar" sampleResponse={sce1Con20} />;
};

export default CurtSection;
