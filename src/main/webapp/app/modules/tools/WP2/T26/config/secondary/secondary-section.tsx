import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Collapse, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Divider from 'app/shared/components/divider/divider';

const SecondarySection = () => {
  const {
    register,
    getValues,
    formState: { errors },
  } = useFormContext();

  const [showSecondarySection, setSecondarySection] = React.useState<boolean>(true);
  const run_energy = getValues('parameters.run_energy');
  const run_secondary = getValues('parameters.run_secondary');
  const run_tertiary = getValues('parameters.run_tertiary');

  return (
    <div className="section-with-border">
      <div
        style={{ display: 'flex', justifyContent: 'space-between', cursor: 'pointer' }}
        onClick={() => setSecondarySection(!showSecondarySection)}
      >
        <h6>{'Secondary (optional)'}</h6>
        <div style={{ marginRight: 10 }}>
          <FontAwesomeIcon icon="angle-down" style={showSecondarySection && { transform: 'rotate(180deg)' }} />
        </div>
      </div>
      <Collapse isOpen={showSecondarySection}>
        <Row md="3">
          <Col>
            <ValidatedField
              register={register}
              error={errors?.secondary?.gen_bid_prices_path}
              name="secondary.gen_bid_prices_path"
              label="Gen Bid Prices File"
              type="file"
              accept=".csv"
              validate={{ required: !run_energy && run_secondary }}
            />
          </Col>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.secondary?.gen_bid_qnt_path}
              name="secondary.gen_bid_qnt_path"
              label="Gen Bid Quantity File"
              type="file"
              accept=".csv"
              validate={{ required: !run_energy && run_secondary }}
            />
          </Col>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.secondary?.sec_req_path}
              name="secondary.sec_req_path"
              label="Secondary Req File"
              type="file"
              accept=".csv"
              validate={{ required: run_secondary }}
            />
          </Col>
        </Row>
      </Collapse>
    </div>
  );
};

export default SecondarySection;
