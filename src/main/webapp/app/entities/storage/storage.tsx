import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './storage.reducer';
import { IStorage } from 'app/shared/model/storage.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Storage = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const storageList = useAppSelector(state => state.storage.entities);
  const loading = useAppSelector(state => state.storage.loading);
  const totalItems = useAppSelector(state => state.storage.totalItems);

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
      <h2 id="storage-heading" data-cy="StorageHeading">
        Storages
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Storage
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {storageList && storageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('busNum')}>
                  Bus Num <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('ps')}>
                  Ps <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qs')}>
                  Qs <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('energy')}>
                  Energy <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('eRating')}>
                  E Rating <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('chargeRating')}>
                  Charge Rating <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dischargeRating')}>
                  Discharge Rating <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('chargeEfficiency')}>
                  Charge Efficiency <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('thermalRating')}>
                  Thermal Rating <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qmin')}>
                  Qmin <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qmax')}>
                  Qmax <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('r')}>
                  R <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('x')}>
                  X <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pLoss')}>
                  P Loss <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qLoss')}>
                  Q Loss <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('socInitial')}>
                  Soc Initial <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('socMin')}>
                  Soc Min <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('socMax')}>
                  Soc Max <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Network <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {storageList.map((storage, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${storage.id}`} color="link" size="sm">
                      {storage.id}
                    </Button>
                  </td>
                  <td>{storage.busNum}</td>
                  <td>{storage.ps}</td>
                  <td>{storage.qs}</td>
                  <td>{storage.energy}</td>
                  <td>{storage.eRating}</td>
                  <td>{storage.chargeRating}</td>
                  <td>{storage.dischargeRating}</td>
                  <td>{storage.chargeEfficiency}</td>
                  <td>{storage.thermalRating}</td>
                  <td>{storage.qmin}</td>
                  <td>{storage.qmax}</td>
                  <td>{storage.r}</td>
                  <td>{storage.x}</td>
                  <td>{storage.pLoss}</td>
                  <td>{storage.qLoss}</td>
                  <td>{storage.status}</td>
                  <td>{storage.socInitial}</td>
                  <td>{storage.socMin}</td>
                  <td>{storage.socMax}</td>
                  <td>{storage.network ? <Link to={`network/${storage.network.id}`}>{storage.network.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${storage.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${storage.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${storage.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Storages found</div>
        )}
      </div>
      {totalItems ? (
        <div className={storageList && storageList.length > 0 ? '' : 'd-none'}>
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

export default Storage;
