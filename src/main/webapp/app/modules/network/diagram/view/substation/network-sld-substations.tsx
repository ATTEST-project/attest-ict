import React from 'react';
import { FixedSizeGrid } from 'react-window';
import AutoSizer from 'react-virtualized-auto-sizer';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import { Button } from '@material-ui/core';
import { FixedSizeList } from 'react-window';
import CircularProgress from '@material-ui/core/CircularProgress';
import IconButton from '@material-ui/core/IconButton';
import Drawer from '@material-ui/core/Drawer';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import MenuIcon from '@material-ui/icons/Menu';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import clsx from 'clsx';
import Grid from '@material-ui/core/Grid';
import Alert, { Color } from '@material-ui/lab/Alert';
import { getSvgInfos } from 'app/modules/network/diagram/view/substation/util';
import './network-sld-substations.scss';
import { RouteComponentProps } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { generateSubstationsSLDByNetworkId } from 'app/entities/network/network-sld-substations.reducer';
import GridListSub from 'app/modules/network/diagram/view/substation/grid-list-sub/grid-list-sub';
import ListItemSub from 'app/modules/network/diagram/view/substation/list-item-sub/list-item-sub';
import SelectSub from 'app/modules/network/diagram/view/substation/select-sub/select-sub';

const drawerWidth = 240;

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
    marginLeft: 0,
  },
  contentShift: {
    transition: theme.transitions.create('margin', {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
    marginLeft: 0,
  },
  list: {
    scrollBehavior: 'smooth',
  },
}));

interface NetworkProps {
  [id: string]: {
    svg: string;
    metadata: string;
  };
}

function SLDGrid(props: RouteComponentProps<{ url: string }>) {
  const dispatch = useAppDispatch();
  const classes = useStyles();
  const theme = useTheme();

  const networkEntity = useAppSelector(state => state.network.entity);

  const [network, setNetwork] = React.useState<NetworkProps>({});
  const [rowHeight, setRowHeight] = React.useState(250);
  const [columnCount, setColumnCount] = React.useState(3);
  const [rowCount, setRowCount] = React.useState(1);
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [openDropdown, setOpenDropdown] = React.useState(false);
  const [viewGrid, setViewGrid] = React.useState(false);
  const [indexesMap, setIndexesMap] = React.useState(new Map());

  const gridRef = React.useRef<FixedSizeGrid>();
  const listRef = React.useRef<FixedSizeList>();

  const [reRender, setReRender] = React.useState(new Date());

  const nItems = Object.keys(network).length;

  const networkSLDEntity = useAppSelector(state => state.networkSubstationsSLD.entity);
  const loadingSLD = useAppSelector(state => state.networkSubstationsSLD.loading);

  const [errorMessage, setErrorMessage] = React.useState(null);

  React.useEffect(() => {
    if (Object.keys(networkEntity).length === 0) {
      props.history.goBack();
      return;
    }
    setViewGrid(true);
    dispatch(generateSubstationsSLDByNetworkId(networkEntity.id))
      .unwrap()
      .then(res => res)
      .catch(err => {
        const response = err.response.data;
        setErrorMessage(response.message);
        setViewGrid(false);
      });
  }, []);

  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    console.log('NetworkSLDEntity: ', networkSLDEntity);
    if (Object.keys(networkSLDEntity).length === 0) {
      return;
    }
    const svgs = getSvgInfos(networkSLDEntity);
    setNetwork(svgs);
  }, [networkSLDEntity]);

  React.useEffect(() => {
    switch (columnCount) {
      case 1:
        setRowHeight(600);
        break;
      case 2:
        setRowHeight(400);
        break;
      case 3:
      case 4:
        setRowHeight(250);
        break;
      default:
        setRowHeight(250);
    }

    if (nItems % columnCount === 0) {
      setRowCount(Math.floor(nItems / columnCount));
    } else {
      setRowCount(Math.floor(nItems / columnCount) + 1);
    }

    for (let i = 0; i < rowCount; ++i) {
      for (let j = 0; j < columnCount; ++j) {
        const index = i * columnCount + j;
        setIndexesMap(prev => new Map([...prev, [index, { x: i, y: j }]]));
      }
    }
  }, [nItems, rowCount, columnCount]);

  const resize = () => {
    /* eslint-disable-next-line no-console */
    console.log('resize!');
  };

  const handleOpenDropdown = event => {
    setAnchorEl(event.currentTarget);
    setOpenDropdown(true);
  };

  const handleCloseDropdown = () => {
    setOpenDropdown(false);
    setAnchorEl(null);
  };

  const handleSelectDropdown = event => {
    setColumnCount(Number(event.target.innerText.charAt(0)));
    handleCloseDropdown();
  };

  const handleReRender = () => {
    setReRender(new Date());
  };

  return (
    <div>
      <div className={classes.divDrawerRoot}>
        <div style={{ flexGrow: 1 }}>
          {viewGrid ? (
            <div>
              <Button
                aria-controls="simple-menu"
                aria-haspopup="true"
                variant="outlined"
                style={{ marginBottom: 10 }}
                onClick={handleOpenDropdown}
              >
                view by
              </Button>
              <Menu
                id="simple-menu"
                anchorEl={anchorEl}
                keepMounted
                open={openDropdown}
                getContentAnchorEl={null}
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'center',
                }}
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'center',
                }}
                onClose={handleCloseDropdown}
              >
                <MenuItem style={{ width: 100 }} onClick={handleSelectDropdown}>
                  1 column
                </MenuItem>
                <MenuItem onClick={handleSelectDropdown}>2 columns</MenuItem>
                <MenuItem onClick={handleSelectDropdown}>3 columns</MenuItem>
                <MenuItem onClick={handleSelectDropdown}>4 columns</MenuItem>
              </Menu>
              <SelectSub network={Object.keys(network)} ref={gridRef} indexesMap={indexesMap} />
              <div
                style={{
                  height: 'calc(100vh - 220px)',
                  display: 'flex',
                  flexDirection: 'column',
                  flex: 1,
                  backgroundColor: 'white',
                }}
              >
                {reRender && nItems > 0 && !loadingSLD ? (
                  <AutoSizer onResize={resize}>
                    {({ width, height }) => (
                      <FixedSizeGrid
                        ref={gridRef}
                        className={classes.list}
                        style={{ overflowX: 'hidden' }}
                        height={height}
                        rowCount={rowCount}
                        rowHeight={rowHeight}
                        width={width}
                        columnCount={columnCount}
                        columnWidth={width / columnCount}
                      >
                        {({ columnIndex, rowIndex, style }) => (
                          <div style={{ ...style, border: '1px solid black' }}>
                            {Object.keys(network)[rowIndex * columnCount + columnIndex] ? (
                              <GridListSub
                                svg={Object.values(network)[rowIndex * columnCount + columnIndex]}
                                subId={Object.keys(network)[rowIndex * columnCount + columnIndex]}
                                index={rowIndex * columnCount + columnIndex}
                                key={rowIndex * columnCount + columnIndex}
                                columnCount={columnCount}
                                network={network}
                                networkEntity={networkEntity}
                                ref={listRef}
                                handleReRender={handleReRender}
                              />
                            ) : (
                              <div />
                            )}
                          </div>
                        )}
                      </FixedSizeGrid>
                    )}
                  </AutoSizer>
                ) : (
                  <Grid container direction="column" alignItems="center" justifyContent="center" style={{ minHeight: '80vh' }}>
                    <CircularProgress color="inherit" />
                  </Grid>
                )}
              </div>
            </div>
          ) : (
            <Alert severity="error">{errorMessage}</Alert>
          )}
        </div>
      </div>
    </div>
  );
}

export default SLDGrid;
