import React from 'react';
import './parameters.scss';
import { Col, Collapse, Input, Label, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useFormContext } from 'react-hook-form';
import Divider from 'app/shared/components/divider/divider';

const ParametersSection = props => {
  const {
    register,
    formState: { errors },
    reset,
  } = useFormContext();

  const [showParameters, setShowParameters] = React.useState<boolean>(true);

  return (
    <>
      <div className="section-with-border">
        <div
          style={{ display: 'flex', justifyContent: 'space-between', cursor: 'pointer' }}
          onClick={() => setShowParameters(!showParameters)}
        >
          <h6>{'Parameters'}</h6>
          <div style={{ marginRight: 10 }}>
            <FontAwesomeIcon icon="angle-down" style={showParameters && { transform: 'rotate(180deg)' }} />
          </div>
        </div>
        <Collapse isOpen={showParameters}>
          <Divider />
          <Row>
            <Col md="3">
              <ValidatedField
                register={register}
                error={errors?.parameters?.output_distribution_bus}
                id={'output_distribution_bus'}
                label="Output Distribution Bus"
                name="parameters[output_distribution_bus]"
                data-cy="output_distribution_bus"
                type="text"
                validate={{ required: true }}
              />
            </Col>

            <Col md="3">
              <ValidatedField
                register={register}
                error={errors?.parameters?.current_time_period}
                id={'current_time_period'}
                label="Current Time Period"
                name="parameters[current_time_period]"
                data-cy="current_time_period"
                type="text"
                validate={{ required: true }}
              />
            </Col>
          </Row>
          <Divider />
          <Row>
            <Col>
              <ValidatedField
                register={register}
                id={'case_name'}
                label="Case Name"
                name="parameters[case_name]"
                data-cy="case_name"
                type="text"
                validate={{ required: false }}
              />
            </Col>
            <Col>
              <ValidatedField
                register={register}
                label="Season"
                id={'season'}
                name="parameters[season]"
                data-cy="season"
                type="select"
                validate={{ required: false }}
              >
                <option value="" hidden>
                  Select the season...
                </option>
                <option value="Su">Summer</option>
                <option value="W">Winter</option>
              </ValidatedField>
            </Col>
            <Col>
              <ValidatedField
                register={register}
                label="Year"
                id={'year'}
                name="parameters[year]"
                data-cy="year"
                type="select"
                validate={{ required: false }}
              >
                <option value="" hidden>
                  Select the year...
                </option>
                <option value="2020">2020</option>
                <option value="2030">2030</option>
                <option value="2040">2040</option>
                <option value="2050">2050</option>
              </ValidatedField>
            </Col>
            <Col style={{ alignSelf: 'center' }}>
              <ValidatedField
                className="input-row-checkbox"
                register={register}
                error={errors?.parameters?.with_flex}
                id={'with_flex'}
                label="With Flexibility"
                name="parameters[with_flex]"
                data-cy="with_flex"
                type="checkbox"
              />
            </Col>
          </Row>
        </Collapse>
      </div>
    </>
  );
};

export default ParametersSection;
