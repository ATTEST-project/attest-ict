import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import Divider from 'app/shared/components/divider/divider';
import SectionHeader from 'app/shared/components/section-header/section-header';

const ParametersSection = (props: any) => {
  const { marketNotSelected } = props;

  const {
    register,
    formState: { errors },
  } = useFormContext();

  return (
    <div className="section-with-border">
      <SectionHeader title="Parameters" />
      {marketNotSelected && <div> Please Select at least one market! </div>}

      <Divider />
      <Row md="3">
        <Col>
          <ValidatedField
            className="input-row-checkbox"
            register={register}
            errors={errors?.parameters?.run_energy}
            name="parameters.run_energy"
            label="Run Energy"
            type="checkbox"
          />
        </Col>
        <Col>
          <ValidatedField
            className="input-row-checkbox"
            register={register}
            error={errors?.parameters?.run_secondary}
            name="parameters.run_secondary"
            label="Run Secondary"
            type="checkbox"
          />
        </Col>
        <Col>
          <ValidatedField
            className="input-row-checkbox"
            register={register}
            error={errors?.parameters?.run_tertiary}
            name="parameters.run_tertiary"
            label="Run Tertiary"
            type="checkbox"
          />
        </Col>
      </Row>
    </div>
  );
};

export default ParametersSection;
