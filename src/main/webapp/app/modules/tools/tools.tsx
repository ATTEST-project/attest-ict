import React from 'react';
import './tools.scss';

import Carousel from 'react-elastic-carousel';
import { Button, CardDeck } from 'reactstrap';
import toolsInfo from './info/tools-info';
import ToolCard from 'app/shared/components/tool-card/tool-card';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import NetworkSearch from 'app/shared/components/network-search/network-search';
import { INetwork } from 'app/shared/model/network.model';
import Divider from 'app/shared/components/divider/divider';
import getStore, { useAppDispatch, useAppSelector } from 'app/config/store';
import { useDispatch } from 'react-redux';
import { storeWP } from 'app/shared/reducers/tool-config';
import { WP_IMAGES } from 'app/shared/util/wp-image-constant';

const Tools = props => {
  const dispatch = useAppDispatch();
  const lastWP = useAppSelector(state => state.toolConfig.workPackage);

  const lastNetworkSelected: INetwork = JSON.parse(sessionStorage.getItem('network')) || null;
  const [wpSelected, setWpSelected] = React.useState(toolsInfo[lastWP] || null);
  const [network, setNetwork] = React.useState<INetwork>(lastNetworkSelected);
  const setNetworkCallback = (n: INetwork[]) => setNetwork(n[0]);

  const [mouseDown, setMouseDown] = React.useState<boolean>(false);
  const initialButtonsStatus = React.useMemo(() => WP_IMAGES.map(() => false), []);
  const [buttonsStatus, setButtonsStatus] = React.useState<boolean[]>(initialButtonsStatus);

  const handleItemClicked = (wp: string) => {
    /* eslint-disable-next-line no-console */
    console.log('handleItemClicked wp:', wp);

    const wpInfo = toolsInfo[wp];
    /* eslint-disable-next-line no-console */
    console.log('handleItemClicked:', wpInfo);

    setWpSelected([...wpInfo]);
    dispatch(storeWP(wp));
  };

  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    console.log('useEffect network:', network);

    if (!network) {
      return;
    }
    sessionStorage.setItem('network', JSON.stringify(network));
  }, [network]);

  const onMouseDownEvent = () => {
    setMouseDown(true);
  };

  const onMouseUpEvent = () => {
    setMouseDown(false);
    setButtonsStatus(initialButtonsStatus);
  };

  const onMouseMoveEvent = (index: number) => {
    if (!mouseDown) {
      return;
    }

    const currentStatus = [...buttonsStatus];
    /* eslint-disable-next-line no-console */
    console.log('onMouseUpEvent - index:', index);

    /* eslint-disable-next-line no-console */
    console.log('onMouseUpEvent - currentStatus before:', currentStatus);

    currentStatus[index] = true;
    /* eslint-disable-next-line no-console */
    console.log('onMouseUpEvent - currentStatus after:', currentStatus);
    setButtonsStatus(currentStatus);
  };

  /* eslint-disable-next-line no-console */
  console.log('Tools: wpSelected:', wpSelected);

  return (
    <>
      <Carousel className="tools-carousel" itemPadding={[10, 50]} isRTL={false} itemsToShow={3}>
        {WP_IMAGES.map((image, index) => (
          <div key={index} onMouseDown={onMouseDownEvent} onMouseUp={onMouseUpEvent} onMouseMove={() => onMouseMoveEvent(index)}>
            <Button onClick={() => handleItemClicked(image.workPackage)}>
              <div style={{ display: 'flex', justifyContent: 'center', flexDirection: 'column' }}>
                <img alt={image.altText} src={image.src} width={500} height={300} style={{ pointerEvents: 'none' }} />
                <div>{image.caption}</div>
              </div>
            </Button>
          </div>
        ))}
      </Carousel>
      <Divider />
      <div>
        {wpSelected && (
          <>
            <NetworkSearch setRowsSelectedCallback={setNetworkCallback} selectionType={SELECTION_TYPE.SINGLE} />
            <CardDeck>
              {wpSelected.map((tool, index) => (
                <ToolCard
                  key={index}
                  index={index}
                  title={tool.name}
                  text={tool.description}
                  toPage={tool.to}
                  isActive={tool.active}
                  supportedNetworkType={tool.supportedNetworkType}
                  network={network}
                />
              ))}
            </CardDeck>
          </>
        )}
      </div>
    </>
  );
};

export default Tools;
