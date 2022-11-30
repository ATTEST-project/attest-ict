import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './branch-extension.reducer';
import { IBranchExtension } from 'app/shared/model/branch-extension.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BranchExtension = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const branchExtensionList = useAppSelector(state => state.branchExtension.entities);
  const loading = useAppSelector(state => state.branchExtension.loading);
  const totalItems = useAppSelector(state => state.branchExtension.totalItems);

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
      <h2 id="branch-extension-heading" data-cy="BranchExtensionHeading">
        Branch Extensions
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Branch Extension
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {branchExtensionList && branchExtensionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('stepSize')}>
                  Step Size <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('actTap')}>
                  Act Tap <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('minTap')}>
                  Min Tap <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('maxTap')}>
                  Max Tap <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('normalTap')}>
                  Normal Tap <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('nominalRatio')}>
                  Nominal Ratio <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('rIp')}>
                  R Ip <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('rN')}>
                  R N <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('r0')}>
                  R 0 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('x0')}>
                  X 0 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('b0')}>
                  B 0 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('length')}>
                  Length <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('normStat')}>
                  Norm Stat <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('g')}>
                  G <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('mRid')}>
                  M Rid <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Branch <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {branchExtensionList.map((branchExtension, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${branchExtension.id}`} color="link" size="sm">
                      {branchExtension.id}
                    </Button>
                  </td>
                  <td>{branchExtension.stepSize}</td>
                  <td>{branchExtension.actTap}</td>
                  <td>{branchExtension.minTap}</td>
                  <td>{branchExtension.maxTap}</td>
                  <td>{branchExtension.normalTap}</td>
                  <td>{branchExtension.nominalRatio}</td>
                  <td>{branchExtension.rIp}</td>
                  <td>{branchExtension.rN}</td>
                  <td>{branchExtension.r0}</td>
                  <td>{branchExtension.x0}</td>
                  <td>{branchExtension.b0}</td>
                  <td>{branchExtension.length}</td>
                  <td>{branchExtension.normStat}</td>
                  <td>{branchExtension.g}</td>
                  <td>{branchExtension.mRid}</td>
                  <td>
                    {branchExtension.branch ? <Link to={`branch/${branchExtension.branch.id}`}>{branchExtension.branch.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${branchExtension.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${branchExtension.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${branchExtension.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Branch Extensions found</div>
        )}
      </div>
      {totalItems ? (
        <div className={branchExtensionList && branchExtensionList.length > 0 ? '' : 'd-none'}>
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

export default BranchExtension;
