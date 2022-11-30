import React from 'react';

interface MouseTooltipProps {
  visible: boolean;
  children: React.ReactNode;
  offsetX?: number;
  offsetY?: number;
  className?: string;
  style?: object; // eslint-disable-line react/forbid-prop-types
}

const MouseTooltip = (props: MouseTooltipProps) => {
  const [mouseState, setMouseState] = React.useState({
    xPosition: 0,
    yPosition: 0,
    mouseMoved: false,
    listenerActive: false,
  });

  React.useEffect(() => {
    addListener();

    return () => {
      removeListener();
    };
  }, []);

  React.useEffect(() => {
    updateListener();
  }, [props]);

  const getTooltipPosition = ({ clientX: xPosition, clientY: yPosition }) => {
    setMouseState(prevState => ({
      ...prevState,
      xPosition,
      yPosition,
      mouseMoved: true,
    }));
  };

  const addListener = () => {
    window.addEventListener('mousemove', getTooltipPosition);
    setMouseState(prevState => ({ ...prevState, listenerActive: true }));
  };

  const removeListener = () => {
    window.removeEventListener('mousemove', getTooltipPosition);
    setMouseState(prevState => ({ ...prevState, listenerActive: false }));
  };

  const updateListener = () => {
    if (!mouseState.listenerActive && props.visible) {
      addListener();
    }

    if (mouseState.listenerActive && !props.visible) {
      removeListener();
    }
  };

  return (
    <div
      className={props.className}
      style={{
        display: props.visible && mouseState.mouseMoved ? 'block' : 'none',
        position: 'fixed',
        top: mouseState.yPosition + props.offsetY,
        left: mouseState.xPosition + props.offsetX,
        ...props.style,
      }}
    >
      {props.children}
    </div>
  );
};

MouseTooltip.defaultProps = {
  offsetX: 0,
  offsetY: 0,
};

export default MouseTooltip;
