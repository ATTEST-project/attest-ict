import React from 'react';
import { Col, Row, Spinner } from 'reactstrap';

interface SpinnerLoadingInterface {
  text: string;
}

export const SpinnerLoading = (props: SpinnerLoadingInterface) => {
  return (
    <div className="spinner-container">
      <Spinner color="primary" />
      <span className="spinner-text"> {props.text}</span>
    </div>
  );
};
