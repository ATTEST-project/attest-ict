import React from 'react';
import './svg-handler.scss';
import { TransformComponent, TransformWrapper } from 'react-zoom-pan-pinch';
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
  divZoomButtons: {
    position: 'absolute',
    top: 250,
    right: 50,
  },
  divZoomButtonsGrid: {
    position: 'absolute',
    top: 0,
    right: 0,
  },
  zoomButton: {
    fontFamily: '"Lucida Console", Courier, monospace',
    width: '1px',
    display: 'block',
    color: '#10272F',
    borderColor: '#10272F',
    padding: 2,
    margin: 2,
  },
}));

const SVGHandler = props => {
  const classes = useStyles();

  const { network, refTC } = props;

  // const refTC = React.useRef<TransformComponent>();

  function clickReset() {
    const { setTransform } = refTC.current.context.dispatch;
    window.requestAnimationFrame(() => {
      setTransform(14.5, 9, 15, 200, 'easeOut');
    });
  }

  return (
    <TransformWrapper
      defaultScale={15}
      defaultPositionX={14.5}
      defaultPositionY={9}
      options={{
        // wrapperClass: classes.transformWrapper,
        minPositionX: 14.5,
        minPositionY: 9,
        minScale: 2,
        maxScale: 1000,
        limitToWrapper: true,
      }}
      pan={{
        velocity: false,
      }}
      wheel={{
        step: 200,
      }}
    >
      {({ zoomIn, zoomOut, ...rest }) => (
        <div style={{ height: '100%' }}>
          <TransformComponent ref={refTC}>
            <div id="network_svg" dangerouslySetInnerHTML={{ __html: network.svg.svg }} />
          </TransformComponent>
          <div className={classes.divZoomButtons}>
            <Button variant="outlined" className={classes.zoomButton} onClick={zoomIn}>
              +
            </Button>
            <Button variant="outlined" className={classes.zoomButton} onClick={zoomOut}>
              -
            </Button>
            <Button variant="outlined" className={classes.zoomButton} onClick={clickReset}>
              reset
            </Button>
          </div>
        </div>
      )}
    </TransformWrapper>
  );
};

export default SVGHandler;
