import React from 'react';
import T42Config from 'app/modules/tools/WP4/T42/config/T42-config';

import ParametersSection from 'app/modules/tools/WP4/T42/parameters/parameters';
import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';

const T42 = (props: any) => {
  return (
    <>
      <T42Config
        title={TOOLS_INFO.T42_AS_REAL_TIME_DX.description}
        toolName={TOOLS_INFO.T42_AS_REAL_TIME_DX.name}
        resultsPath={TOOLS_INFO.T42_AS_REAL_TIME_DX.path + '/results'}
        parameters={<ParametersSection />}
        {...props}
      />
    </>
  );
};

export default T42;
