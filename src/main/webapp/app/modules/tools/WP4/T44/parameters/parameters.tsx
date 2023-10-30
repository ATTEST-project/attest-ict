import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import SectionHeader from 'app/shared/components/section-header/section-header';

const Parameters = () => {
  const {
    register,
    formState: { errors },
    reset,
  } = useFormContext();

  return (
    <>
      <div className="section-with-border">
        <SectionHeader title="Parameters" />

        <Row>
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
          <Col md="2">
            <ValidatedField
              register={register}
              error={errors?.parameters?.problem}
              id={'problem'}
              label="Which problem to solve?..."
              name="parameters[problem]"
              data-cy="problem"
              type="select"
              validate={{ required: true }}
            >
              <option value="" hidden>
                Select problem ...
              </option>
              <option value="1" disabled>
                Contingency Filtering
              </option>
              <option value="2" disabled>
                AC-OPF
              </option>
              <option value="3" disabled>
                AC-SCOPF
              </option>
              <option value="4">Tractable S-MP-AC-SCOPF</option>
              <option value="5" disabled>
                Security Assessment
              </option>
              <option value="6" disabled>
                Power Flow
              </option>
            </ValidatedField>
          </Col>
          <Col md="2">
            <ValidatedField
              register={register}
              error={errors?.parameters?.profile}
              id={'profile'}
              label="Load Profile season"
              name="parameters[profile]"
              data-cy="profile"
              type="select"
              validate={{ required: true }}
            >
              <option value="" hidden>
                Select season ...
              </option>
              <option value="1">Summer</option>
              <option value="2">Winter</option>
            </ValidatedField>
          </Col>
          <Col md="2">
            <ValidatedField
              register={register}
              error={errors?.parameters?.profile}
              id={'year'}
              label="Year"
              name="parameters[year]"
              data-cy="year"
              type="select"
              validate={{ required: true }}
            >
              <option value="" hidden>
                {' '}
                Select the year..{' '}
              </option>
              <option value="2020">2020</option>
              <option value="2030">2030</option>
              <option value="2040">2040</option>
              <option value="2050">2050</option>
            </ValidatedField>
          </Col>
          <Col md="1" style={{ alignSelf: 'center' }}>
            <ValidatedField
              className="input-row-checkbox"
              register={register}
              error={errors?.parameters?.flexibility}
              id={'flexibility'}
              label="With Flexibility"
              name="parameters[flexibility]"
              data-cy="flexibility"
              type="checkbox"
            />
          </Col>
        </Row>
      </div>
    </>
  );
};

export default Parameters;
