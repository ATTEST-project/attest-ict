import React from 'react';
import { Button, Col, Collapse, Form, Row, Spinner } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { useForm } from 'react-hook-form';
import { INetwork } from 'app/shared/model/network.model';
import { useAppDispatch } from 'app/config/store';
import { getEntities } from 'app/entities/network/network-search.reducer';
import NetworkSearchResults from 'app/shared/components/network-search/results/network-search-results-1';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const initialSearchState: { [key: string]: any } = {
  type: '',
  country: '',
  mpcName: '',
  fromNetworkDate: '',
  toNetworkDate: '',
};

const NetworkSearch = props => {
  const dispatch = useAppDispatch();

  const getDefaultValues = () => {
    return JSON.parse(sessionStorage.getItem('lastSearch'));
  };

  const {
    handleSubmit,
    register,
    formState: { errors },
    reset,
  } = useForm({ defaultValues: getDefaultValues() });

  const [networks, setNetworks] = React.useState<INetwork[]>(null);
  const [loading, setLoading] = React.useState<boolean>(false);
  const [showResults, setShowResults] = React.useState<boolean>(true);

  const submitForm = data => {
    /* eslint-disable-next-line no-console */
    console.log('Form data: ', data);
    sessionStorage.setItem('lastSearch', JSON.stringify(data));
    setLoading(true);
    setNetworks(null);
    setShowResults(true);
    dispatch(
      getEntities({
        type: data.type,
        country: data.country,
        mpcName: data.mpcName,
        fromNetworkDate: data.fromDate,
        toNetworkDate: data.toDate,
      })
    )
      .unwrap()
      .then(res => {
        setLoading(false);
        setNetworks(res.data);
      })
      .catch(err => {
        setLoading(false);
        setNetworks(null);
      });
  };

  const resetRow = () => {
    sessionStorage.removeItem('lastSearch');
    reset({ ...initialSearchState });
    setNetworks(null);
    setShowResults(true);
    props.setRowsSelectedCallback([]);
  };

  return (
    <div style={{ border: '1px solid white', borderRadius: 10, padding: 10 }}>
      <h5>{'Search Test Cases'}</h5>
      <Form onSubmit={handleSubmit(submitForm)}>
        <Row md="7">
          <Col>
            <ValidatedField
              register={register}
              error={errors?.country}
              id={'country-select'}
              label="Country"
              name="country"
              data-cy="country"
              type="select"
              validate={{ required: true }}
            >
              <option value="" hidden>
                Country...
              </option>
              <option value="HR">Croatia</option>
              <option value="PT">Portugal</option>
              <option value="ES">Spain</option>
              <option value="UK">United Kingdom</option>
            </ValidatedField>
          </Col>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.type}
              id={'type-select'}
              label="Type"
              name="type"
              data-cy="type"
              type="select"
              validate={{ required: true }}
            >
              <option value="" hidden>
                Type...
              </option>
              <option value="DX">Distribution</option>
              <option value="TX">Transmission</option>
            </ValidatedField>
          </Col>
          <Col>
            <ValidatedField
              register={register}
              error={errors?.mpcName}
              id={'mpc-name-select'}
              label="MPC Name"
              name="mpcName"
              data-cy="mpcName"
              type="text"
              placeholder="MPC Name... (optional)"
            />
          </Col>
          <Col>
            <ValidatedField
              id="from-date-input"
              register={register}
              error={errors?.fromDate}
              label="From Date"
              name="fromDate"
              data-cy="fromDate"
              type="datetime-local"
            />
          </Col>
          <Col>
            <ValidatedField
              id="to-date-input"
              register={register}
              error={errors?.toDate}
              label="To Date"
              name="toDate"
              data-cy="toDate"
              type="datetime-local"
            />
          </Col>
          <Col md="2" style={{ alignSelf: 'end' }}>
            <div className="mb-3" style={{ display: 'flex', justifyContent: 'space-between' }}>
              <Button id="upload-button" color="primary" type="submit">
                {loading ? (
                  <Spinner color="light" size="sm" />
                ) : (
                  <>
                    <FontAwesomeIcon icon="search" />
                    <span>{' Search'}</span>
                  </>
                )}
              </Button>
              <div onClick={resetRow} style={{ cursor: 'pointer', padding: 10 }}>
                <FontAwesomeIcon icon="times" />
                {' Remove Filters'}
              </div>
            </div>
          </Col>
          {networks && (
            <Col style={{ alignSelf: 'end', textAlign: 'right' }}>
              <div className="mb-3">
                <Button onClick={() => setShowResults(!showResults)} color="dark">
                  <FontAwesomeIcon icon="angle-down" style={showResults && { transform: 'rotate(-180deg)' }} />
                </Button>
              </div>
            </Col>
          )}
        </Row>
      </Form>
      <div>
        <Collapse isOpen={showResults}>
          {networks && (
            <NetworkSearchResults
              networks={networks}
              setRowsSelectedCallback={props.setRowsSelectedCallback}
              selectionType={props.selectionType}
            />
          )}
        </Collapse>
      </div>
    </div>
  );
};

export default NetworkSearch;
