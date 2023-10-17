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
          <h6>{'Tool Parameters'}</h6>
          <div style={{ marginRight: 10 }}>
            <FontAwesomeIcon icon="angle-down" style={showParameters && { transform: 'rotate(180deg)' }} />
          </div>
        </div>
        <Collapse isOpen={showParameters}>
          <Divider />

          <Row>
            <Col md="3">
              <ValidatedField
                disabled
                className="input-number-pos"
                register={register}
                error={errors?.parameters?.ntp}
                id={'ntp'}
                label="Time Resolution"
                name="parameters[ntp]"
                data-cy="ntp"
                type="number"
                min="1"
                validate={{ required: true }}
              />
            </Col>
          </Row>
          <Row>
            <Col md="3">
              <ValidatedField
                register={register}
                error={errors?.parameters?.scenario}
                id={'scenario'}
                label="Scenario"
                name="parameters[scenario]"
                data-cy="scenario"
                type="checkbox"
              />
            </Col>
          </Row>
          <Divider />
          <div className="section-with-border">
            <h6>Selection of Flexible Options</h6>
            <Divider />
            <Row className="param-container">
              <span>Active Power Curtailment</span>
              <Col md="3">
                <ValidatedField
                  disabled
                  className="input-checkbox-pos"
                  register={register}
                  error={errors?.parameters?.flex_apc}
                  id={'flex_apc'}
                  label="Flex APC"
                  name="parameters[flex_apc]"
                  data-cy="flex_apc"
                  type="checkbox"
                />
              </Col>
            </Row>
            <Row className="param-container">
              <span>OLTC Transformer</span>
              <Col md="3">
                <ValidatedField
                  className="input-checkbox-pos"
                  register={register}
                  error={errors?.parameters?.flex_oltc}
                  id={'flex_oltc'}
                  label="Flex OLTC"
                  name="parameters[flex_oltc]"
                  data-cy="flex_oltc"
                  type="checkbox"
                />
              </Col>
              <Col md="3">
                <ValidatedField
                  className="input-checkbox-pos"
                  register={register}
                  error={errors?.parameters?.oltc_bin}
                  id={'oltc_bin'}
                  label="OLTC Bin"
                  name="parameters[oltc_bin]"
                  data-cy="oltc_bin"
                  type="checkbox"
                />
              </Col>
            </Row>
            <Row className="param-container">
              <span>Reactive Power from RES</span>
              <Col md="3">
                <ValidatedField
                  className="input-checkbox-pos"
                  register={register}
                  error={errors?.parameters?.flex_adpf}
                  id={'flex_adpf'}
                  label="Flex ADPF"
                  name="parameters[flex_adpf]"
                  data-cy="flex_adpf"
                  type="checkbox"
                />
              </Col>
            </Row>
            <Row className="param-container">
              <span>Participation of Flexible Load</span>
              <Col md="3">
                <ValidatedField
                  className="input-checkbox-pos"
                  register={register}
                  error={errors?.parameters?.flex_fl}
                  id={'flex_fl'}
                  label="Flex FL"
                  name="parameters[flex_fl]"
                  data-cy="flex_fl"
                  type="checkbox"
                />
              </Col>
              <Col md="3">
                <ValidatedField
                  className="input-checkbox-pos"
                  register={register}
                  error={errors?.parameters?.fl_bin}
                  id={'fl_bin'}
                  label="Flex Bin"
                  name="parameters[fl_bin]"
                  data-cy="fl_bin"
                  type="checkbox"
                />
              </Col>
            </Row>
            <Row className="param-container">
              <span>Usage of Electrical Storage</span>
              <Col md="3">
                <ValidatedField
                  className="input-checkbox-pos"
                  register={register}
                  error={errors?.parameters?.flex_str}
                  id={'flex_str'}
                  label="Flex STR"
                  name="parameters[flex_str]"
                  data-cy="flex_str"
                  type="checkbox"
                />
              </Col>
              <Col md="3">
                <ValidatedField
                  className="input-checkbox-pos"
                  register={register}
                  error={errors?.parameters?.str_bin}
                  id={'str_bin'}
                  label="STR Bin"
                  name="parameters[str_bin]"
                  data-cy="str_bin"
                  type="checkbox"
                />
              </Col>
            </Row>
          </div>
          <Divider />
          <Row>
            <Col md="3">
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
            <Col md="3">
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

            <Col md="3">
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
          </Row>
        </Collapse>
      </div>
    </>
  );
};

export default ParametersSection;
