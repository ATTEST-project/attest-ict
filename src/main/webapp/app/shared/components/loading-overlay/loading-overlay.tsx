import React from 'react';
import './loading-overlay.scss';
import { Spinner } from 'reactstrap';

const LoadingOverlay = React.forwardRef((props: any, ref: any) => {
  const divRef = React.useRef<HTMLDivElement>();

  const handleContainerResize = () => {
    const parentElement = ref.current;
    const elementDimensions = parentElement.getBoundingClientRect();
    const overlay = divRef.current;
    overlay.style.width = elementDimensions.width + 'px';
    overlay.style.height = elementDimensions.height + 'px';
    overlay.style.top = parentElement.offsetTop + 'px';
    overlay.style.left = parentElement.offsetLeft + 'px';
  };

  /* list of mutations captured by MutationObserver */
  // const callback = (mutationList, observer) => {
  //   for (const mutation of mutationList) {
  //     handleContainerResize();
  //   }
  // };

  React.useEffect(() => {
    if (!ref.current) {
      return;
    }
    handleContainerResize();

    // const parentElement = ref.current;

    /* with MutationObserver (it does not work since mutations on style attribute are not registered) */
    // const resizeObserver = new MutationObserver(callback);
    // resizeObserver.observe(parentElement, { attributes: true });

    /* with ResizeObserver (in 2022 it seems to be compatible with all browsers) */
    // new ResizeObserver(handleChartResize).observe(parentElement);

    /* old style: event listener on window resize */
    window.addEventListener('resize', handleContainerResize);
    return () => {
      /* disconnect MutationObserver on component destroy */
      // resizeObserver.disconnect();
      window.removeEventListener('resize', handleContainerResize);
    };
  }, []);

  /* React.useEffect(() => {
    const parentElement = ref.current;
    if (!parentElement) {
      return;
    }
    handleChartResize();
  }, [ref]); */

  return (
    <div ref={divRef} className="loading-overlay">
      <Spinner color="dark" />
    </div>
  );
});

export default LoadingOverlay;
