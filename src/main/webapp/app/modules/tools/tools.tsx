import React from 'react';
import './tools.scss';
import carouselImage1 from '../../../content/images/carousel_img_1.png';
import carouselImage2 from '../../../content/images/carousel_img_2.png';
import carouselImage3 from '../../../content/images/carousel_img_3.png';
import carouselImage4 from '../../../content/images/carousel_img_4.png';
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

const images = [
  {
    workPackage: 'WP2',
    altText: 'Image 1',
    caption: 'WP2',
    key: 1,
    src: carouselImage4,
  },
  {
    workPackage: 'WP3',
    altText: 'Image 2',
    caption: 'WP3 - Optimal Planning of Power System',
    key: 2,
    src: carouselImage1,
  },
  {
    workPackage: 'WP4',
    altText: 'Image 3',
    caption: 'WP4 - Optimal Operation of Power System',
    key: 3,
    src: carouselImage2,
  },
  {
    workPackage: 'WP5',
    altText: 'Image 4',
    caption: 'WP5 - Optimal Asset Management',
    key: 4,
    src: carouselImage3,
  },
];

const Tools = props => {
  const dispatch = useAppDispatch();
  const lastWP = useAppSelector(state => state.toolConfig.workPackage);

  const lastNetworkSelected: INetwork = JSON.parse(sessionStorage.getItem('network')) || null;
  const [wpSelected, setWpSelected] = React.useState(toolsInfo[lastWP] || null);
  const [network, setNetwork] = React.useState<INetwork>(lastNetworkSelected);
  const setNetworkCallback = (n: INetwork[]) => setNetwork(n[0]);

  const [mouseDown, setMouseDown] = React.useState<boolean>(false);
  const initialButtonsStatus = React.useMemo(() => images.map(() => false), []);
  const [buttonsStatus, setButtonsStatus] = React.useState<boolean[]>(initialButtonsStatus);

  const handleItemClicked = (wp: string) => {
    const wpInfo = toolsInfo[wp];
    setWpSelected([...wpInfo]);
    dispatch(storeWP(wp));
  };

  React.useEffect(() => {
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
    currentStatus[index] = true;
    setButtonsStatus(currentStatus);
  };

  return (
    <>
      <Carousel className="tools-carousel" itemPadding={[10, 50]} isRTL={false} itemsToShow={3}>
        {images.map((image, index) => (
          <div key={index} onMouseDown={onMouseDownEvent} onMouseUp={onMouseUpEvent} onMouseMove={() => onMouseMoveEvent(index)}>
            <Button disabled={buttonsStatus[index]} onClick={() => handleItemClicked(image.workPackage)}>
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
                  isReady={tool.ready}
                  type={tool.type}
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
