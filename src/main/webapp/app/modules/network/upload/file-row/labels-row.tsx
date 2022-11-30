import React from 'react';
import { Col, Row } from 'reactstrap';

const LabelsRow = props => {
  const { section } = props;

  const [component, setComponent] = React.useState('Generator');

  React.useEffect(() => {
    switch (section) {
      case 'gen':
        setComponent('Generator');
        break;
      case 'load':
        setComponent('Load');
        break;
      case 'flex':
        setComponent('Flex');
        break;
      default:
        setComponent('Generator');
    }
  }, []);

  return (
    <div>
      <Row md="7">
        <Col>Season</Col>
        <Col>Typical Day</Col>
        <Col>Mode</Col>
        <Col xs="4" />
        <Col />
        <Col />
        <Col />
      </Row>
    </div>
  );
};

export default LabelsRow;
