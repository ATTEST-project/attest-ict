import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, getEntitiesByNetworkId } from './generator.reducer';
import { IGenerator } from 'app/shared/model/generator.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

import { hasCrudOperationsAuthority } from 'app/shared/util/authorizationUtils';

export const Generator = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();
  const userEntity = useAppSelector(state => state.authentication.account);
  const hasCrudOptAuth = hasCrudOperationsAuthority(userEntity.authorities);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  React.useEffect(() => {
    if (Object.keys(networkEntity).length === 0) {
      props.history.push('/network');
    }
  }, []);

  const generatorList = useAppSelector(state => state.generator.entities);
  const loading = useAppSelector(state => state.generator.loading);
  const totalItems = useAppSelector(state => state.generator.totalItems);
  const networkEntity = useAppSelector(state => state.network.entity);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const getEntitiesOfNetwork = () => {
    if (!networkEntity.id) {
      return;
    }
    dispatch(
      getEntitiesByNetworkId({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        networkId: networkEntity.id,
      })
    );
  };

  const sortEntities = () => {
    getEntitiesOfNetwork();
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
      <h2 id="generator-heading" data-cy="GeneratorHeading">
        Generators
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          {hasCrudOptAuth && (
            <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp; Create new Generator
            </Link>
          )}
        </div>
      </h2>
      <div className="table-responsive">
        {generatorList && generatorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('busNum')}>
                  Bus Num <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pg')}>
                  Pg <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qg')}>
                  Qg <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qmax')}>
                  Qmax <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qmin')}>
                  Qmin <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('vg')}>
                  Vg <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('mBase')}>
                  M Base <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pmax')}>
                  Pmax <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pmin')}>
                  Pmin <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pc1')}>
                  Pc 1 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pc2')}>
                  Pc 2 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qc1min')}>
                  Qc 1 Min <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qc1max')}>
                  Qc 1 Max <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qc2min')}>
                  Qc 2 Min <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qc2max')}>
                  Qc 2 Max <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('rampAgc')}>
                  Ramp Agc <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ramp10')}>
                  Ramp 10 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ramp30')}>
                  Ramp 30 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('rampQ')}>
                  Ramp Q <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('apf')}>
                  Apf <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Network <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {generatorList.map((generator, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{generator.id}</td>
                  <td>{generator.busNum}</td>
                  <td>{generator.pg}</td>
                  <td>{generator.qg}</td>
                  <td>{generator.qmax}</td>
                  <td>{generator.qmin}</td>
                  <td>{generator.vg}</td>
                  <td>{generator.mBase}</td>
                  <td>{generator.status}</td>
                  <td>{generator.pmax}</td>
                  <td>{generator.pmin}</td>
                  <td>{generator.pc1}</td>
                  <td>{generator.pc2}</td>
                  <td>{generator.qc1min}</td>
                  <td>{generator.qc1max}</td>
                  <td>{generator.qc2min}</td>
                  <td>{generator.qc2max}</td>
                  <td>{generator.rampAgc}</td>
                  <td>{generator.ramp10}</td>
                  <td>{generator.ramp30}</td>
                  <td>{generator.rampQ}</td>
                  <td>{generator.apf}</td>
                  <td>{generator.network ? <Link to={`/network/${generator.network.id}`}>{generator.network.id}</Link> : ''}</td>
                  {hasCrudOptAuth && (
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${generator.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${generator.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${generator.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Generators found</div>
        )}
      </div>
      {totalItems ? (
        <div className={generatorList && generatorList.length > 0 ? '' : 'd-none'}>
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

export default Generator;
