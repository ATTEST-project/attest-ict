import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row, Tooltip } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import Divider from 'app/shared/components/divider/divider';
import SectionHeader from 'app/shared/components/section-header/section-header';
import './parameters.scss';

const Parameters = () => {
  const {
    register,
    formState: { errors },
    reset,
    resetField,
    setValue,
    getValues,
    watch,
  } = useFormContext();

  const [showPeakTooltip, setShowPeakTooltip] = React.useState<boolean>(false);
  const [showRunBothTooltip, setShowRunBothTooltip] = React.useState<boolean>(false);
  const [showEvPvStrTooltip, setShowEvPvStrTooltip] = React.useState<boolean>(false);
  const [requiredAddLoadData, setRequiredAddLoadData] = React.useState<boolean>(false);
  const profiles = watch('profiles');

  React.useEffect(() => {
    const useLoadDataCheckbox = getValues('parameters[use_load_data_update]');
    setRequiredAddLoadData(useLoadDataCheckbox);
  }, []);

  const profileSelection = () => {
    return (
      <Col>
        <ValidatedField
          register={register}
          error={errors?.parameters?.xlsx_file_name}
          name="parameters.xlsx_file_name"
          label="Profile File. [e.g: Transmission_Network_UK_24hLoadData_W_Bd.xlsx]"
          type="file"
          accept=".xlsx"
        />
      </Col>
    );
  };

  return (
    <>
      <div className="section-with-border">
        <SectionHeader title="Parameters" />
        <Row>
          <Col>
            <Tooltip target="peak_hour" isOpen={showPeakTooltip}>
              Specify the number between 1 and 24, i.e., 19 for 7 p.m.
            </Tooltip>
            <ValidatedField
              id="peak_hour"
              register={register}
              error={errors?.parameters?.peak_hour}
              name="parameters.peak_hour"
              label="Peak Hour"
              type="number"
              defaultValue={19}
              min={1}
              max={24}
              validate={{ required: true }}
              onMouseEnter={() => setShowPeakTooltip(true)}
              onMouseLeave={() => setShowPeakTooltip(false)}
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
              <option value="all">Running All model (Screening and Investment)</option>
              <option value="investment">Running the investment model</option>
              <option value="screening">Running the screening model</option>
            </ValidatedField>
          </Col>
          <Col style={{ alignSelf: 'center' }}>
            <Tooltip target="run-both-checkbox" isOpen={showRunBothTooltip}>
              Define investment setting, Check for considering both investment cost and operation cost, Uncheck for considering investment
              cost only
            </Tooltip>
            <ValidatedField
              id="run-both-checkbox"
              className="input-row-checkbox"
              register={register}
              error={errors?.parameters?.run_both}
              name="parameters.run_both"
              label="Define investment setting"
              type="checkbox"
              onMouseEnter={() => setShowRunBothTooltip(true)}
              onMouseLeave={() => setShowRunBothTooltip(false)}
            />
          </Col>
        </Row>
      </div>

      <Divider />

      <div className="section-with-border">
        <SectionHeader title="Upload Auxiliary Data" />

        <Row md="3">
          {profiles?.length > 0 ? (
            <Col>
              <span> {profiles[0].fileName} </span>
            </Col>
          ) : (
            profileSelection()
          )}
        </Row>
        <Row md="3">
          <Col>
            <ValidatedField
              register={register}
              error={errors?.parameters?.ods_file_name}
              name="parameters.ods_file_name"
              label="Contingencies File. [e.g: Transmission_Network_UK_v3_PROF_update.ods]"
              type="file"
              accept=".ods"
              validate={{ required: true }}
            />
          </Col>
        </Row>
        <Row md="3">
          <Col>
            <ValidatedField
              register={register}
              error={errors?.parameters?.scenario_gen}
              name="parameters.scenario_gen"
              label="Scenario Gen File  [e.g: scenario_gen.ods]"
              type="file"
              accept=".ods"
              validate={{ required: true }}
            />
          </Col>
        </Row>
        <Row>
          <Col style={{ alignSelf: 'center' }}>
            <Tooltip target="use_load_data_update" isOpen={showEvPvStrTooltip}>
              {' '}
              Check for using ATTEST data for EV, PV and storage{' '}
            </Tooltip>
            <ValidatedField
              register={register}
              error={errors?.parameters?.use_load_data_update}
              id="use_load_data_update"
              className="input-row-checkbox"
              label="Use additional ATTEST data for EV, PV and storage"
              name="parameters[use_load_data_update]"
              data-cy="use_load_data_update"
              type="checkbox"
              onChange={e => setRequiredAddLoadData(e.target.checked)}
              onMouseEnter={() => setShowEvPvStrTooltip(true)}
              onMouseLeave={() => setShowEvPvStrTooltip(false)}
            />
          </Col>
        </Row>
        <Row>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.parameters?.EV_data_file_name}
              name="parameters[EV_data_file_name]"
              label=" ATTEST data for EV, PV and storage data for Simulation [e.g: EV-PV-Storage_Data_for_Simulations.xlsx]"
              type="file"
              accept=".xlsx"
              validate={requiredAddLoadData ? { required: true } : { required: false }}
            />
          </Col>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.parameters?.add_load_data_case_name}
              id={'add_load_data_case_name'}
              label="Name of the case for which the additional load data should be included"
              name="parameters[add_load_data_case_name]"
              data-cy="add_load_data_case_name"
              type="text"
              validate={requiredAddLoadData ? { required: true } : { required: false }}
            />
          </Col>
        </Row>
      </div>
    </>
  );
};

export default Parameters;
