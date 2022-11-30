import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, getEntitiesByNetworkId } from './bus.reducer';
import { IBus } from 'app/shared/model/bus.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/network/network.reducer';

export const Bus = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  React.useEffect(() => {
    if (Object.keys(networkEntity).length === 0) {
      props.history.push('/network');
    }
  }, []);

  const busList = useAppSelector(state => state.bus.entities);
  const loading = useAppSelector(state => state.bus.loading);
  const totalItems = useAppSelector(state => state.bus.totalItems);
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
      <h2 id="bus-heading" data-cy="BusHeading">
        Buses
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Bus
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {busList && busList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('busNum')}>
                  Bus Num <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('type')}>
                  Type <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('activePower')}>
                  Active Power <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('reactivePower')}>
                  Reactive Power <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('conductance')}>
                  Conductance <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('susceptance')}>
                  Susceptance <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('area')}>
                  Area <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('vm')}>
                  Vm <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('va')}>
                  Va <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('baseKv')}>
                  Base Kv <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('zone')}>
                  Zone <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('vmax')}>
                  Vmax <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('vmin')}>
                  Vmin <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Network <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {busList.map((bus, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${bus.id}`} color="link" size="sm">
                      {bus.id}
                    </Button>
                  </td>
                  <td>{bus.busNum}</td>
                  <td>{bus.type}</td>
                  <td>{bus.activePower}</td>
                  <td>{bus.reactivePower}</td>
                  <td>{bus.conductance}</td>
                  <td>{bus.susceptance}</td>
                  <td>{bus.area}</td>
                  <td>{bus.vm}</td>
                  <td>{bus.va}</td>
                  <td>{bus.baseKv}</td>
                  <td>{bus.zone}</td>
                  <td>{bus.vmax}</td>
                  <td>{bus.vmin}</td>
                  <td>{bus.network ? <Link to={`/network/${bus.network.id}`}>{bus.network.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${bus.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bus.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${bus.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Buses found</div>
        )}
      </div>
      {totalItems ? (
        <div className={busList && busList.length > 0 ? '' : 'd-none'}>
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

export default Bus;
