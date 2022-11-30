import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Collapse, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const TertiarySection = () => {
  const {
    register,
    formState: { errors },
  } = useFormContext();

  const [showTertiarySection, setTertiarySection] = React.useState<boolean>(false);

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
            />
          </Col>
        </Row>
      </Collapse>
    </div>
  );
};

export default TertiarySection;
