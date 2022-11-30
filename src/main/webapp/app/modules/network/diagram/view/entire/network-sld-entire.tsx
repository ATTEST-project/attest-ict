import React from 'react';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import CircularProgress from '@material-ui/core/CircularProgress';
import IconButton from '@material-ui/core/IconButton';
import Drawer from '@material-ui/core/Drawer';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import MenuIcon from '@material-ui/icons/Menu';
import clsx from 'clsx';
import Grid from '@material-ui/core/Grid';
import Alert from '@material-ui/lab/Alert';
import './network-sld-entire.scss';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { generateEntireSLDById } from 'app/entities/network/network-sld-entire.reducer';
import { RouteComponentProps } from 'react-router-dom';
import { getSvgInfos } from 'app/modules/network/diagram/view/entire/util';
import { addGenLoadRects } from 'app/modules/network/diagram/view/entire/gens_loads_utils';
import SubstationsList from 'app/modules/network/diagram/view/entire/substations-list/substations-list';
import SVGHandler from 'app/modules/network/diagram/view/entire/svg-handler/svgdotjs/svg-handler';
import GenLoadPopover from 'app/modules/network/diagram/view/entire/gen-load-popover/gen-load-popover';
import GenLoadModal from 'app/modules/network/diagram/view/entire/gen-load-modal/gen-load-modal';
import { TransformComponent } from 'react-zoom-pan-pinch';
import { getEntity } from 'app/entities/network/network.reducer';

const useStyles = makeStyles(theme => ({
  divDrawerRoot: {
    display: 'flex',
  },
  menuButton: {
    padding: 10,
    margin: 2,
    width: '5%',
    top: '-4px',
  },
  hide: {
    display: 'none',
  },
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
  },
  drawerPaper: {
    width: drawerWidth,
    top: 'unset',
  },
  drawerHeader: {
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
  },
  content: {
    flexGrow: 1,
    transition: theme.transitions.create('margin', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    // marginLeft: -drawerWidth,
  },
  contentShift: {
    transition: theme.transitions.create('margin', {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
    marginLeft: 0,
  },
  divZoomButtons: {
    position: 'absolute',
    top: 250,
    right: 50,
  },
}));

export interface NetworkProps {
  ids: string[];
  svg: {
    metadata: string;
    svg: string;
  };
}

const drawerWidth = 240;

function SLDEntire(props) {
  const dispatch = useAppDispatch();
  const classes = useStyles();

  const networkEntity = props.network || useAppSelector(state => state.network.entity);
  const loadingEntity = useAppSelector(state => state.network.loading);

  const [network, setNetwork] = React.useState<NetworkProps>({
    ids: [],
    svg: {
      metadata: '',
      svg: '',
    },
  });
  const [viewGrid, setViewGrid] = React.useState(false);

  const networkSLDEntity = useAppSelector(state => state.networkEntireSLD.entity);
  const loadingSLD = useAppSelector(state => state.networkEntireSLD.loading);

  const [rect, setRect] = React.useState({
    id: '',
    component: '',
  });
  const [openPopup, setOpenPopup] = React.useState(false);
  const toggle = () => setOpenPopup(!openPopup);
  const [errorMessage, setErrorMessage] = React.useState(null);

  const [openInFullscreen, setOpenInFullscreen] = React.useState(false);
  const toggleFullscreen = () => setOpenInFullscreen(!openInFullscreen);

  React.useEffect(() => {
    if (Object.keys(networkEntity).length === 0) {
      props.history?.goBack();
      return;
    }
    setViewGrid(true);
    dispatch(generateEntireSLDById(networkEntity.id))
      .unwrap()
      .then(res => {
        const svg = getSvgInfos(res.data);
        setNetwork(svg);
      })
      .catch(err => {
        const response = err.response.data;
        setErrorMessage(response.message);
        setViewGrid(false);
        setNetwork({
          ids: [],
          svg: {
            metadata: '',
            svg: '',
          },
        });
      });
  }, []);

  React.useEffect(() => {
    if (network.ids.length === 0) {
      return;
    }
    setTimeout(() => {
      if (network.svg.svg && !loadingSLD) {
        const rects = addGenLoadRects(network, network.svg);
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
          });
        });
      }
    }, 500);
  }, [network, loadingSLD]);

  React.useEffect(() => {
    if (rect.id === '') {
      return;
    }
    /* eslint-disable-next-line no-console */
    console.log('Rect: ', rect);
    toggle();
  }, [rect]);

  React.useEffect(() => {
    if (!openPopup && !openInFullscreen) {
      setRect({
        id: '',
        component: '',
      });
    }
  }, [openPopup, openInFullscreen]);

  const openFullscreen = () => {
    setOpenInFullscreen(true);
    setOpenPopup(false);
  };

  return (
    <div id="svg-main-div">
      <div className={classes.divDrawerRoot}>
        <div style={{ flexGrow: 1 }}>
          {viewGrid ? (
            <div>
              <div id={props.containerId || 'svg-container'} className={!props.containerId ? 'network-svg-div' : null}>
                {network.ids.length > 0 && !loadingSLD ? (
                  <SVGHandler network={network} containerId={props.containerId || ''} />
                ) : (
                  <Grid container direction="column" alignItems="center" justifyContent="center" style={{ minHeight: '80vh' }}>
                    <CircularProgress color="primary" />
                  </Grid>
                )}
              </div>
            </div>
          ) : (
            <Alert severity="error">{errorMessage}</Alert>
          )}
          {rect.id && (
            <GenLoadPopover
              rect={rect}
              openPopup={openPopup}
              toggle={toggle}
              openFullscreen={openFullscreen}
              networkEntity={networkEntity}
              svgContainerId={props.containerId}
            />
          )}
          <GenLoadModal rect={rect} openInFullscreen={openInFullscreen} toggleFullscreen={toggleFullscreen} networkEntity={networkEntity} />
        </div>
      </div>
    </div>
  );
}

export default SLDEntire;
