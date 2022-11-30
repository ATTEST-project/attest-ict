import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './asset-transformer.reducer';
import { IAssetTransformer } from 'app/shared/model/asset-transformer.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AssetTransformer = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const assetTransformerList = useAppSelector(state => state.assetTransformer.entities);
  const loading = useAppSelector(state => state.assetTransformer.loading);
  const totalItems = useAppSelector(state => state.assetTransformer.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="asset-transformer-heading" data-cy="AssetTransformerHeading">
        Asset Transformers
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Asset Transformer
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {assetTransformerList && assetTransformerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('busNum')}>
                  Bus Num <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('voltageRatio')}>
                  Voltage Ratio <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('insulationMedium')}>
                  Insulation Medium <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('type')}>
                  Type <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('indoorOutdoor')}>
                  Indoor Outdoor <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('annualMaxLoadKva')}>
                  Annual Max Load Kva <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('age')}>
                  Age <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('externalCondition')}>
                  External Condition <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ratingKva')}>
                  Rating Kva <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('numConnectedCustomers')}>
                  Num Connected Customers <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('numSensitiveCustomers')}>
                  Num Sensitive Customers <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('backupSupply')}>
                  Backup Supply <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('costOfFailureEuro')}>
                  Cost Of Failure Euro <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Network <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {assetTransformerList.map((assetTransformer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${assetTransformer.id}`} color="link" size="sm">
                      {assetTransformer.id}
                    </Button>
                  </td>
                  <td>{assetTransformer.busNum}</td>
                  <td>{assetTransformer.voltageRatio}</td>
                  <td>{assetTransformer.insulationMedium}</td>
                  <td>{assetTransformer.type}</td>
                  <td>{assetTransformer.indoorOutdoor}</td>
                  <td>{assetTransformer.annualMaxLoadKva}</td>
                  <td>{assetTransformer.age}</td>
                  <td>{assetTransformer.externalCondition}</td>
                  <td>{assetTransformer.ratingKva}</td>
                  <td>{assetTransformer.numConnectedCustomers}</td>
                  <td>{assetTransformer.numSensitiveCustomers}</td>
                  <td>{assetTransformer.backupSupply}</td>
                  <td>{assetTransformer.costOfFailureEuro}</td>
                  <td>
                    {assetTransformer.network ? (
                      <Link to={`network/${assetTransformer.network.id}`}>{assetTransformer.network.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${assetTransformer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${assetTransformer.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${assetTransformer.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Asset Transformers found</div>
        )}
      </div>
      {totalItems ? (
        <div className={assetTransformerList && assetTransformerList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default AssetTransformer;
