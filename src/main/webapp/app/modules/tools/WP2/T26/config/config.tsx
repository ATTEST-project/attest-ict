import React from 'react';
import Divider from 'app/shared/components/divider/divider';
import NetworkSection from 'app/modules/tools/WP2/T26/config/network/network-section';
import EnergySection from 'app/modules/tools/WP2/T26/config/energy/energy-section';
import SecondarySection from 'app/modules/tools/WP2/T26/config/secondary/secondary-section';
import TertiarySection from 'app/modules/tools/WP2/T26/config/tertiary/tertiary-section';
import ParametersSection from 'app/modules/tools/WP2/T26/config/parameters/parameters-section';

const Config = (props: any) => {
  const { marketNotSelected } = props;
  return (
    <>
      <ParametersSection marketNotSelected={marketNotSelected} />
      <Divider />
      <NetworkSection />
      <Divider />
      <EnergySection />
      <Divider />
      <SecondarySection />
      <Divider />
      <TertiarySection />
    </>
  );
};

export default Config;
