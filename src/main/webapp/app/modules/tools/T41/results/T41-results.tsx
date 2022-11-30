import React from 'react';
import { fdr1 } from './fdr1';
import { fdr2 } from './fdr2';
import { fdr3 } from './fdr3';
import { fdr4 } from './fdr4';
import { vsc } from './vsc';
import { FixedSizeGrid } from 'react-window';
import AutoSizer from 'react-virtualized-auto-sizer';
import { makeStyles } from '@material-ui/core/styles';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import Button from '@material-ui/core/Button';

const useStyles = makeStyles({
  list: {
    scrollBehavior: 'smooth',
  },
});

const rowHeight = 250;
const columnCount = 3;

function T41Results() {
  const classes = useStyles();

  const defaultNetworkName = 'TestNetwork1';

  const [rowCount, setRowCount] = React.useState(1);
  const [items, setItems] = React.useState(fdr1);
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [openDropdown, setOpenDropdown] = React.useState(false);

  const nItems = Object.keys(items).length;

  React.useEffect(() => {
    if (nItems % columnCount === 0) {
      setRowCount(Math.floor(nItems / columnCount));
    } else {
      setRowCount(Math.floor(nItems / columnCount) + 1);
    }

    /* for (let i = 0; i < rowCount; ++i) {
        for (let j = 0; j < columnCount; ++j) {
            let index = i * columnCount + j;
            setIndexesMap(prev => new Map([...prev, [index, { x: i, y: j }]]));
        }
    } */
  }, [nItems, rowCount]);

  const handleOpenDropdown = event => {
    setAnchorEl(event.currentTarget);
    setOpenDropdown(true);
  };

  const handleCloseDropdown = () => {
    setOpenDropdown(false);
    setAnchorEl(null);
  };

  const handleSelectDropdown = event => {
    setObjectItems(event.target.innerText);
    handleCloseDropdown();
  };

  const setObjectItems = name => {
    switch (name) {
      case 'fdr1':
        setItems(fdr1);
        break;
      case 'fdr2':
        setItems(fdr2);
        break;
      case 'fdr3':
        setItems(fdr3);
        break;
      case 'fdr4':
        setItems(fdr4);
        break;
      case 'vsc':
        setItems(vsc);
        break;
      default:
        setItems(fdr1);
    }
  };

  return (
    <div>
      <div className="container-results-41ancillary">
        <h1>{"Results of Ancillary Services Tool for '" + defaultNetworkName + "' network"}</h1>
        <Button
          aria-controls="simple-menu"
          aria-haspopup="true"
          variant="outlined"
          style={{ marginBottom: 10, marginLeft: 20, width: 100 }}
          onClick={handleOpenDropdown}
        >
          {'choose scenario'}
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
            fdr1
          </MenuItem>
          <MenuItem onClick={handleSelectDropdown}>fdr2</MenuItem>
          <MenuItem onClick={handleSelectDropdown}>fdr3</MenuItem>
          <MenuItem onClick={handleSelectDropdown}>fdr4</MenuItem>
          <MenuItem onClick={handleSelectDropdown}>vsc</MenuItem>
        </Menu>
        <div
          style={{
            height: 'calc(100vh - 260px)',
            display: 'flex',
            flexDirection: 'column',
            flex: 1,
            backgroundColor: 'white',
          }}
        >
          <AutoSizer>
            {({ width, height }) => (
              <FixedSizeGrid
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
                    {Object.keys(items)[rowIndex * columnCount + columnIndex] ? (
                      <img
                        src={Object.values(items)[rowIndex * columnCount + columnIndex]}
                        alt={'result'}
                        style={{ height: '100%', width: '100%', objectFit: 'contain' }}
                      ></img>
                    ) : (
                      <div />
                    )}
                  </div>
                )}
              </FixedSizeGrid>
            )}
          </AutoSizer>
        </div>
      </div>
    </div>
  );
}
export default T41Results;
