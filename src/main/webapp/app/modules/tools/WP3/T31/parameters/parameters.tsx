import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Collapse, Input, Label, Row, Tooltip } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toast } from 'react-toastify';
import { ValidatedField } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import GrowthDSRTable from 'app/modules/tools/WP3/T31/parameters/growth-dsr-table/growth-dsr-table';
import Divider from 'app/shared/components/divider/divider';
import { getEntitiesByNetworkId } from 'app/modules/tools/WP3/T31/reducer/branch-length.reducer';
import SectionHeader from 'app/shared/components/section-header/section-header';

const Parameters = (props: any) => {
  const {
    register,
    formState: { errors },
    reset,
    resetField,
    setValue,
    getValues,
  } = useFormContext();

  const dispatch = useAppDispatch();
  const [showParameters, setShowParameters] = React.useState<boolean>(false);
  const [showTooltip, setShowTooltip] = React.useState<boolean>(false);
  const [requiredAddLoadData, setRequiredAddLoadData] = React.useState<boolean>(false);
  const allLineLength = useAppSelector(state => state.branchLength.entities);
  const numbersListRegex = React.useMemo(() => /^(\s*-?\d+(\.\d+)?)(\s*,\s*-?\d+(\.\d+)?)*$/, []);
  const setLineDefaultValue = () => {
    resetField('parameters.line_length');
  };

  const getLineLengthFromDB = event => {
    if (!event.target.checked) {
      setLineDefaultValue();
      return;
    }
    dispatch(getEntitiesByNetworkId(props.network.id))
      .unwrap()
      .then(res => {
        if (res.data?.length === 0) {
          toast.error('No data related to branch length was found in the database for the network: ' + props.network.name);
        }
      })
      .catch(err => {
        toast.error('An error occurred while trying to retrieve branch length data for the network: ' + props.network.name);
      });
  };

  React.useEffect(() => {
    if (allLineLength?.length === 0) {
      return;
    }
    const lineLengthFiltered = allLineLength.map(l => l.kmLength);
    if (lineLengthFiltered?.length > 0) {
      setValue('parameters.line_length', [...lineLengthFiltered]);
    } else {
      setLineDefaultValue();
    }
  }, [allLineLength]);

  React.useEffect(() => {
    const useLoadDataCheckbox = getValues('parameters[use_load_data_update]');
    setRequiredAddLoadData(useLoadDataCheckbox);
  }, []);

  return (
    <>
      <div className="section-with-border">
        <div
          style={{ display: 'flex', justifyContent: 'space-between', cursor: 'pointer' }}
          onClick={() => setShowParameters(!showParameters)}
        >
          <SectionHeader title="Parameters" />

          <div style={{ marginRight: 10 }}>
            <FontAwesomeIcon icon="angle-down" style={showParameters && { transform: 'rotate(180deg)' }} />
          </div>
        </div>
        <Collapse isOpen={showParameters}>
          <Row>
            <Col>
              <ValidatedField
                register={() =>
                  register('parameters[line_capacities]', {
                    pattern: {
                      value: numbersListRegex,
                      message: 'Please insert a list of numbers separated by a comma',
                    },
                  })
                }
                error={errors?.parameters?.line_capacities}
                id={'line-capacities'}
                label={'Line Capacities [MVA]'}
                name="parameters[line_capacities]"
                data-cy="line_capacities"
                type="text"
                placeholder={
                  'Default: 0.045,0.075,0.1125,0.15,0.225,0.3,0.5,0.75,1.0,2.0,5.0,10.0,20.0,' +
                  '30.0,40.0,50.0,60.0,80.0,100.0,250.0,500.0'
                }
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <ValidatedField
                register={() =>
                  register('parameters[TRS_capacities]', {
                    pattern: {
                      value: numbersListRegex,
                      message: 'Please insert a list of numbers separated by a comma',
                    },
                  })
                }
                error={errors?.parameters?.TRS_capacities}
                id={'trs-capacities'}
                label="Transformer capacities [MVA]"
                name="parameters[TRS_capacities]"
                data-cy="TRS_capacities"
                type="text"
                placeholder={'Default: 1.0,2.0,5.0,10.0,20.0,30.0,40.0,50.0,60.0,80.0,100.0,250.0,500.0'}
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <ValidatedField
                register={() =>
                  register('parameters[line_costs]', {
                    pattern: {
                      value: numbersListRegex,
                      message: 'Please insert a list of numbers separated by a comma',
                    },
                  })
                }
                error={errors?.parameters?.line_costs}
                id={'line-costs'}
                label="Line costs [Currency/km]"
                name="parameters[line_costs]"
                data-cy="line_costs"
                type="text"
                placeholder={'Default: []'}
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <ValidatedField
                register={() =>
                  register('parameters[TRS_costs]', {
                    pattern: {
                      value: numbersListRegex,
                      message: 'Please insert a list of numbers separated by a comma',
                    },
                  })
                }
                error={errors?.parameters?.TRS_costs}
                id={'tsr-costs'}
                label="Transformer costs [Currency]"
                name="parameters[TRS_costs]"
                data-cy="TRS_costs"
                type="text"
                placeholder={'Default: []'}
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <ValidatedField
                register={() =>
                  register('parameters[cont_list]', {
                    pattern: {
                      value: numbersListRegex,
                      message: 'Please insert a list of numbers separated by a comma',
                    },
                  })
                }
                error={errors?.parameters?.cont_list}
                id={'cont-list'}
                label="Contingencies List"
                name="parameters[cont_list]"
                data-cy="cont_list"
                type="text"
                placeholder={'Default: []'}
              />
            </Col>
          </Row>
          <Row>
            <Col md="9">
              <ValidatedField
                register={() =>
                  register('parameters[line_length]', {
                    pattern: {
                      value: numbersListRegex,
                      message: 'Please insert a list of numbers separated by a comma',
                    },
                  })
                }
                error={errors?.parameters?.line_length}
                id={'line-length'}
                label="Length of each branch [km]"
                name="parameters[line_length]"
                data-cy="line_length"
                type="text"
                placeholder={'Default: []'}
              />
            </Col>
            <Col md="3" style={{ alignSelf: 'end', textAlign: 'right' }}>
              <div className="mb-3">
                <Label htmlFor="line-length-from-db">{'Use Line Length From DB '}</Label>{' '}
                <Input id="line-length-from-db" type="checkbox" name="lineLengthFromDB" onChange={getLineLengthFromDB} />
              </div>
            </Col>
          </Row>
          <Row>
            <Col>
              <GrowthDSRTable title="Dictionary with demand growth [%] for selected years" section="growth" />
            </Col>
          </Row>
          <Row>
            <Col>
              <GrowthDSRTable title="Dictionary with DSR [%] for selected years" section="DSR" />
            </Col>
          </Row>
          <Row>
            <Col>
              <ValidatedField
                register={register}
                error={errors?.parameters?.cluster}
                id={'cluster'}
                label="List of investment clusters [MVA]"
                name="parameters[cluster]"
                data-cy="cluster"
                type="text"
                placeholder={'Default: None'}
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <ValidatedField
                register={register}
                error={errors?.parameters?.oversize}
                id={'oversize'}
                label="Oversize: Option to intentionally oversize investments"
                name="parameters[oversize]"
                data-cy="oversize"
                type="number"
                placeholder={'Default: 0'}
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <ValidatedField
                register={register}
                error={errors?.parameters?.Max_clusters}
                id={'maxClusters'}
                label="Max Clusters: Constraint on the maximum number of clusters considered"
                name="parameters[Max_clusters]"
                data-cy="Max_clusters"
                type="number"
                placeholder={'Default: 3'}
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <ValidatedField
                register={() =>
                  register('parameters[scenarios]', {
                    pattern: {
                      value: numbersListRegex,
                      message: 'Please insert a list of numbers separated by a comma',
                    },
                  })
                }
                error={errors?.parameters?.scenarios}
                id={'scenarios'}
                label="List of Scenarios to model."
                name="parameters[scenarios]"
                data-cy="scenarios"
                type="text"
                placeholder={'Default: []'}
              />
            </Col>
          </Row>
        </Collapse>
      </div>
      <Divider />
      <div className="section-with-border">
        <SectionHeader title="Upload Auxiliary Data" />

        <Row>
          <Col style={{ alignSelf: 'center' }}>
            <Tooltip target="use_load_data_update" isOpen={showTooltip}>
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
              onMouseEnter={() => setShowTooltip(true)}
              onMouseLeave={() => setShowTooltip(false)}
            />
          </Col>
        </Row>
        <Divider />
        <Row>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.parameters?.EV_data_file_path}
              name="parameters[EV_data_file_path]"
              label="ATTEST data for EV, PV and storage [e.g EV-PV-Storage_Data_for_Simulations.xlsx]"
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
