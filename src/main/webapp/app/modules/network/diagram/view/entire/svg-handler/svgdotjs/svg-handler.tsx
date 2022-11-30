import React from 'react';
import './svg-handler.scss';
import { Box, Point, Svg, SVG } from '@svgdotjs/svg.js';
import '@svgdotjs/svg.panzoom.js';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { addHighlight, getElementCoordinates, removeHighlight } from 'app/modules/network/diagram/view/entire/util';
import { chooseSubstation } from 'app/shared/reducers/choose-substation';
import Select from 'react-select';

interface CustomHTMLDivElement extends HTMLDivElement {
  [key: string]: any;
}

const customSelectStyles = {
  container: provided => ({
    ...provided,
    position: 'absolute',
    right: 20,
  }),
  control: provided => ({
    ...provided,
    width: 200,
    float: 'right',
  }),
  menu: provided => ({
    ...provided,
    width: 200,
    right: 0,
  }),
  option: (provided, state) => ({
    ...provided,
    color: state.isSelected ? 'white' : 'black',
  }),
};

const SVGHandler = props => {
  const { network, containerId } = props;

  const divRef = React.useRef<CustomHTMLDivElement>();
  const [defaultViewBox, setDefaultViewBox] = React.useState<Box>(new Box());

  const dispatch = useAppDispatch();
  const substation = useAppSelector(state => state.chooseSubstation.substation);

  const [selectedOption, setSelectedOption] = React.useState(null);

  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    console.log('Props: ', props);

    if (network && network.svg) {
      const divElt = divRef.current;
      if (!divElt) {
        return;
      }
      const divContainer = document.querySelector(containerId ? '#' + containerId : '#svg-container');
      const divWidth = divContainer?.getBoundingClientRect().width;
      const divHeight = divContainer?.getBoundingClientRect().height;
      divElt.innerHTML += network.svg.svg;

      // calculate svg width and height from svg bounding box
      const svgEl = divElt.getElementsByTagName('svg')[0];
      const bbox = svgEl.getBBox();
      const xOrigin = bbox.x - 20;
      const yOrigin = bbox.y - 20;
      const svgWidth = bbox.width + 40;
      const svgHeight = bbox.height + 40;

      setDefaultViewBox(new Box({ x: xOrigin, y: yOrigin, width: svgWidth, height: svgHeight }));

      const width = divWidth || svgWidth;
      const height = divHeight || svgHeight;

      // using svgdotjs panzoom component to pan and zoom inside the svg, using svg width and height previously calculated for size and viewbox
      divElt.innerHTML = ''; // clear the previous svg in div element before replacing
      const draw = SVG()
        .addTo(divElt)
        .size(width, height)
        .viewbox(xOrigin, yOrigin, svgWidth, svgHeight)
        .panZoom({
          panning: true,
          zoomMin: 0.2,
          zoomMax: 5,
          zoomFactor: 0.15,
          margins: { top: 0, left: 0, right: 0, bottom: 0 },
        });

      if (divRef.current && !divRef.current.svg) {
        divRef.current.svg = draw;
      }

      // set overflow visible to display entire diagram
      const drawSvg = draw.svg(network.svg.svg);
      const drawNodeChild = drawSvg.node.firstElementChild as HTMLElement;
      drawNodeChild.style.overflow = 'visible';

      // PowSyBl SLD introduced server side calculated SVG viewbox
      // waiting for deeper adaptation, remove it and still rely on client side computed viewbox
      /* if (drawNodeChild.getAttribute('viewbox')) {
        drawNodeChild.removeAttribute('viewbox')
      } */

      draw.on('panStart', function (evt) {
        divElt.style.cursor = 'move';
      });
      draw.on('panEnd', function (evt) {
        divElt.style.cursor = 'default';
      });
    }
    return () => {
      dispatch(chooseSubstation(''));
    };
  }, []);

  React.useEffect(() => {
    if (!substation) {
      return;
    }
    const draw: Svg = divRef.current?.svg;
    draw.animate().viewbox(defaultViewBox.x, defaultViewBox.y, defaultViewBox.width, defaultViewBox.height);
    // draw.zoom(0.2);
    setTimeout(() => {
      const svgNet = divRef.current.querySelector<SVGSVGElement>('svg > svg');
      const subCoordinates = getElementCoordinates(substation);
      let point = svgNet.createSVGPoint();
      point.x = subCoordinates.x;
      point.y = subCoordinates.y;
      point = point.matrixTransform(svgNet.getScreenCTM()?.inverse());
      draw.animate().zoom(1, new Point(point.x, point.y));

      addHighlight(substation);
      setTimeout(() => removeHighlight(), 1000);
    }, 500);
  }, [substation]);

  const focusOnSub = option => {
    dispatch(chooseSubstation(option.value));
    setSelectedOption(option);
  };

  const getOptions = () => {
    const subIds = [...network.ids];
    return subIds.map(id => ({ value: id, label: id }));
  };

  return (
    <>
      {network && (
        <>
          <Select value={selectedOption} onChange={focusOnSub} options={getOptions()} styles={customSelectStyles} />
          <div
            id="div-svg"
            ref={divRef}
            // dangerouslySetInnerHTML={{ __html: network.svg.svg }}
          />
        </>
      )}
    </>
  );
};

export default SVGHandler;
