import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import Divider from 'app/shared/components/divider/divider';

const EnergySection = () => {
  const {
    register,
    getValues,
    formState: { errors },
  } = useFormContext();

  const run_energy = getValues('parameters.run_energy');

  const isRequired = () => {
    const required = { required: run_energy };
    /* eslint-disable-next-line no-console */
    console.log(' Energy-Section: run_energy:  ' + run_energy + ' Required: ' + JSON.stringify(required));
    return required;
  };

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
            validate={isRequired()}
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
            validate={isRequired()}
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
            validate={isRequired()}
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
            validate={isRequired()}
          />
        </Col>
      </Row>
    </div>
  );
};

export default EnergySection;
