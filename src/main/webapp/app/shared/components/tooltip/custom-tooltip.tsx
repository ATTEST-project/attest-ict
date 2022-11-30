import React, { useState } from 'react';
import { Tooltip } from 'reactstrap';

export const CustomTooltip = (props: { target: string; tooltip: string }) => {
  const { target, tooltip } = props;

  const [tooltipOpen, setTooltipOpen] = useState<boolean>(false);

  return (
    <Tooltip placement="bottom" isOpen={tooltipOpen} target={target} toggle={() => setTooltipOpen(!tooltipOpen)}>
      {tooltip}
    </Tooltip>
  );
};

export default CustomTooltip;
