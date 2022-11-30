import React from 'react';
import Divider from 'app/shared/components/divider/divider';
import ApcMw from 'app/modules/tools/WP4/T41/tractability-tool/results/section/apc/apc-mw';
import EesMw from 'app/modules/tools/WP4/T41/tractability-tool/results/section/ees/ees-mw';
import FlMw from 'app/modules/tools/WP4/T41/tractability-tool/results/section/fl/fl-mw';

const AllResults = ({ location }) => {
  return (
    <>
      <ApcMw location={location} />
      <Divider />
      <EesMw location={location} />
      <Divider />
      <FlMw location={location} />
    </>
  );
};

export default AllResults;
