import React from 'react';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { useFormContext } from 'react-hook-form';

const ParametersSection = () => {
  const {
    register,
    formState: { errors },
    reset,
  } = useFormContext();

  return (
    <>
      <div className="section-with-border">
        <h6>Tool Parameters</h6>
        <Row md="7">
          <Col md="4">
            <ValidatedField
              defaultValue={10}
              register={register}
              error={errors?.parameters?.nsc}
              id={'nsc'}
              label="Number of scenarios"
              name="parameters[nsc]"
              data-cy="nsc"
              type="number"
              validate={{ required: true }}
            />
          </Col>
        </Row>
      </div>
    </>
  );
};

export default ParametersSection;
