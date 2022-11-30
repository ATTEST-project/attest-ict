import React from 'react';
import './grid-list-sub.scss';
import { addNavigationArrow } from 'app/modules/network/diagram/view/substation/util';
import SVGHandler from 'app/modules/network/diagram/view/substation/svg-handler/svgdotjs/svg-handler';
import Tooltip from '@material-ui/core/Tooltip';
import IconButton from '@material-ui/core/IconButton';
import FullscreenIcon from '@material-ui/icons/Fullscreen';
import { Dialog } from '@material-ui/core';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import { makeStyles } from '@material-ui/core/styles';
import { TransitionProps } from '@material-ui/core/transitions';
import Slide from '@material-ui/core/Slide';
import { FixedSizeList } from 'react-window';
import { addGenLoadRects } from 'app/modules/network/diagram/view/entire/gens_loads_utils';
import GenLoadModal from 'app/modules/network/diagram/view/entire/gen-load-modal/gen-load-modal';
import { INetwork } from 'app/shared/model/network.model';

interface NetworkProps {
  [id: string]: {
    svg: string;
    metadata: string;
  };
}

const Transition = React.forwardRef(function Transition(
  props: TransitionProps & { children?: React.ReactElement },
  ref: React.Ref<unknown>
) {
  return <Slide direction="up" ref={ref} {...props} />;
});

const useStyles = makeStyles(theme => ({
  appBar: {
    position: 'relative',
    backgroundColor: '#10272F',
    textAlign: 'center',
  },
  divZoomButtonsGrid: {
    position: 'absolute',
    top: 0,
    right: 0,
  },
  title: {
    marginLeft: theme.spacing(2),
    flex: 1,
  },
  svgTransition: {
    opacity: 0,
  },
}));

interface GridListSubProps {
  index: number;
  svg: any;
  subId: string;
  style?: any;
  network: NetworkProps;
  networkEntity: INetwork;
  columnCount: number;
  handleReRender: () => void;
}

const GridListSub = React.forwardRef((props: GridListSubProps, ref: FixedSizeList) => {
  const { index, svg, subId, style, network, networkEntity, columnCount, handleReRender } = props;

  const classes = useStyles();

  const [openOverlay, setOpenOverlay] = React.useState(false);
  const [newSvg, setNewSvg] = React.useState(svg);

  const [openInFullscreen, setOpenInFullscreen] = React.useState(false);
  const toggleFullscreen = () => setOpenInFullscreen(!openInFullscreen);
  const [rect, setRect] = React.useState({
    id: '',
    component: '',
  });

  /* React.useEffect(() => {
    if (openInFullscreen) {
      return;
    }
    setNewSvg(svg);
  }, [svg, openInFullscreen]); */

  const clickFocus = () => {
    setOpenOverlay(!openOverlay);
  };

  const handleClose = () => {
    setOpenOverlay(false);
    handleReRender();
    /* Object.entries(network).forEach(([k, v]) => {
      if (document.getElementById(k) && document.getElementById(k).innerHTML === '') {
        // document.getElementById(k).innerHTML = v.svg;
        setNewSvg(v)
      }
    }); */
    // setNewSvg(svg);
  };

  /* const setListItemIndex = (event, newIndex) => {
    if (openMenu && selectedIndex !== newIndex) {
      ref.current.scrollToItem(newIndex, 'start');
      setSelectedIndexCallback(newIndex);
    }
  }; */

  React.useEffect(() => {
    if (!openOverlay) {
      return;
    }
    const drawArrows = newSvg => {
      const arr = addNavigationArrow(network, newSvg);
      arr.forEach(function (element, index) {
        document.getElementById('arrow_' + index).addEventListener('click', function () {
          /* eslint-disable-next-line no-console */
          console.log(element);
          const newSubId = element[0];
          const currentSubIdOverlay = subId + '_overlay';
          if (document.getElementById(newSubId)) {
            document.getElementById(newSubId).innerHTML = '';
          }
          document.getElementById(currentSubIdOverlay).style.transition = 'opacity .5s ease';
          document.getElementById(currentSubIdOverlay).classList.add(classes.svgTransition);
          setTimeout(function () {
            // document.getElementById(currentSubIdOverlay).innerHTML = element[1].svg;
            document.getElementById(currentSubIdOverlay).classList.remove(classes.svgTransition);
            document.getElementById(subId + '_title').innerText = 'Substation: ' + newSubId;
            setNewSvg(element[1]);
          }, 1000);
        });
      });
      const rects = addGenLoadRects(network, newSvg);
      rects.forEach((element, index) => {
        element.group.addEventListener('click', function () {
          const rectId = element.group.getAttribute('id');
          const equipmentId = element.element.equipmentId;
          /* eslint-disable-next-line no-console */
          console.log('ID of element clicked: ', rectId);
          setRect({
            id: rectId,
            component: equipmentId,
          });
          setOpenInFullscreen(true);
        });
      });
    };

    const divOverlay = document.getElementById(subId + '_overlay');
    if (openOverlay && divOverlay) {
      drawArrows(newSvg);
    } else if (openOverlay && !divOverlay) {
      let observer = new MutationObserver(() => {
        const divElement = document.getElementById(subId + '_overlay');
        if (divElement) {
          if (observer) {
            observer.disconnect();
            observer = null;
          }
          /* eslint-disable-next-line no-console */
          console.log(divElement);
          drawArrows(newSvg);
        }
      });
      observer.observe(document, { subtree: true, childList: true });
    }
  }, [subId, newSvg, openOverlay]);

  /* React.useEffect(() => {
    setTimeout(() => {
      const rects = addGenLoadRects(network, newSvg);
      rects.forEach((element, index) => {
        element.group.addEventListener('click', function () {
          const rectId = element.group.getAttribute('id');
          const equipmentId = element.element.equipmentId;
          /!* eslint-disable-next-line no-console *!/
          console.log('ID of element clicked: ', rectId);
          setRect({
            id: rectId,
            component: equipmentId,
          });
          setOpenInFullscreen(true);
        });
      });
    }, 500)
  }, [newSvg]) */

  return (
    <>
      <div id={subId + '_parent'} style={{ height: '100%' }}>
        <div className="grid-item-title">{subId}</div>
        {!openOverlay && <SVGHandler substation={newSvg} divId={subId} columnCount={columnCount} />}
      </div>
      <div className={classes.divZoomButtonsGrid}>
        <Tooltip title="Fullscreen" aria-label="fullscreen">
          <IconButton color="inherit" aria-label="refresh list" component="span" onClick={clickFocus}>
            <FullscreenIcon />
          </IconButton>
        </Tooltip>
        <Dialog fullScreen open={openOverlay} TransitionComponent={Transition}>
          <AppBar className={classes.appBar}>
            <Toolbar>
              <Typography id={subId + '_title'} variant="h6" className={classes.title}>
                {'Substation: ' + subId}
              </Typography>
              <Tooltip title="Close" aria-label="close">
                <IconButton color="inherit" aria-label="close" component="span" onClick={handleClose}>
                  <CloseIcon />
                </IconButton>
              </Tooltip>
            </Toolbar>
          </AppBar>
          {openOverlay && <SVGHandler substation={newSvg} divId={subId + '_overlay'} fullscreen={true} />}
          {openOverlay && (
            <GenLoadModal
              rect={rect}
              openInFullscreen={openInFullscreen}
              toggleFullscreen={toggleFullscreen}
              networkEntity={networkEntity}
            />
          )}
        </Dialog>
      </div>
    </>
  );
});

export default GridListSub;
