import React from 'react';
import Wind from 'app/modules/tools/WP4/scenario-gen-tool/results/section/wind/wind';
import Divider from 'app/shared/components/divider/divider';
import PV from 'app/modules/tools/WP4/scenario-gen-tool/results/section/pv/pv';
import Probabilities from 'app/modules/tools/WP4/scenario-gen-tool/results/section/probabilities/probabilities';

const AllResults = ({ location }) => {
  return (
    <>
      <Wind location={location} />
      <Divider />
      <PV location={location} />
      <Divider />
      <Probabilities location={location} />
    </>
  );
};

export default AllResults;
