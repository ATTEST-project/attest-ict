import React from 'react';
import ArpSection from 'app/modules/tools/WP4/T44/results/charts/section/arp/arp';
import Divider from 'app/shared/components/divider/divider';
import FlSection from 'app/modules/tools/WP4/T44/results/charts/section/fl/fl';
import StrSection from 'app/modules/tools/WP4/T44/results/charts/section/str/str';
import CurtSection from 'app/modules/tools/WP4/T44/results/charts/section/curt/curt';

const AllResults = (props: any) => {
  return (
    <>
      <ArpSection location={props.location} />
      <Divider />
      <FlSection location={props.location} />
      <Divider />
      <StrSection location={props.location} />
      <Divider />
      <CurtSection location={props.location} />
    </>
  );
};

export default AllResults;
