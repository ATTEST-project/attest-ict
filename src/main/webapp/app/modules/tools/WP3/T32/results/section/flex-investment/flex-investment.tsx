import React from 'react';
import ChartInvestment from 'app/modules/tools/WP3/T32/results/chart/chart-investment';

const FlexInvestment = () => {
  return <ChartInvestment investmentType={'Flexibility investment (MW)'} yAxisTitle={'MW'} />;
};

export default FlexInvestment;
