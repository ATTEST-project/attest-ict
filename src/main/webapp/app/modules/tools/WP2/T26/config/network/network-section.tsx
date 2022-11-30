import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import Divider from 'app/shared/components/divider/divider';

const NetworkSection = () => {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  return (
    <div className="section-with-border">
      <h6>{'Network'}</h6>
      <Divider />
      <Row md="4">
        <Col>
          <ValidatedField
            register={register}
            error={errors?.network?.branches_path}
            name="network.branches_path"
            label="Branches File"
            type="file"
            accept=".csv"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.network?.buses_path}
            name="network.buses_path"
            label="Buses File"
            type="file"
            accept=".csv"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.network?.generators_path}
            name="network.generators_path"
            label="Generators File"
            type="file"
            accept=".csv"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.network?.loads_info_path}
            name="network.loads_info_path"
            label="Loads Info File"
            type="file"
            accept=".csv"
            validate={{ required: true }}
          />
        </Col>
      </Row>
    </div>
  );
};

export default NetworkSection;
