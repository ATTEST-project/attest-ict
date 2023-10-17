import React from 'react';
import TotalCost from 'app/modules/tools/WP3/T31/results/section/total-cost/total-cost';
import Divider from 'app/shared/components/divider/divider';
import OperationCostPerYear from 'app/modules/tools/WP3/T31/results/section/cost-per-year/operation-cost-per-year';
import TotalInvestCostYear from 'app/modules/tools/WP3/T31/results/section/cost-per-year/total-investment-cost-per-year';
import BranchInvestment from 'app/modules/tools/WP3/T31/results/section/branch-investment/branch-investment';
import FlexInvestment from 'app/modules/tools/WP3/T31/results/section/flex-investment/flex-investment';
import Total from 'app/modules/tools/WP3/T31/results/section/cost-per-year/operation-cost-per-year';

const AllResults = () => {
  return (
    <>
      <TotalCost />
      <Divider />
      <OperationCostPerYear />
      <Divider />
      <TotalInvestCostYear />
      <Divider />
      <BranchInvestment />
      <Divider />
      <FlexInvestment />
    </>
  );
};

export default AllResults;
