import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import Divider from 'app/shared/components/divider/divider';

const EnergySection = () => {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  return (
    <div className="section-with-border">
      <h6>{'Energy'}</h6>
      <Divider />
      <Row md="4">
        <Col>
          <ValidatedField
            register={register}
            error={errors?.energy?.gen_bid_prices_path}
            name="energy.gen_bid_prices_path"
            label="Gen Bid Prices File"
            type="file"
            accept=".csv"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.energy?.gen_bid_qnt_path}
            name="energy.gen_bid_qnt_path"
            label="Gen Bid Quantity File"
            type="file"
            accept=".csv"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.energy?.load_bid_prices_path}
            name="energy.load_bid_prices_path"
            label="Load Bid Prices File"
            type="file"
            accept=".csv"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.energy?.loads_bid_qnt_path}
            name="energy.loads_bid_qnt_path"
            label="Loads Bid Quantity File"
            type="file"
            accept=".csv"
            validate={{ required: true }}
          />
        </Col>
      </Row>
    </div>
  );
};

export default EnergySection;
