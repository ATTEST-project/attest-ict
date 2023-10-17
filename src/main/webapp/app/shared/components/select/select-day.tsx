import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Col, Row, Input } from 'reactstrap';

interface SelectDayInterface {
  defaultDay?: string;
  defaultShow?: boolean;
  handleOnChange;
}

const SelectDay = (props: SelectDayInterface) => {
  const { defaultDay, defaultShow, handleOnChange } = props;
  const [day, setDay] = React.useState<string>('');
  const [show, setShow] = React.useState<boolean>(false);

  const options = [
    { label: 'Select Day...', value: '' },
    { label: 'Autumn', value: 'Autumn' },
    { label: 'Spring', value: 'Spring' },
    { label: 'Summer', value: 'Summer' },
    { label: 'Winter', value: 'Winter' },
  ];

  const initDefaultValue = () => {
    setDay(defaultDay);
    setShow(defaultShow);
  };

  React.useEffect(() => {
    initDefaultValue();
  }, [defaultDay, defaultShow, day, show]);

  return (
    <div>
      {show ? (
        <div className="section-with-border">
          <Row>
            <Col md="3">
              Select Day:
              <Input id="selectDay" type="select" register={day} value={day} onChange={handleOnChange}>
                {options.map((option, index) => (
                  <option key={index} value={option.value}>
                    {option.label}
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

export default SelectDay;
