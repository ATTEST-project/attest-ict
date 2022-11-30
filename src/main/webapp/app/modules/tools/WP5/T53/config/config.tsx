import React from 'react';
import { useAppDispatch } from 'app/config/store';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import Config from 'app/modules/tools/WP5/T52/config/config';
import Divider from 'app/shared/components/divider/divider';

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
      <Config />
      <Divider />
      <div className="section-with-border">
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
