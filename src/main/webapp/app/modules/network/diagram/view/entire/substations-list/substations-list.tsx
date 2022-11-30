import React from 'react';
import './substations-list.scss';
import AutoSizer from 'react-virtualized-auto-sizer';
import { FixedSizeList } from 'react-window';
import ListItemSub from 'app/modules/network/diagram/view/entire/substations-list/item/list-item-sub';
import { makeStyles } from '@material-ui/core/styles';
import { NetworkProps } from 'app/modules/network/diagram/view/entire/network-sld-entire';
import { TransformComponent } from 'react-zoom-pan-pinch';

const useStyles = makeStyles(theme => ({
  list: {
    scrollBehavior: 'smooth',
  },
}));

interface SubstationsListProps {
  network: NetworkProps;
  refTC: React.Ref<TransformComponent>;
  openMenu: boolean;
}

const SubstationsList = (props: SubstationsListProps) => {
  const classes = useStyles();

  const { network, refTC, openMenu } = props;

  const listRef = React.useRef();
  const [selectedIndex, setSelectedIndex] = React.useState(null);
  const changeIndex = index => setSelectedIndex(index);

  React.useEffect(() => {
    if (!openMenu) {
      changeIndex(null);
    }
  }, [openMenu]);

  const resize = () => {
    /* eslint-disable-next-line no-console */
    console.log('Resize!');
  };

  return (
    <AutoSizer onResize={resize}>
      {({ width, height }) => (
        <FixedSizeList
          id="list"
          className={classes.list}
          ref={listRef}
          height={height - 300}
          width={width}
          itemSize={46}
          itemCount={network.ids.length}
          itemData={{ ids: network.ids, selectedIndex, changeIndex, refTC }}
        >
          {ListItemSub}
        </FixedSizeList>
      )}
    </AutoSizer>
  );
};

export default SubstationsList;
