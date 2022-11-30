/* eslint-env browser */
export const moveChartPopover = (chartPopover, chartPopoverHeader) => {
  let mousePosition;
  let offset = [];
  let isDown = false;

  chartPopoverHeader.style.cursor = 'move';

  chartPopoverHeader.addEventListener(
    'mousedown',
    function (e) {
      isDown = true;
      offset = [chartPopover.offsetLeft - e.clientX, chartPopover.offsetTop - e.clientY];
    },
    true
  );

  document.addEventListener(
    'mouseup',
    function () {
      isDown = false;
    },
    true
  );

  document.addEventListener(
    'mousemove',
    function (event) {
      event.preventDefault();
      if (isDown) {
        mousePosition = {
          x: event.clientX,
          y: event.clientY,
        };
        chartPopover.style.left = mousePosition.x + offset[0] + 'px';
        chartPopover.style.top = mousePosition.y + offset[1] + 'px';
      }
    },
    true
  );
};

export const moveElement = (container, popup, popupHeader) => {
  let isDown = false;
  let shiftX, shiftY;

  popupHeader.style.cursor = 'move';

  popupHeader.addEventListener(
    'mousedown',
    function (event) {
      isDown = true;
      const popupLeft = popup.getBoundingClientRect().left;
      const popupTop = popup.getBoundingClientRect().top;
      shiftX = event.clientX - popupLeft;
      shiftY = event.clientY - popupTop;
      if (popup.style.transform !== '') {
        popup.style.transform = '';
        popup.style.left = popupLeft + 'px';
        popup.style.top = popupTop + 'px';
        // popup.style.inset = '';
      }
    },
    true
  );

  document.addEventListener(
    'mouseup',
    function () {
      isDown = false;
    },
    true
  );

  document.addEventListener(
    'mousemove',
    function (event) {
      // event.preventDefault(); // -> removed preventDefault since on firefox mouse move event does not work with it

      const boundingClientRect = container.getBoundingClientRect();

      if (isDown) {
        let newX = event.clientX - shiftX;
        let newY = event.clientY - shiftY;

        const newBottom = newY + popup.offsetHeight;

        if (newX < boundingClientRect.left) {
          newX = boundingClientRect.left;
        }

        if (newX > boundingClientRect.right - popup.clientWidth) {
          newX = boundingClientRect.right - popup.clientWidth;
        }

        if (newY < boundingClientRect.top) {
          newY = Math.max(newY, boundingClientRect.top);
        }

        if (newBottom > boundingClientRect.bottom) {
          newY = Math.min(newY, boundingClientRect.bottom - popup.offsetHeight);
        }

        popup.style.left = newX + 'px';
        popup.style.top = newY + 'px';
      }
    },
    true
  );
};
