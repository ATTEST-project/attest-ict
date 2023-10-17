import React, { useEffect, useState } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Progress, Table, Tooltip } from 'reactstrap';
import { getSortState, JhiItemCount, JhiPagination, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './network.reducer';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';

export const Network = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState({
    ...overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search),
    sort: 'id',
    order: 'desc',
  });

  const networkList = useAppSelector(state => state.network.entities);
  const loading = useAppSelector(state => state.network.loading);
  const totalItems = useAppSelector(state => state.network.totalItems);

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
    /* eslint-disable-next-line no-console */
    console.log('Enter useEffect to set PaginationState from params  ', params);

    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      if (sortSplit[0] !== 'id') {
        setPaginationState({
          ...paginationState,
          activePage: +page,
          sort: sortSplit[0],
          order: sortSplit[1],
        });
      }
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
      <h2 id="network-heading" data-cy="NetworkHeading">
        Networks
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>

          <Link to={`${match.url}/import-cim`} className="btn btn-primary jh-import-cim" id="jh-import-cim" data-cy="entityImportCimButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Import from CIM repo
          </Link>

          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Network
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {networkList && networkList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('mpcName')}>
                  Mpc Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('country')}>
                  Country <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('type')}>
                  Type <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  Description <FontAwesomeIcon icon="sort" />
                </th>
                {/*  Comment 2023/10/12
                  The functionality for logical deletion of the network has not been implemented yet
                  <th className="hand" onClick={sort('isDeleted')}>
                    Is Deleted <FontAwesomeIcon icon="sort" />
                  </th>
                */}
                <th className="hand" onClick={sort('networkDate')}>
                  Network Date <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('version')}>
                  Version <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('creationDateTime')}>
                  Creation Date Time <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updateDateTime')}>
                  Update Date Time <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {networkList.map((network, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${network.id}`} color="link" size="sm">
                      {network.id}
                    </Button>
                  </td>
                  <td>{network.name}</td>
                  <td>{network.mpcName}</td>
                  <td>{network.country}</td>
                  <td>{network.type}</td>
                  <td>{network.description}</td>
                  {/* Comment 2023/10/12
                        The functionality for logical deletion of the network has not been implemented yet
                        <td>{network.isDeleted ? 'true' : 'false'}</td>
                   */}
                  <td>{network.networkDate ? <TextFormat type="date" value={network.networkDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{network.version}</td>
                  <td>
                    {network.creationDateTime ? <TextFormat type="date" value={network.creationDateTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {network.updateDateTime ? <TextFormat type="date" value={network.updateDateTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {/* Comment on 2023/09/05: similar  to edit
                      <Button
                        id={'view_button_' + i}
                        tag={Link}
                        to={`${match.url}/${network.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        {' '}
                        <FontAwesomeIcon icon="eye" />
                        <CustomTooltip target={'view_button_' + i} tooltip={'View'} />
                      </Button>
                      */}

                      <Button id={'upload_button_' + i} tag={Link} to={`${match.url}/${network.id}/upload`} color="primary" size="sm">
                        {' '}
                        <FontAwesomeIcon icon="file-upload" />
                        <CustomTooltip target={'upload_button_' + i} tooltip={'Upload'} />
                      </Button>
                      <Button id={'export_button_' + i} tag={Link} to={`${match.url}/${network.id}/export`} color="primary" size="sm">
                        {' '}
                        <FontAwesomeIcon icon="file-download" />
                        <CustomTooltip target={'export_button_' + i} tooltip={'Export'} />
                      </Button>
                      <Button id={'data_button_' + i} tag={Link} to={`${match.url}/${network.id}/data`} color="primary" size="sm">
                        {' '}
                        <FontAwesomeIcon icon="table" />
                        <CustomTooltip target={'data_button_' + i} tooltip={'Data'} />
                      </Button>
                      <Button id={'sld_button_' + i} tag={Link} to={`${match.url}/${network.id}/sld`} color="primary" size="sm">
                        {' '}
                        <FontAwesomeIcon icon="project-diagram" />
                        <CustomTooltip target={'sld_button_' + i} tooltip={'SLD'} />
                      </Button>
                      {/* Comment on 2023/09/05:we are unable to show the network's map, due to the missing data such as coordinates
                      <Row md="2">
                      <Button id={'map_button_' + i} tag={Link} to={`${match.url}/${network.id}/map`} color="primary" size="sm">
                        {' '}
                        <FontAwesomeIcon icon="map" />
                        <CustomTooltip target={'map_button_' + i} tooltip={'Map'} />
                      </Button>
                      */}
                      <Button
                        id={'edit_button_' + i}
                        tag={Link}
                        to={`${match.url}/${network.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        {' '}
                        <FontAwesomeIcon icon="pencil-alt" />
                        <CustomTooltip target={'edit_button_' + i} tooltip={'Edit'} />
                      </Button>
                      <Button
                        id={'delete_button_' + i}
                        tag={Link}
                        to={`${match.url}/${network.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        {' '}
                        <FontAwesomeIcon icon="trash" />
                        <CustomTooltip target={'delete_button_' + i} tooltip={'Delete'} />
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Networks found</div>
        )}
      </div>
      {totalItems ? (
        <div className={networkList && networkList.length > 0 ? '' : 'd-none'}>
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

export default Network;
