import React from 'react';
import T45Config from 'app/modules/tools/WP4/T45/config/T45-config';

import ParametersSection from 'app/modules/tools/WP4/T45/parameters/parameters';
import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';

const T45 = (props: any) => {
  return (
    <>
      <T45Config
        toolDescription={TOOLS_INFO.T45_AS_REAL_TIME_TX.description}
        toolName={TOOLS_INFO.T45_AS_REAL_TIME_TX.name}
        resultsPath={TOOLS_INFO.T45_AS_REAL_TIME_TX.path + '/results'}
        parameters={<ParametersSection />}
        {...props}
      />
    </>
  );
};

export default T45;
