import React from 'react';
import Button from '@material-ui/core/Button';
import { Link } from 'react-router-dom';

interface Props {
  name: string;
  count?: string;
  src: string;
  alt: string;
}

const CarouselItem = (props: Props) => {
  const str = props.name;

  const name1 = str.split(' ').slice(0, 2).join(' ');
  const name2 = str.split(' ').slice(2, Number(props.count)).join(' ');

  return (
    <div className="tool-carousel-item">
      <Button className="tools-carousel-button" color="inherit" variant="text" component={Link} to="/wip">
        <div className="tools-carousel-text">
          <p> {name1} </p>
          <br />
          <p>{name2}</p>
        </div>
        <img className="tools-carousel-image" src={props.src} alt={props.alt} />
      </Button>
    </div>
  );
};

export default CarouselItem;
