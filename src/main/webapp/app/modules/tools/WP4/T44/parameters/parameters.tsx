import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';

const Parameters = () => {
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
          <Col>
            <ValidatedField
              register={register}
              error={errors?.parameters?.problem}
              id={'problem'}
              label="Problem"
              name="parameters[problem]"
              data-cy="problem"
              type="select"
              validate={{ required: true }}
            >
              <option value="" hidden>
                Problem...
              </option>
              <option value="0">Contingency Filtering</option>
              <option value="1">AC-OPF</option>
              <option value="2">AC-SCOPF</option>
              <option value="3">DC-SCOPF</option>
              <option value="4">Security Assessment</option>
            </ValidatedField>
          </Col>
        </Row>
      </div>
    </>
  );
};

export default Parameters;
