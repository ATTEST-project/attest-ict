import React from 'react';
import { Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';

const CellButton = (props: any) => {
  const profile = props.data;
  const headerName = props.colDef.headerName;

  const buttonId = headerName.toLowerCase().includes('chart') ? 'chart-button' : 'download-button';
  // const tooltip = headerName.toLowerCase().includes('chart') ? 'View Chart' : 'Download';
  const icon = headerName.toLowerCase().includes('chart') ? 'chart-bar' : 'file-download';
  const eventType = headerName.toLowerCase().includes('chart') ? 'chart' : 'download';

  const buttonClicked = () => {
    props.context.setProfileCallback(profile, eventType);
  };

  return (
    <Button disabled={eventType === 'chart'} id={buttonId + '-' + props.node.rowIndex} onClick={() => buttonClicked()}>
      <FontAwesomeIcon icon={icon} />
      {/* <CustomTooltip target={buttonId + '-' + props.node.rowIndex} tooltip={tooltip} /> */}
    </Button>
  );
};

export default CellButton;
