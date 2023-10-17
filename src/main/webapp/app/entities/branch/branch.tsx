import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, getEntitiesByNetworkId } from './branch.reducer';
import { IBranch } from 'app/shared/model/branch.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { hasCrudOperationsAuthority } from 'app/shared/util/authorizationUtils';

export const Branch = (props: RouteComponentProps<{ url: string }>) => {
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

  const branchList = useAppSelector(state => state.branch.entities);
  const loading = useAppSelector(state => state.branch.loading);
  const totalItems = useAppSelector(state => state.branch.totalItems);
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
      <h2 id="branch-heading" data-cy="BranchHeading">
        Branches
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          {hasCrudOptAuth && (
            <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp; Create new Branch
            </Link>
          )}
        </div>
      </h2>
      <div className="table-responsive">
        {branchList && branchList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('fbus')}>
                  Fbus <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tbus')}>
                  Tbus <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('r')}>
                  R <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('x')}>
                  X <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('b')}>
                  B <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ratea')}>
                  Ratea <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('rateb')}>
                  Rateb <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ratec')}>
                  Ratec <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tapRatio')}>
                  Tap Ratio <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('angle')}>
                  Angle <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('angmin')}>
                  Angmin <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('angmax')}>
                  Angmax <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Network <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {branchList.map((branch, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{branch.id}</td>
                  <td>{branch.fbus}</td>
                  <td>{branch.tbus}</td>
                  <td>{branch.r}</td>
                  <td>{branch.x}</td>
                  <td>{branch.b}</td>
                  <td>{branch.ratea}</td>
                  <td>{branch.rateb}</td>
                  <td>{branch.ratec}</td>
                  <td>{branch.tapRatio}</td>
                  <td>{branch.angle}</td>
                  <td>{branch.status}</td>
                  <td>{branch.angmin}</td>
                  <td>{branch.angmax}</td>
                  <td>{branch.network ? <Link to={`/network/${branch.network.id}`}>{branch.network.id}</Link> : ''}</td>
                  {hasCrudOptAuth && (
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${branch.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${branch.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${branch.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Branches found</div>
        )}
      </div>
      {totalItems ? (
        <div className={branchList && branchList.length > 0 ? '' : 'd-none'}>
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

export default Branch;
