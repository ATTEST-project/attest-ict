import React from 'react';
import './svg-handler.scss';
import { Point, SVG } from '@svgdotjs/svg.js';
import '@svgdotjs/svg.panzoom.js';

const SVGHandler = props => {
  const { substation, divId, columnCount, fullscreen } = props;

  const divRef = React.useRef<HTMLDivElement>();
  const [divIdParent, setDivIdParent] = React.useState<string>(null);

  React.useEffect(() => {
    if (!fullscreen) {
      setDivIdParent('#' + divId + '_parent');
    } else {
      setDivIdParent('#' + divId);
    }
  }, [fullscreen]);

  React.useEffect(() => {
    /* eslint-disable-next-line no-console */
    // console.log('Props: ', props);

    if (substation && substation.svg) {
      const divElt = divRef.current;
      if (!divElt) {
        return;
      }

      // get width and height of div parent to set the correct dimension for svg
      let divParent = document.querySelector(divIdParent);
      divParent = fullscreen ? divParent?.parentElement : divParent;
      const divWidth = divParent?.getBoundingClientRect().width;
      const divHeight = divParent?.getBoundingClientRect().height;

      divElt.innerHTML = substation.svg;

      // calculate svg width and height from svg bounding box
      const svgEl = divElt.getElementsByTagName('svg')[0];
      const bbox = svgEl.getBBox();
      const xOrigin = bbox.x - 20;
      const yOrigin = bbox.y - 20;
      const svgWidth = bbox.width + 40;
      const svgHeight = bbox.height + 40;

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
          zoomMin: fullscreen ? 1 : 0.2,
          zoomMax: 3,
          zoomFactor: 0.15,
          margins: { top: 0, left: 0, right: 0, bottom: 0 },
        });

      // set overflow visible to display entire diagram
      const drawSvg = draw.svg(substation.svg);
      const drawNodeChild = drawSvg.node.firstElementChild as HTMLElement;
      drawNodeChild.style.overflow = 'visible';

      // zoom programmatically to fit and center in the div, also based on number of columns
      if (divWidth && divHeight) {
        const cx = divWidth / 2 - 100;
        const cy = divHeight / 2 - 100;
        if (columnCount === 2) {
          draw.zoom(0.5, new Point(cx, cy));
        } else if (columnCount > 2) {
          draw.zoom(0.3, new Point(cx, cy));
        }
      }

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
  }, [divIdParent, substation]);

  return (
    <>
      {substation && (
        <div
          id={divId}
          ref={divRef}
          style={fullscreen ? { overflow: 'hidden' } : null}
          // dangerouslySetInnerHTML={{ __html: substation.svg }}
        />
      )}
    </>
  );
};

export default SVGHandler;
