import React from 'react';
import { useAppDispatch } from 'app/config/store';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import Config from 'app/modules/tools/WP5/T52/config/config';
import Divider from 'app/shared/components/divider/divider';
import { RUN_TOOL_START, RUN_TOOL_FAILURE } from 'app/shared/util/toast-msg-constants';
import SectionHeader from 'app/shared/components/section-header/section-header';

const T53Config = () => {
  const dispatch = useAppDispatch();

  const {
    register,
    formState: { errors },
    reset,
    resetField,
    unregister,
  } = useFormContext();

  const nScenariosList = React.useMemo(() => [5, 10, 15, 20, 25], []);

  return (
    <>
      <div className="section-with-border">
        <SectionHeader title="Upload Auxiliary Data" />
        <Config />
        <Divider />

        <Row md="4">
          <Col>
            <ValidatedField
              register={register}
              error={errors?.assetsName}
              name="assetsName"
              label="Assets Name"
              type="text"
              placeholder="Assets Name..."
              validate={{ required: true }}
            />
          </Col>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.nScenarios}
              name="nScenarios"
              label="Number of Scenarios"
              type="select"
              validate={{ required: true }}
            >
              <option key={0} value="" hidden>
                Choose...
              </option>
              {nScenariosList.map((value, index) => (
                <option key={index} value={value}>
                  {value}
                </option>
              ))}
            </ValidatedField>
          </Col>
        </Row>
      </div>
    </>
  );
};

export default T53Config;
