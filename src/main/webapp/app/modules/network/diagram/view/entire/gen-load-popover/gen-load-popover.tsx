import React from 'react';
import './gen-load-popover.scss';
import { moveChartPopover, moveElement } from './gen-load-popover-util';
import { PopoverBody, PopoverHeader, UncontrolledPopover } from 'reactstrap';
import Button from '@material-ui/core/Button';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ProfileChart from 'app/modules/network/diagram/view/entire/chart/profile-chart';
import { INetwork } from 'app/shared/model/network.model';

interface GenLoadPopoverProps {
  rect: {
    id: string;
    component: string;
  };
  openPopup: boolean;
  toggle: () => void;
  openFullscreen: () => void;
  networkEntity: INetwork;
  svgContainerId?: string;
}

const GenLoadPopover = (props: GenLoadPopoverProps) => {
  const { rect, openPopup, toggle, openFullscreen, networkEntity, svgContainerId } = props;

  const fixedRectId = rect.component.includes('#') ? rect.id.replace('#', '\\#') : rect.id;

  React.useEffect(() => {
    if (!openPopup) {
      return;
    }
    const svgContainer = document.querySelector(svgContainerId ? '#' + svgContainerId : '#svg-container');
    const chartPopover = document.querySelector('.popover');
    const chartPopoverHeader = document.querySelector('.popover-header');
    moveElement(svgContainer, chartPopover, chartPopoverHeader);
  }, [openPopup]);

  return (
    <UncontrolledPopover
      isOpen={openPopup}
      placement="right"
      target={fixedRectId}
      toggle={toggle}
      trigger="focus"
      // style={{ width: 500, height: 400 }}
    >
      <PopoverHeader style={{ maxWidth: '100%' }}>
        <div className="popover-header-content">
          <span>{rect.component}</span>
          <div>
            <Button onClick={openFullscreen}>
              <FontAwesomeIcon icon="expand" style={{ color: 'white' }} />
            </Button>
            <Button onClick={toggle}>
              <FontAwesomeIcon icon="times" style={{ color: 'white' }} />
            </Button>
          </div>
        </div>
      </PopoverHeader>
      <PopoverBody>
        <ProfileChart isFullscreen={false} component={rect.component} network={networkEntity} />
        <div className="resize-icon" />
      </PopoverBody>
    </UncontrolledPopover>
  );
};

export default GenLoadPopover;
