import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, getEntitiesByUserId } from './task.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const Task = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState({ activePage: 1, itemsPerPage: ITEMS_PER_PAGE, sort: 'id', order: 'desc' });

  React.useEffect(() => {
    setPaginationState({ ...paginationState, order: DESC });
  }, []);

  const taskList = useAppSelector(state => state.task.entities);
  const loading = useAppSelector(state => state.task.loading);
  const totalItems = useAppSelector(state => state.task.totalItems);
  const userEntity = useAppSelector(state => state.authentication.account);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const getEntitiesByAccountId = () => {
    if (!userEntity.id) {
      return;
    }
    dispatch(
      getEntitiesByUserId({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        userId: userEntity.id,
      })
    );
  };

  const sortEntities = () => {
    getEntitiesByAccountId();
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
      <h2 id="task-heading" data-cy="TaskHeading">
        Tasks
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          {isAdmin ? (
            <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp; Create new Task
            </Link>
          ) : (
            ''
          )}
        </div>
      </h2>
      <div className="table-responsive">
        {taskList && taskList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('taskStatus')}>
                  Task Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('info')}>
                  Info <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateTimeStart')}>
                  Date Time Start <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateTimeEnd')}>
                  Date Time End <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Tool Log File <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Simulation <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Tool <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  User <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {taskList.map((task, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${task.id}`} color="link" size="sm">
                      {task.id}
                    </Button>
                  </td>
                  <td>{task.taskStatus}</td>
                  <td>{task.info}</td>
                  <td>{task.dateTimeStart ? <TextFormat type="date" value={task.dateTimeStart} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{task.dateTimeEnd ? <TextFormat type="date" value={task.dateTimeEnd} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{task.toolLogFileId ? <Link to={`tool-log-file/${task.toolLogFileId}`}>{task.toolLogFileName}</Link> : ''}</td>
                  <td>{task.simulationUuid ? <Link to={`simulation/${task.simulationId}`}>{task.simulationUuid}</Link> : ''}</td>
                  <td>{task.tool ? <Link to={`tool/${task.tool.id}`}>{task.tool.name}</Link> : ''}</td>
                  <td>{task.user ? task.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        disabled={!task.toolLogFileId}
                        tag={Link}
                        to={`${match.url}/${task.id}/task-log-dl`}
                        color="primary"
                        size="sm"
                        data-cy="entityLogDownloadButton"
                      >
                        <FontAwesomeIcon icon="file-download" /> <span className="d-none d-md-inline">Download Log</span>
                      </Button>
                      <Button
                        disabled={!task.simulationUuid}
                        tag={Link}
                        to={`${match.url}/${task.id}/task-results-dl`}
                        color="primary"
                        size="sm"
                        data-cy="entityLogDownloadButton"
                      >
                        <FontAwesomeIcon icon="file-download" /> <span className="d-none d-md-inline">Download Results</span>
                      </Button>
                      <Button
                        disabled={!task.simulationUuid}
                        tag={Link}
                        to={`${match.url}/${task.id}/results`}
                        color="primary"
                        size="sm"
                        data-cy="entityShowResultsButton"
                      >
                        <FontAwesomeIcon icon="poll" /> <span className="d-none d-md-inline">Show Results</span>
                      </Button>

                      <Button
                        hidden={!isAdmin}
                        tag={Link}
                        to={`${match.url}/${task.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>

                      <Button
                        hidden={!isAdmin}
                        tag={Link}
                        to={`${match.url}/${task.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        hidden={!isAdmin}
                        tag={Link}
                        to={`${match.url}/${task.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
          !loading && <div className="alert alert-warning">No Tasks found</div>
        )}
      </div>
      {totalItems ? (
        <div className={taskList && taskList.length > 0 ? '' : 'd-none'}>
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

export default Task;
