/* eslint-env browser */
const SVG_NS = 'http://www.w3.org/2000/svg';

const rectSvg = '<svg> <rect class="custom-rect" width="40" height="35" style="fill:rgba(0,0,0,0);" /> </svg>';

export const createSvgRect = (network, element, position, index, x, highestY, lowestY) => {
  const svgInsert = document.getElementById(element.id).parentElement.parentElement;
  const group = document.createElementNS(SVG_NS, 'g');

  group.setAttribute('id', 'rect_' + element.equipmentId);

  let y;
  if (position === 'TOP') {
    y = lowestY - 20;
    x = x - 10;
  } else {
    y = highestY + 30;
    x = x + 30;
  }

  if (position === 'BOTTOM') {
    group.setAttribute('transform', 'translate(' + x + ',' + y + ') rotate(180)');
  } else {
    group.setAttribute('transform', 'translate(' + x + ',' + y + ')');
  }

  group.innerHTML = rectSvg;

  svgInsert.appendChild(group);

  // handling the navigation between voltage levels
  group.style.cursor = 'pointer';
  let dragged = false;
  group.addEventListener('mousedown', function (event) {
    dragged = false;
  });
  group.addEventListener('mousemove', function (event) {
    dragged = true;
  });
  group.addEventListener('mouseup', function (event) {
    if (dragged || event.button !== 0) {
      return;
    }
  });

  // handling the color changes when hovering
  group.addEventListener('mouseenter', function (e) {
    e.target.querySelector('.custom-rect').style.stroke = 'black';
  });

  group.addEventListener('mouseleave', function (e) {
    e.target.querySelector('.custom-rect').style.stroke = '';
  });

  return {
    element,
    group,
    x,
    y,
  };
};

export const addGenLoadRects = (network, svg) => {
  let navigable = svg.metadata.nodes.filter(el => el.componentType === 'GENERATOR' || el.componentType === 'LOAD');

  let vlList = svg.metadata.nodes.map(element => element.vid);
  vlList = vlList.filter((element, index) => element !== '' && vlList.indexOf(element) === index);

  // remove arrows if the arrow points to the current svg
  navigable = navigable.filter(element => {
    return vlList.indexOf(element.nextVId) === -1;
  });

  const highestY = new Map();
  const lowestY = new Map();
  let y;

  navigable.forEach(element => {
    const transform = document.getElementById(element.id).getAttribute('transform').split(',');

    y = parseInt(transform[1].match(/\d+/), 10);
    if (highestY.get(element.vid) === undefined || y > highestY.get(element.vid)) {
      highestY.set(element.vid, y);
    }
    if (lowestY.get(element.vid) === undefined || y < lowestY.get(element.vid)) {
      lowestY.set(element.vid, y);
    }
  });

  const rects = [];

  navigable.forEach((element, index) => {
    const transform = document.getElementById(element.id).getAttribute('transform').split(',');
    const x = parseInt(transform[0].match(/\d+/), 10);
    rects.push(createSvgRect(network, element, element.direction, index, x, highestY.get(element.vid), lowestY.get(element.vid)));
  });

  return rects;
};
