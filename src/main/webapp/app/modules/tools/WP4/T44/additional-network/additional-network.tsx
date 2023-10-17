import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';

const AdditionalNetwork = () => {
  const {
    register,
    formState: { errors },
    reset,
  } = useFormContext();

  return (
    <>
      <div className="section-with-border">
        <h6>ODS Network File </h6>
        <Row md="7">
          <Col md="4">
            <ValidatedField
              register={register}
              error={errors?.additionalNetwork}
              id={'additional-network'}
              name="additionalNetwork"
              data-cy="additional-network"
              type="file"
              accept=".ods"
            />
          </Col>
        </Row>
      </div>
    </>
  );
};

export default AdditionalNetwork;
