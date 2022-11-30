import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './bus-extension.reducer';
import { IBusExtension } from 'app/shared/model/bus-extension.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BusExtension = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const busExtensionList = useAppSelector(state => state.busExtension.entities);
  const loading = useAppSelector(state => state.busExtension.loading);
  const totalItems = useAppSelector(state => state.busExtension.totalItems);

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
      <h2 id="bus-extension-heading" data-cy="BusExtensionHeading">
        Bus Extensions
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Bus Extension
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {busExtensionList && busExtensionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('hasGen')}>
                  Has Gen <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isLoad')}>
                  Is Load <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('snomMva')}>
                  Snom Mva <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('sx')}>
                  Sx <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('sy')}>
                  Sy <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gx')}>
                  Gx <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('gy')}>
                  Gy <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('incrementCost')}>
                  Increment Cost <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('decrementCost')}>
                  Decrement Cost <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('mRid')}>
                  M Rid <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Bus <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {busExtensionList.map((busExtension, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${busExtension.id}`} color="link" size="sm">
                      {busExtension.id}
                    </Button>
                  </td>
                  <td>{busExtension.hasGen}</td>
                  <td>{busExtension.isLoad}</td>
                  <td>{busExtension.snomMva}</td>
                  <td>{busExtension.sx}</td>
                  <td>{busExtension.sy}</td>
                  <td>{busExtension.gx}</td>
                  <td>{busExtension.gy}</td>
                  <td>{busExtension.status}</td>
                  <td>{busExtension.incrementCost}</td>
                  <td>{busExtension.decrementCost}</td>
                  <td>{busExtension.mRid}</td>
                  <td>{busExtension.bus ? <Link to={`bus/${busExtension.bus.id}`}>{busExtension.bus.busNum}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${busExtension.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${busExtension.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${busExtension.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Bus Extensions found</div>
        )}
      </div>
      {totalItems ? (
        <div className={busExtensionList && busExtensionList.length > 0 ? '' : 'd-none'}>
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

export default BusExtension;
