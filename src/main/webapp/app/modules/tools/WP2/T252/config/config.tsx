import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import SectionHeader from 'app/shared/components/section-header/section-header';

const Config = () => {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  return (
    <div className="section-with-border">
      <SectionHeader title="Upload Auxiliary Data" />
      <Row md="3">
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.input_network_path}
            name="parameters.input_network_path"
            label="Network File"
            type="file"
            accept=".xlsx"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.input_resources_path}
            name="parameters.input_resources_path"
            label="Resources File"
            type="file"
            accept=".xlsx"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.input_other_path}
            name="parameters.input_other_path"
            label="Other File"
            type="file"
            accept=".xlsx"
            validate={{ required: true }}
          />
        </Col>
      </Row>
      <Row md="3">
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.input_da_bids}
            name="parameters.input_da_bids"
            label="DA Bids File"
            type="file"
            accept=".xlsx"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.input_agc_signal}
            name="parameters.input_agc_signal"
            label="AGC Signal File"
            type="file"
            accept=".xlsx"
            validate={{ required: true }}
          />
        </Col>
      </Row>
    </div>
  );
};

export default Config;
