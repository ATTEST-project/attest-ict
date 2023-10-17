import React from 'react';
import T4144Config from 'app/shared/components/T41-44/config/T41-44-config';
import Parameters from 'app/modules/tools/WP4/T44/parameters/parameters';
import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import AdditionalNetwork from 'app/modules/tools/WP4/T44/additional-network/additional-network';

const T44 = (props: any) => {
  return (
    <>
      <T4144Config
        title={TOOLS_INFO.T44_AS_DAY_HEAD_TX.description}
        toolName={TOOLS_INFO.T44_AS_DAY_HEAD_TX.name}
        resultsPath={TOOLS_INFO.T44_AS_DAY_HEAD_TX.path + '/results'}
        additionalNetwork={<AdditionalNetwork />}
        parameters={<Parameters />}
        {...props}
      />
    </>
  );
};

export default T44;
