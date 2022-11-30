import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './flex-el-val.reducer';
import { IFlexElVal } from 'app/shared/model/flex-el-val.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FlexElVal = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const flexElValList = useAppSelector(state => state.flexElVal.entities);
  const loading = useAppSelector(state => state.flexElVal.loading);
  const totalItems = useAppSelector(state => state.flexElVal.totalItems);

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
      <h2 id="flex-el-val-heading" data-cy="FlexElValHeading">
        Flex El Vals
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Flex El Val
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {flexElValList && flexElValList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('busNum')}>
                  Bus Num <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('hour')}>
                  Hour <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('min')}>
                  Min <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pfmaxUp')}>
                  Pfmax Up <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('pfmaxDn')}>
                  Pfmax Dn <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qfmaxUp')}>
                  Qfmax Up <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('qfmaxDn')}>
                  Qfmax Dn <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Flex Profile <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {flexElValList.map((flexElVal, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${flexElVal.id}`} color="link" size="sm">
                      {flexElVal.id}
                    </Button>
                  </td>
                  <td>{flexElVal.busNum}</td>
                  <td>{flexElVal.hour}</td>
                  <td>{flexElVal.min}</td>
                  <td>{flexElVal.pfmaxUp}</td>
                  <td>{flexElVal.pfmaxDn}</td>
                  <td>{flexElVal.qfmaxUp}</td>
                  <td>{flexElVal.qfmaxDn}</td>
                  <td>
                    {flexElVal.flexProfile ? <Link to={`flex-profile/${flexElVal.flexProfile.id}`}>{flexElVal.flexProfile.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${flexElVal.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${flexElVal.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${flexElVal.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Flex El Vals found</div>
        )}
      </div>
      {totalItems ? (
        <div className={flexElValList && flexElValList.length > 0 ? '' : 'd-none'}>
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

export default FlexElVal;
