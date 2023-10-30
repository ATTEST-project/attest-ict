import React from 'react';
import ParametersSection from 'app/modules/tools/WP4/T41/tractability-tool/parameters/parameters';
import { TOOLS_INFO, TOOLS_NAMES } from 'app/modules/tools/info/tools-names';
import T4144Config from 'app/shared/components/T41-44/config/T41-44-config';
import toolsInfo from 'app/modules/tools/info/tools-info';

const T41TractabilityTool = props => {
  const toolNum = toolsInfo.WP4[1].name;
  return (
    <>
      <T4144Config
        toolNum={toolNum}
        title={TOOLS_INFO.T41_TRACTABILITY.description}
        toolName={TOOLS_INFO.T41_TRACTABILITY.name}
        resultsPath={TOOLS_INFO.T41_TRACTABILITY.path + '/results'}
        parameters={<ParametersSection />}
        {...props}
      />
    </>
  );
};

export default T41TractabilityTool;
