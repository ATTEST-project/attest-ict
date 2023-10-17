import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Col, Row, Input } from 'reactstrap';

interface SelectInterface {
  options: any[];
  defaultVal?: number;
  type: string;
  handleOnChange;
}

const Select = (props: SelectInterface) => {
  const { options, defaultVal, type, handleOnChange } = props;
  const [show, setShow] = React.useState<boolean>(false);

  React.useEffect(() => {
    if (options) {
      setShow(true);
    } else return;
  }, [options]);

  return (
    <div>
      {show ? (
        <div className="section-with-border">
          <Row>
            <Col md="3">
              Select {type}:
              <Input id="selectNode" type="select" register={defaultVal} value={defaultVal} onChange={handleOnChange}>
                {options.map((node, index) => (
                  <option key={index} value={node}>
                    {' '}
                    {node}{' '}
                  </option>
                ))}
              </Input>
            </Col>
          </Row>
        </div>
      ) : (
        <div></div>
      )}
    </div>
  );
};

export default Select;
