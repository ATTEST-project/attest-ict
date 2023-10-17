import React from 'react';
import Divider from 'app/shared/components/divider/divider';
import ApcMw from 'app/modules/tools/WP4/T42/results/section/apc/apc-mw';
import EesPMw from 'app/modules/tools/WP4/T42/results/section/ees/ees-p-mw';
import EesQMw from 'app/modules/tools/WP4/T42/results/section/ees/ees-q-mw';
import FlMw from 'app/modules/tools/WP4/T42/results/section/fl/fl-mw';

const AllResults = ({ location }) => {
  return (
    <>
      <ApcMw location={location} />
      <Divider />
      <EesPMw location={location} />
      <EesQMw location={location} />
      <Divider />
      <FlMw location={location} />
    </>
  );
};

export default AllResults;
