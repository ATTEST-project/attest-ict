/* eslint-env browser */
export const SVG_NS = 'http://www.w3.org/2000/svg';
export const arrowSvg =
  '<svg width="44" height="44" viewBox="0 0 44 44" fill="#10272F" xmlns="http://www.w3.org/2000/svg"> <path class="arrow" fill-rule="evenodd" clip-rule="evenodd" d="M16 24.0163L17.2358 25.3171L21.9837 20.5691L26.7317 25.3171L28 24.0163L21.9837 18L16 24.0163Z"/> </svg>';
export const arrowHoverSvg =
  '<svg width="44" height="44" viewBox="0 0 44 44" fill="#dcdcdc" xmlns="http://www.w3.org/2000/svg"> <path class="arrow-hover" fill-rule="evenodd" clip-rule="evenodd" d="M22 35C29.1797 35 35 29.1797 35 22C35 14.8203 29.1797 9 22 9C14.8203 9 9 14.8203 9 22C9 29.1797 14.8203 35 22 35ZM17.2358 25.3171L16 24.0163L21.9837 18L28 24.0163L26.7317 25.3171L21.9837 20.5691L17.2358 25.3171Z"/> </svg>';

const createSvgArrow = (network, element, position, index, x, highestY, lowestY) => {
  const svgInsert = document.getElementById(element.id).parentElement.parentElement;
  const group = document.createElementNS(SVG_NS, 'g');

  group.setAttribute('id', 'arrow_' + index);

  let y;
  if (position === 'TOP') {
    y = lowestY - 65;
    x = x - 22;
  } else {
    y = highestY + 65;
    x = x + 22;
  }

  if (position === 'BOTTOM') {
    group.setAttribute('transform', 'translate(' + x + ',' + y + ') rotate(180)');
  } else {
    group.setAttribute('transform', 'translate(' + x + ',' + y + ')');
  }

  group.innerHTML = arrowSvg + arrowHoverSvg;

  svgInsert.appendChild(group);

  function filterMetadata(nodes, element) {
    const nodesFiltered = nodes.filter(el => el.vid === element);
    return nodesFiltered.length > 0;
  }

  const nextVId = element.nextVId;
  const nextSubstation = Object.entries(network).find(([k, el]) => filterMetadata(el.metadata.nodes, nextVId));
  // console.log(nextSubstation);

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
    // const id = document.getElementById(element.id).id;
    // const meta = svg.metadata.nodes.find((other) => other.id === id);
    // onNextVoltageLevelClick(meta.nextVId);
  });

  // handling the color changes when hovering
  group.addEventListener('mouseenter', function (e) {
    e.target.querySelector('.arrow').style.fill = '#dcdcdc';
    e.target.querySelector('.arrow-hover').style.fill = '#10272F';
  });

  group.addEventListener('mouseleave', function (e) {
    e.target.querySelector('.arrow').style.fill = '#10272F';
    e.target.querySelector('.arrow-hover').style.fill = '#dcdcdc';
  });

  return nextSubstation;
};

export const addNavigationArrow = (network, svg) => {
  let navigable = svg.metadata.nodes.filter(el => el.nextVId !== null);

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

  const arrows = [];

  navigable.forEach((element, index) => {
    const transform = document.getElementById(element.id).getAttribute('transform').split(',');
    const x = parseInt(transform[0].match(/\d+/), 10);
    arrows.push(createSvgArrow(network, element, element.direction, index, x, highestY.get(element.vid), lowestY.get(element.vid)));
  });

  return arrows;
};

export const getSvgInfos = data => {
  return Object.fromEntries(Object.entries(data).map(([k, v]) => [k, { svg: v.svg, metadata: JSON.parse(v.metadata) }]));
};
