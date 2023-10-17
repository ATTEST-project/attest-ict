import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { hasAuthorityForUpdateTask, shouldFilterTask } from 'app/shared/util/authorizationUtils';
import { addSpaceAfterComma } from 'app/shared/util/string-utils';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';

import { ITask, TaskStatus } from 'app/shared/model/task.model';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getEntities } from './task.reducer';

export const Task = (props: RouteComponentProps<{ url: string }>) => {
  const intervalInMilliSec = 50000;

  const [paginationState, setPaginationState] = useState({
    ...overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search),
    sort: 'id',
    order: 'desc',
  });

  const dispatch = useAppDispatch();
  const userEntity = useAppSelector(state => state.authentication.account);
  const hasAuthorityForUpdate = hasAuthorityForUpdateTask(userEntity.authorities);
  const shouldFilter = shouldFilterTask(userEntity.authorities);

  const taskList = useAppSelector(state => state.task.entities); // Get the list of tasks  from the Redux store
  const loading = useAppSelector(state => state.task.loading);
  const totalItems = useAppSelector(state => state.task.totalItems);
  const [isColumnSortAction, setColumnSortAction] = useState(false);

  const splitInMultipleRows = (description: string) => {
    if (!description) return '';
    const arrayDescr = description.split(',');
    return (
      <>
        {' '}
        {arrayDescr.map((value, index) => (
          <div key={index}> {value} </div>
        ))}{' '}
      </>
    );
  };

  const getAllEntities = () => {
    const params = {
      page: paginationState.activePage - 1,
      size: paginationState.itemsPerPage,
      sort: `${paginationState.sort},${paginationState.order}`,
    };
    const requestParam = shouldFilter ? { ...params, userId: userEntity.id } : params;
    /* eslint-disable-next-line no-console */
    console.log('getAllEntities() - requestParam ', requestParam);
    dispatch(getEntities(requestParam));
  };

  useEffect(() => {
    // Check if there is at least one entity with "ONGOING" status
    const hasPendingEntity = taskList.some(entity => entity.taskStatus === TaskStatus.ONGOING);

    // If there is at least one entity with "ONGOING" status, set the interval to 10 secs
    if (hasPendingEntity) {
      const intervalId = setInterval(() => {
        /* eslint-disable-next-line no-console */
        console.log('useEffect() -  isColumnSortAction: ', isColumnSortAction);
        if (!isColumnSortAction && !loading) {
          getAllEntities();
        }
      }, intervalInMilliSec);

      // Clear the interval when the component unmounts
      return () => clearInterval(intervalId);
    }
  }, [taskList, isColumnSortAction]);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    /* eslint-disable-next-line no-console */
    console.log('task useEffect() - sortEntities ');
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
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
    /* eslint-disable-next-line no-console */
    console.log('Task sort() - column -  ', p);

    // stop interval for automatic refresh, during the column sort
    setColumnSortAction(true);
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    // activate interval used for automatic refresh
    setColumnSortAction(false);
  };

  const handlePagination = currentPage => {
    /* eslint-disable-next-line no-console */
    console.log('Task handlePagination() - currentPage -  ', currentPage);

    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });
  };
  const handleSyncList = () => {
    /* eslint-disable-next-line no-console */
    console.log('Task handleSyncList() - refresh   ');
    sortEntities();
  };

  const { match } = props;

  const btnShowResultsIsDisabled = (task: ITask) => {
    const disabled = task.taskStatus !== TaskStatus.PASSED || task.tool.workPackage === 'WP2';
    return disabled;
  };

  const btnDownloadResultsIsDisabled = (task: ITask) => {
    const disabled = task.taskStatus !== TaskStatus.PASSED;
    return disabled;
  };

  const showKillButton = (task: ITask) => {
    const enabled = task.taskStatus === TaskStatus.ONGOING;
    return enabled;
  };

  return (
    <div>
      <h2 id="task-heading" data-cy="TaskHeading">
        Tasks
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          {hasAuthorityForUpdate && (
            <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp; Create new Task
            </Link>
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
                  Status <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateTimeStart')}>
                  Date Time Start <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateTimeEnd')}>
                  Date Time End <FontAwesomeIcon icon="sort" />
                </th>
                <th>Simulation Info</th>
                <th>Network Name</th>
                <th className="hand" onClick={sort('toolNum')}>
                  Tool <FontAwesomeIcon icon="sort" />
                </th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {taskList.map((task, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td> {task.id} </td>
                  <td>{task.info ? task.info : task.taskStatus}</td>
                  <td>{task.dateTimeStart ? <TextFormat type="date" value={task.dateTimeStart} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{task.dateTimeEnd ? <TextFormat type="date" value={task.dateTimeEnd} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{addSpaceAfterComma(task.simulationDescr)}</td>
                  <td>{task.networkName ? task.networkName : ''}</td>
                  <td>{task.toolNum ? task.toolNum : ''}</td>
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
                        <FontAwesomeIcon icon="file-download" /> <span className="d-none d-md-inline">Log</span>
                      </Button>

                      <Button
                        disabled={btnDownloadResultsIsDisabled(task)}
                        tag={Link}
                        to={`${match.url}/${task.id}/task-results-dl`}
                        color="primary"
                        size="sm"
                        data-cy="entityLogDownloadButton"
                      >
                        <FontAwesomeIcon icon="file-download" /> <span className="d-none d-md-inline">Results</span>
                      </Button>

                      <Button
                        disabled={btnShowResultsIsDisabled(task)}
                        tag={Link}
                        to={`${match.url}/${task.id}/results`}
                        color="success"
                        size="sm"
                        data-cy="entityShowResultsButton"
                      >
                        <FontAwesomeIcon icon="poll" /> <span className="d-none d-md-inline">Show Results</span>
                      </Button>

                      <Button tag={Link} to={`${match.url}/${task.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>

                      <Button
                        hidden={!hasAuthorityForUpdate}
                        tag={Link}
                        to={`${match.url}/${task.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${task.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>

                      <Button
                        disabled={!showKillButton(task)}
                        tag={Link}
                        to={`${match.url}/${task.id}/stop?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="warning"
                        size="sm"
                        data-cy="entityKillButton"
                      >
                        <FontAwesomeIcon icon="times" /> <span className="d-none d-md-inline">Stop</span>
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
