import React from 'react';
import './tool-title.scss';

interface ToolTitleInterface {
  title: string;
  imageAlt: string;
  imageSrc: string;
}

const ToolTitle = (props: ToolTitleInterface) => {
  const tool = { ...props };
  return (
    <div className="title-container">
      <img className="title-img" alt={tool.imageAlt} src={tool.imageSrc} />
      <h4 className="title-description">{tool.title}</h4>
    </div>
  );
};

export default ToolTitle;
