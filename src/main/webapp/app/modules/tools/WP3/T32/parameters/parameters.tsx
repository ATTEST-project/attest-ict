import React from 'react';
import './parameters.scss';
import { useFormContext } from 'react-hook-form';
import { Col, Row, Tooltip } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';

const Parameters = () => {
  const {
    register,
    formState: { errors },
    reset,
    resetField,
    setValue,
    watch,
  } = useFormContext();

  const [showTooltip, setShowTooltip] = React.useState<boolean>(false);

  const profiles = watch('profiles');

  return (
    <div className="section-with-border">
      <Row md="4">
        <Col>
          <ValidatedField
            disabled={profiles?.length > 0}
            register={register}
            error={errors?.parameters?.xlsx_file_name}
            name="parameters.xlsx_file_name"
            label="Profile File"
            type="file"
            accept=".xlsx"
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.ods_file_name}
            name="parameters.ods_file_name"
            label="Contingencies File"
            type="file"
            accept=".ods"
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.scenario_gen}
            name="parameters.scenario_gen"
            label="Scenario Gen File"
            type="file"
            accept=".ods"
            validate={{ required: true }}
          />
        </Col>
      </Row>
      <Row>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.peak_hour}
            name="parameters.peak_hour"
            label="Peak Hour"
            type="number"
            defaultValue={19}
            min={1}
            max={24}
            validate={{ required: true }}
          />
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.no_year}
            name="parameters.no_year"
            label="Years"
            type="select"
            validate={{ required: true }}
          >
            <option value="" hidden>
              Choose...
            </option>
            <option value={1}>2020</option>
            <option value={2}>2020, 2030</option>
            <option value={3}>2020, 2030, 2040</option>
            <option value={4}>2020, 2030, 2040, 2050</option>
          </ValidatedField>
        </Col>
        <Col>
          <ValidatedField
            register={register}
            error={errors?.parameters?.model}
            name="parameters.model"
            label="Model"
            type="select"
            defaultValue="investment"
            validate={{ required: true }}
          >
            <option value="" hidden>
              Choose...
            </option>
            {/* <option value="all">All</option> */}
            <option value="investment">Investment</option>
            {/* <option value="screening">Screening</option> */}
          </ValidatedField>
        </Col>
        <Col style={{ alignSelf: 'center' }}>
          <Tooltip target="run-both-checkbox" isOpen={showTooltip}>
            Investment and Operation Costs
          </Tooltip>
          <ValidatedField
            id="run-both-checkbox"
            className="input-row-checkbox"
            register={register}
            error={errors?.parameters?.run_both}
            name="parameters.run_both"
            label="Run Both"
            type="checkbox"
            onMouseEnter={() => setShowTooltip(true)}
            onMouseLeave={() => setShowTooltip(false)}
          />
        </Col>
      </Row>
    </div>
  );
};

export default Parameters;
