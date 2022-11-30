import React from 'react';
import TotalCost from 'app/modules/tools/WP3/T32/results/section/total-cost/total-cost';
import Divider from 'app/shared/components/divider/divider';
import CostPerYear from 'app/modules/tools/WP3/T32/results/section/cost-per-year/cost-per-year';
import BranchInvestment from 'app/modules/tools/WP3/T32/results/section/branch-investment/branch-investment';
import FlexInvestment from 'app/modules/tools/WP3/T32/results/section/flex-investment/flex-investment';

const AllResults = () => {
  return (
    <>
      <TotalCost />
      <Divider />
      <CostPerYear />
      <Divider />
      <BranchInvestment />
      <Divider />
      <FlexInvestment />
    </>
  );
};

export default AllResults;
