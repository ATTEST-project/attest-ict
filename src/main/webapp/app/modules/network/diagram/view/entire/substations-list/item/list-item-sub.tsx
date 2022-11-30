import React from 'react';
import './list-item-sub.scss';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';
import { addHighlight, getElementCoordinates, removeHighlight } from 'app/modules/network/diagram/view/entire/util';
import { useAppDispatch } from 'app/config/store';
import { chooseSubstation } from 'app/shared/reducers/choose-substation';

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
}));

const ListItemSub = props => {
  const classes = useStyles();

  const { index, data, style } = props;

  const dispatch = useAppDispatch();

  const focusOnSubstation = event => {
    /* const pos = getElementCoordinates(event.target.innerText);
    const { setTransform } = data.refTC.current.context.dispatch;
    const previousScale = data.refTC.current.context.state.previousScale;
    window.requestAnimationFrame(() => {
      setTransform(pos.x, pos.y, previousScale, 200, 'easeOut');
    }); */
    dispatch(chooseSubstation(event.target.innerText));
  };

  const highlightSVGSubstation = event => {
    /* eslint-disable-next-line no-console */
    console.log('Substation selected: ', event.target.innerText);
    // to be fixed!
    focusOnSubstation(event);
    setTimeout(() => {
      addHighlight(event.target.innerText);
      setTimeout(() => removeHighlight(), 1000);
    }, 500);
  };

  const handleItemClick = (event, index) => {
    data.changeIndex(index);
    highlightSVGSubstation(event);
  };

  return (
    <ListItem
      button
      classes={{ root: classes.listItemRoot, selected: classes.listItemSelected }}
      style={style}
      key={index}
      selected={data.selectedIndex === index}
      onClick={event => handleItemClick(event, index)}
    >
      <ListItemText primary={data.ids[index]} />
    </ListItem>
  );
};

export default ListItemSub;
