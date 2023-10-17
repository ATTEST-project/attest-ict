import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button, Col, Collapse, Form, Row, Spinner } from 'reactstrap';
import { Link, RouteComponentProps } from 'react-router-dom';
import { ValidatedField } from 'react-jhipster';
import { useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { getCimRepoNetworks, importCimNetwork, reset as resetReducer } from 'app/shared/reducers/network-import-from-cim-repo';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import { ICimRepoNetwork } from 'app/shared/model/cim-repo-network.model';
import NetworkFromCimRepoGrid from 'app/modules/network/cim-repo/network-from-cim-repo-grid';
import { isStringEmptyOrNullOrUndefined } from 'app/shared/util/string-utils';
import { generateNetworkTypeOptions } from 'app/shared/util/authorizationUtils';
import { generateCountryOptions } from 'app/shared/util/options-map-utils';
import { SpinnerLoading } from 'app/shared/components/spinner/spinner-loading';
import Divider from 'app/shared/components/divider/divider';

const NetworkImportFromCimRepo = (props: RouteComponentProps<{ url: string }>) => {
  const { match, location, history } = props;

  const {
    handleSubmit,
    register,
    formState: { errors },
    reset,
  } = useForm();

  const account = useAppSelector(state => state.authentication.account);
  const networkTypeOptions = generateNetworkTypeOptions(account.authorities);
  const countryOptions = generateCountryOptions();

  const dispatch = useAppDispatch();
  const cimNetworks = useAppSelector(state => state.networkImportFromCimRepo.cimNetworks); // array with networkId, networkName retrieve from the cim repo from Virtual Box
  const [networkToImport, setNetworkToImport] = React.useState<ICimRepoNetwork>(null);
  const setNetworkToImportCallback = (selected: ICimRepoNetwork[]) => setNetworkToImport(selected[0]);

  const loading = useAppSelector(state => state.networkImportFromCimRepo.loading);
  const country = useAppSelector(state => state.networkImportFromCimRepo.country); // set country  with the default value value set in the initialState of the reducer
  const type = useAppSelector(state => state.networkImportFromCimRepo.type); // set type with the   default value set  in the initialState of the reducer
  const importedIdNetworks = useAppSelector(state => state.networkImportFromCimRepo.networkIdUploaded);
  const importSuccess = useAppSelector(state => state.networkImportFromCimRepo.importSuccess);

  const errorMessage = useAppSelector(state => state.networkImportFromCimRepo.errorMessage);

  React.useEffect(() => {
    dispatch(getCimRepoNetworks());
    return () => {
      // -- call  reset,  when unmount component (-> page exit)
      dispatch(resetReducer());
    };
  }, []);

  const convertNetworkName = (url: string): string => {
    // eg cimRepo contains this networkName for TX "http://www.hops.hr/OperationalPlanning";
    let cleanedUrl = url.replace('http://', '');
    cleanedUrl = cleanedUrl.replace(/\./g, '-');
    cleanedUrl = cleanedUrl.replace(/\//g, '-');
    /* eslint-disable-next-line no-console */
    //  console.log('convertNetworkName()- return cleanedUrl ', cleanedUrl); // Output: "www-hops-hr-OperationalPlanning"
    return cleanedUrl;
  };

  const handleClose = () => {
    props.history.push('/network' + props.location.search);
  };

  const submitForm = data => {
    const selectedNetId = Number(networkToImport.networkId);
    const selectedNetName = convertNetworkName(networkToImport.networkName);
    const networkName = isStringEmptyOrNullOrUndefined(data.caseName) ? selectedNetName : convertNetworkName(data.caseName);
    dispatch(importCimNetwork({ networkId: selectedNetId, networkName, type: data.type, country: data.country }));
  };

  React.useEffect(() => {
    if (importSuccess) {
      handleClose();
    }
  }, [importSuccess]);

  return (
    <>
      <div>
        <Row className="justify-content-center">
          <Col md="5">
            <h2 id="network-import-from-cimRepo" data-cy="network-import-from-cimRepo">
              Network Import From Cim REPO
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          {loading ? (
            <Col md="5">
              <SpinnerLoading text={'Loading ...'} />
            </Col>
          ) : (
            <Col md="5">
              {/* Section allowing selection of network to import from CimRepo */}
              <NetworkFromCimRepoGrid setRowsSelectedCallback={setNetworkToImportCallback} selectionType={SELECTION_TYPE.SINGLE} />
            </Col>
          )}
        </Row>
        {networkToImport ? (
          <>
            <Form onSubmit={handleSubmit(submitForm)}>
              <Row className="justify-content-center">
                <Col md="5">
                  <ValidatedField
                    register={register}
                    error={errors?.country}
                    id={'select-country'}
                    label="Country"
                    name="country"
                    data-cy="country"
                    type="select"
                    defaultValue={country}
                    validate={{ required: { value: true, message: 'Country is a required field.' } }}
                  >
                    <option value="" hidden>
                      Select the Country...
                    </option>
                    {countryOptions?.map((option, index) => (
                      <option key={index} value={option.value}>
                        {' '}
                        {option.label}{' '}
                      </option>
                    ))}
                  </ValidatedField>

                  <ValidatedField
                    register={register}
                    error={errors?.type}
                    id={'select-type'}
                    label="Type"
                    name="type"
                    data-cy="type"
                    type="select"
                    defaultValue={type}
                    validate={{ required: { value: true, message: 'Type is a required field.' } }}
                  >
                    {/* Check whether networkTypeOptions has only one element */}
                    {networkTypeOptions && networkTypeOptions.length === 1 ? (
                      <option value={networkTypeOptions[0].value}>{networkTypeOptions[0].value}</option>
                    ) : (
                      <>
                        <option value="" hidden>
                          Select the Type...
                        </option>
                        {networkTypeOptions?.map((option, index) => (
                          <option key={index} value={option.value}>
                            {' '}
                            {option.label}{' '}
                          </option>
                        ))}
                      </>
                    )}
                  </ValidatedField>

                  <ValidatedField
                    register={register}
                    error={errors?.name}
                    id={'case-name'}
                    label="Case Name"
                    name="caseName"
                    data-cy="caseName"
                    type="text"
                  />

                  <Button className="me-2" color="info" type="submit" disabled={loading}>
                    <FontAwesomeIcon icon="plus" /> &nbsp; Import
                  </Button>
                  <Button tag={Link} id="back" data-cy="backButton" to="/network" replace color="info">
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                    <span className="d-none d-md-inline">Back</span>
                  </Button>
                </Col>
              </Row>
            </Form>
          </>
        ) : (
          ''
        )}
      </div>
      {!loading && !networkToImport && (
        <>
          <Divider />
          <Row className="justify-content-center">
            <Col md="5">
              <Button tag={Link} id="back" data-cy="backButton" to="/network" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
            </Col>
          </Row>
        </>
      )}
    </>
  );
};

export default NetworkImportFromCimRepo;
