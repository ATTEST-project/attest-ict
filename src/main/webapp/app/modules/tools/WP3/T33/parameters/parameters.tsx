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
              register={register}
              error={errors?.parameters?.case_dir}
              name="parameters.case_dir"
              label="Directory of the Test Case to be run"
              type="text"
              defaultValue={'CS1'}
              validate={{ required: true }}
            />
          </Col>
        </Row>
        <Row md="7">
          <Col md="4">
            <ValidatedField
              register={register}
              error={errors?.parameters?.specification_file}
              name="parameters.specification_file"
              label="Problem Specification File"
              type="text"
              defaultValue={'CS1.txt'}
              validate={{ required: true }}
            />
          </Col>
        </Row>
      </div>
    </>
  );
};

export default ParametersSection;
