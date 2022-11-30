import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';

const CustomNetworkSection = (props: any) => {
  const {
    register,
    formState: { errors },
    reset,
  } = useFormContext();

  return (
    <>
      <div className="section-with-border">
        <h6>Custom Network</h6>
        <Row md="7">
          <Col md="4">
            <ValidatedField
              register={register}
              error={errors?.customNetwork}
              id={'custom-network'}
              name="customNetwork"
              data-cy="custom-network"
              type="file"
              accept=".ods"
            />
          </Col>
        </Row>
      </div>
    </>
  );
};

export default CustomNetworkSection;
