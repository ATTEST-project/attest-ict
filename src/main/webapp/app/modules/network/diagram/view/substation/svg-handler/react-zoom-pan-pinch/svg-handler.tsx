import React from 'react';
import './svg-handler.scss';
import { TransformComponent, TransformWrapper } from 'react-zoom-pan-pinch';

const SVGHandler = (props: any) => {
  const { network, defaultScale, subId, style, index } = props;

  const refTC = React.useRef<TransformComponent>();

  return (
    <TransformWrapper
      defaultScale={defaultScale}
      defaultPositionX={14.5}
      defaultPositionY={9}
      options={{
        // wrapperClass: classes.transformWrapper,
        minPositionX: 14.5,
        minPositionY: 9,
        minScale: defaultScale,
        maxScale: 10,
        limitToWrapper: true,
      }}
    >
      {({ zoomIn, zoomOut, ...rest }) => (
        <div id={subId + '_parent'} style={{ ...style, height: '90%' }}>
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>{subId}</div>
          <TransformComponent key={index} ref={refTC}>
            <div
              id={subId}
              dangerouslySetInnerHTML={{ __html: network.svg }}
              // onClick={event => setListItemIndex(event, index)}
            />
          </TransformComponent>
        </div>
      )}
    </TransformWrapper>
  );
};

export default SVGHandler;
