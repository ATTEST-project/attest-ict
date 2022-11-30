import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, FormGroup, Input, Label, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import Divider from 'app/shared/components/divider/divider';
import InputRow from 'app/modules/tools/WP5/T51/characterization-tool/input-row/input-row';

const Config = (props: any) => {
  const {
    register,
    formState: { errors },
    reset,
  } = useFormContext();

  const [nRows, setNRows] = React.useState<number>(1);
  const callbackNRows = (value: number) => setNRows(prevState => prevState + value);

  return (
    <div className="section-with-border">
      <Row>
        <Col md="3">
          <ValidatedField
            register={register}
            error={errors?.assestsType}
            name="assestsType"
            label="Assets Type"
            type="text"
            placeholder="Assets type..."
            validate={{ required: true }}
          />
        </Col>
      </Row>
      <Row>
        {[...Array(nRows).keys()].map((e, i) => (
          <div key={i}>
            <InputRow index={i} nRows={nRows} callbackNRows={callbackNRows} />
            {nRows > 1 && <Divider />}
          </div>
        ))}
      </Row>
    </div>
  );
};

export default Config;