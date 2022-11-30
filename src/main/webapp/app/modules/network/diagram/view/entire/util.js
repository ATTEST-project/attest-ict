/* eslint-env browser */
export const addHighlight = stringSubId => {
  const { allGs, firstEl, nextEl } = getFromToSubstations(stringSubId);

  // add highlight
  for (let i = firstEl.pos; i < nextEl.pos; ++i) {
    allGs.item(i).classList.add('highlight');
  }
};

export const removeHighlight = () => {
  document.querySelectorAll('.highlight').forEach(el => el.classList.remove('highlight'));
};

export const getSvgInfos = data => {
  return {
    ids: data.ids,
    svg: {
      metadata: JSON.parse(data.svg.metadata),
      svg: data.svg.svg,
    },
  };
};

const getAllSubstations = stringSubId => {
  const allGs = document.querySelectorAll('g');
  const arrGs = [...allGs];

  // to have an idea of what and where idbus elements are
  let idBusElements = [];
  arrGs.forEach((e, i) => {
    if (e.getAttribute('id') !== null && e.getAttribute('id').startsWith('idbus')) {
      idBusElements = [...idBusElements, { pos: i, sub: e.getAttribute('id') }];
    }
  });

  // split substation id to get each voltage level id
  const subId = stringSubId.substr(stringSubId.indexOf('_') + 1).split('_'); // returns ['8', '6', '5'] for example

  // get only elements with that voltage level ids
  const subs = [];
  subId.forEach(s => {
    const regex = 'idbus_.+_' + s + '$';
    Object.values(idBusElements).forEach(g => {
      if (g.sub.match(new RegExp(regex, 'g'))) {
        subs.push(g);
      }
    });
  });
  subs.sort((a, b) => (a.pos > b.pos ? 1 : -1));

  return {
    allGs,
    arrGs,
    idBusElements,
    subs,
  };
};

const getNextSubstation = (arrGs, idBusElements, subs) => {
  // get next element after last
  let nextEl;
  const nextPos = idBusElements.indexOf(idBusElements.find(({ sub }) => sub === subs[subs.length - 1].sub)) + 1;
  if (nextPos < Object.keys(idBusElements).length) {
    nextEl = idBusElements[nextPos];
  } else {
    const overLast = arrGs.find(el => el.getAttribute('id') != null && el.getAttribute('id').match(/idline_.*_[0-9]+$/));
    nextEl = {
      pos: arrGs.indexOf(overLast),
      sub: overLast.getAttribute('id'),
    };
  }
  return nextEl;
};

const getFromToSubstations = stringSubId => {
  const { allGs, arrGs, idBusElements, subs } = getAllSubstations(stringSubId);

  const firstEl = subs[0];
  const nextEl = getNextSubstation(arrGs, idBusElements, subs);

  return {
    allGs,
    firstEl,
    nextEl,
  };
};

const getFirstAndLastSubstationsFromSubId = stringSubId => {
  const subs = getAllSubstations(stringSubId)?.subs;
  if (subs.length === 0) {
    return;
  }
  const firstSub = subs[0],
    lastSub = subs[subs.length - 1];
  return { firstSub, lastSub };
};

export const getElementCoordinates = stringSubId => {
  const { firstSub, lastSub } = getFirstAndLastSubstationsFromSubId(stringSubId);
  const firstSubDim = document.querySelector('#' + firstSub.sub)?.getBoundingClientRect();
  const lastSubDim = document.querySelector('#' + lastSub.sub)?.getBoundingClientRect();
  // const lastSubLineLength = document.querySelector('#' + lastSub.sub)?.querySelector('line').getTotalLength();
  return {
    x: (firstSubDim.x + lastSubDim.x) / 2,
    y: firstSubDim.y,
  };
  // return calculatePercentPos(coordinates);
};

const calculatePercentPos = coordinates => {
  return {
    x: coordinates.x / window.innerWidth,
    y: coordinates.y / window.innerHeight,
  };
};
