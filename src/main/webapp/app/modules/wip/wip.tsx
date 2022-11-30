import React from 'react';
import './wip.scss';
import background from '../../../content/images/renewable_energy_bg.webp';

function WorkInProgress() {
  return (
    <div className="main" style={{ background: 'url(' + background + ')' }}>
      <div className="container">
        <h1>Work in progress...</h1>
      </div>
    </div>
  );
}

export default WorkInProgress;
