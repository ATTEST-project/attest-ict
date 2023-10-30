import React from 'react';
import './text-truncate.scss';

interface TextTruncateInterface {
  maxWidth: string;
  text: string;
}

const TextTruncate = (props: TextTruncateInterface) => {
  return (
    <div className="text-container" style={{ maxWidth: props.maxWidth }}>
      <span title={props.text}>
        <span className="text-hover">{props.text}</span>
      </span>
    </div>
  );
};

export default TextTruncate;
