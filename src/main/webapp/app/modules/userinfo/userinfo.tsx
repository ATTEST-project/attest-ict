import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { RouteComponentProps } from 'react-router-dom';
import { getAccount } from 'app/shared/reducers/authentication';
import { Button, Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const UserInfo = (props: RouteComponentProps) => {
  const dispatch = useAppDispatch();

  React.useEffect(() => {
    dispatch(getAccount());
  }, []);

  const currentUser = useAppSelector(state => state.authentication.account);

  // --- 2023/09/2023

  const authorities = currentUser.authorities;

  const goToPrevPage = () => {
    props.history.goBack();
  };

  return (
    <Row>
      <Col md="8">
        <h2>User Info</h2>
        <dl className="jh-entity-details">
          <dt>ID</dt>
          <dd>{currentUser.id}</dd>
        </dl>
        <dl className="jh-entity-details">
          <dt>Username</dt>
          <dd>{currentUser.login}</dd>
        </dl>
        <dl className="jh-entity-details">
          <dt>Email</dt>
          <dd>{currentUser.email}</dd>
        </dl>
        <dl className="jh-entity-details">
          <dt>First Name</dt>
          <dd>{currentUser.firstName}</dd>
        </dl>
        <dl className="jh-entity-details">
          <dt>Last Name</dt>
          <dd>{currentUser.lastName}</dd>
        </dl>

        <dl className="jh-entity-details">
          <dt>Roles</dt>
          <dd>{currentUser.authorities.join(',')}</dd>
        </dl>
        <Button onClick={goToPrevPage} color="info" data-cy="entityUserInfoBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserInfo;
