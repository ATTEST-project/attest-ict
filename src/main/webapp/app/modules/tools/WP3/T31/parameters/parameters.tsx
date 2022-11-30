import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Collapse, Input, Label, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import GrowthDSRTable from 'app/modules/tools/WP3/T31/parameters/growth-dsr-table/growth-dsr-table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntitiesByNetworkId } from 'app/modules/tools/WP3/T31/reducer/branch-length.reducer';
import { defaultParameters } from 'app/modules/tools/WP3/T31/parameters/default-parameters';
import { toast } from 'react-toastify';

const Parameters = (props: any) => {
  const {
    register,
    formState: { errors },
    reset,
    resetField,
    setValue,
  } = useFormContext();

  const dispatch = useAppDispatch();

  const [showParameters, setShowParameters] = React.useState<boolean>(false);

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
          toast.error('No length data found for network: ' + props.network.name);
        }
      })
      .catch(err => {
        toast.error('Error retrieving length data for network: ' + props.network.name);
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
                label="TRS Capacities [MVA]"
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
                label="Line Costs [currency/km]"
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
                label="TSR Costs [currency]"
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
                label="Line Length [km]"
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
              <GrowthDSRTable title="Growth [%]" section="growth" />
            </Col>
          </Row>
          <Row>
            <Col>
              <GrowthDSRTable title="DSR [%]" section="DSR" />
            </Col>
          </Row>
          <Row>
            <Col>
              <ValidatedField
                register={register}
                error={errors?.parameters?.cluster}
                id={'cluster'}
                label="Cluster [MVA]"
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
                label="Oversize"
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
                label="Max Clusters"
                name="parameters[Max_clusters]"
                data-cy="Max_clusters"
                type="number"
                placeholder={'Default: 5'}
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
                label="Scenarios"
                name="parameters[scenarios]"
                data-cy="scenarios"
                type="text"
                placeholder={'Default: []'}
              />
            </Col>
          </Row>
        </Collapse>
      </div>
    </>
  );
};

export default Parameters;
