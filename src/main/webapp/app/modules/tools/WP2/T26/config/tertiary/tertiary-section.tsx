import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Collapse, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const TertiarySection = () => {
  const {
    register,
    getValues,
    formState: { errors },
  } = useFormContext();

  const [showTertiarySection, setTertiarySection] = React.useState<boolean>(true);

  const run_energy = getValues('parameters.run_energy');
  const run_secondary = getValues('parameters.run_secondary');
  const run_tertiary = getValues('parameters.run_tertiary');

  return (
    <div className="section-with-border">
      <div
        style={{ display: 'flex', justifyContent: 'space-between', cursor: 'pointer' }}
        onClick={() => setTertiarySection(!showTertiarySection)}
      >
        <h6>{'Tertiary (optional)'}</h6>
        <div style={{ marginRight: 10 }}>
          <FontAwesomeIcon icon="angle-down" style={showTertiarySection && { transform: 'rotate(180deg)' }} />
        </div>
      </div>
      <Collapse isOpen={showTertiarySection}>
        <Row md="3">
          <Col>
            <ValidatedField
              register={register}
              error={errors?.tertiary?.gen_bid_prices_path}
              name="tertiary.gen_bid_prices_path"
              label="Gen Bid Prices File"
              type="file"
              accept=".csv"
              validate={{ required: !run_energy && run_tertiary }}
            />
          </Col>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.tertiary?.gen_bid_qnt_path}
              name="tertiary.gen_bid_qnt_path"
              label="Gen Bid Quantity File"
              type="file"
              accept=".csv"
              validate={{ required: !run_energy && run_tertiary }}
            />
          </Col>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.tertiary?.ter_req_path}
              name="tertiary.ter_req_path"
              label="Tertiary Req File"
              type="file"
              accept=".csv"
              validate={{ required: run_tertiary }}
            />
          </Col>
        </Row>
      </Collapse>
    </div>
  );
};

export default TertiarySection;
