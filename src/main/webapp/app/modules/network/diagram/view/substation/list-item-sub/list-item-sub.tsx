import React from 'react';
// import './list-item-sub.scss';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';
import { FixedSizeGrid } from 'react-window';

interface ListItemSubProps {
  index: number;
  data: {
    network: any;
    ref: FixedSizeGrid;
    indexesMap: any;
    selectedIndex: number;
    setSelectedIndexCallback: (index: number) => void;
  };
  style?: object;
}

const useStyles = makeStyles(theme => ({
  listItemRoot: {
    backgroundColor: '#10272F',
    color: 'white',
    '&:hover': {
      backgroundColor: '#dcdcdc',
      color: '#10272F',
    },
  },
  listItemSelected: {
    color: '#10272F',
    '&:hover': {
      backgroundColor: '#dcdcdc !important',
      color: '#10272F',
    },
  },
  svgBG: {
    background: '#dcdcdc',
  },
}));

const ListItemSub = (props: ListItemSubProps) => {
  const { index, data, style } = props;
  const classes = useStyles();

  const focusOnCurrentSVG = (event, index) => {
    // first focus on element
    data.ref.current.scrollToItem({
      rowIndex: data.indexesMap.get(index).x,
      columnIndex: data.indexesMap.get(index).y,
    });

    // then highlight
    setTimeout(function () {
      const subId = event.target.innerText;
      const element = document.getElementById(subId + '_parent');
      const parent = element.parentElement;
      parent.style.transition = 'background-color .5s ease';
      parent.classList.toggle(classes.svgBG);
      setTimeout(function () {
        parent.classList.toggle(classes.svgBG);
        data.setSelectedIndexCallback(index);
      }, 1000);
    }, 1000);
  };

  return (
    <ListItem
      button
      classes={{ root: classes.listItemRoot, selected: classes.listItemSelected }}
      style={style}
      key={index}
      selected={data.selectedIndex === index}
      onClick={event => focusOnCurrentSVG(event, index)}
    >
      <ListItemText primary={data.network[index]} />
    </ListItem>
  );
};

export default ListItemSub;
